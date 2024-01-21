// package simulation.producer.api;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.CrossOrigin;

// import simulation.producer.managers.SimulationManager;

// @Controller
// @CrossOrigin

// public class WebSocketController {

//     @Autowired
//     private SimpMessagingTemplate messagingTemplate;

//     @MessageMapping("/sendUpdate")
//     public void sendUpdate(String message) {
//         messagingTemplate.convertAndSend("/topic/updates", SimulationManager.getInstance().getMachines());
//     }
// }