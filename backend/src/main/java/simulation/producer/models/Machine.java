package simulation.producer.models;

import simulation.producer.managers.SimulationManager;
import simulation.producer.models.observer.Subject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Machine extends Subject implements Runnable{

    private static int count = 0;
    private  String defaultColor;

    
    private int id;
    private String x;
    private String y;

    private List<Queue> observerList = new ArrayList<Queue>();
    private Queue outQueue;
    private String currentColor;
    private int serviceTime;
    public Machine(String x, String y, String defaultColor){
        this.id = count++;
        this.x = x;
        this.y = y;
        this.serviceTime = (new Random()).nextInt(2, 8)*1000;
        this.currentColor = defaultColor;
        this.defaultColor = defaultColor;
    }

    public Machine(Machine other) {
        this.id = other.id;
        this.x = other.x;
        this.y = other.y;
        this.serviceTime = other.serviceTime;
        this.currentColor = other.currentColor;
        this.defaultColor = other.defaultColor;
        if(this.currentColor==null){
            this.currentColor=other.defaultColor;
        }

        // Copy the observerList
        this.observerList = new ArrayList<>();
        for (Queue observer : other.observerList) {
            this.observerList.add(new Queue(observer));
        }

        // You might want to adjust this part based on your actual implementation
        if (other.outQueue != null) {
            this.outQueue = new Queue(other.outQueue);
        }
    }



    // convert a rgb Color representation to hexa representation

    // generate a random color in hexa representation
    public static String generateRandomColor(){
        Random random = new Random();
        return "#"+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2);
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



    public List<Queue> getObserverList() {
        return observerList;
    }

    public void setObserverList(List<Queue> observerList) {
        this.observerList = observerList;
    }

    public Queue getOutQueue() {
        return outQueue;
    }

    public void setOutQueue(Queue outQueue) {
        this.outQueue = outQueue;
    }

    public int getId() {
        return id;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public void attach(Queue addQueue){
        observerList.add(addQueue);
    }

    public void detach(Queue removeQueue){
        observerList.remove(removeQueue);
    }

    public synchronized void process(Product currentProduct){
        this.currentColor = currentProduct.getColor();
        try {
            System.out.println("Machine "+this.id+" is processing product "+currentProduct.getId()+" for "+this.serviceTime+" ms");
            SimulationManager.getInstance().sendToMemento();
            Thread.sleep(this.serviceTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //send prcessed product to next queue
        outQueue.addProduct(currentProduct);
        this.currentColor = this.defaultColor;

    }

    public void notifyObservers() {
        for (Queue observer : observerList) {
            System.out.println("Machine "+this.id+" is notifying queue "+observer.getId());
            observer.update(this);
        }
    }

    //set color random
    public static void setDefaultColor(){

    }

    @Override
    public void run() {
        while(true){
            try {
                notifyObservers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return count == machine.count && id == machine.id && serviceTime == machine.serviceTime && Objects.equals(observerList, machine.observerList) && Objects.equals(outQueue, machine.outQueue) && Objects.equals(defaultColor, machine.defaultColor) && Objects.equals(currentColor, machine.currentColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, observerList, outQueue, id, defaultColor, currentColor, serviceTime);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "count=" + count +
                ", observerList=" + observerList +
                ", outQueue=" + outQueue +
                ", id=" + id +
                ", defaultColor=" + defaultColor +
                ", currentColor=" + currentColor +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
