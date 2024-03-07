package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.config.*;
import cn.com.twoke.game.tank.entity.Transform;
import cn.com.twoke.game.tank.util.AssetPool;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.function.Function;

import static cn.com.twoke.game.tank.config.Settings.TANK_INITIALIZE_FRAME_HEIGHT;
import static cn.com.twoke.game.tank.config.Settings.TANK_INITIALIZE_FRAME_WIDTH;

/**
 * 坦克
 */
public class TankComponent extends Component {

    BufferedImage[] tankImages;
    private Dir dir = Dir.UP;
    private int[][] grid;
    private EnemyType type;
    private EnemyLevel level;

    /**
     * 初始化完成标识
     */
    private boolean initialized = false;
    private boolean autoMoveable = true;
    private boolean moving = true;

    private BufferedImage tankInitializeImage;


    public  TankComponent(int[][] grid, EnemyLevel level, EnemyType type) {
        this.grid = grid;
        tankInitializeImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_INITIALIZE);
        tankImages = new BufferedImage[8 * 8];
        BufferedImage bufferedImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_ENEMYS);

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
        // 敌人坦克绘制逻辑
        // type = 2 level = 0 => default + 2 * (type - 1) + 4 * 0
        // type = 2 level = 1 => default + 2 * (type - 1) + 4 * 1
        // type = 2 level = 2 => default + 2 * (type - 1) + 4 * 9
        // 绘制坦克生成动画
        drawAnimationForInitializingTank(g);
        if(initialized) {
            // 绘制坦克
            drawTank(g);
        }
    }


    private int animationFrameIndex = 0; // 动画帧数索引
    private int animationLoopCount = 2; // 动画循环次数
    private int singleFrameShowTimeCount = 0;

    /**
     * 绘制初始化坦克动画
     * @param g
     */
    private void drawAnimationForInitializingTank(Graphics g) {
        if (initialized) return;
        int idx = animationFrameIndex % 4;
        g.drawImage(tankInitializeImage.getSubimage(idx * TANK_INITIALIZE_FRAME_WIDTH,0, TANK_INITIALIZE_FRAME_WIDTH, TANK_INITIALIZE_FRAME_HEIGHT), (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y , (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), null);
        if (singleFrameShowTimeCount > 10) { // 动画中的一帧展示10次切换下一帧
            animationFrameIndex++;
            singleFrameShowTimeCount = 0;
        }
        if (animationFrameIndex >= animationLoopCount * 4) { // 初始化动画循环两次后初始完成
            initialized = true;
            singleFrameShowTimeCount = 0;
            animationFrameIndex = 0;
        }
        singleFrameShowTimeCount ++;
    }

    private void drawTank(Graphics g) {
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
        if (moving) {
            if (index + 1 > 1) {
                index = 0;
            }   else {
                this.index++;
            }
        }
        updatePos();
    }

    private void updatePos() {
        if (autoMoveable) {
            updateAutoMovePos();
        } else {
            // TODO
        }
    }

    private void updateAutoMovePos() {
        if (!initialized) return;
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
