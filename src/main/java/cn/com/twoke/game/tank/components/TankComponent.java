package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.config.*;
import cn.com.twoke.game.tank.config.tank.Dir;
import cn.com.twoke.game.tank.entity.tank.TankEntity;
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

    private Dir dir = Dir.UP;
    private final GridPlaygroundComponent playground;
    private final TankEntity tankEntity;
    private BufferedImage tankInitializeImage;
    /**
     * 初始化完成标识
     */
    private boolean isInitialized = false;
    /**
     * 是否自动移动
     */
    private boolean isAutoMove = true;
    /**
     * 是否移动中
     */
    private boolean isMoving = true;
    int movingFrameIndex = 0;


    public  TankComponent(GridPlaygroundComponent playground, TankEntity tankEntity) {
        this.playground = playground;
        this.tankEntity = tankEntity;
        this.tankEntity.setComponent(this);
        tankInitializeImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_INITIALIZE);
    }

    @Override
    public void render(Graphics g) {
        // 绘制坦克生成动画
        drawAnimationForInitializingTank(g);
        if(isInitialized) {
            // 绘制坦克
            drawTank(g);
        }
    }


    private int animationFrameIndex = 0; // 动画帧数索引
    private int singleFrameShowTimeCount = 0;
    private static final int INITIALIZE_ANIMATION_LOOP_COUNT = 2; // 动画循环次数
    private static final int INITIALIZE_ANIMATION_FRAME_COUNT = 4; // 动画帧数
    private static final int SINGLE_FRAME_RUNTIME_COUNT = 10; // 单帧动画运行次数

    /**
     * 绘制初始化坦克动画
     * @param g
     */
    private void drawAnimationForInitializingTank(Graphics g) {
        if (isInitialized) return;
        int idx = animationFrameIndex % INITIALIZE_ANIMATION_FRAME_COUNT;
        g.drawImage(tankInitializeImage.getSubimage(idx * TANK_INITIALIZE_FRAME_WIDTH,0, TANK_INITIALIZE_FRAME_WIDTH, TANK_INITIALIZE_FRAME_HEIGHT), (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y , (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), (int)(TANK_INITIALIZE_FRAME_HEIGHT * Settings.SCALE), null);
        if (singleFrameShowTimeCount > SINGLE_FRAME_RUNTIME_COUNT) { // 动画中的一帧展示10次切换下一帧
            animationFrameIndex++;
            singleFrameShowTimeCount = 0;
        }
        if (animationFrameIndex >= INITIALIZE_ANIMATION_LOOP_COUNT * INITIALIZE_ANIMATION_FRAME_COUNT) { // 初始化动画循环两次后初始完成
            isInitialized = true;
            singleFrameShowTimeCount = 0;
            animationFrameIndex = 0;
        }
        singleFrameShowTimeCount ++;
    }

    private void drawTank(Graphics g) {
        g.drawImage(tankEntity.getCurrentFrame(movingFrameIndex),
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
        if (isMoving) {
            if (movingFrameIndex + 1 > 1) {
                movingFrameIndex = 0;
            }   else {
                this.movingFrameIndex++;
            }
        }
        updatePos();
    }

    private void updatePos() {
        if (isAutoMove) {
            updateAutoMovePos();
        } else {
            // TODO 实现键盘操作控制坦克逻辑
        }
    }

    private void updateAutoMovePos() {
        if (!isInitialized) return;
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
        return playground.getGridData()[tileY][tileX] != 1 && playground.getGridData()[tileY][tileX] != 2;
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

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        this.isInitialized = initialized;
    }


    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }


    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}
