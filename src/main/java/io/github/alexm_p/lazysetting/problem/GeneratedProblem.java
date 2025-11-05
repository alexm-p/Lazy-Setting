package io.github.alexm_p.lazysetting.problem;

import io.github.alexm_p.lazysetting.model.*;
import io.github.alexm_p.lazysetting.movement.MoveMap;
import io.github.alexm_p.lazysetting.movement.MoveStyle;
import io.github.alexm_p.lazysetting.strategies.ProblemGenerationStrategy;

import java.util.List;

public class GeneratedProblem extends Problem{
    private final int numMoves;
    private final List<MoveStyle> moveStyles;
    private final ProblemGenerationStrategy strategy;

    public GeneratedProblem(Board board,
                            int numMoves,
                            List<MoveStyle> moveStyles,
                            ProblemGenerationStrategy strategy,
                            Grade desiredGrade) {
        super(board);
        this.numMoves = numMoves;
        this.moveStyles = moveStyles;
        this.strategy = strategy;
        this.grade = desiredGrade;
        this.holds = generate();
    }

    public List<Hold> generate() {
        double totalGrade = grade.getDifficultyWeight();
        MoveMap moveMap = new MoveMap(board, moveStyles);

        return strategy.generate(moveMap, numMoves, totalGrade, moveStyles);
    }
}