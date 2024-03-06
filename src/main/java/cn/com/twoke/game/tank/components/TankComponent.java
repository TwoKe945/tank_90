package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.config.Dir;
import cn.com.twoke.game.tank.config.EnemyLevel;
import cn.com.twoke.game.tank.config.EnemyType;
import cn.com.twoke.game.tank.config.Settings;
import cn.com.twoke.game.tank.entity.Transform;
import cn.com.twoke.game.tank.util.ResourceLoader;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 坦克
 */
public class TankComponent extends Component {

    BufferedImage[] tankImages;
    private Dir dir = Dir.UP;
    private int[][] grid;

    private EnemyType type;
    private EnemyLevel level;

    public  TankComponent(int[][] grid, EnemyLevel level, EnemyType type) {
        tankImages = new BufferedImage[8 * 8];
        this.grid = grid;
        BufferedImage bufferedImage = ResourceLoader.loadImage("/textures/tank/Enemys.bmp");
        for (int i = 0; i < tankImages.length; i++) {
            int x = i % 8;
            int y = i / 8;
            tankImages[i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
        this.level = level;
        this.type = type;
    }

    int index = 0;


    @Override
    public void render(Graphics g) {

    // type = 1 level = 0 => default + 2 * (type - 1) + 4 * 0
    // type = 1 level = 1 => default + 2 * (type - 1) + 4 * 1
    // type = 1 level = 9 => default + 2 * (type - 1) + 4 * 2

    // type = 2 level = 0 => default + 2 * (type - 1)
    // type = 2 level = 1 => default + 2 * (type - 1) + 4
    // type = 2 level = 2 => default + 2 * (type - 1) + 4 * 9

        g.drawImage(tankImages[index + dir.getIndex() * 8 + 2 * (type.code - 1) + 4 * level.code],
                (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height,
                null
                );
        g.setColor(Color.red);
        g.drawRect(
                (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height);
    }


    protected float speed = 0.5f;

    @Override
    public void update(float dt) {
        if (index + 1 > 1) {
            index = 0;
        }   else {
            this.index++;
        }

        Transform transform = entity.getTransform();

        Vec2f position = new Vec2f(transform.getPosition());
        switch (dir) {
            case UP:
                position.y = position.y - speed;
                break;
            case LEFT:
                position.x = position.x - speed;
                break;
            case RIGHT:
                position.x =  position.x + speed;
                break;
            case DOWN:
                position.y  = position.y  + speed;
                break;
        }
        if (canMove(position.x, position.y)) {
            transform.setPosition(position.x, position.y);
        } else {
            Dir value = Dir.values()[random.nextInt(Dir.values().length)];
            if (value != dir) {
                dir = value;
            }
        }
    }

    private boolean canMove(float x, float y) {
        return traversalFourPoints(x,y, this::check) && traversalFourPoints(x,y, ps -> PLAYGROUND_RECT.contains(ps[0], ps[1]));
    }

    private boolean check(float[] point) {
        int tileX = (int) (point[0] - Settings.PLAYGROUND_MARGIN_LEFT) / Settings.TILE_WIDTH;
        int tileY = (int) (point[1] - Settings.PLAYGROUND_MARGIN_TOP) / Settings.TILE_HEIGHT;
        if (tileY >= Settings.PLAYGROUND_ROW ||
            tileX >= Settings.PLAYGROUND_COL ||
            tileX < 0 || tileY < 0
        ) return false;
        return grid[tileY][tileX] != 1 && grid[tileY][tileX] != 2;
    }

    public boolean traversalFourPoints(float x, float y, Function<float[], Boolean> handler) {
        float tempX = x, tempY = y;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                tempX += 28;
            }
            if (i == 2) {
                tempY += 28;
            }
            if (i == 3) {
                tempX -= 28;
            }
            if (!handler.apply(new float[]{tempX, tempY})) {
                return false;
            }
        }
        return true;
    }


    static Random random = new Random();

    static Rectangle PLAYGROUND_RECT = new Rectangle(
            Settings.PLAYGROUND_MARGIN_LEFT,
            Settings.PLAYGROUND_MARGIN_TOP,
            Settings.PLAYGROUND_WIDTH,
            Settings.PLAYGROUND_HEIGHT
    );


    public void setGrid(int[][] grid) {
        this.grid = grid;
    }
}
