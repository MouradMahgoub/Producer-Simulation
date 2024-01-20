package simulation.producer.models;

import java.awt.Color;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Synchronized;
import simulation.producer.models.observer.Observer;
import simulation.producer.models.observer.Subject;

public class Queue implements Observer{
    private static int count = 0;
    private static String defaultColor;

    private int id;
    private BlockingQueue<Product> products = new LinkedBlockingQueue<>();
    private String x;
    private String y;

    public Queue(String x, String y, String defaultColor){
        this.id = count++;
        this.x = x;
        this.y = y;
        Queue.defaultColor = defaultColor;
    }

    @Override
    public void update(Subject subject) {
        try{
            if(products.isEmpty()){
                System.out.println("Queue " + this.id + " is empty");
                return;
            }
            System.out.println("Product" + products.peek().getId() + " processed by machine " + ((Machine)subject).getId());
            ((Machine)subject).process(products.take());
        }catch(Exception e){
            e.printStackTrace();
        }
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

    public String getX(){
        return this.x;
    }

    public void setX(String x){
        this.x = x;
    }
    
    public String getY(){
        return this.y;
    }

    public void setY(String y){
        this.y = y;
    }

}
