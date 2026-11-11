import fri.shapesge.Circle;
public class Waypoint {
    private int x;
    private int y;
    private boolean end;

    public Waypoint(int x, int y) {
        this(x, y, false);
    }

    public Waypoint(int x, int y, boolean end) {
        this.x = x;
        this.y = y;
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void debugPoint() {
        Circle circle = new Circle(x, y);
        circle.changeSize(10);
        circle.changeColor("red");
        circle.makeVisible();
    }   
}
