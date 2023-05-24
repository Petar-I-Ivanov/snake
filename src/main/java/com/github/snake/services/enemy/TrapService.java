package com.github.snake.services.enemy;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.enemy.Trap;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrapService {

  private static final byte TURNS_BOMB_TO_STAY = 2;

  private Repository repository;

  public TrapService(Repository repository) {
    this.repository = repository;
  }

  public void generateTrap(Game game, Position position) {

    Trap trap = new Trap();
    trap.setLocation(position);
    trap.setTurnPlaced(game.getTurn());
    trap.setGame(game);

    repository.save(trap);

    // game.getEnemies().add(trap);
  }

  public void trapsRemovalCheck(Game game) {

    for (Trap trap : repository.findListByGameId(game.getId(), Trap.class)) {

      if (game.getTurn() - trap.getTurnPlaced() == TURNS_BOMB_TO_STAY) {
        repository.delete(trap);
      }
    }
  }
}
