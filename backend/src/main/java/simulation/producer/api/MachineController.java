package simulation.producer.api;

import org.springframework.web.bind.annotation.RestController;

import simulation.producer.managers.MachineManager;
import simulation.producer.models.Machine;
import simulation.producer.models.RequestObject;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
public class MachineController {
    
    @PostMapping("/machine/add")
    public ArrayList<Machine> addMachine(@RequestBody RequestObject requestObject) {
        return MachineManager.getInstance().addMachine(
            requestObject.getData().get("x").toString(),
            requestObject.getData().get("y").toString(),
            requestObject.getData().get("defaultColor").toString()
        );
    }

    @PutMapping("/machine/connect")
    public void connectMAchineToQueue(@RequestBody RequestObject requestObject) {
        MachineManager.getInstance().connectMAchineToQueue(
            Integer.parseInt(requestObject.getData().get("machineId").toString()),
            Integer.parseInt(requestObject.getData().get("queueId").toString())
        );
    }

}
