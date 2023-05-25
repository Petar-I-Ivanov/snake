package com.github.snake.services.gameboard.food;

import com.github.snake.models.Game;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FoodService {

  private NormalFoodService normalFoodService;
  private SpecialFoodService specialFoodService;

  public FoodService(NormalFoodService normalFoodService, SpecialFoodService specialFoodService) {

    this.normalFoodService = normalFoodService;
    this.specialFoodService = specialFoodService;
  }

  public void foodCheck(Game game) {

    normalFoodService.normalFoodCheck(game);
    specialFoodService.specialFoodCheck(game);
  }

  public void eatFoodAtPosition(Long gameId, Position position) {

    normalFoodService.eatFoodAtPosition(gameId, position);
    specialFoodService.eatFoodAtPosition(gameId, position);
  }
}
