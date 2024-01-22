package simulation.producer.api;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import simulation.producer.managers.SimulationManager;
import simulation.producer.models.ResponseObject;
import simulation.producer.models.memento.CareTaker;

@Component
public class SimulationUpdater {
    private boolean flagForLogic = false;
    private boolean flagForReplay = false;

    private static int count=0;

    CareTaker memento;

    private final SimpMessagingTemplate messagingTemplate;

    public SimulationUpdater(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 100)
    public void sendSimulationUpdates() {
        messagingTemplate.convertAndSend("/topic/updates", new ResponseObject(SimulationManager.getInstance().getMachines(), SimulationManager.getInstance().getQueues()));
        System.out.println(SimulationManager.getInstance().getMachines());
    }

    @Scheduled(fixedRate = 1000)
    public void sendToMemento() {
        if(flagForLogic) {
            System.out.println("zzzzzzzzzzzzzzzzzzzz");
            memento.add(new ResponseObject(SimulationManager.getInstance().getMachines(), SimulationManager.getInstance().getQueues()));
        }
    }

    @Scheduled(fixedRate = 1000)
    public void Replay() {
        if(flagForReplay && count<memento.getSize()) {
            System.out.println("zzzzzzzzzzzzzzzzzzzz");
           memento.get(count);
            count++;
        }
    }

    public void setFlagForLogic(boolean flag) {
        flagForLogic = flag;
    }

    public void setFlagForReplay(boolean flag) {
        flagForLogic = flag;
    }

}
