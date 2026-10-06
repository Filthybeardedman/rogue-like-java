import java.awt.Font;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BSP {
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
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        for (String[] row : map) {
            for (String cell : row) {
                sb.append(cell);
            }
            sb.append(System.lineSeparator());
        }
        area.setText(sb.toString());

        JFrame frame = new JFrame("BSP Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(area));
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

