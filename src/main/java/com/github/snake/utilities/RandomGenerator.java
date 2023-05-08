package com.github.snake.utilities;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.normal.RunningFood;
import com.github.snake.models.gameboard.foods.normal.StaticFood;
import java.util.Random;

public class RandomGenerator {

  private static Random random = new Random();

  private RandomGenerator() {}

  public static int randomInt(int border) {
    return random.nextInt(border);
  }

  public static NormalFood randomNormalFood() {

    return switch (randomInt(2)) {

      case 0 -> new StaticFood();
      case 1 -> new RunningFood();

      default -> throw new IllegalArgumentException("No such normal food.");
    };
  }

  public static Position randomPositionInBorders(Game game) {

    byte border = Constants.getGameboardRowCol(game);

    return new Position(randomInt(border), randomInt(border));
  }

  public static Position randomNearPosition(Game game, Position position) {

    int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

    Position newPosition;

    do {
      int randomIndex = randomInt(8);

      int newRow = position.getRow() + rowOffsets[randomIndex];
      int newCol = position.getCol() + colOffsets[randomIndex];

      newPosition = new Position(newRow, newCol);

    } while (!Position.isPositionInBorders(game, newPosition));

    return newPosition;
  }


}
