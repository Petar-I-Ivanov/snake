package com.github.snake.services.food;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.repositories.Repository;
import com.github.snake.services.GameboardObjectsService;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NormalFoodService {

  private Repository repository;
  private GameboardObjectsService gameboardObjectsService;

  public NormalFoodService(Repository repository, GameboardObjectsService gameboardObjectsService) {

    this.repository = repository;
    this.gameboardObjectsService = gameboardObjectsService;
  }

  public boolean isPositionNormalFood(Long gameId, Position position) {

    NormalFood food = repository.findSingleByGameId(gameId, NormalFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public void normalFoodCheck(Game game, Position snakeHeadLocation) {

    NormalFood food = repository.findSingleByGameId(game.getId(), NormalFood.class);

    if (food == null) {

      food = getNewNormalFood(game, snakeHeadLocation);
      food.setGame(game);
      repository.save(food);

      game.setNormalFood(food);
      return;
    }

    if (isNormalFoodRunning(food)) {

      Position movePosition = getMoveFoodPosition(game, food.getLocation());
      food.setLocation(movePosition);
    }
  }

  public void eatFoodAtPosition(Long gameId, Position position) {

    NormalFood food = repository.findByGameIdAndPosition(gameId, position, NormalFood.class);

    if (food != null) {
      repository.delete(food);
    }
  }

  private NormalFood getNewNormalFood(Game game, Position snakeHeadPosition) {

    NormalFood food = RandomGenerator.randomNormalFood();
    Position position = getSpawnFoodPosition(game, snakeHeadPosition);
    food.setLocation(position);
    return food;
  }

  private Position getSpawnFoodPosition(Game game, Position snakeHeadPosition) {

    Position spawnPositon;

    do {
      spawnPositon = RandomGenerator.randomPositionInBorders(game);
    } while (isPositionUnavailableToSpawn(game.getId(), spawnPositon, snakeHeadPosition));

    return spawnPositon;
  }

  private boolean isPositionUnavailableToSpawn(Long gameId, Position spawnPosition,
      Position snakeHeadPosition) {

    return Position.isSpawnPositionInTwoRowsAndColsFromSnakePosition(spawnPosition,
        snakeHeadPosition) || gameboardObjectsService.isPositionOccupied(gameId, spawnPosition);
  }

  private Position getMoveFoodPosition(Game game, Position nearPosition) {

    Position movePosition;

    do {
      movePosition = RandomGenerator.randomNearPosition(game, nearPosition);
    } while (gameboardObjectsService.isPositionOccupied(game.getId(), movePosition));

    return movePosition;
  }

  private static boolean isNormalFoodRunning(NormalFood food) {
    return food.getSign().equals(Constants.NORMAL_RUNNING_FOOD_SIGN);
  }
}
