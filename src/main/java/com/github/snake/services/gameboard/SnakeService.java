package com.github.snake.services.gameboard;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.services.gameboard.food.FoodService;
import com.github.snake.services.gameboard.helpers.GameboardPositionService;
import com.github.snake.services.gameboard.helpers.RandomPositionGeneratorService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

// TODO: edit the logic and make it work
@ApplicationScoped
public class SnakeService {

  private Repository repository;
  private FoodService foodService;

  private GameboardPositionService positionService;
  private RandomPositionGeneratorService randomPositionService;

  public SnakeService(Repository repository, FoodService foodService,
      GameboardPositionService positionService,
      RandomPositionGeneratorService randomPositionService) {

    this.repository = repository;
    this.foodService = foodService;

    this.positionService = positionService;
    this.randomPositionService = randomPositionService;
  }

  public int getSnakeSize(Long gameId) {
    return repository.findListByGameId(gameId, SnakeBody.class).size() + 1;
  }

  public boolean isSnakeKilled(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isKilled();
  }

  public boolean isSnakeEscaped(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isEscaped();
  }

  public void generateSnake(Game game) {

    SnakeHead snakeHead = new SnakeHead();
    snakeHead.setGame(game);
    repository.save(snakeHead);

    game.setSnakeHead(snakeHead);
  }

  public void move(Game game, char action) {

    SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
    List<SnakeBody> bodies = repository.findListByGameId(game.getId(), SnakeBody.class);

    Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);

    nextPosition = validateNextPosition(game, head, nextPosition);

    if (!positionService.isPositionOccupied(game.getId(), nextPosition)) {
      moveAndReturnLastPosition(head, bodies, nextPosition);
    }

    handleFoodEating(game, head, bodies, nextPosition);
    handleCollisions(game.getId(), head, nextPosition);

    repository.save(head);
    repository.save(bodies);
  }

  private Position validateNextPosition(Game game, SnakeHead head, Position nextPosition) {

    if (!Position.isPositionInBorders(game, nextPosition) && !head.isBorderFoodActive()) {
      throw new IllegalArgumentException("Next position is outside the borders.");
    }

    if (head.isBorderFoodActive() && !Position.isPositionInBorders(game, nextPosition)) {
      head.setBorderFoodActive(false);
      return Position.modifyOutOfBorderPosition(game, nextPosition);
    }

    return nextPosition;
  }

  private void handleFoodEating(Game game, SnakeHead head, List<SnakeBody> bodies,
      Position nextPosition) {

    Long gameId = game.getId();

    if (positionService.isPositionNormalFood(gameId, nextPosition)) {
      moveToNormalFood(game, head, bodies, nextPosition);
    }

    if (positionService.isPositionPoisonousFood(gameId, nextPosition)) {

      SnakeBody lastBody = moveToPoisonousFood(gameId, head, bodies, nextPosition);
      bodies.remove(lastBody);
      repository.delete(lastBody);
      repository.save(bodies);
    }

    if (positionService.isPositionBorderFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(head, bodies, nextPosition);
      head.setBorderFoodActive(true);
    }

    if (positionService.isPositionGrowthFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(head, bodies, nextPosition);
      head.setGrowthFoodActive(true);
    }

    if (positionService.isPositionImmunityFood(gameId, nextPosition)) {

      foodService.eatFoodAtPosition(gameId, nextPosition);
      moveAndReturnLastPosition(head, bodies, nextPosition);
      head.setImmunityFoodActive(true);
    }
  }

  private void handleCollisions(Long gameId, SnakeHead head, Position nextPosition) {

    if (positionService.isPositionEnemy(gameId, nextPosition)) {
      head.setKilled(true);
    }

    if (positionService.isPositionExit(gameId, nextPosition)) {
      head.setEscaped(true);
    }
  }

  private SnakeBody moveToPoisonousFood(Long gameId, SnakeHead head, List<SnakeBody> bodies,
      Position nextPosition) {

    foodService.eatFoodAtPosition(gameId, nextPosition);
    moveAndReturnLastPosition(head, bodies, nextPosition);

    if (head.isImmunityFoodActive()) {
      head.setImmunityFoodActive(false);
      return null;
    }

    if (bodies.isEmpty()) {
      head.setPoisonousFoodActive(true);
      return null;
    }

    return bodies.get(bodies.size() - 1);
  }

  private void moveToNormalFood(Game game, SnakeHead head, List<SnakeBody> bodies,
      Position nextPosition) {

    foodService.eatFoodAtPosition(game.getId(), nextPosition);
    Position lastPosition = moveAndReturnLastPosition(head, bodies, nextPosition);

    if (!head.isPoisonousFoodActive()) {
      addBodyIntoSnake(game, lastPosition, bodies);
    } else {
      head.setPoisonousFoodActive(false);
    }

    if (head.isGrowthFoodActive()) {
      addBodyIntoSnakeAroundLast(game, bodies);
      head.setGrowthFoodActive(false);
    }
  }

  private void addBodyIntoSnakeAroundLast(Game game, List<SnakeBody> bodies) {

    SnakeBody lastBody = bodies.get(bodies.size() - 1);
    Position position =
        randomPositionService.getRandomFreeInlinePositionAround(game, lastBody.getLocation());

    if (position != null) {
      addBodyIntoSnake(game, position, bodies);
    }
  }

  private static Position moveAndReturnLastPosition(SnakeHead head, List<SnakeBody> bodies,
      Position nextPosition) {

    Position temp = head.getLocation();
    head.setLocation(nextPosition);
    nextPosition = temp;

    for (SnakeBody body : bodies) {

      temp = body.getLocation();
      body.setLocation(nextPosition);
      nextPosition = temp;
    }

    return nextPosition;
  }

  private static void addBodyIntoSnake(Game game, Position position, List<SnakeBody> bodies) {

    SnakeBody body = new SnakeBody();
    body.setLocation(position);
    body.setGame(game);
    bodies.add(body);
  }
}
