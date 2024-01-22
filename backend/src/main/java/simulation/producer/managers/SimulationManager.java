package simulation.producer.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simulation.producer.models.Machine;
import simulation.producer.models.Product;
import simulation.producer.models.Queue;
import simulation.producer.models.ResponseObject;
import simulation.producer.models.memento.CareTaker;

public class SimulationManager {
    private static int count=0;
    private static int num=0;
    static CareTaker memento=new CareTaker();
    ArrayList<Queue> queues = new ArrayList<Queue>();
    ArrayList<Machine> machines = new ArrayList<Machine>();
    private static SimulationManager instance = null;

    private SimulationManager() {
    }

    public static synchronized SimulationManager getInstance() {
        if (instance == null) {
            instance = new SimulationManager();
        }
        return instance;
    }

    public ArrayList<Queue> getQueues() {
        return queues;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }
    //start simulation
    public void start() {
        try {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            for(int i = 0; i < (new Random()).nextInt(2, 5); i++){
                                queues.get(0).addProduct(new Product());
                                System.out.println("Product " +  " added to queue 0");
                            }
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            })).start();
            Thread.sleep(100);
            
            for(Machine machine : machines){
                Thread thread = new Thread(machine);
                thread.start();
                SimulationManager.getInstance().sendToMemento();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //pause simulation
    public void pause() {
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            thread.suspend();
        }
    }

    //resume simulation
    public void resume() {
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            thread.resume();
        }
    }

    //stop simulation
    public void stop() {
        for(Machine machine : machines){
            Thread thread = new Thread(machine);
            thread.stop();
        }
    }

    public void sendToMemento() {
            System.out.println("mmmmmmmmmmmmmmmmmmmmmmm");
        memento.add(new ResponseObject(getMachines(),getQueues()));
            System.out.println(memento.get(count++));
    }

    public void Replay() {
        for(int i=0;i<memento.getSize();i++) {
            System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            System.out.println(memento.get(num++));
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // SimulationManager simulationManager = new SimulationManager();
        
        QueueManager.getInstance().addQueue("1", "1");
        QueueManager.getInstance().addQueue("2", "2");
        QueueManager.getInstance().addQueue("3", "3");
        QueueManager.getInstance().addQueue("4", "4");
        MachineManager.getInstance().addMachine("1", "1", "hdh");
        MachineManager.getInstance().addMachine("2", "2", "hdh");
        MachineManager.getInstance().addMachine("3", "3", "hdh");
        MachineManager.getInstance().addMachine("4", "4", "hdh");
        
        QueueManager.getInstance().connectQueueToMachine(0, 0);
        QueueManager.getInstance().connectQueueToMachine(0, 1);
        QueueManager.getInstance().connectQueueToMachine(0, 2);
        
        MachineManager.getInstance().connectMAchineToQueue(0, 1);
        MachineManager.getInstance().connectMAchineToQueue(1, 1);
        MachineManager.getInstance().connectMAchineToQueue(2, 2);
        
        QueueManager.getInstance().connectQueueToMachine(1, 3);
        QueueManager.getInstance().connectQueueToMachine(2, 3);
        
        MachineManager.getInstance().connectMAchineToQueue(3, 3);

        SimulationManager.getInstance().start();
        Thread.sleep(10000);  // Allow simulation to run


            SimulationManager.getInstance().Replay();




        System.out.println("Lllllllllllllll");


    }
}
