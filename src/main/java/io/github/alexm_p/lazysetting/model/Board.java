package io.github.alexm_p.lazysetting.model;

import java.util.List;

public class Board {
    private final List<Hold> holds;

    public Board(List<Hold> holds) {
        if (holds.isEmpty()) {
            throw new IllegalArgumentException("Board holds cannot be empty");
        }
        this.holds = holds;
    }

    public void addHold(Hold hold) {
        if (holds.contains(hold)) {
            throw new RuntimeException("Hold already exists");
        }
        holds.add(hold);
    }

    public List<Hold> getHolds() { return holds; }
}