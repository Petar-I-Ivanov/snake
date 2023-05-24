package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExitService {

  private Repository repository;
  private RandomPositionGeneratorService randomPositionService;

  public ExitService(Repository repository, RandomPositionGeneratorService randomPositionService) {

    this.repository = repository;
    this.randomPositionService = randomPositionService;
  }

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
