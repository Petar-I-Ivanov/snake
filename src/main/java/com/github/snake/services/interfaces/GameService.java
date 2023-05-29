package com.github.snake.services.interfaces;

import com.github.snake.models.Game;

public interface GameService {

  Game findById(Long gameId);

  Game startNewGame();

  Game makeAction(Long gameId, char action);
}
