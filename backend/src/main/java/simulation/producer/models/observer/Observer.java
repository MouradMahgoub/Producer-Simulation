package simulation.producer.models.observer;

import org.springframework.beans.factory.annotation.Autowired;

public interface Observer {
    public void update(Subject subject);
}
