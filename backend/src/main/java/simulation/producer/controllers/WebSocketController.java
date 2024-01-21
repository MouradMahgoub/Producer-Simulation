package simulation.producer.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendUpdate")
    @SendTo("/topic/updates")
    public String sendUpdate(String message) {
        System.out.println("kkkkkkkkkkkkk");
        return "kkkkkkkkkkkk";
    }


}