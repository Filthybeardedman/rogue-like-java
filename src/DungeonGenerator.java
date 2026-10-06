import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DungeonGenerator {
    private final int w;
    private final int h;
    private final int d;
    private final BSPNode[] roots;
    private final List<BSPNode>[] leafNodesByDepth;
    private final Random random = new Random();

    public DungeonGenerator(int w, int h, int d) {
        this.w = w;
        this.h = h;
        this.d = d;
        this.roots = new BSPNode[d];
        this.leafNodesByDepth = new List[d];

        for (int depth = 0; depth < d; depth++) {
            roots[depth] = new BSPNode(0, 0, w, h);
            leafNodesByDepth[depth] = roots[depth].getLeafNodes();
        }
    }

    // マップの生成
    public String[][][] createTileMaps() {
        String[][][] maps = new String[d][w][h];
        for (int depth = 0; depth < d; depth++) {
            maps[depth] = createTileMap(depth);
        }
        return maps;
    }

    public String[][] createTileMap(int depth) {
        assignStairs(depth);

        String[][] map = new String[w][h];
        for (String[] column : map) {
            Arrays.fill(column, "#");
        }

        roots[depth].writeTileMap(map);
        roots[depth].writeStairTiles(map);
        return map;
    }

    // 階段の割り当て
    private void assignStairs(int depth) {
        List<BSPNode> leaves = leafNodesByDepth[depth];

        for (BSPNode leaf : leaves) {
            leaf.setStairType(StairType.NONE);
        }

        Collections.shuffle(leaves, random);
        leaves.get(0).setStairType(StairType.UP);
        leaves.get(leaves.size() - 1).setStairType(StairType.DOWN);
    }
}
