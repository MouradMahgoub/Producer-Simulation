package simulation.producer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Synchronized;
import simulation.producer.models.observer.Observer;
import simulation.producer.models.observer.Subject;

public class Queue implements Observer, Runnable{
    private static int count = 0;
    private int id;
    private BlockingQueue<Product> products = new LinkedBlockingQueue<>();
    // private List<Machine> subjects = new ArrayList<Machine>();

    public Queue(){
        this.id = count++;
    }

    @Override
    public  void update(Subject subject) {
        try{
            if(products.isEmpty()){
                System.out.println("Queue " + this.id + " is empty");
                return;
            }
                System.out.println("Product" + products.peek().getId() + " processed by machine " + ((Machine) subject).getId());
                ((Machine) subject).process(products.take());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
    }   
    
    public void addProduct(Product product){
        try {
            products.put(product);
            System.out.println("Product" + product.getId() + " added to queue " + this.id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BlockingQueue<Product> getProducts() {
        return this.products;
    }

    public void setProducts(BlockingQueue<Product> products) {
        this.products = products;
    }
    
    public int getId() {
        return this.id;
    }

    // public void attach (Machine subject){
    //     subjects.add(subject);
    // }
    
    // public void detach (Machine subject){
    //     subjects.remove(subject);
    // }
}
