package com.github.snake.services.gameboard.terrain;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Barrier;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.BarrierService;
import com.github.snake.services.interfaces.RandomPositionService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BarrierServiceImpl implements BarrierService {

  private Repository repository;
  private RandomPositionService randomPositionService;

  public BarrierServiceImpl(Repository repository, RandomPositionService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

  @Override
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
