package com.github.snake.services;

import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.repositories.GameboardObjectRepository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GameboardObjectsService {

  private GameboardObjectRepository gameboardObjectRepository;

  public GameboardObjectsService(GameboardObjectRepository gameboardObjectRepository) {
    this.gameboardObjectRepository = gameboardObjectRepository;
  }

  public boolean isPositionOccupied(Long gameId, Position position) {
    return gameboardObjectRepository.findAnyByGameIdAndPosition(gameId, position) != null;
  }

  public List<GameboardObject> findAllForGameId(Long gameId) {
    return gameboardObjectRepository.findAllByGameId(gameId);
  }
}
