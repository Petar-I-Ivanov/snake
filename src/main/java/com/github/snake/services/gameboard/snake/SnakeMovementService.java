package com.github.snake.services.gameboard.snake;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.FoodService;
import com.github.snake.services.interfaces.PositionCheckService;
import com.github.snake.services.interfaces.RandomPositionService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeMovementService {

  private Repository repository;
  private FoodService foodService;

  private PositionCheckService positionService;
  private RandomPositionService randomPositionService;

  public SnakeMovementService(Repository repository, FoodService foodService,
      PositionCheckService positionService, RandomPositionService randomPositionService) {

    this.repository = repository;
    this.foodService = foodService;

    this.positionService = positionService;
    this.randomPositionService = randomPositionService;
  }

  public void move(Game game, char action) {

    Long gameId = game.getId();

    SnakeHead head = repository.findSingleByGameId(gameId, SnakeHead.class);

    Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);
    nextPosition = validateNextPosition(game, head, nextPosition);

    if (!positionService.isPositionOccupied(gameId, nextPosition)) {
      moveAndReturnLastPosition(gameId, nextPosition);
    }

    handleMoveToFood(game, head, nextPosition);
    handleCollapse(game, head, nextPosition);

    repository.save(head);
  }

  private Position validateNextPosition(Game game, SnakeHead head, Position nextPosition) {

    if (isNextPositionUnavailable(game, head, nextPosition)) {
      throw new IllegalArgumentException("Next position is outside the borders.");
    }

    if (isMoveOutOfBorderAndAvailable(game, head, nextPosition)) {
      head.setBorderFoodActive(false);
      return Position.modifyOutOfBorderPosition(game, nextPosition);
    }

    return nextPosition;
  }

  public void handleMoveToFood(Game game, SnakeHead head, Position nextPosition) {

    Long gameId = game.getId();

    if (positionService.isPositionNormalFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);

      if (head.isPoisonousFoodActive()) {
        moveAndReturnLastPosition(gameId, nextPosition);
        return;
      }

      Position lastPosition = moveAndReturnLastPosition(gameId, nextPosition);
      addBodyAtPosition(game, lastPosition);

      if (head.isGrowthFoodActive()) {
        addBodyAroundLast(game, lastPosition);
        head.setGrowthFoodActive(false);
      }
    }

    if (positionService.isPositionPoisonousFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(gameId, nextPosition);

      if (head.isImmunityFoodActive()) {
        head.setImmunityFoodActive(false);
        return;
      }

      if (areThereNoBodies(gameId)) {
        head.setPoisonousFoodActive(true);
      } else {
        removeLastBody(gameId);
      }
    }

    if (positionService.isPositionBorderFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(gameId, nextPosition);
      head.setBorderFoodActive(true);
    }

    if (positionService.isPositionGrowthFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(gameId, nextPosition);
      head.setGrowthFoodActive(true);
    }

    if (positionService.isPositionImmunityFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(gameId, nextPosition);
      head.setImmunityFoodActive(true);
    }
  }

  private void handleCollapse(Game game, SnakeHead head, Position nextPosition) {

    if (positionService.isPositionEnemy(game.getId(), nextPosition)) {
      head.setKilled(true);
    }

    if (positionService.isPositionExit(game.getId(), nextPosition)) {
      head.setEscaped(true);
    }
  }

  private boolean isNextPositionUnavailable(Game game, SnakeHead head, Position nextPosition) {

    return isMoveOutOfBorderAndUnvailable(game, head, nextPosition)
        || positionService.isPositionBarrier(game.getId(), nextPosition)
        || positionService.isPositionSnakeBody(game.getId(), nextPosition);
  }

  private boolean areThereNoBodies(Long gameId) {
    return repository.findListByGameId(gameId, SnakeBody.class).isEmpty();
  }

  private Position moveAndReturnLastPosition(Long gameId, Position nextPosition) {

    SnakeHead head = repository.findSingleByGameId(gameId, SnakeHead.class);
    List<SnakeBody> bodies = repository.findListByGameId(gameId, SnakeBody.class);

    Position temp = head.getLocation();
    head.setLocation(nextPosition);
    nextPosition = temp;

    for (SnakeBody body : bodies) {

      temp = body.getLocation();
      body.setLocation(nextPosition);
      nextPosition = temp;
    }

    repository.save(head);
    repository.save(bodies);

    return nextPosition;
  }

  private void addBodyAroundLast(Game game, Position lastPosition) {

    Position aroundLastPosition =
        randomPositionService.getRandomFreeInlinePositionAround(game, lastPosition);

    if (aroundLastPosition != null) {
      addBodyAtPosition(game, aroundLastPosition);
    }
  }

  private void addBodyAtPosition(Game game, Position position) {

    SnakeBody body = new SnakeBody();

    body.setLocation(position);
    body.setGame(game);
    // game.getSnakeBodies().add(body);

    repository.save(body);
  }

  private void removeLastBody(Long gameId) {

    List<SnakeBody> bodies = repository.findListByGameId(gameId, SnakeBody.class);

    int lastBodyIndex = bodies.size() - 1;
    SnakeBody lastBody = bodies.get(lastBodyIndex);

    repository.delete(lastBody);
  }

  private static boolean isMoveOutOfBorderAndAvailable(Game game, SnakeHead head,
      Position nextPosition) {
    return !Position.isPositionInBorders(game, nextPosition) && head.isBorderFoodActive();
  }

  private static boolean isMoveOutOfBorderAndUnvailable(Game game, SnakeHead head,
      Position nextPosition) {
    return !Position.isPositionInBorders(game, nextPosition) && !head.isBorderFoodActive();
  }
}
