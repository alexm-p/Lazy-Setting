package io.github.alexm_p.lazysetting.movement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.alexm_p.lazysetting.model.Board;
import io.github.alexm_p.lazysetting.model.Hold;

public class MoveMap {

    private final Map<HoldPair, MoveCharacteristics> movesData;

    private final List<MoveStyle> moveStyles;

    public MoveMap(Board board, List<MoveStyle> moveStyles) {
        this.moveStyles = moveStyles;
        movesData = new HashMap<>();
        List<Hold> holds = board.holds();

        // Create fully connected default (base difficulty, even favourability) graph
        for (Hold from: holds) {
            for (Hold to: holds) {
                // From hold, to Hold
                MoveCharacteristics chars1 = MoveEngine.calculateCharacteristics
                        (from, to, null, moveStyles);
                HoldPair pair1 = new HoldPair(from, to);
                movesData.put(pair1, chars1);

                // To hold, From hold
                MoveCharacteristics chars2 = MoveEngine.calculateCharacteristics
                        (to, from, null, moveStyles);
                HoldPair pair2 = new HoldPair(to, from);
                movesData.put(pair2, chars2);
            }
        }
    }

    public void updateAll(MoveContext context) {
        for (HoldPair move : movesData.keySet()) {
            double newDiff = MoveEngine.calculateDifficulty(move.from(), move.to(), context);
            movesData.get(move).setDifficulty(newDiff);
        }
    }

    public void updateNeighbours(Hold from, MoveContext context) {
        List<HoldPair> neighbours = movesData.keySet().stream()
                .filter(pair -> pair.from().equals(from))
                .toList();

        for (HoldPair move : neighbours) {
            double newDiff = MoveEngine.calculateDifficulty(move.from(), move.to(), context);
            movesData.get(move).setDifficulty(newDiff);
        }
    }

    public void addMoveStyles(List<MoveStyle> newMoveStyles) {
        for (MoveStyle style : newMoveStyles) {
            if (!moveStyles.contains(style)) {
                moveStyles.add(style);
            }
        }
    }

    public void removeMoveStyles(List<MoveStyle> newMoveStyles) {
        for (MoveStyle style : newMoveStyles) {
            moveStyles.remove(style);
        }
    }

    public Map<HoldPair, MoveCharacteristics> getMovesData() {
        return movesData;
    }
}