import java.util.ArrayList;
import fri.shapesge.Rectangle;
import java.io.File;
import java.util.Scanner;

public class GameMap {
    private static final int ROWS = 10;
    private static final int COLS = 20;
    private static final int TILE_SIZE = 64; 

    private int[][] map;
    private ArrayList<Rectangle> rectangles;
    private ArrayList<Waypoint> path;
    private ArrayList<Waypoint> starts;

    public GameMap(String mapFile) {
        this.rectangles = new ArrayList<>();
        this.path = new ArrayList<>();
        this.starts = new ArrayList<>();
        this.map = new int[ROWS][COLS];
        this.loadFromFile(mapFile);
        this.buildMap();
    }

    private void loadFromFile(String mapFile) {
        try  {
            File file = new File(mapFile);
            Scanner scanner = new Scanner(file);
            for (int row = 0; row < ROWS && scanner.hasNextLine();row++) {
                for (int col = 0; col < COLS && scanner.hasNext(); col++) {
                    this.map[row][col] = scanner.nextInt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildMap() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                Rectangle rectangle = new Rectangle(x, y);
                rectangle.changeSize(TILE_SIZE, TILE_SIZE);

                if (this.map[row][col] == 0) {
                    // path (pavement)
                    rectangle.changeColor("brown");
                    this.path.add(new Waypoint(x, y));
                } else if (this.map[row][col] == 1) {
                    // grass
                    rectangle.changeColor("");
                } else if (this.map[row][col] == 2){
                    //start
                    rectangle.changeColor("blue");
                    Waypoint start = new Waypoint(x, y);
                    this.path.add(start);
                    this.starts.add(start);
                }
                else if (this.map[row][col] == 3){
                    //end
                    rectangle.changeColor("yellow");
                    this.path.add(new Waypoint(x, y, true));
                }

                rectangle.makeVisible();
                this.rectangles.add(rectangle);

                if (this.map[row][col] == 0) {
                    this.path.get(this.path.size() - 1).debugPoint();
                } // debugging: visualize waypoints
            }
        }
    }

    public ArrayList<Waypoint> getPath() {
        return this.path;
    }

    public ArrayList<Waypoint> getStarts() {
        return this.starts;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public boolean canBuildTowerAt(int x, int y) {
        int col = x / TILE_SIZE;
        int row = y / TILE_SIZE;

        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false; // Out of bounds
        }

        return this.map[row][col] != 0; // Can build if it's not a path tile
    }
}
