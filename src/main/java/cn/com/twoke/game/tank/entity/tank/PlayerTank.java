package cn.com.twoke.game.tank.entity.tank;

import cn.com.twoke.game.tank.components.tank.TankComponent;
import cn.com.twoke.game.tank.config.Constant;
import cn.com.twoke.game.tank.config.tank.PlayerLevel;
import cn.com.twoke.game.tank.config.tank.PlayerType;
import cn.com.twoke.game.tank.util.AssetPool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerTank extends AbstractTankEntity {

    private static final BufferedImage[][] PLAYER_TANK_TEXTURES;
    static {
        PLAYER_TANK_TEXTURES = new BufferedImage[2][4 * 8];
        BufferedImage bufferedImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_PLAYER1);
        for (int i = 0; i < PLAYER_TANK_TEXTURES[0].length; i++) {
            int x = i % 8;
            int y = i / 8;
            PLAYER_TANK_TEXTURES[0][i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
        bufferedImage = AssetPool.loadTexture(Constant.TEXTURE_TANK_PLAYER2);
        for (int i = 0; i < PLAYER_TANK_TEXTURES[1].length; i++) {
            int x = i % 8;
            int y = i / 8;
            PLAYER_TANK_TEXTURES[1][i] = bufferedImage.getSubimage(x * 28, y * 28, 28, 28);
        }
    }
    public PlayerTank() {
        this(PlayerType.PLAYER_1, PlayerLevel.LEVEL_1);
    }
    public PlayerTank(PlayerType type) {
        this.type = type;
        this.level = PlayerLevel.LEVEL_1;
    }
    public PlayerTank(PlayerType type, PlayerLevel level) {
        this.type = type;
        this.level = level;
    }

    /**
     * 获取当前帧的玩家坦克图像
     *
     * @param index
     * @return
     */
    @Override
    public Image getCurrentFrame(int index) {
        return PLAYER_TANK_TEXTURES[type.getTypeCode()]
                [index + component.getDir().getIndex() * 8 + 2 * level.getLevelCode()];
    }

    @Override
    public void setComponent(TankComponent tankComponent) {
        super.setComponent(tankComponent);
        component.setAutoTurn(false); // 关闭自动转向
        component.setMoving(false); // 关闭移动
        component.setAutoFire(false); // 关闭自动发射子弹
    }
}
