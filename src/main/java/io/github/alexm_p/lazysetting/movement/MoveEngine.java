package io.github.alexm_p.lazysetting.movement;

import io.github.alexm_p.lazysetting.model.Hold;
import io.github.alexm_p.lazysetting.model.HoldType;

import java.util.List;

final public class MoveEngine {

    private MoveEngine() {}

    public static MoveCharacteristics calculateCharacteristics(Hold from, Hold to, MoveContext context, List<MoveStyle> styles) {
        return new MoveCharacteristics(calculateDifficulty(from, to, context), calculateFavourability(from, to, styles));
    }

    public static double calculateDifficulty(Hold from, Hold to, MoveContext context) {
        // Move distance difficulty
        double yDiff = to.pose().y() - from.pose().y();
        double xDiff = to.pose().x() - from.pose().x();
        double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

        // Hold difficulty (how positive the holds are)
        double holdDifficulty = (from.difficulty() * 0.3) + (to.difficulty() * 0.7);

        // TODO: Hold rotation (easier if move direction and hold rotation are similar, harder the more opposite)
        /*
        double moveDirection;
        double holdRotation;

         */

        // TODO: Context difficulty (Moving left is harder if using right hand )
        /*
        if (context != null) {

        }

         */
        return distance * holdDifficulty;
    }

    public static double calculateFavourability(Hold from, Hold to, List<MoveStyle> styles) {
        double favourability = 0;

        for (MoveStyle style : styles) {
            switch (style) {
                case CRIMPY -> {
                    favourability += crimpy(from, to);
                }
                case DEADPOINT -> {

                }
            }
        }

        return favourability;
    }

    private static double crimpy(Hold from, Hold to) {
        if (to.holdType() == HoldType.CRIMP) {
            return 1;
        } else {
            return 0;
        }
    }

}