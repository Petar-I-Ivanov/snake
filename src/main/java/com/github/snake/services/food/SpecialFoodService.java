package com.github.snake.services.food;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.special.BorderFood;
import com.github.snake.models.gameboard.foods.special.GrowthFood;
import com.github.snake.models.gameboard.foods.special.ImmunityFood;
import com.github.snake.models.gameboard.foods.special.PoisonousFood;
import com.github.snake.models.gameboard.foods.special.SpecialFood;
import com.github.snake.repositories.Repository;
import com.github.snake.services.GameboardPositionService;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SpecialFoodService {

  private static final int POISONOUS_TURN = 10;
  private static final int SPECIAL_FOOD_TURN = 5;

  private Repository repository;
  private GameboardPositionService gameboardObjectsService;

  public SpecialFoodService(Repository repository,
      GameboardPositionService gameboardObjectsService) {
    this.repository = repository;
    this.gameboardObjectsService = gameboardObjectsService;
  }

  public boolean isPositionPoisonousFood(Long gameId, Position position) {

    PoisonousFood food = repository.findSingleByGameId(gameId, PoisonousFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public boolean isPositionBorderFood(Long gameId, Position position) {

    BorderFood food = repository.findSingleByGameId(gameId, BorderFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public boolean isPositionGrowthFood(Long gameId, Position position) {

    GrowthFood food = repository.findSingleByGameId(gameId, GrowthFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public boolean isPositionImmunityFood(Long gameId, Position position) {

    ImmunityFood food = repository.findSingleByGameId(gameId, ImmunityFood.class);
    return food != null && food.getLocation().equals(position);
  }

  public void specialFoodCheck(Game game, Position snakeHeadPosition) {

    if (game.getTurn() % POISONOUS_TURN == 0) {
      addPoisonousFood(game, snakeHeadPosition);
    }

    if (game.getTurn() % SPECIAL_FOOD_TURN == 0) {
      addSpecialFood(game, snakeHeadPosition);
    }
  }

  public void eatFoodAtPosition(Long gameId, Position position) {

    SpecialFood food = repository.findByGameIdAndPosition(gameId, position, SpecialFood.class);

    if (food != null) {
      repository.delete(food);
    }
  }

  private void addPoisonousFood(Game game, Position snakeHeadPosition) {

    PoisonousFood food = new PoisonousFood();
    food.setLocation(getSpawnFoodPosition(game, snakeHeadPosition));
    food.setGame(game);

    repository.save(food);
  }

  private void addSpecialFood(Game game, Position snakeHeadPosition) {

    SpecialFood food = RandomGenerator.randomSpecialFood();
    food.setLocation(getSpawnFoodPosition(game, snakeHeadPosition));
    food.setGame(game);

    repository.save(food);
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
}
