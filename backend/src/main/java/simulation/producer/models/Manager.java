package simulation.producer.models;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.List;
import java.util.ArrayList;

@Component
public class Manager {

    private static  WebSocketController webSocketController;

    @Autowired
    public Manager(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }
    ArrayList<Queue> queues = new ArrayList<Queue>();
    ArrayList<Machine> machines = new ArrayList<Machine>();
    ArrayList<Product> products = new ArrayList<Product>();





    public static void main(String[] args) {

        Queue q0 = new Queue();
        Queue q1 = new Queue();
        Machine m0 = new Machine(webSocketController);
        Machine m1 = new Machine(webSocketController);

        for (int i = 0; i < 10; i++) {
            q0.addProduct(new Product());
        }

        m1.attach(q0);
        m0.attach(q0);

        m1.setOutQueue(q1);
        m0.setOutQueue(q1);

        Thread m0Thread = new Thread(m0);
        Thread m1Thread = new Thread(m1);

        m1Thread.start();
        m0Thread.start();


    }
}
