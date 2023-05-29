package com.github.snake.services.gameboard.enemy;

import com.github.snake.models.Game;
import com.github.snake.services.interfaces.EnemyService;
import com.github.snake.services.interfaces.PoacherService;
import com.github.snake.services.interfaces.TrapService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnemyServiceImpl implements EnemyService {

  private PoacherService poacherService;
  private TrapService trapService;

  public EnemyServiceImpl(PoacherService poacherService, TrapService trapService) {
    this.poacherService = poacherService;
    this.trapService = trapService;
  }

  @Override
  public void generatePoacher(Game game) {
    poacherService.generatePoacher(game);
  }

  @Override
  public void turnActionAndChecks(Game game) {
    poacherService.movePoachers(game);
    trapService.trapsRemovalCheck(game);
  }
}
