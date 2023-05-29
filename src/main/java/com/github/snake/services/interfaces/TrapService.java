package com.github.snake.services.interfaces;

import com.github.snake.models.Game;
import com.github.snake.utilities.Position;

public interface TrapService {

  void generateTrap(Game game, Position position);

  void trapsRemovalCheck(Game game);
}
