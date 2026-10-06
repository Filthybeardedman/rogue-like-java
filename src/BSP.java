import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BSP {
    private static final Color BACKGROUND = new Color(24, 29, 30);
    private static final Color SURFACE = new Color(34, 41, 41);
    private static final Color WALL = new Color(30, 37, 36);
    private static final Color FLOOR = new Color(177, 194, 157);
    // タイルを追加するときは、ここに表示色を定義する。
    private static final Color TEXT = new Color(230, 234, 222);
    private static final Color MUTED_TEXT = new Color(157, 169, 155);

    private final int w;
    private final int h;
    private final BSPNode root;

    public BSP(int w, int h) {
        this.w = w;
        this.h = h;
        this.root = new BSPNode(0, 0, w, h);
    }

    public String[][] createMap() {
        String[][] map = new String[w][h];
        for (String[] row : map) {
            Arrays.fill(row, "#");
        }
        root.writeMap(map);
        return map;
    }

    public void printMap(String[][] map) {
        for (String[] row : map) {
            StringBuilder sb = new StringBuilder();
            for (String cell : row) {
                sb.append(cell);
            }
            System.out.println(sb.toString());
        }
    }

    public void showUI() {
        String[][] map = createMap();
        MapPanel mapPanel = new MapPanel(map);

        JFrame frame = new JFrame("BSP Dungeon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BACKGROUND);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SURFACE);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel titleGroup = new JPanel(new BorderLayout(0, 4));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("BSP DUNGEON");
        title.setForeground(TEXT);
        title.setFont(title.getFont().deriveFont(17f).deriveFont(java.awt.Font.BOLD));
        JLabel dimensions = new JLabel(w + " x " + h + "   /   " + (w * h) + " tiles");
        dimensions.setForeground(MUTED_TEXT);
        titleGroup.add(title, BorderLayout.NORTH);
        titleGroup.add(dimensions, BorderLayout.SOUTH);
        header.add(titleGroup, BorderLayout.WEST);

        JButton refreshButton = new JButton("更新");
        refreshButton.setForeground(TEXT);
        refreshButton.setBackground(new Color(66, 82, 67));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        refreshButton.addActionListener(event -> mapPanel.repaint());
        header.add(refreshButton, BorderLayout.EAST);

        JPanel mapHolder = new JPanel(new GridBagLayout());
        mapHolder.setBackground(BACKGROUND);
        mapHolder.add(mapPanel);

        JScrollPane scrollPane = new JScrollPane(mapHolder);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        footer.setBackground(SURFACE);
        // タイルを追加したら、色と名前を指定して凡例にも追加する。
        footer.add(createLegendItem(WALL, "壁"));
        footer.add(createLegendItem(FLOOR, "部屋・通路"));

        frame.add(header, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);
        frame.setSize(900, 760);
        frame.setMinimumSize(new Dimension(480, 400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createLegendItem(Color color, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        item.setOpaque(false);
        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(12, 12));
        item.add(swatch);
        JLabel text = new JLabel(label);
        text.setForeground(MUTED_TEXT);
        item.add(text);
        return item;
    }

    private static class MapPanel extends JPanel {
        private static final int TILE_SIZE = 14;
        private String[][] map;

        private MapPanel(String[][] map) {
            setMap(map);
        }

        private void setMap(String[][] map) {
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
                    Color tileColor = getTileColor(mapValue);

                    //タイルを変更する場合は、ここで g.fillRect(...) や g.fillRoundRect(...) の呼び出しを変更
                    g.setColor(tileColor);
                    if ("#".equals(mapValue)) {
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    } else if (".".equals(mapValue)) {
                        g.fillRoundRect(x * TILE_SIZE + 1, y * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2, 3, 3);
                    //以降に追加
                }
            }
            g.dispose();
        }

        // 「マップの文字列」と「画面に描くタイル色」の対応はここで設定する。
        private Color getTileColor(String mapValue) {
            return switch (mapValue) {
                case "#" -> WALL;
                case "." -> FLOOR;
                default -> Color.MAGENTA;
            };
        }
    }
}

