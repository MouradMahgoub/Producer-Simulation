package simulation.producer.services;

import java.util.ArrayList;

import simulation.producer.models.Machine;
import simulation.producer.models.Queue;

public class QueueManager {
    
    private static QueueManager instance = null;

    private QueueManager() {
    }
    public static synchronized QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public ArrayList<Queue> addQueue(String x, String y, String defaultColor) {
        SimulationManager.getInstance().queues.add(new Queue(x, y, defaultColor));
        return SimulationManager.getInstance().queues;
    }

    public ArrayList<Queue> connectQueueToMachine(int queueId, int machineId) {
        for(Machine machine : SimulationManager.getInstance().machines){
            if(machine.getId() == machineId)
            for(Queue queue : SimulationManager.getInstance().queues){
                if(queue.getId() == queueId){
                    machine.attach(queue);
                    break;
                }
            }
        }
        return SimulationManager.getInstance().queues;
    }  
    
    // public void removeQueue(int queueId) {
    //     for(Queue queue : queues){
    //         if(queue.getId() == queueId){
    //             queues.remove(queue);
    //             return;
    //         }
    //     }
    // }

}
