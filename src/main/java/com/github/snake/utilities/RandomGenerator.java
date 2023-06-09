package com.github.snake.utilities;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.normal.RunningFood;
import com.github.snake.models.gameboard.foods.normal.StaticFood;
import com.github.snake.models.gameboard.foods.special.BorderFood;
import com.github.snake.models.gameboard.foods.special.GrowthFood;
import com.github.snake.models.gameboard.foods.special.ImmunityFood;
import com.github.snake.models.gameboard.foods.special.SpecialFood;
import java.util.Random;

public class RandomGenerator {

  private static Random random = new Random();

  private RandomGenerator() {}

  public static int randomInt(int border) {
    return random.nextInt(border);
  }

  public static boolean isTwentyFivePercentChanceSuccessful() {
    return randomInt(4) == 1;
  }

  public static NormalFood randomNormalFood() {

    return switch (randomInt(2)) {

      case 0 -> new StaticFood();
      case 1 -> new RunningFood();

      default -> throw new IllegalArgumentException("No such normal food.");
    };
  }

  public static SpecialFood randomSpecialFood() {

    return switch (randomInt(3)) {

      case 0 -> new BorderFood();
      case 1 -> new GrowthFood();
      case 2 -> new ImmunityFood();

      default -> throw new IllegalArgumentException("No such special food.");
    };
  }

  public static Position randomPositionInBorders(Game game) {

    byte border = Constants.getGameboardRowCol(game);
    return new Position(randomInt(border), randomInt(border));
  }
}
