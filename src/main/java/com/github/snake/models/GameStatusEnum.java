package com.github.snake.models;

public enum GameStatusEnum {

  LEVEL_ONE(1, 5),
  LEVEL_TWO(6, 10),
  LEVEL_THREE(11, 15),
  LEVEL_FOUR(16, 20),
  LEVEL_FIVE(21, 25),
  LEVEL_SIX(26, Integer.MAX_VALUE),
  
  WON(Integer.MIN_VALUE, Integer.MIN_VALUE),
  LOST(Integer.MIN_VALUE, Integer.MIN_VALUE);

  private final int lowerBound;
  private final int upperBound;

  GameStatusEnum(int lowerBound, int upperBound) {

    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  public int getLowerBound() {
    return lowerBound;
  }

  public int getUpperBound() {
    return upperBound;
  }
}
