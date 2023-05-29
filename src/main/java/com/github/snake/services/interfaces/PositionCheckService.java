package com.github.snake.services.interfaces;

import com.github.snake.utilities.Position;

public interface PositionCheckService {

  Position getSnakeHeadPosition(Long gameId);

  boolean isPositionOccupied(Long gameId, Position position);

  boolean isPositionNormalFood(Long gameId, Position position);

  boolean isPositionBorderFood(Long gameId, Position position);

  boolean isPositionGrowthFood(Long gameId, Position position);

  boolean isPositionImmunityFood(Long gameId, Position position);

  boolean isPositionPoisonousFood(Long gameId, Position position);

  boolean isPositionSnakeBody(Long gameId, Position position);

  boolean isPositionEnemy(Long gameId, Position position);

  boolean isPositionBarrier(Long gameId, Position position);

  boolean isPositionExit(Long gameId, Position position);
}
