package simulation.producer.models.memento;

import simulation.producer.models.Machine;
import simulation.producer.models.Queue;
import simulation.producer.models.ResponseObject;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private List<ResponseObject> mementoList = new ArrayList<>();

    public void add(ResponseObject state) {
        // Make a deep copy to avoid reference sharing
        List<Machine> machinesCopy = new ArrayList<>();
        for (Machine machine : state.getMachines()) {
            // Assuming Machine class has a copy constructor
            machinesCopy.add(new Machine(machine));
        }

        List<Queue> queuesCopy = new ArrayList<>();
        for (Queue queue : state.getQueues()) {
            // Assuming Queue class has a copy constructor
            queuesCopy.add(new Queue(queue));
        }

        mementoList.add(new ResponseObject(machinesCopy, queuesCopy));
    }


    public ResponseObject get(int index) {
        return mementoList.get(index);
    }

    public int getSize() {
        return mementoList.size();
    }
}
