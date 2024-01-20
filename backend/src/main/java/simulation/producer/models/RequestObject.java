package simulation.producer.models;

import java.util.Map;
import java.util.Objects;

public class RequestObject {
    private Map<String, Object> data;

    public RequestObject() {
    }

    public RequestObject(Map<String,Object> data) {
        this.data = data;
    }

    public Map<String,Object> getData() {
        return this.data;
    }

    public void setData(Map<String,Object> data) {
        this.data = data;
    }

    public RequestObject data(Map<String,Object> data) {
        setData(data);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RequestObject)) {
            return false;
        }
        RequestObject requestObject = (RequestObject) o;
        return Objects.equals(data, requestObject.data);
    }

    @Override
    public String toString() {
        return "{" +
            " data='" + getData() + "'" +
            "}";
    }
    
}
