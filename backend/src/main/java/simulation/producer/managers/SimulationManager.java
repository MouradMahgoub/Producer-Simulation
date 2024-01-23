package simulation.producer.managers;

import java.util.ArrayList;
import java.util.Random;

import simulation.producer.models.Machine;
import simulation.producer.models.Product;
import simulation.producer.models.Queue;
import simulation.producer.models.ResponseObject;
import simulation.producer.models.memento.CareTaker;
import simulation.producer.models.memento.Memento;
import simulation.producer.models.memento.Originator;

public class SimulationManager {
    ArrayList<Queue> queues = new ArrayList<Queue>();
    ArrayList<Machine> machines = new ArrayList<Machine>(); 
    ArrayList<ArrayList<Product>> careTaker = new ArrayList<>();
    boolean paused = false;
    boolean replay = false;
    
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

    public synchronized void save(ArrayList<Product> products){
        careTaker.add(products);
    }   
    
    public void newSimulation(){
        queues.clear();
        machines.clear();
        careTaker.clear();
    }

    //start simulation
    public void start() {
        try {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0; i<3; i++){
                        try {
                            //int numOfProducts = (new Random()).nextInt(2, 7);
                            ArrayList<Product> inputProducts = generateInputProduct();
                            save(inputProducts);
                            for(Product product : inputProducts){
                                queues.get(0).addProduct(product);
                                System.out.println("Product " + product.getId() + " added to queue 0");
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

    public ArrayList<Product> generateInputProduct(){
        ArrayList<Product> inputProducts = new ArrayList<>();
        for(int i=0; i<(new Random()).nextInt(2, 7); i++)
            inputProducts.add(new Product());
        return inputProducts;
    }

    public synchronized void pause() {
        paused = true;
        for (Machine machine : machines)
            machine.pause();
    }

    public synchronized void resume() {
        paused = false;
        for (Machine machine : machines) 
            machine.resume();
    }

    public synchronized void replay() {
        replay = true;
        for(Queue queue : queues)
            queue.clear();
        for(ArrayList<Product> inputProducts : careTaker){
            for(Product product : inputProducts){
                queues.get(0).addProduct(product);
                System.out.println("Product " + product.getId() + " added to queue 0");
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        replay = false;
        pause();
    }


    public static void main(String[] args) {

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
        // Thread thread = new Thread(new Runnable() {
        //     @Override
        //     public void run() {
        //         while(true){
                    try {
                        Thread.sleep(20000);
                        System.out.println("replay =============================================================");
                        SimulationManager.getInstance().replay();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        //         }
        //     }
        // });
    }
}
