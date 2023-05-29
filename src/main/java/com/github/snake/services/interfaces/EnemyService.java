package com.github.snake.services.interfaces;

import com.github.snake.models.Game;

public interface EnemyService {

  void generatePoacher(Game game);

  void turnActionAndChecks(Game game);
}
