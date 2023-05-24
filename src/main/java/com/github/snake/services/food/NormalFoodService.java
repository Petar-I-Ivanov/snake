package com.github.snake.services.food;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.repositories.Repository;
import com.github.snake.services.RandomPositionGeneratorService;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NormalFoodService {

  private Repository repository;
  private RandomPositionGeneratorService randomPositionService;

  public NormalFoodService(Repository repository,
      RandomPositionGeneratorService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

  public void normalFoodCheck(Game game) {

    NormalFood food = repository.findSingleByGameId(game.getId(), NormalFood.class);

    if (food == null) {
      generateNewNormalFood(game);
      return;
    }

    if (isNormalFoodRunning(food)) {
      moveRunningFood(game, food);
    }

  }

  public void eatFoodAtPosition(Long gameId, Position position) {

    NormalFood food = repository.findByGameIdAndPosition(gameId, position, NormalFood.class);

    if (food != null) {
      repository.delete(food);
    }
  }

  private void generateNewNormalFood(Game game) {

    Position position = randomPositionService.getRandomSpawnFoodPosition(game);

    if (position != null) {

      NormalFood food = RandomGenerator.randomNormalFood();

      food.setLocation(position);
      food.setGame(game);

      repository.save(food);
      game.setNormalFood(food);
    }
  }

  private void moveRunningFood(Game game, NormalFood food) {

    Position movePosition =
        randomPositionService.getRandomFreePositionAround(game, food.getLocation());

    if (movePosition != null) {

      food.setLocation(movePosition);
      repository.save(food);
    }
  }

  private static boolean isNormalFoodRunning(NormalFood food) {
    return food.getSign().equals(Constants.NORMAL_RUNNING_FOOD_SIGN);
  }
}
