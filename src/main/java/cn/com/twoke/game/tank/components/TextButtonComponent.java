package cn.com.twoke.game.tank.components;

import cn.com.twoke.game.tank.entity.Transform;

import java.awt.*;
import java.util.Objects;

public class TextButtonComponent extends Component{
    private String text;
    private Font font;

    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public TextButtonComponent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(Graphics g) {
        if (Objects.nonNull(color)) {
            g.setColor(color);
        }
        Transform transform = entity.getTransform();
        FontMetrics fm;
        if (Objects.nonNull(font)) {
            g.setFont(font);
            fm = g.getFontMetrics(font);
        } else {
           fm = g.getFontMetrics();
        }
        g.drawString(text,transform.getPosition().x + (transform.getSize().width-(int)fm.getStringBounds(text, g).getWidth())/2,
                transform.getPosition().y + (transform.getSize().height - fm.getHeight()) / 2 + fm.getAscent());
    }
}
