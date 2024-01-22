package simulation.producer.api;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import simulation.producer.managers.QueueManager;
import simulation.producer.models.Queue;
import simulation.producer.models.RequestObject;


@RestController
@CrossOrigin
public class QueueController {
        
        @PostMapping("/queue/add")
        public ArrayList<Queue> addQueue(@RequestBody RequestObject requestObject) {
            return QueueManager.getInstance().addQueue(
                requestObject.getData().get("x").toString(),
                requestObject.getData().get("y").toString()
            );
        }
    
        @PutMapping("/queue/connect")
        public void connectQueueToMachine(@RequestBody RequestObject requestObject) {
            QueueManager.getInstance().connectQueueToMachine(
                Integer.parseInt(requestObject.getData().get("queueId").toString()),
                Integer.parseInt(requestObject.getData().get("machineId").toString())
            );
        }
}
