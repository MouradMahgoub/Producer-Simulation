package simulation.producer.models.memento;

public class Originator {
    private String state;

    public Originator(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public Memento save (){
        return new Memento(state);
    }
    
    public void restore (Memento memento){
        state = memento.getState();
    }
}
