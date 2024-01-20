package simulation.producer.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simulation.producer.models.observer.Subject;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Machine extends Subject implements Runnable{

    private WebSocketController webSocketController;
    private static int count=0;
    private int id = 0;

    private List<Queue> observerList=new ArrayList<>();
    private Queue outQueue = new Queue();
    private Color defaultColor;
    private Color currentColor;
    private int serviceTime;
//    private boolean state;

    @Autowired
    public Machine(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }
    public Machine(){
        this.id = count++;
        Random randcol = new Random();
        this.currentColor = new Color(randcol.nextInt(256), randcol.nextInt(256), randcol.nextInt(256));
    }

    public Machine(Color color){
        this.id = count++;
        this.defaultColor = color;
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


    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

//    public boolean isState() {
//        return state;
//    }
//
//    public void setReady(boolean ready) {
//        state = ready;
//    }


    public void attach(Queue addQueue){
        observerList.add(addQueue);
    }

    public void detach(Queue removeQueue){
        observerList.remove(removeQueue);
    }

    public synchronized void process(Product currentProduct){
        this.currentColor = currentProduct.getColor();
        Random randtime = new Random();
        this.serviceTime=randtime.nextInt(2,10);
        this.serviceTime=this.serviceTime*1000;
        try {
            System.out.println("Machine "+this.id+" is processing product "+currentProduct.getId()+" for "+this.serviceTime+" ms");
            Thread.sleep(this.serviceTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.currentColor = this.defaultColor;
        //send prcessed product to next queue
        outQueue.addProduct(currentProduct);

    }

    public void notifyObservers() {
        for (Queue observer : observerList) {
            System.out.println("Machine "+this.id+" is notifying queue "+observer.getId());
            observer.update(this);

//        if (webSocketController != null) {

            webSocketController.sendUpdate("kkkkkkkkkkkkkkkkkkkkkk " + this.id + " processed something");
//        }
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                // Thread.sleep(1000);
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
        return id == machine.id && serviceTime == machine.serviceTime && Objects.equals(observerList, machine.observerList) && Objects.equals(outQueue, machine.outQueue) && Objects.equals(defaultColor, machine.defaultColor) && Objects.equals(currentColor, machine.currentColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, observerList, outQueue, defaultColor, currentColor, serviceTime);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", observerList=" + observerList +
                ", outQueue=" + outQueue +
                ", defaultColor=" + defaultColor +
                ", currentColor=" + currentColor +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
