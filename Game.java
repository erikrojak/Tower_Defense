import java.util.ArrayList;
import java.util.Random;

public class Game {
    
    GameMap gameMap;
    Enemy enemy;
    
    /** Called by the Manager every 25 ms (see [Timers] in sbge.ini). */
    public void tick() {
        if (gameMap == null) {
            gameMap = new GameMap("map.txt");
            ArrayList<Waypoint> starts = gameMap.getStarts();
            if (!starts.isEmpty()) {
                Waypoint start = starts.get(new Random().nextInt(starts.size()));
                enemy = new Enemy(start, 2.0);
            }
        }
        if (enemy == null) {
            return;
        }
        if (!enemy.hasReachedEnd()) {
            enemy.move(gameMap.getPath());
        } else {
            enemy.remove();
            enemy = null;
        }
    }
}
