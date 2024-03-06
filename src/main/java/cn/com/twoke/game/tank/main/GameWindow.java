package cn.com.twoke.game.tank.main;

import cn.com.twoke.game.tank.util.ResourceLoader;

import javax.swing.*;

/**
 * 游戏窗口
 */
public class GameWindow {

    private final JFrame frame;

    public GameWindow(JPanel panel) {
        frame = new JFrame();
        frame.setTitle("90经典坦克游戏 v1.0");
//		设置默认关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
//		不允许变动窗口大小
        frame.setResizable(false);
        frame.pack();
//      设置显示位置到屏幕中间,必须在pack后面设置
        frame.setLocationRelativeTo(null);
//		设置窗口显示
        frame.setVisible(true);
        frame.setIconImage(ResourceLoader.loadImage("/icon.png"));
    }

    public JFrame getFrame() {
        return frame;
    }
}
