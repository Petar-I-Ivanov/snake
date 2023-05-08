package com.github.snake.services.food;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.repositories.Repository;
import com.github.snake.services.GameboardObjectsService;
import com.github.snake.services.SnakeService;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NormalFoodService {

  private Repository repository;

  private SnakeService snakeService;
  private GameboardObjectsService gameboardObjectsService;

  public NormalFoodService(Repository repository, SnakeService snakeService,
      GameboardObjectsService gameboardObjectsService) {

    this.repository = repository;
    this.snakeService = snakeService;
    this.gameboardObjectsService = gameboardObjectsService;
  }

  public boolean isPositionNormalFood(Long gameId, Position position) {

    NormalFood food = repository.findSingleByGameId(gameId, NormalFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public void addCheck(Game game) {

    NormalFood food = repository.findSingleByGameId(game.getId(), NormalFood.class);

    if (food == null) {

      food = getNewNormalFood(game);
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
    repository.delete(food);
  }

  private NormalFood getNewNormalFood(Game game) {

    NormalFood food = RandomGenerator.randomNormalFood();
    Position position = getSpawnFoodPosition(game);
    food.setLocation(position);
    return food;
  }

  private Position getSpawnFoodPosition(Game game) {

    Position spawnPositon;

    do {
      spawnPositon = RandomGenerator.randomPositionInBorders(game);
    } while (isPositionUnavailableToSpawn(game.getId(), spawnPositon));

    return spawnPositon;
  }

  private Position getMoveFoodPosition(Game game, Position nearPosition) {

    Position movePosition;

    do {
      movePosition = RandomGenerator.randomNearPosition(game, nearPosition);
    } while (gameboardObjectsService.isPositionOccupied(game.getId(), movePosition));

    return movePosition;
  }

  private boolean isPositionUnavailableToSpawn(Long gameId, Position spawnPosition) {

    return isPositionInTwoRolsOrColsFromSnake(gameId, spawnPosition)
        || gameboardObjectsService.isPositionOccupied(gameId, spawnPosition);
  }

  private boolean isPositionInTwoRolsOrColsFromSnake(Long gameId, Position spawnPosition) {

    Position snakeHeadLocation = snakeService.getSnakeHeadLocation(gameId);

    int rowCoefficient = snakeHeadLocation.getRow() - spawnPosition.getRow();
    int colCoefficient = snakeHeadLocation.getCol() - spawnPosition.getCol();

    return (rowCoefficient == 0 && colCoefficient <= 2)
        || (colCoefficient == 0 && rowCoefficient <= 2);

  }

  private static boolean isNormalFoodRunning(NormalFood food) {
    return food.getSign().equals(Constants.NORMAL_RUNNING_FOOD_SIGN);
  }
}
