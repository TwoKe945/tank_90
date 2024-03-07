package cn.com.twoke.game.tank.scenes;

import cn.com.twoke.game.tank.components.Component;
import cn.com.twoke.game.tank.entity.GameEntity;
import cn.com.twoke.game.tank.main.TankGame;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Scene implements MouseListener, MouseMotionListener, KeyListener {

    private final List<GameEntity> entities;
    private List<MouseListener> mouseListeners;
    private List<MouseMotionListener> mouseMotionListeners;
    private List<KeyListener> keyListeners;

    protected TankGame game;

    public void setGame(TankGame game) {
        this.game = game;
    }

    protected Scene() {
        this.entities = new ArrayList<>();
        this.mouseMotionListeners = new ArrayList<>();
        this.mouseListeners = new ArrayList<>();
        this.keyListeners = new ArrayList<>();
    }

    public void addToScene(GameEntity entity) {
        if (Objects.isNull(entity)) return;
        entities.add(entity);
        for (Component component : entity.getAllComponents()) {
            if (MouseListener.class.isAssignableFrom(component.getClass())) {
                this.mouseListeners.add((MouseListener) component);
            }
            if (MouseMotionListener.class.isAssignableFrom(component.getClass())) {
                this.mouseMotionListeners.add((MouseMotionListener) component);
            }
            if (KeyListener.class.isAssignableFrom(component.getClass())) {
                this.keyListeners.add((KeyListener) component);
            }
        }
    }

    public void render(Graphics g) {
        doRender(g);
        for (GameEntity entity : entities) {
            entity.render(g);
        }
        doRenderEntityAfter(g);
    }



    public  void update(float dt) {
        doUpdate(dt);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update(dt);
        }
        doUpdateEntityAfter(dt);
    }

    protected void doUpdateEntityAfter(float dt) {
    }

    protected void doRenderEntityAfter(Graphics g) {
    }

    protected void doUpdate(float dt) {}
    protected void doRender(Graphics g){}

    @Override
    public void mouseClicked(MouseEvent e) {
        for (MouseListener mouseListener : this.mouseListeners) {
            mouseListener.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MouseListener mouseListener : this.mouseListeners) {
            mouseListener.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MouseListener mouseListener : this.mouseListeners) {
            mouseListener.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (MouseListener mouseListener : this.mouseListeners) {
            mouseListener.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (MouseListener mouseListener : this.mouseListeners) {
            mouseListener.mouseExited(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (MouseMotionListener mouseListener : this.mouseMotionListeners) {
            mouseListener.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MouseMotionListener mouseListener : this.mouseMotionListeners) {
            mouseListener.mouseMoved(e);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (KeyListener keyListener : keyListeners) {
            keyListener.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (KeyListener keyListener : keyListeners) {
            keyListener.keyReleased(e);
        }
    }
}
