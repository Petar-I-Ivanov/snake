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

    return (position.row >= 0 && position.row < border)
        && (position.col >= 0 && position.col < border);
  }

  public static boolean arePositionsInlineAndAround(Position positionOne, Position positionTwo) {

    int rowDifference = Math.abs(positionOne.row - positionTwo.row);
    int colDifference = Math.abs(positionOne.col - positionTwo.col);

    return (rowDifference == 1 && colDifference == 0) || (rowDifference == 0 && colDifference == 1);
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

  public static Position modifyOutOfBorderPosition(Game game, Position position) {

    byte border = (byte) (Constants.getGameboardRowCol(game) - 1);

    byte row = position.row;
    byte col = position.col;

    row = (row < 0) ? border : (row > border) ? 0 : row;
    col = (col < 0) ? border : (col > border) ? 0 : col;

    return new Position(row, col);
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
