package com.github.snake.services.gameboard.food;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.foods.special.PoisonousFood;
import com.github.snake.models.gameboard.foods.special.SpecialFood;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.RandomPositionService;
import com.github.snake.services.interfaces.SpecialFoodService;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SpecialFoodServiceImpl implements SpecialFoodService {

  private static final int POISONOUS_TURN = 10;
  private static final int SPECIAL_FOOD_TURN = 5;

  private Repository repository;
  private RandomPositionService randomPositionService;

  public SpecialFoodServiceImpl(Repository repository,
      RandomPositionService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

  @Override
  public void specialFoodCheck(Game game) {

    if (game.getTurn() % POISONOUS_TURN == 0) {
      addPoisonousFood(game);
    }

    if (game.getTurn() % SPECIAL_FOOD_TURN == 0) {
      addSpecialFood(game);
    }
  }

  @Override
  public void eatFoodAtPosition(Long gameId, Position position) {

    SpecialFood food = repository.findByGameIdAndPosition(gameId, position, SpecialFood.class);

    if (food != null) {
      repository.delete(food);
    }
  }

  private void addPoisonousFood(Game game) {

    Position position = randomPositionService.getRandomSpawnFoodPosition(game);

    if (position != null) {

      PoisonousFood food = new PoisonousFood();

      food.setLocation(position);
      food.setGame(game);

      repository.save(food);
      // game.getSpecialFoods().add(food);
    }
  }

  private void addSpecialFood(Game game) {

    Position position = randomPositionService.getRandomSpawnFoodPosition(game);

    if (position != null) {

      SpecialFood food = RandomGenerator.randomSpecialFood();

      food.setLocation(position);
      food.setGame(game);

      repository.save(food);
      // game.getSpecialFoods().add(food);
    }
  }
}
