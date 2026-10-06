import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class DungeonPanel extends JPanel {
    private static final int TILE_SIZE = 14;
    static final Color WALL = new Color(30, 37, 36);
    static final Color FLOOR = new Color(177, 194, 157);
    static final Color UP_STAIRS = new Color(226, 190, 100);
    static final Color DOWN_STAIRS = new Color(112, 174, 188);

    private String[][] map;

    public DungeonPanel(String[][] map) {
        setMap(map);
    }

    public void setMap(String[][] map) {
        this.map = map;
        setBackground(WALL);
        setPreferredSize(new Dimension(map.length * TILE_SIZE, map[0].length * TILE_SIZE));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                String mapValue = map[x][y];
                g.setColor(getTileColor(mapValue));
                if ("#".equals(mapValue)) {
                    g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    g.fillRoundRect(x * TILE_SIZE + 1, y * TILE_SIZE + 1,
                            TILE_SIZE - 2, TILE_SIZE - 2, 3, 3);
                }
            }
        }
        g.dispose();
    }

    // Map symbols are mapped to their visual tile colors here.
    private Color getTileColor(String mapValue) {
        return switch (mapValue) {
            case "#" -> WALL;
            case "." -> FLOOR;
            case "<" -> UP_STAIRS;
            case ">" -> DOWN_STAIRS;
            default -> Color.MAGENTA;
        };
    }
}
