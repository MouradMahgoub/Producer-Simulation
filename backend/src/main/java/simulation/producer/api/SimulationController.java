package simulation.producer.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import simulation.producer.managers.SimulationManager;

@Controller
public class SimulationController {

    @MessageMapping("/start")
    // @SendTo("/topic/updates")
    public void handleStart(String message) {
        SimulationManager.getInstance().start();
        // return "Simulation started!";
    }

    @MessageMapping("/stop")
    // @SendTo("/topic/updates")
    public void handleStop(String message) {
        SimulationManager.getInstance().stop();
        // return "Simulation stopped!";
    }

    @MessageMapping("/pause")
    // @SendTo("/topic/updates")
    public void handlePause(String message) {

        SimulationManager.getInstance().pause();
        // return "Simulation paused!";
    }
}
