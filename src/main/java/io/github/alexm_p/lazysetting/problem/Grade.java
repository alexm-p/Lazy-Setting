package io.github.alexm_p.lazysetting.problem;

public enum Grade {
    // Linear grade scale might not be accurate to real life
    V1(1.0),
    V2(2.0),
    V3(3.0),
    V4(4.0),
    V5(5.0),
    V6(6.0),
    V7(7.0),
    V8(8.0),
    V9(9.0),
    V10(10.0),
    V11(11.0),
    V12(12.0),
    V13(13.0);

    private final double difficultyWeight;

    Grade(double difficultyWeight) {
        this.difficultyWeight = difficultyWeight;
    }

    public double getDifficultyWeight() { return difficultyWeight; }
}