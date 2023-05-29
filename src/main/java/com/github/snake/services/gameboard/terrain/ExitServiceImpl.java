package com.github.snake.services.gameboard.terrain;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.ExitService;
import com.github.snake.services.interfaces.RandomPositionService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExitServiceImpl implements ExitService {

  private Repository repository;
  private RandomPositionService randomPositionService;

  public ExitServiceImpl(Repository repository, RandomPositionService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

  @Override
  public void generateExit(Game game) {

    Position position = randomPositionService.getRandomSpawnExitPosition(game);

    if (position != null) {

      Exit exit = new Exit();

      exit.setLocation(position);
      exit.setGame(game);

      repository.save(exit);
      game.setExit(exit);
    }
  }
}
