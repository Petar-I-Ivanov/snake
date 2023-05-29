package com.github.snake.services.interfaces;

import com.github.snake.models.Game;
import com.github.snake.utilities.Position;

public interface FoodService {

  void foodCheck(Game game);

  void eatFoodAtPosition(Long gameId, Position position);
}
