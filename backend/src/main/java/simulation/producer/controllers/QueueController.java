package simulation.producer.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

import simulation.producer.models.Queue;
import simulation.producer.models.RequestObject;
import simulation.producer.services.QueueManager;


@RestController
public class QueueController {
        
        @PostMapping("/queue/add")
        public ArrayList<Queue> addQueue(@RequestBody RequestObject requestObject) {
            return QueueManager.getInstance().addQueue(
                requestObject.getData().get("x").toString(),
                requestObject.getData().get("y").toString(),
                requestObject.getData().get("defaultColor").toString()
            );
        }
    
        @PostMapping("/queue/connect")
        public ArrayList<Queue> connectQueueToMachine(@RequestBody RequestObject requestObject) {
            return QueueManager.getInstance().connectQueueToMachine(
                Integer.parseInt(requestObject.getData().get("queueId").toString()),
                Integer.parseInt(requestObject.getData().get("machineId").toString())
            );
        }
    
        // @PostMapping("/queue/remove")
        // public String removeQueue(@RequestBody String queue) {
        //     return queue;
        // }
}
