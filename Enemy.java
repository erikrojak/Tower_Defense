import fri.shapesge.Circle;
import fri.shapesge.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private double x;
    private double y;
    private double speed;
    private Waypoint current;                               // waypoint we're standing on / last reached
    private Waypoint target;                                // waypoint we're currently walking to
    private ArrayList<Waypoint> visited = new ArrayList<>();
    private boolean finished;
    private Random random = new Random();
    Circle body;
    Rectangle healthBar;

    int BODY_SIZE = 40;
    int HEALTH_BAR_WIDTH = 40;
    int HEALTH_BAR_HEIGHT = 5;

    public Enemy(Waypoint start, double speed) {
        this.x = start.getX();
        this.y = start.getY();
        this.speed = speed;
        this.current = start;
        this.visited.add(start);
        this.finished = start.isEnd();

        // Create the enemy's body (circle)
        body = new Circle();
        body.changeSize(BODY_SIZE);
        body.changeColor("red");

        // Create the health bar (rectangle)
        healthBar = new Rectangle();
        healthBar.changeSize(HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
        healthBar.changeColor("green");

        updateShapes();
        body.makeVisible();
        healthBar.makeVisible();
    }

    /** Places the shapes at the current (x, y), centered in the 32px tile. */
    private void updateShapes() {
        int px = (int) Math.round(this.x);
        int py = (int) Math.round(this.y);
        body.changePosition(px + 6, py + 6);
        healthBar.changePosition(px + 6, py);
    }

    /**
     * Moves one step along the road. When the enemy reaches a waypoint, it
     * picks the closest adjacent waypoint it hasn't visited yet (the road has
     * no dead ends by design). It stops once it steps on an end waypoint.
     */
    public void move(ArrayList<Waypoint> path) {
        if (this.finished) {
            return;
        }
        if (this.target == null) {
            this.target = chooseNextWaypoint(path);
        }

        double dx = this.target.getX() - this.x;
        double dy = this.target.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= this.speed) {
            // Arrived at the target waypoint
            this.x = this.target.getX();
            this.y = this.target.getY();
            this.current = this.target;
            this.visited.add(this.target);
            if (this.target.isEnd()) {
                finished = true;
            }
            this.target = null;
        } else {
            this.x += (dx / distance) * this.speed;
            this.y += (dy / distance) * this.speed;
        }

        updateShapes();
    }

    /** Returns the closest unvisited neighbouring waypoint. */
    private Waypoint chooseNextWaypoint(ArrayList<Waypoint> path) {
        ArrayList<Waypoint> closest = new ArrayList<>();
        double bestDistance = Double.MAX_VALUE;

        for (Waypoint w : path) {
            if (this.visited.contains(w)) {
                continue;
            }
            double d = distanceBetween(this.current, w);
            if (d > GameMap.getTileSize()) {
                continue; // not a neighbouring tile
            }
            if (d < bestDistance) {
                bestDistance = d;
                closest.clear();
                closest.add(w);
            } else if (d == bestDistance) {
                closest.add(w);
            }
        }

        // Several equally close options (a fork): pick one at random
        return closest.get(random.nextInt(closest.size()));
    }

    private static double distanceBetween(Waypoint a, Waypoint b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean hasReachedEnd() {
        return finished;
    }

    public void remove() {
        this.body.makeInvisible();
        this.healthBar.makeInvisible();
    }
}
