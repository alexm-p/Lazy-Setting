package io.github.alexm_p.lazysetting.strategies;

import io.github.alexm_p.lazysetting.model.Hold;
import io.github.alexm_p.lazysetting.movement.*;

import java.util.List;

public interface ProblemGenerationStrategy {
    List<Hold> generate(MoveMap moveMap, int numMoves, double totalGrade, List<MoveStyle> moveStyles);
}