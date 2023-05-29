package com.github.snake.services.gameboard.food;

import com.github.snake.models.Game;
import com.github.snake.services.interfaces.FoodService;
import com.github.snake.services.interfaces.NormalFoodService;
import com.github.snake.services.interfaces.SpecialFoodService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FoodServiceImpl implements FoodService {

  private NormalFoodService normalFoodService;
  private SpecialFoodService specialFoodService;

  public FoodServiceImpl(NormalFoodService normalFoodService,
      SpecialFoodService specialFoodService) {

    this.normalFoodService = normalFoodService;
    this.specialFoodService = specialFoodService;
  }

  @Override
  public void foodCheck(Game game) {

    normalFoodService.normalFoodCheck(game);
    specialFoodService.specialFoodCheck(game);
  }

  @Override
  public void eatFoodAtPosition(Long gameId, Position position) {

    normalFoodService.eatFoodAtPosition(gameId, position);
    specialFoodService.eatFoodAtPosition(gameId, position);
  }
}
