package simulation.producer.models;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import simulation.producer.models.observer.Observer;
import simulation.producer.models.observer.Subject;

public class Queue implements Observer{
    private static int count = 0;

    private int id;
    private BlockingQueue<Product> products = new LinkedBlockingQueue<>();
    private String x;
    private String y;

    public Queue(String x, String y){
        this.id = count++;
        this.x = x;
        this.y = y;
    }
    public Queue(Queue other) {
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;

        // Create a new BlockingQueue and copy each Product
        this.products = new LinkedBlockingQueue<>();
        for (Product product : other.products) {
            this.products.add(new Product());
        }
    }


    @Override
    public void update(Subject subject) {
        try{
            Product pr =  products.take();
            System.out.println("Product " + pr.getId() + " processed by machine " + ((Machine)subject).getId());
            ((Machine)subject).process(pr);
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
