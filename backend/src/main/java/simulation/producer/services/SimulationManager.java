package simulation.producer.services;

import java.util.ArrayList;
import java.util.Random;

import simulation.producer.models.Machine;
import simulation.producer.models.Product;
import simulation.producer.models.Queue;

public class SimulationManager {
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

    public static void main(String[] args) {

        // BlockingQueue<Product> products = new LinkedBlockingQueue<Product>();
        // for (int i = 0; i < 10; i++) {
        //     products.add(new Product());
        // }

        // SimulationManager simulationManager = new SimulationManager();
        
        // simulationManager.addQueue("1", "1", "hdh");
        // simulationManager.addQueue("2", "2", "hdh");
        // simulationManager.addQueue("3", "3", "hdh");
        // simulationManager.addQueue("4", "4", "hdh");
        // simulationManager.addMachine("1", "1", "hdh");
        // simulationManager.addMachine("2", "2", "hdh");
        // simulationManager.addMachine("3", "3", "hdh");
        // simulationManager.addMachine("4", "4", "hdh");
        
        // simulationManager.connectQueueToMachine(0, 0);
        // simulationManager.connectQueueToMachine(0, 1);
        // simulationManager.connectQueueToMachine(0, 2);
        
        // simulationManager.connectMAchineToQueue(0, 1);
        // simulationManager.connectMAchineToQueue(1, 1);
        // simulationManager.connectMAchineToQueue(2, 2);
        
        // simulationManager.connectQueueToMachine(1, 3);
        // simulationManager.connectQueueToMachine(2, 3);
        
        // simulationManager.connectMAchineToQueue(3, 3);
        
        // // simulationManager.setProducts(new ArrayList<Product>(products));
        
        // simulationManager.start();
    }
}
