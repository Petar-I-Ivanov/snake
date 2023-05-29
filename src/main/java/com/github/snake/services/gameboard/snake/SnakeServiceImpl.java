package com.github.snake.services.gameboard.snake;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.PositionCheckService;
import com.github.snake.services.interfaces.SnakeService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeServiceImpl implements SnakeService {

  private Repository repository;
  private SnakeMovementService snakeMovementService;
  private PositionCheckService positionService;

  public SnakeServiceImpl(Repository repository, SnakeMovementService snakeMovementService,
      PositionCheckService positionService) {

    this.repository = repository;
    this.snakeMovementService = snakeMovementService;
    this.positionService = positionService;
  }

  @Override
  public int getSnakeSize(Long gameId) {
    return repository.findListByGameId(gameId, SnakeBody.class).size() + 1;
  }

  @Override
  public boolean isBorderFoodActive(Long gameId) {
    SnakeHead head = repository.findSingleByGameId(gameId, SnakeHead.class);
    return head != null && head.isBorderFoodActive();
  }

  @Override
  public boolean isGrowthFoodActive(Long gameId) {
    SnakeHead head = repository.findSingleByGameId(gameId, SnakeHead.class);
    return head != null && head.isGrowthFoodActive();
  }

  @Override
  public boolean isImmunityFoodActive(Long gameId) {
    SnakeHead head = repository.findSingleByGameId(gameId, SnakeHead.class);
    return head != null && head.isImmunityFoodActive();
  }

  @Override
  public boolean isSnakeKilled(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isKilled();
  }

  @Override
  public boolean isSnakeEscaped(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isEscaped();
  }

  @Override
  public void generateSnake(Game game) {

    SnakeHead snakeHead = new SnakeHead();
    snakeHead.setGame(game);
    repository.save(snakeHead);

    game.setSnakeHead(snakeHead);
  }

  @Override
  public void move(Game game, char action) {

    snakeMovementService.move(game, action);

    if (isSnakeTrapped(game)) {

      SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
      head.setKilled(true);
      repository.save(head);
    }
  }

  private boolean isSnakeTrapped(Game game) {

    SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);

    for (Position position : getPositionsToCheckForTrapped(head.getLocation())) {

      if (!isPositionUnavailableToMove(game, position)) {
        return false;
      }
    }

    return true;
  }

  private boolean isPositionUnavailableToMove(Game game, Position position) {

    return !Position.isPositionInBorders(game, position)
        || positionService.isPositionBarrier(game.getId(), position)
        || positionService.isPositionSnakeBody(game.getId(), position);
  }

  private static List<Position> getPositionsToCheckForTrapped(Position snakePosition) {

    return List.of(new Position(snakePosition.getRow() - 1, snakePosition.getCol()),
        new Position(snakePosition.getRow() + 1, snakePosition.getCol()),
        new Position(snakePosition.getRow(), snakePosition.getCol() - 1),
        new Position(snakePosition.getRow(), snakePosition.getCol() + 1));
  }
}
