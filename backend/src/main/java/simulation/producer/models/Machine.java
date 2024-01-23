package simulation.producer.models;

import simulation.producer.models.observer.Subject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Machine extends Subject implements Runnable{

    private static int count = 0;
    private static String defaultColor;

    
    private int id;
    private String x;
    private String y;

    private ArrayList<Queue> observerList = new ArrayList<Queue>();
    private Queue outQueue;
    private String currentColor;
    private int serviceTime;
    private int remainingTime;
    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    private AtomicBoolean isRunning = new AtomicBoolean(true);
    private boolean paused = false;

    public Machine(String x, String y, String defaultColor){
        this.id = count++;
        this.x = x;
        this.y = y;
        this.serviceTime = (new Random()).nextInt(2, 8)*1000;
        this.remainingTime = this.serviceTime;
        this.currentColor = defaultColor;
        Machine.defaultColor = defaultColor;
    }
    
    // generate a random color in hexa representation
    public static String generateRandomColor(){
        Random random = new Random();
        return "#"+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2)+Integer.toHexString(random.nextInt(255)).substring(2);
    }

    public synchronized void process(Product currentProduct) throws Exception{
        this.currentColor = currentProduct.getColor();
        try {
            System.out.println("Machine "+this.id+" is processing product "+currentProduct.getId()+" for "+this.serviceTime+" ms");
            while((remainingTime-=1000) > 0){
                Thread.sleep(1000);
                while (paused) {wait();}
            }
            this.remainingTime = this.serviceTime;
            //send prcessed product to next queue
            outQueue.addProduct(currentProduct);
            this.currentColor = Machine.defaultColor;
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyObservers() {
        for (Queue observer : observerList) {
            if(!isRunning.get()) break;
            System.out.println("Machine "+this.id+" is notifying queue "+observer.getId());
            observer.update(this);
        }
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    @Override
    public void run() {
        while(this.isRunning.get() || this.outQueue == null){
            try {
                notifyObservers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning.set(false);
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

    public void setObserverList(ArrayList<Queue> observerList) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return count == machine.count && id == machine.id && serviceTime == machine.serviceTime && Objects.equals(observerList, machine.observerList) && Objects.equals(outQueue, machine.outQueue) && Objects.equals(defaultColor, machine.defaultColor) && Objects.equals(currentColor, machine.currentColor)&& remainingTime==machine.remainingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, observerList, outQueue, id, defaultColor, currentColor,remainingTime,serviceTime);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "count=" + count +
                ", observerList=" + observerList +
                ", outQueue=" + outQueue +
                ", id=" + id +
                ", defaultColor=" + defaultColor +
                ", remainingTime=" + remainingTime +
                ", currentColor=" + currentColor +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
