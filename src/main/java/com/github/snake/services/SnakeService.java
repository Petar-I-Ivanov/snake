package com.github.snake.services;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SnakeService {

  private Repository repository;

  public SnakeService(Repository repository) {
    this.repository = repository;
  }

  public Position getSnakeHeadLocation(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).getLocation();
  }

  public boolean isPositionSnake(Long gameId, Position position) {

    return repository.findByGameIdAndPosition(gameId, position, SnakeHead.class) != null
        || repository.findByGameIdAndPosition(gameId, position, SnakeBody.class) != null;
  }

  public void addSnake(Game game) {

    SnakeHead snakeHead = new SnakeHead();
    snakeHead.setGame(game);
    repository.save(snakeHead);

    game.setSnakeHead(snakeHead);
  }

  public void move(Game game, char action) {

    SnakeHead head = repository.findSingleByGameId(game.getId(), SnakeHead.class);
    List<SnakeBody> bodies = repository.findListByGameId(game.getId(), SnakeBody.class);

    Position nextPosition = Position.getNextPositionFromChar(head.getLocation(), action);

    if (!Position.isPositionInBorders(game, nextPosition)) {
      throw new IllegalArgumentException("Next position is outside the borders.");
    }

    if (isNextPositionFood(nextPosition)) {

      Position lastPosition = moveAndReturnLastPosition(head, bodies, nextPosition);

      SnakeBody body = new SnakeBody();
      body.setLocation(lastPosition);
      body.setGame(game);
      bodies.add(body);

      repository.save(head);
      repository.save(bodies);
    }
  }

  private boolean isNextPositionFood(Position position) {
    // TODO: validate if it food
    return true;
  }

  private static Position moveAndReturnLastPosition(SnakeHead head, List<SnakeBody> bodies,
      Position position) {

    Position temp = head.getLocation();
    head.setLocation(position);
    position = temp;

    for (SnakeBody body : bodies) {

      temp = body.getLocation();
      body.setLocation(position);
      position = temp;
    }

    return position;
  }
}
