package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.services.food.FoodService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeService {

  private Repository repository;
  private FoodService foodService;

  public SnakeService(Repository repository) {
    this.repository = repository;
  }

  public void setFoodService(FoodService foodService) {
    this.foodService = foodService;
  }

  public Position getSnakeHeadLocation(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).getLocation();
  }

  public int getSnakeSize(Long gameId) {
    return repository.findListByGameId(gameId, SnakeBody.class).size() + 1;
  }

  public void addSnake(Game game) {

    SnakeHead snakeHead = new SnakeHead();
    snakeHead.setGame(game);
    repository.save(snakeHead);

    game.setSnakeHead(snakeHead);
  }

  public void move(Game game, char action) {

    SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
    List<SnakeBody> bodies = repository.findListByGameId(game.getId(), SnakeBody.class);

    Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);

    if (!Position.isPositionInBorders(game, nextPosition)) {
      throw new IllegalArgumentException("Next position is outside the borders.");
    }

    if (nextPositionObjectCheck(game.getId(), nextPosition, head)) {

      Position lastPosition = moveAndReturnLastPosition(head, bodies, nextPosition);
      foodService.eatFoodAtPosition(game.getId(), nextPosition);

      SnakeBody body = new SnakeBody();
      body.setLocation(lastPosition);
      body.setGame(game);
      bodies.add(body);

      repository.save(head);
      repository.save(bodies);
      return;
    }

    moveAndReturnLastPosition(head, bodies, nextPosition);
  }

  private static Position moveAndReturnLastPosition(SnakeHead head, List<SnakeBody> bodies,
      Position position) {

    Position temp = head.getLocation();
    head.setLocation(position);
    position = temp;

    for (SnakeBody body : bodies) {

      temp = body.getLocation();
      body.setLocation(position);
      position = temp;
    }

    return position;
  }

  private boolean nextPositionObjectCheck(Long gameId, Position nextPosition, SnakeHead snakeHead) {

    if (foodService.isPositionNormalFood(gameId, nextPosition)) {
      return true;
    }

    if (foodService.isPositionPoisonousFood(gameId, nextPosition)) {
      snakeHead.setPoisonousFoodActive(true);
      foodService.eatFoodAtPosition(gameId, nextPosition);
    }

    if (foodService.isPositionBorderFood(gameId, nextPosition)) {
      snakeHead.setBorderFoodActive(true);
      foodService.eatFoodAtPosition(gameId, nextPosition);

    }

    if (foodService.isPositionGrowthFood(gameId, nextPosition)) {
      snakeHead.setGrowthFoodActive(true);
      foodService.eatFoodAtPosition(gameId, nextPosition);
    }

    if (foodService.isPositionImmunityFood(gameId, nextPosition)) {
      snakeHead.setImmunityFoodActive(true);
      foodService.eatFoodAtPosition(gameId, nextPosition);
    }

    return false;
  }
}
