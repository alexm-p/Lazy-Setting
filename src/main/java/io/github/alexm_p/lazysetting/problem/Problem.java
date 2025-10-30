package io.github.alexm_p.lazysetting.problem;

import io.github.alexm_p.lazysetting.model.*;
import java.util.List;

public abstract class Problem {
    protected String name;
    protected final Board board;
    // Might be good to have holds be final. However, only initialised when generate or manual input called.
    protected List<Hold> holds;
    protected Grade grade;
    protected String comment;

    protected Problem(Board board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public List<Hold> getHolds() {
        return holds;
    }

    public Grade getGrade() {
        return grade;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}