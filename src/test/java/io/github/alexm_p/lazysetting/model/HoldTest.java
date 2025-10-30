package io.github.alexm_p.lazysetting.model;

import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class HoldTest {

    @Test
    void validHoldShouldBeCreated() {
        Hold h = new Hold(HoldType.JUG, 3, new Pose(1, 1, 180));
        assertEquals(HoldType.JUG, h.holdType());
        assertEquals(new Point2D.Double(1, 1), h.getPosition());
        assertEquals(180, h.pose().rotation());
        assertEquals(180, h.getRotation());
    }

    @Test
    void nullHoldTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Hold(null, 1, new Pose(1, 1, 180)));
    }

    @Test
    void illegalRotationThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Hold(HoldType.SLOPER, 10, new Pose(1, 1, 400)));
    }
}
