package com.github.snake.services.interfaces;

import com.github.snake.models.Game;

public interface GamePlayService {

  boolean isGrowthFoodActive(Long gameId);

  boolean isBorderFoodActive(Long gameId);

  boolean isImmunityFoodActive(Long gameId);

  void generateStartingObjects(Game game);

  void gameLoop(Game game, char action);

  String[][] getGameboard(Game game);
}
