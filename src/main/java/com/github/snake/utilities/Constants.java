package com.github.snake.utilities;

import com.github.snake.models.Game;

public class Constants {

  public static final byte LEVEL_ONE_ROW_COL = 5;
  public static final byte LEVEL_TWO_ROW_COL = 7;
  public static final byte LEVEL_THREE_ROW_COL = 9;
  public static final byte LEVEL_FOUR_ROW_COL = 11;
  public static final byte LEVEL_FIVE_ROW_COL = 13;
  public static final byte LEVEL_SIX_ROW_COL = 15;

  public static final String SNAKE_HEAD_SIGN = "H";
  public static final String SNAKE_BODY_SIGN = "B";

  public static final String NORMAL_STATIC_FOOD_SIGN = "O";
  public static final String NORMAL_RUNNING_FOOD_SIGN = "R";

  public static final String SPECIAL_BORDER_FOOD_SIGN = "Q";
  public static final String SPECIAL_GROWTH_FOOD_SIGN = "W";
  public static final String SPECIAL_IMMUNITY_FOOD_SIGN = "U";

  public static final String POISONOUS_FOOD_SIGN = "O";

  public static final String POACHER_SIGN = "P";
  public static final String TRAP_SIGN = "T";

  public static final String BARRIER_SIGN = "Y";
  public static final String EXIT_SIGN = "E";

  public static final String EMPTY_SPACE_SIGN = "X";

  public static final char FORWARD_MOVE = 'w';
  public static final char RIGHT_MOVE = 'd';
  public static final char BACK_MOVE = 's';
  public static final char LEFT_MOVE = 'a';

  private Constants() {}

  public static byte getGameboardRowCol(Game game) {

    return switch (game.getStatus()) {

      case LEVEL_ONE -> LEVEL_ONE_ROW_COL;
      case LEVEL_TWO -> LEVEL_TWO_ROW_COL;
      case LEVEL_THREE -> LEVEL_THREE_ROW_COL;

      case LEVEL_FOUR -> LEVEL_FOUR_ROW_COL;
      case LEVEL_FIVE -> LEVEL_FIVE_ROW_COL;
      case LEVEL_SIX -> LEVEL_SIX_ROW_COL;

      default -> throw new IllegalArgumentException("This game is already compleated.");
    };
  }

  public static boolean isCharAvailableMove(char sign) {

    return sign == FORWARD_MOVE || sign == RIGHT_MOVE || sign == BACK_MOVE || sign == LEFT_MOVE;
  }
}
