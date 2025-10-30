package io.github.alexm_p.lazysetting.problem;

import io.github.alexm_p.lazysetting.model.*;

import java.util.List;

public class ManualProblem extends Problem {
    public ManualProblem(Board board, List<Hold> holds) {
        super(board);
        this.holds = holds;
    }

    public void addHold(Hold hold) {
        if (holds.contains(hold)) {
            throw new RuntimeException("Hold already exists");
        }
        holds.add(hold);
    }

    public void removeHold(Hold hold) {
        if (!holds.contains(hold)) {
            throw new RuntimeException("Hold does not exist");
        }
        holds.remove(hold);
    }
}