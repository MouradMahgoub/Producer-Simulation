package simulation.producer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import simulation.producer.managers.MachineManager;
import simulation.producer.managers.SimulationManager;
import simulation.producer.models.Machine;
import simulation.producer.models.RequestObject;

import java.util.ArrayList;

@RestController
public class SimulationController {

    @GetMapping("/start")
    public void start(@RequestBody RequestObject requestObject) {
        SimulationManager.getInstance().start();
    }
    
}
