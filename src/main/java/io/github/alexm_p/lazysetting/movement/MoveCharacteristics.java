package io.github.alexm_p.lazysetting.movement;

public class MoveCharacteristics {

    private double difficulty;

    private double favorability;

    public MoveCharacteristics(double difficulty, double favorability) {
        this.difficulty = difficulty;
        this.favorability = favorability;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public double getFavorability() {
        return favorability;
    }

    public void setFavorability(double favorability) {
        this.favorability = favorability;
    }
}