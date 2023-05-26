package com.github.snake.services.gameboard;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SnakeService {

  private Repository repository;
  private SnakeMovementService snakeMovementService;

  public SnakeService(Repository repository, SnakeMovementService snakeMovementService) {

    this.repository = repository;
    this.snakeMovementService = snakeMovementService;
  }

  public int getSnakeSize(Long gameId) {
    return repository.findListByGameId(gameId, SnakeBody.class).size() + 1;
  }

  public boolean isSnakeKilled(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isKilled();
  }

  public boolean isSnakeEscaped(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).isEscaped();
  }

  public void generateSnake(Game game) {

    SnakeHead snakeHead = new SnakeHead();
    snakeHead.setGame(game);
    repository.save(snakeHead);

    game.setSnakeHead(snakeHead);
  }

  public void move(Game game, char action) {
    snakeMovementService.move(game, action);
  }
}
