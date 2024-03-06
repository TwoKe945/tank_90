package cn.com.twoke.game.tank.config;

public enum EnemyLevel {
    LEVEL_1(0),
    LEVEL_2(1),
    LEVEL_3(9);

    public final int code;

    EnemyLevel(int code) {
        this.code = code;
    }
}
