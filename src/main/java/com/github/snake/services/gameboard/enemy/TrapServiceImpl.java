package com.github.snake.services.gameboard.enemy;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.enemy.Trap;
import com.github.snake.repositories.Repository;
import com.github.snake.services.interfaces.TrapService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrapServiceImpl implements TrapService {

  private static final byte TURNS_BOMB_TO_STAY = 2;

  private Repository repository;

  public TrapServiceImpl(Repository repository) {
    this.repository = repository;
  }

  @Override
  public void generateTrap(Game game, Position position) {

    Trap trap = new Trap();
    trap.setLocation(position);
    trap.setTurnPlaced(game.getTurn());
    trap.setGame(game);

    repository.save(trap);

    // game.getEnemies().add(trap);
  }

  @Override
  public void trapsRemovalCheck(Game game) {

    for (Trap trap : repository.findListByGameId(game.getId(), Trap.class)) {

      if (game.getTurn() - trap.getTurnPlaced() == TURNS_BOMB_TO_STAY) {
        repository.delete(trap);
      }
    }
  }
}
