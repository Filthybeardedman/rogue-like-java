import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DungeonWindow {
    private static final Color BACKGROUND = new Color(24, 29, 30);
    private static final Color SURFACE = new Color(34, 41, 41);
    private static final Color TEXT = new Color(230, 234, 222);
    private static final Color MUTED_TEXT = new Color(157, 169, 155);

    public void showWindow(String[][][] maps, int initialDepth) {
        if (maps == null || maps.length == 0) {
            throw new IllegalArgumentException("表示する階層マップがありません。");
        }
        if (initialDepth < 0 || initialDepth >= maps.length) {
            throw new IndexOutOfBoundsException("初期階層が範囲外です: " + initialDepth);
        }

        String[][] map = maps[initialDepth];
        DungeonPanel mapPanel = new DungeonPanel(map);

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
        JLabel dimensions = new JLabel(map.length + " x " + map[0].length
                + "   /   " + (map.length * map[0].length) + " tiles");
        dimensions.setForeground(MUTED_TEXT);
        titleGroup.add(title, BorderLayout.NORTH);
        titleGroup.add(dimensions, BorderLayout.SOUTH);
        header.add(titleGroup, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        String[] depthOptions = new String[maps.length];
        for (int depth = 0; depth < maps.length; depth++) {
            depthOptions[depth] = "階層 " + (depth + 1);
        }
        JComboBox<String> depthSelector = new JComboBox<>(depthOptions);
        depthSelector.setSelectedIndex(initialDepth);
        depthSelector.addActionListener(event ->
            mapPanel.setMap(maps[depthSelector.getSelectedIndex()]));
        controls.add(depthSelector);

        JButton refreshButton = new JButton("更新");
        refreshButton.setForeground(TEXT);
        refreshButton.setBackground(new Color(66, 82, 67));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        refreshButton.addActionListener(event -> mapPanel.repaint());
        controls.add(refreshButton);
        header.add(controls, BorderLayout.EAST);

        JPanel mapHolder = new JPanel(new GridBagLayout());
        mapHolder.setBackground(BACKGROUND);
        mapHolder.add(mapPanel);

        JScrollPane scrollPane = new JScrollPane(mapHolder);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        footer.setBackground(SURFACE);
        footer.add(createLegendItem(DungeonPanel.WALL, "壁"));
        footer.add(createLegendItem(DungeonPanel.FLOOR, "部屋・通路"));
        footer.add(createLegendItem(DungeonPanel.UP_STAIRS, "上り階段"));
        footer.add(createLegendItem(DungeonPanel.DOWN_STAIRS, "下り階段"));

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
}
