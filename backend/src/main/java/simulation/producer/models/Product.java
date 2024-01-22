package simulation.producer.models;
import java.util.Objects;
import java.util.Random;

public class Product {
    private static int count = 0;
    private int id;
    private String color;

    public Product() {
        this.id = count++;
        Random random = new Random();
        this.color = String.format("#%02X%02X%02X", random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return this.id;
    }

    public Product color(String color) {
        setColor(color);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(color, product.color) && id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, id);
    }

    @Override
    public String toString() {
        return "{" +
            " color='" + getColor() + "'" +
            ", id='" + getId() + "'" +
            "}";
    }
    
}
