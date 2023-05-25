package com.github.snake.services.gameboard.helpers;

import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CommonGameboardService {

  private Repository repository;

  public CommonGameboardService(Repository repository) {
    this.repository = repository;
  }

  public void moveAllWithOneRowAndCol(Long gameId) {

    for (GameboardObject object : findAllForGameId(gameId)) {

      Position nextPosition =
          new Position(object.getRowLocation() + 1, object.getColLocation() + 1);
      object.setLocation(nextPosition);
      repository.save(object);
    }
  }

  public List<GameboardObject> findAllForGameId(Long gameId) {
    return repository.findAllByGameId(gameId);
  }
}
