package entities.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() { return dx; }
    public int getDy() { return dy; }

    public static Direction random() {
        Direction[] values = values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
