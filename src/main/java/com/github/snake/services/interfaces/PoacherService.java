package com.github.snake.services.interfaces;

import com.github.snake.models.Game;

public interface PoacherService {

  void generatePoacher(Game game);

  void movePoachers(Game game);
}
