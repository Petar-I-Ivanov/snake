package com.github.snake.repositories;

import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Position;
import java.util.List;

public interface Repository {

  GameboardObject findAnyByGameIdAndPosition(Long gameId, Position position);

  List<GameboardObject> findAllByGameId(Long gameId);

  <T extends GameboardObject> T findByGameIdAndPosition(Long gameId, Position position,
      Class<T> entityClass);

  <T extends GameboardObject> T findSingleByGameId(Long gameId, Class<T> entityClass);

  <T extends GameboardObject> List<T> findListByGameId(Long gameId, Class<T> entityClass);

  <T extends GameboardObject> void save(T entity);


  /**
   * @param <T> - the expected .class for the entity we're working with
   * @param entities - the entity list that should be saved
   */
  default <T extends GameboardObject> void save(List<T> entities) {

    for (T entity : entities) {
      save(entity);
    }
  }

  <T extends GameboardObject> void delete(T entity);

  /**
   * @param gameId - the game ID where all related gameboard entities should be deleted
   */
  default void deleteAll(Long gameId) {
    findAllByGameId(gameId).stream().forEach(this::delete);
  }
}
