package letsapps.com.letscube.util;

/**
 * Created by marti on 26/08/2016.
 */
public class Circle {
    public float x;
    public float y;
    public float radius;

    public Circle(){

    }

    public Circle(Circle circle){
        this(circle.x, circle.y, circle.radius);
    }

    public Circle(float x, float y, float radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public String toString(){
        return "((" + x + ", " + y + "), " + radius + ")";
    }

    public boolean isEmpty(){
        return x == 0 && y == 0 && radius == 0;
    }
}
