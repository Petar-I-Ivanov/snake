package com.github.snake.services.interfaces;

import com.github.snake.models.Game;

public interface SnakeService {

  int getSnakeSize(Long gameId);

  boolean isBorderFoodActive(Long gameId);

  boolean isGrowthFoodActive(Long gameId);

  boolean isImmunityFoodActive(Long gameId);

  boolean isSnakeKilled(Long gameId);

  boolean isSnakeEscaped(Long gameId);

  void generateSnake(Game game);

  void move(Game game, char action);
}
