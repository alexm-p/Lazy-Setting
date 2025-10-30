package io.github.alexm_p.lazysetting.model;

import java.awt.geom.Point2D;

public record Hold(HoldType holdType, double difficulty, Pose pose) {
    public Hold {
        if (holdType == null)
            throw new IllegalArgumentException("Hold type cannot be null");

        if (difficulty < 0 || difficulty > 10)
            throw new IllegalArgumentException("Hold difficulty must be in [0, 10]");

        if (pose == null)
            throw new IllegalArgumentException("Hold pose cannot be null");
    }

    public double getRotation() { return pose.rotation(); }

    public Point2D.Double getPosition() { return new Point2D.Double(pose.x(), pose.y()); }
}