package simulation.producer.managers;

import java.util.ArrayList;

import simulation.producer.models.Machine;
import simulation.producer.models.Queue;
import simulation.producer.models.WebSocketController;

public class MachineManager {
    private static MachineManager instance = null;
    WebSocketController myWebSocketController = new WebSocketController();
    
    private MachineManager() {
    }

    public static synchronized MachineManager getInstance() {
        if (instance == null) {
            instance = new MachineManager();
        }
        return instance;
    }

    public ArrayList<Machine> addMachine(String x, String y, String defaultColor) {
        SimulationManager.getInstance().machines.add(new Machine(x, y, defaultColor,myWebSocketController));
        return SimulationManager.getInstance().machines;
    }


    public ArrayList<Machine> connectMAchineToQueue(int machineId, int queueId) {
        for(Machine machine : SimulationManager.getInstance().machines){
            if(machine.getId() == machineId)
            for(Queue queue : SimulationManager.getInstance().queues){
                if(queue.getId() == queueId){
                    machine.setOutQueue(queue);
                    break;
                }
            }
        }
        return SimulationManager.getInstance().machines;
    }

    
    // public void removeMachine(int machineId) {
    //     for(Machine machine : machines){
    //         if(machine.getId() == machineId){
    //             machines.remove(machine);
    //             return;
    //         }
    //     }
    // }   
}
