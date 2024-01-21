package simulation.producer.models;

import org.springframework.beans.factory.annotation.Autowired;
import simulation.producer.models.observer.Subject;
import simulation.producer.models.WebSocketController;



import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Machine extends Subject implements Runnable{

    @Autowired
    WebSocketController webSocketController;
    private static int count = 0;
    private static String defaultColor;

    
    private int id;
    private String x;
    private String y;

    private List<Queue> observerList = new ArrayList<Queue>();
    private Queue outQueue;
    private String currentColor;
    private int serviceTime;

    public Machine(String x, String y, String defaultColor,WebSocketController webSocketController){
        this.id = count++;
        this.x = x;
        this.y = y;
        this.serviceTime = (new Random()).nextInt(2, 8)*1000;
        Machine.defaultColor = defaultColor;
        this.webSocketController=webSocketController;
    }

    // convert a rgb Color representation to hexa representation

    // generate a random color in hexa representation
    public static String generateRandomColor(){
        Random random = new Random();
        return "#"+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2);
    }


    // //set color random in hexa representation
    // public static void setDefaultColor(String hexaColor){
    //     defaultColor = Color.decode(hexaColor);
    // }

    // //set color random in hexa representation
    // public static void setDefaultColor(){
    //     Random random = new Random();
    //     defaultColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    // }

    // //send color in hexa representation
    // public String getDefaultColor(){
    //     return "#"+Integer.toHexString(defaultColor.getRGB()).substring(2);
    // }

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
            Thread.sleep(this.serviceTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //send prcessed product to next queue
        outQueue.addProduct(currentProduct);
        this.currentColor = Machine.defaultColor;
    }

    public void notifyObservers() {
        for (Queue observer : observerList) {
            System.out.println("Machine "+this.id+" is notifying queue "+observer.getId());
            observer.update(this);
            webSocketController.sendUpdate("Product processed by machine " + this.id);
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
