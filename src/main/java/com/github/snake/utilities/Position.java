package com.github.snake.utilities;

import com.github.snake.models.Game;
import lombok.Data;

@Data
public class Position {

  private byte row;
  private byte col;

  public Position(int row, int col) {
    this.row = (byte) row;
    this.col = (byte) col;
  }

  public static boolean isPositionInBorders(Game game, Position position) {

    int border = Constants.getGameboardRowCol(game);

    return (position.getRow() >= 0 && position.getRow() < border)
        && (position.getCol() >= 0 && position.getCol() < border);
  }

  public static boolean isSpawnPositionInTwoRowsAndColsFromSnakePosition(Position spawn,
      Position snakeHead) {

    int rowCoefficient = snakeHead.getRow() - spawn.getRow();
    int colCoefficient = snakeHead.getCol() - spawn.getCol();

    return (rowCoefficient == 0 && colCoefficient <= 2)
        || (colCoefficient == 0 && rowCoefficient <= 2);
  }

  public static Position getNextPositionFromChar(Position fromPosition, char action) {

    byte row = fromPosition.row;
    byte col = fromPosition.col;

    return switch (action) {

      case Constants.FORWARD_MOVE -> new Position(row - 1, col);
      case Constants.BACK_MOVE -> new Position(row + 1, col);

      case Constants.RIGHT_MOVE -> new Position(row, col + 1);
      case Constants.LEFT_MOVE -> new Position(row, col - 1);

      default -> throw new IllegalArgumentException("Invalid action sign for next position.");
    };
  }

  @Override
  public boolean equals(Object object) {

    if (object == this) {
      return true;
    }

    if (object instanceof Position position) {
      return this.row == position.row && this.col == position.col;
    }

    return false;
  }

  @Override
  public int hashCode() {

    final int PRIME = 31;

    int result = 1;

    result = PRIME * result + row;
    result = PRIME * result + col;

    return result;
  }
}
