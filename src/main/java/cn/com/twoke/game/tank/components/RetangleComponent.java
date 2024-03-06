package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.entity.Transform;

import java.awt.*;
import java.util.Objects;

public class RetangleComponent extends Component {

    private Color bgColor;
    private Color borderColor;

    public RetangleComponent(Color color) {
        this.bgColor = color;
        this.borderColor = null;
    }

    public RetangleComponent(Color color, Color borderColor) {
        this.bgColor = color;
        this.borderColor = borderColor;
    }

    @Override
    public void render(Graphics g) {
        Rectangle hitbox = entity.getHitbox();
        g.setColor(bgColor);
        g.fillRect(hitbox.x,hitbox.y,hitbox.width, hitbox.height);
        if (Objects.nonNull(borderColor)) {
            g.setColor(borderColor);
            g.drawRect(hitbox.x,hitbox.y,hitbox.width, hitbox.height);
        }
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
