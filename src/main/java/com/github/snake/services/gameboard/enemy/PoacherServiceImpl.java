package com.github.snake.services.gameboard.enemy;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.enemy.Poacher;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.PoacherService;
import com.github.snake.services.interfaces.PositionCheckService;
import com.github.snake.services.interfaces.RandomPositionService;
import com.github.snake.services.interfaces.TrapService;
import com.github.snake.utilities.Position;
import com.github.snake.utilities.RandomGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PoacherServiceImpl implements PoacherService {

  private Repository repository;
  private TrapService trapService;
  private RandomPositionService randomPositionService;
  private PositionCheckService positionService;

  public PoacherServiceImpl(Repository repository, TrapService trapService,
      RandomPositionService randomPositionService, PositionCheckService positionService) {

    this.repository = repository;
    this.trapService = trapService;
    this.randomPositionService = randomPositionService;
    this.positionService = positionService;
  }

  @Override
  public void generatePoacher(Game game) {

    Position position = randomPositionService.getRandomSpawnPoacherPosition(game);

    if (position != null) {

      Poacher poacher = new Poacher();

      poacher.setLocation(position);
      poacher.setGame(game);

      repository.save(poacher);

      // game.getEnemies().add(poacher);
    }
  }

  @Override
  public void movePoachers(Game game) {

    for (Poacher poacher : repository.findListByGameId(game.getId(), Poacher.class)) {

      Position poacherPosition =
          RandomGenerator.isTwentyFivePercentChanceSuccessful() ? poacher.getLocation() : null;

      movePoacher(game, poacher);

      if (poacherPosition != null) {
        trapService.generateTrap(game, poacherPosition);
      }

      repository.save(poacher);
    }
  }

  private void movePoacher(Game game, Poacher poacher) {

    Position nextPosition =
        randomPositionService.getRandomFreeOrBarrierPositionAround(game, poacher.getLocation());

    if (nextPosition == null) {
      return;
    }

    if (positionService.isPositionBarrier(game.getId(), nextPosition)) {

      do {
        nextPosition = randomPositionService.getRandomFreePositionAround(game, nextPosition);
      } while (nextPosition.equals(poacher.getLocation()));
    }

    if (nextPosition != null) {
      poacher.setLocation(nextPosition);
    }
  }
}
