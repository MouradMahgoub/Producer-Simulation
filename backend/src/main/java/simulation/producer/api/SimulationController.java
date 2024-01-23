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

    @MessageMapping("/newSimulation")
    public void handleNewSimulation(String message) {
        SimulationManager.getInstance().newSimulation();
    }

    @MessageMapping("/pause")
    public void handlePause(String message) {
        SimulationManager.getInstance().pause();
    }

    @MessageMapping("/resume")
    public void handleResume(String message) {
        SimulationManager.getInstance().resume();
    }

    @MessageMapping("/replay")
    public void handleReply(String message) {
        SimulationManager.getInstance().replay();
    }
}
