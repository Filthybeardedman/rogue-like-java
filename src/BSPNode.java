import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BSPNode {
    private String[][] map;

    private final int x;
    private final int y;
    private final int w;
    private final int h;

    private int xCenter;
    private int yCenter;

    private BSPNode left;
    private BSPNode right;

    private StairType stairType = StairType.NONE;

    private static final int MIN_SIZE = 10;
    private final Random random = new Random();

    private boolean verticalSplit;
    private boolean canSplit;

    // BSP木の生成
    public BSPNode(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        if (split()) {
            canSplit = true;
            if (verticalSplit) {
                this.xCenter = right.xCenter;
                this.yCenter = right.yCenter;
            } else {
                this.xCenter = left.xCenter;
                this.yCenter = left.yCenter;
            }
        } else {
            canSplit = false;
            this.xCenter = x + w / 2;
            this.yCenter = y + h / 2;
        }
    }

    // 部屋の分割
    private boolean split() {
        boolean canSplitW = w >= MIN_SIZE * 2;
        boolean canSplitH = h >= MIN_SIZE * 2;

        if (!canSplitW && !canSplitH) {
            return false;
        }

        if (canSplitW && canSplitH) {
            if (w > h) {
                splitW();
            } else {
                splitH();
            }
            return true;
        }

        if (canSplitW) {
            splitW();
            return true;
        }

        if (canSplitH) {
            splitH();
            return true;
        }

        return false;
    }

    private void splitW() {
        int range = w - MIN_SIZE * 2;
        int leftW = MIN_SIZE + random.nextInt(range + 1);
        int rightW = w - leftW;

        verticalSplit = true;
        left = new BSPNode(x, y, leftW, h);
        right = new BSPNode(x + leftW, y, rightW, h);
    }

    private void splitH() {
        int range = h - MIN_SIZE * 2;
        int topH = MIN_SIZE + random.nextInt(range + 1);
        int bottomH = h - topH;

        verticalSplit = false;
        left = new BSPNode(x, y, w, topH);
        right = new BSPNode(x, y + topH, w, bottomH);
    }

    // 葉ノードの収集
    public List<BSPNode> getLeafNodes() {
        List<BSPNode> leafNodes = new ArrayList<>();
        collectLeafNodes(leafNodes);
        return leafNodes;
    }

    private void collectLeafNodes(List<BSPNode> leafNodes) {
        if (canSplit) {
            left.collectLeafNodes(leafNodes);
            right.collectLeafNodes(leafNodes);
        } else {
            leafNodes.add(this);
        }
    }

    public void setStairType(StairType stairType) {
        this.stairType = stairType;
    }

    // Mapの書きだし
    public String[][] writeTileMap(String[][] map) {
        this.map = map;
        if (canSplit) {
            left.writeTileMap(map);
            right.writeTileMap(map);
            carveStreet();
        } else {
            carveRoom();
        }
        return map;
    }

    // 階段の書きだし
    public void writeStairTiles(String[][] map) {
        if (canSplit) {
            left.writeStairTiles(map);
            right.writeStairTiles(map);
        } else if (stairType == StairType.UP) {
            map[xCenter][yCenter] = "<";
        } else if (stairType == StairType.DOWN) {
            map[xCenter][yCenter] = ">";
        }
    }

    //通路のmap書きだし
    private void carveStreet() {
        int leftCenterX = left.xCenter;
        int leftCenterY = left.yCenter;
        int rightCenterX = right.xCenter;
        int rightCenterY = right.yCenter;
        int averageX = (leftCenterX + rightCenterX) / 2;
        int averageY = (leftCenterY + rightCenterY) / 2;

        if (!verticalSplit) {
            int currentY = leftCenterY;
            while (currentY != averageY) {
                map[leftCenterX][currentY] = ".";
                currentY++;
            }

            currentY = rightCenterY;
            while (currentY != averageY) {
                map[rightCenterX][currentY] = ".";
                currentY--;
            }

            for (int currentX = Math.min(leftCenterX, rightCenterX);
                    currentX <= Math.max(leftCenterX, rightCenterX);
                    currentX++) {
                map[currentX][currentY] = ".";
            }

        } else {
            int currentX= leftCenterX;
            while (currentX != averageX) {
                map[currentX][leftCenterY] = ".";
                currentX++;
            }

            currentX = rightCenterX;
            while (currentX != averageX) {
                map[currentX][rightCenterY] = ".";
                currentX--;
            }

            for (int currentY = Math.min(leftCenterY, rightCenterY);
                    currentY <= Math.max(leftCenterY, rightCenterY);
                    currentY++) {
                map[averageX][currentY] = ".";
            }
        }
    }

    //部屋のmap書きだし
    private void carveRoom() {
        int pad = random.nextInt(4)+1;
        int roomX = x + pad;
        int roomY = y + pad;
        int roomW = w - pad * 2;
        int roomH = h - pad * 2;

        for (int col = roomX; col < Math.min(map.length, roomX + roomW); col++) {
            for (int row = roomY; row < Math.min(map[col].length, roomY + roomH); row++) {
                map[col][row] = ".";
            }
        }
    }
}

enum StairType {
    NONE,
    UP,
    DOWN
}