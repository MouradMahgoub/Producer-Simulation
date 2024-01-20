package simulation.producer.controllers;

import org.springframework.web.bind.annotation.RestController;

import simulation.producer.services.MachineManager;
import simulation.producer.models.Machine;
import simulation.producer.models.RequestObject;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class MachineController {
    
    @PostMapping("/machine/add")
    public ArrayList<Machine> addMachine(@RequestBody RequestObject requestObject) {
        return MachineManager.getInstance().addMachine(
            requestObject.getData().get("x").toString(),
            requestObject.getData().get("y").toString(),
            requestObject.getData().get("defaultColor").toString()
        );
    }

    @PostMapping("/machine/connect")
    public ArrayList<Machine> connectMAchineToQueue(@RequestBody RequestObject requestObject) {
        return MachineManager.getInstance().connectMAchineToQueue(
            Integer.parseInt(requestObject.getData().get("machineId").toString()),
            Integer.parseInt(requestObject.getData().get("queueId").toString())
        );
    }

    // @PostMapping("/machine/remove")
    // public String removeMachine(@RequestBody String machine) {
    //     return machine;
    // }
}
