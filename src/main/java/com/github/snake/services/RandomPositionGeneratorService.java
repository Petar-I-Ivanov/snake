package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RandomPositionGeneratorService {

  private GameboardPositionService positionService;

  public RandomPositionGeneratorService(GameboardPositionService positionService) {
    this.positionService = positionService;
  }

  public Position getRandomSpawnFoodPosition(Game game) {

    Position headPosition = positionService.getSnakeHeadPosition(game.getId());
    Position randomPosition;

    do {
      randomPosition = RandomGenerator.randomPositionInBorders(game);
    } while (isFoodSpawnPositionUnavailable(game, randomPosition, headPosition));

    return randomPosition;
  }

  public Position getRandomSpawnBarrierPosition(Game game) {

    List<Position> filteredPositions = getBarrierSpawnPositions(game).stream()
        .filter(position -> isPositionFree(game.getId(), position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  public Position getRandomSpawnPoacherPosition(Game game) {

    List<Position> filteredPositions = getPoacherSpawnPositions(game).stream()
        .filter(position -> isPositionFree(game.getId(), position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  public Position getRandomSpawnExitPosition(Game game) {

    List<Position> filteredPositions = getExitSpawnPositions(game).stream()
        .filter(position -> isPositionFree(game.getId(), position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  public Position getRandomFreePositionAround(Game game, Position aroundPosition) {

    List<Position> filteredPositions = getPositionsAround(game, aroundPosition).stream()
        .filter(position -> isPositionFree(game.getId(), position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  public Position getRandomFreeOrBarrierPositionAround(Game game, Position aroundPosition) {

    List<Position> filteredPositions = getPositionsAround(game, aroundPosition).stream()
        .filter(position -> isPositionFreeOrBarrier(game.getId(), position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  public Position getRandomFreeInlinePositionAround(Game game, Position aroundPosition) {

    List<Position> filteredPositions = getPositionsAround(game, aroundPosition).stream()
        .filter(position -> isPositionFreeAndInline(game.getId(), position, position)).toList();

    return getRandomPositionFromList(filteredPositions);
  }

  private static boolean isSpawnInTwoRowsAndColsFromHead(Position spawn, Position snakeHead) {

    int rowCoefficient = snakeHead.getRow() - spawn.getRow();
    int colCoefficient = snakeHead.getCol() - spawn.getCol();

    return (rowCoefficient == 0 && colCoefficient <= 2)
        || (colCoefficient == 0 && rowCoefficient <= 2);
  }

  private static Position getRandomPositionFromList(List<Position> positions) {

    if (positions.isEmpty()) {
      return null;
    }

    int randomIndex = RandomGenerator.randomInt(positions.size());
    return positions.get(randomIndex);
  }

  private static List<Position> getPositionsAround(Game game, Position aroundPosition) {

    List<Position> positions = new ArrayList<>();

    int startRow = aroundPosition.getRow() - 1;
    int startCol = aroundPosition.getCol() - 1;

    int endRow = aroundPosition.getRow() + 1;
    int endCol = aroundPosition.getCol() + 1;

    for (int row = startRow; row <= endRow; row++) {
      for (int col = startCol; col <= endCol; col++) {

        Position position = new Position(row, col);

        if (Position.isPositionInBorders(game, position) && !position.equals(aroundPosition)) {
          positions.add(position);
        }
      }
    }

    return positions;
  }

  // TODO: edit
  private static List<Position> getBarrierSpawnPositions(Game game) {

    List<Position> positions = new ArrayList<>();

    int border = Constants.getGameboardRowCol(game) - 1;

    // up left
    positions.add(new Position(1, 2));
    positions.add(new Position(2, 1));
    positions.add(new Position(2, 2));

    // up right
    positions.add(new Position(1, border - 2));
    positions.add(new Position(2, border - 2));
    positions.add(new Position(2, border - 1));

    // down left
    positions.add(new Position(border - 2, 1));
    positions.add(new Position(border - 2, 2));
    positions.add(new Position(border - 1, 2));

    // down right
    positions.add(new Position(border - 1, border - 2));
    positions.add(new Position(border - 2, border - 2));
    positions.add(new Position(border - 2, border - 1));

    return positions;
  }

  private static List<Position> getPoacherSpawnPositions(Game game) {

    int border = Constants.getGameboardRowCol(game) - 1;

    List<Position> positions = new ArrayList<>();

    positions.add(new Position(0, 0));
    positions.add(new Position(0, border));
    positions.add(new Position(border, 0));
    positions.add(new Position(border, border));

    return positions;
  }

  private static List<Position> getExitSpawnPositions(Game game) {

    List<Position> positions = new ArrayList<>();

    int border = Constants.getGameboardRowCol(game) - 1;

    for (int row = 0; row <= 1; row++) {
      for (int col = 0; col <= border; col++) {
        positions.add(new Position(row, col));
      }
    }

    for (int row = 0; row <= border; row++) {
      for (int col = 0; col <= 1; col++) {
        positions.add(new Position(row, col));
      }
    }

    return positions;
  }

  private boolean isFoodSpawnPositionUnavailable(Game game, Position spawn, Position head) {
    return !isPositionFree(game.getId(), spawn) || isSpawnInTwoRowsAndColsFromHead(spawn, head);
  }

  private boolean isPositionFree(Long gameId, Position position) {
    return !positionService.isPositionOccupied(gameId, position);
  }

  private boolean isPositionFreeAndInline(Long gameId, Position aroundPosition, Position position) {

    return !positionService.isPositionOccupied(gameId, position)
        && Position.arePositionsInlineAndAround(aroundPosition, position);
  }

  private boolean isPositionFreeOrBarrier(Long gameId, Position position) {

    return !positionService.isPositionOccupied(gameId, position)
        || positionService.isPositionBarrier(gameId, position);
  }
}
