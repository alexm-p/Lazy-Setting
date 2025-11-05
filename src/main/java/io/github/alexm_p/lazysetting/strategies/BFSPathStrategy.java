package main.java.io.github.alexm_p.lazysetting.strategies;

import io.github.alexm_p.lazysetting.movement.MoveMap;
import io.github.alexm_p.lazysetting.strategies.ProblemGenerationStrategy;
import io.github.alexm_p.lazysetting.model.Board;
import io.github.alexm_p.lazysetting.model.Hold;
import io.github.alexm_p.lazysetting.movement.MoveStyle;

import java.util.List;

public class BFSPathStrategy implements ProblemGenerationStrategy {
    @Override
    public List<Hold> generate(MoveMap moveMap, int numMoves, double grade, List<MoveStyle> moveStyles) {
        return List.of();
    }
}
