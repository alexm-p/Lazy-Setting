package io.github.alexm_p.lazysetting.core;

import io.github.alexm_p.lazysetting.model.Board;
import io.github.alexm_p.lazysetting.problem.Problem;

import java.util.List;

public record TrainingBook(Board board, List<Problem> problems) {

    public void addProblem(Problem problem) {
        if (!problems.contains(problem)) {
            problems.add(problem);
        }
    }

    public void removeProblem(Problem problem) {
        problems.remove(problem);
    }
}