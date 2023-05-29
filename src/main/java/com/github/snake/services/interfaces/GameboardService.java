package com.github.snake.services.interfaces;

import com.github.snake.models.gameboard.GameboardObject;
import java.util.List;

public interface GameboardService {

  void moveAllWithOneRowAndCol(Long gameId);

  List<GameboardObject> findAllForGameId(Long gameId);

  void deleteGameboardObjects(Long gameId);
}
