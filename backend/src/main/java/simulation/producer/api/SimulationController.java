package simulation.producer.api;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import simulation.producer.managers.SimulationManager;

@Controller
public class SimulationController {
    SimulationUpdater updater;

    @MessageMapping("/start")
    @SendTo("/topic/updates")
    public String handleStart(String message) {
        SimulationManager.getInstance().sendToMemento();
        return "Simulation started!";
    }

    @MessageMapping("/stop")
    @SendTo("/topic/updates")
    public String handleStop(String message) {
        SimulationManager.getInstance().stop();
        return "Simulation stopped!";
    }

    @MessageMapping("/pause")
    @SendTo("/topic/updates")
    public String handlePause(String message) {
        SimulationManager.getInstance().pause();
        return "Simulation paused!";
    }

    @MessageMapping("/replay")
//    @SendTo("/topic/updates")
    public String replay(String message) {
        SimulationManager.getInstance().Replay();
        return "Simulation paused!";
    }
}
