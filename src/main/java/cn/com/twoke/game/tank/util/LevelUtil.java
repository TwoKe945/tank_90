package cn.com.twoke.game.tank.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelUtil {

    public static List<int[][]> levels = new ArrayList<>();

    public static final String LEVEL_DIR = System.getProperty("user.dir") + "/levels";

    public static void loadLevels() throws IOException {
        File file = Paths.get(LEVEL_DIR).toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        int[][] temp;
        for (File itemFile : Objects.requireNonNull(file.listFiles())) {
            if (itemFile.isFile()) {
                List<String> lines = Files.readAllLines(itemFile.toPath());
                temp  = new int[lines.size()][lines.get(0).length()];
                for (int y = 0; y < lines.size(); y++) {
                    for (int x = 0; x < lines.get(y).length(); x++) {
                        temp[y][x] = lines.get(y).charAt(x) - 48;
                    }
                }
                levels.add(temp);
            }
        }
    }


    public static void saveLevel(int[][] grid) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                sb.append(grid[y][x]);
            }
            sb.append("\n");
        }
        Files.write(new File(LEVEL_DIR + "/" + levels.size() + ".level").toPath(), sb.toString().getBytes());
        levels.add(grid);
    }


    public static int[][] get(int index) {
        return levels.get(index);
    }
}
