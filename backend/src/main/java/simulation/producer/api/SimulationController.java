package simulation.producer.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import simulation.producer.managers.SimulationManager;

@Controller
public class SimulationController {

    @MessageMapping("/start")
    public void handleStart(String message) {
        SimulationManager.getInstance().start();
    }

    @MessageMapping("/pause")
    public void handlePause(String message) {
        SimulationManager.getInstance().pause();
    }

    
}
