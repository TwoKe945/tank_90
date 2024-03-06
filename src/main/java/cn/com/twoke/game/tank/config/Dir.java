package cn.com.twoke.game.tank.config;

public enum Dir {
    UP(0, 2),
    RIGHT(1, 3),
    DOWN(2, 0),
    LEFT(3, 1);

    private final int index;
    private final int reverseIndex;

    public int getIndex() {
        return index;
    }

    public int getReverseIndex() {
        return reverseIndex;
    }

    private Dir(int index, int reverseIndex) {
        this.index = index;
        this.reverseIndex = reverseIndex;
    }
}
