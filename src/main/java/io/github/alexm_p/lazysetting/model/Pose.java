package io.github.alexm_p.lazysetting.model;

public record Pose (double x, double y, double rotation) {
    public Pose {
        if (rotation < 0 || rotation >= 360)
            throw new IllegalArgumentException("Pose rotation must be in [0, 360]");
    }
}