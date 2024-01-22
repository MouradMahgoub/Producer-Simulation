package simulation.producer.models;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {
    private List<Machine> machines;
    private List<Queue> queues;

    public ResponseObject(List<Machine> machines, List<Queue> queues) {
        // Make a deep copy to avoid reference sharing
        this.machines = new ArrayList<>(machines);
        this.queues = new ArrayList<>(queues);
    }


    public List<Machine> getMachines() {
        return machines;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "machines=" + machines +
                ", queues=" + queues +
                '}';
    }
}
