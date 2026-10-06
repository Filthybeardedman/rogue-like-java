import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DungeonGenerator generator = new DungeonGenerator(100, 50, 10);
            String[][][] maps = generator.createTileMaps();
            int initialDepth = 0;
            
            DungeonWindow window = new DungeonWindow();
            window.showWindow(maps, initialDepth);
        });
    }
}
