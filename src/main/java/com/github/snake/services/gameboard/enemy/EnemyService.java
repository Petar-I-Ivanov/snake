package com.github.snake.services.gameboard.enemy;

import com.github.snake.models.Game;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnemyService {

	private PoacherService poacherService;
	private TrapService trapService;
	
	public EnemyService(PoacherService poacherService, TrapService trapService) {
		this.poacherService = poacherService;
		this.trapService = trapService;
	}
	
	public void generatePoacher(Game game) {
		poacherService.generatePoacher(game);
	}
	
	public void turnActionAndChecks(Game game) {
		poacherService.movePoachers(game);
		trapService.trapsRemovalCheck(game);
	}
}
