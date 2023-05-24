package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Barrier;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BarrierService {

  private Repository repository;
  private RandomPositionGeneratorService randomPositionService;

  public BarrierService(Repository repository,
      RandomPositionGeneratorService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

  public void generateBarrier(Game game) {

    Position position = randomPositionService.getRandomSpawnBarrierPosition(game);

    if (position != null) {

      Barrier barrier = new Barrier();

      barrier.setLocation(position);
      barrier.setGame(game);

      repository.save(barrier);
      // game.getBarriers().add(barrier);
    }
  }
}
