package com.github.snake.services.interfaces;

import com.github.snake.models.Game;
import com.github.snake.utilities.Position;

public interface RandomPositionService {

  Position getRandomSpawnFoodPosition(Game game);

  Position getRandomSpawnBarrierPosition(Game game);

  Position getRandomSpawnPoacherPosition(Game game);

  Position getRandomSpawnExitPosition(Game game);

  Position getRandomFreePositionAround(Game game, Position aroundPosition);

  Position getRandomFreeOrBarrierPositionAround(Game game, Position aroundPosition);

  Position getRandomFreeInlinePositionAround(Game game, Position aroundPosition);
}
