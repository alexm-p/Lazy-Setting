package io.github.alexm_p.lazysetting.model;

import java.util.List;

public record Board(List<Hold> holds) {
    public Board {
        if (holds.isEmpty()) {
            throw new IllegalArgumentException("Board holds cannot be empty");
        }
    }

    public void addHold(Hold hold) {
        if (holds.contains(hold)) {
            throw new RuntimeException("Hold already exists");
        }
        holds.add(hold);
    }

    // Calculate board dimensions based on hold positions
    public double getWidth() {
        if (holds == null || holds.isEmpty()) {
            return 1000; // default width
        }

        double minX = holds.stream()
                .mapToDouble(hold -> hold.pose().x())
                .min().orElse(0);
        double maxX = holds.stream()
                .mapToDouble(hold -> hold.pose().x())
                .max().orElse(1000);
        return maxX - minX + 200; // Add padding
    }

    public double getHeight() {
        if (holds == null || holds.isEmpty()) {
            return 1000; // default height
        }

        double minY = holds.stream()
                .mapToDouble(hold -> hold.pose().y())
                .min().orElse(0);
        double maxY = holds.stream()
                .mapToDouble(hold -> hold.pose().y())
                .max().orElse(1000);
        return maxY - minY + 200; // Add padding
    }

    // Get the bounds for proper centering
    public double getMinX() {
        return holds.stream()
                .mapToDouble(hold -> hold.pose().x())
                .min().orElse(0) - 100; // padding
    }

    public double getMinY() {
        return holds.stream()
                .mapToDouble(hold -> hold.pose().y())
                .min().orElse(0) - 100; // padding
    }
}