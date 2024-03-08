package cn.com.twoke.game.tank.components.tank;

import cn.com.twoke.game.tank.components.Component;
import cn.com.twoke.game.tank.components.bullet.BulletManager;
import cn.com.twoke.game.tank.components.common.GridPlaygroundComponent;
import cn.com.twoke.game.tank.components.bullet.BulletComponent;
import cn.com.twoke.game.tank.config.*;
import cn.com.twoke.game.tank.config.Dir;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.entity.tank.TankEntity;
import cn.com.twoke.game.tank.entity.Transform;
import cn.com.twoke.game.tank.util.AssetPool;
import com.sun.javafx.geom.Vec2f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
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
    private boolean isAutoTurn = true;
    private boolean isAutoFire = true;
    /**
     * 是否移动中
     */
    private boolean isMoving = true;
    private int movingFrameIndex = 0;
    private BulletManager bulletManager;


    public  TankComponent(GridPlaygroundComponent playground, TankEntity tankEntity) {

        this.playground = playground;
        this.tankEntity = tankEntity;
        this.tankEntity.setComponent(this);
        this.approachCollisionRect = new Rectangle();
        tankInitializeImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_INITIALIZE);
    }

    @Override
    public void setEntity(GameEntity entity) {
        super.setEntity(entity);
        this.bulletManager = new BulletManager(this);
    }

    @Override
    public void render(Graphics g) {
        // 绘制坦克生成动画
        drawAnimationForInitializingTank(g);
        Optional.ofNullable(bulletManager).ifPresent(manager -> manager.render(g));
        if(isInitialized) {
            // 绘制坦克
            drawTank(g);
            debug(() -> {
                drawHitBox(g);
                drawApproachCollision(g);
            });
        }
    }

    private void drawHitBox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(
                (int)entity.getTransform().getPosition().x,
                (int)entity.getTransform().getPosition().y,
                entity.getTransform().getSize().width,
                entity.getTransform().getSize().height);
    }

    private void drawApproachCollision(Graphics g) {
        g.setColor(Color.orange);
        g.drawRect(approachCollisionRect.x,
                approachCollisionRect.y,
                approachCollisionRect.width,
                approachCollisionRect.height
        );
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
    }

    @Override
    public void update(float dt) {
        if (isInitialized && isMoving) {
            if (movingFrameIndex + 1 > 1) {
                movingFrameIndex = 0;
            }   else {
                this.movingFrameIndex++;
            }
            updatePos(dt);
            updateBullet();
        }
        updateApproachCollisionRect();
        Optional.ofNullable(bulletManager).ifPresent(manager -> manager.update(dt));
    }

    private void updateApproachCollisionRect() {
        int offset = dir == Dir.LEFT || dir == Dir.UP ? approachCollisionSize : 1;
        int offsetCenterX = 0;
        int offsetCenterY = 0;
        if (dir.getVecY() == 0) {
            offsetCenterY = (entity.getTransform().getSize().height - 10) / 2;
        } else if (dir.getVecX() == 0) {
            offsetCenterX = (entity.getTransform().getSize().width - 10) / 2;
        }
        approachCollisionRect.x = (int)entity.getTransform().getPosition().x + dir.getVecX() * entity.getTransform().getSize().width * offset + offsetCenterX;
        approachCollisionRect.y = (int)entity.getTransform().getPosition().y + dir.getVecY() * entity.getTransform().getSize().height * offset + offsetCenterY;
        approachCollisionRect.width = dir.getVecX() == 0 ? 10 : entity.getTransform().getSize().width + Math.abs(dir.getVecX()) * (approachCollisionSize - 1) * entity.getTransform().getSize().width;
        approachCollisionRect.height = dir.getVecY() == 0 ? 10 :  entity.getTransform().getSize().height  + Math.abs(dir.getVecY()) * (approachCollisionSize - 1) * entity.getTransform().getSize().height;
    }


    private void updateBullet() {
        if (!isAutoFire) return;
        if (bulletCountNumber() == 0 && !isApproachCollision) {
            bulletManager.fire();
        }
    }

    public void fire() {
        bulletManager.fire();
    }

    private void updatePos(float dt) {
        Transform transform = entity.getTransform();
        Vec2f position = new Vec2f(transform.getPosition());
        position.y = position.y + dir.getVecY() * tankEntity.getMoveSpeed() ;
        position.x = position.x + dir.getVecX() * tankEntity.getMoveSpeed() ;
        if (canMove(position.x, position.y)) {
            transform.setPosition(position.x, position.y);
        } else if (isAutoTurn) {
            Dir value = Dir.values()[random.nextInt(Dir.values().length)];
            if (value != dir) {
                dir = value;
            }
        }
    }

    private boolean canMove(float x, float y) {
        return collisionDetection(x, y) && traversalFourPoints(x,y, ps -> PLAYGROUND_RECT.contains(ps[0], ps[1]));
    }

    private boolean isApproachCollision = false;
    private int approachCollisionSize = 2;
    private Rectangle approachCollisionRect;

    private boolean collisionDetection(float tankX, float tankY) {
        isApproachCollision = false;
        int[][] gridData = playground.getGridData();
        Rectangle hitbox = entity.getHitbox();
        for (int y = 0; y < gridData.length; y++) {
            for (int x = 0; x < gridData[y].length; x++) {
                if (gridData[y][x] == 1 || gridData[y][x] == 4) {
                    if (approachCollisionRect.intersects(Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                            Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT,
                            Settings.TILE_WIDTH,
                            Settings.TILE_HEIGHT)) {
                        isApproachCollision = true;
                    }
                }
                if (gridData[y][x] == 1 || gridData[y][x] == 2 || gridData[y][x] == 4) {
                    if (intersects(tankX, tankY, hitbox.width, hitbox.height,
                            Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                            Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT,
                            Settings.TILE_WIDTH,
                            Settings.TILE_HEIGHT)) {
                        return false;
                    }
                }
            }
        }
        if (!PLAYGROUND_RECT.contains(approachCollisionRect)) {
            isApproachCollision = true;
        }

        return true;
    }

    public boolean intersects(float tankX, float tankY, float tankWidth, float tankHeight,
                              float tileX, float tileY, float tileWidth, float tileHeight) {
        float tw = tankWidth;
        float th = tankHeight;
        float rw = tileWidth;
        float rh = tileHeight;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        rw += tileX;
        rh += tileY;
        tw += tankX;
        th += tankY;
        //      overflow || intersect
        return ((rw < tileX || rw > tankX) &&
                (rh < tileY || rh > tankY) &&
                (tw < tankX || tw > tileX) &&
                (th < tankY || th > tileY));
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

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }


    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public void setAutoTurn(boolean autoTurn) {
        isAutoTurn = autoTurn;
    }

    public void setAutoFire(boolean autoFire) {
        isAutoFire = autoFire;
    }

    public boolean canMove(BulletComponent bulletComponent) {
        // 判断是否超出边界
        if (!PLAYGROUND_RECT.contains(new Rectangle(bulletComponent.getNextX(), bulletComponent.getNextY(),
                bulletComponent.getWidth(), bulletComponent.getHeight()))) return false;
        int[][] gridData = playground.getGridData();
        int bulletX = bulletComponent.getNextX();
        int bulletY = bulletComponent.getNextY();
        for (int y = 0; y < gridData.length; y++) {
            for (int x = 0; x < gridData[y].length; x++) {
                if (gridData[y][x] == 1 || gridData[y][x] == 4) {
                    if (intersects(bulletX, bulletY, 10, 10,
                            Settings.PLAYGROUND_MARGIN_LEFT + x * Settings.TILE_WIDTH,
                            Settings.PLAYGROUND_MARGIN_TOP + y * Settings.TILE_HEIGHT,
                            Settings.TILE_WIDTH,
                            Settings.TILE_HEIGHT)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int bulletCountNumber() {
        return bulletManager.size();
    }
}
