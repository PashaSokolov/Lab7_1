import java.io.Serializable;

public class Location implements Serializable {
    private float x;
    private int y; //Поле не может быть null
    private Float z;

    public Location(float x, int y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }
}