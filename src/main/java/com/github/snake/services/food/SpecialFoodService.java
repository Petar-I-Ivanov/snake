package com.github.snake.services.food;

import com.github.snake.repositories.Repository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SpecialFoodService {

  private Repository repository;

  public SpecialFoodService(Repository repository) {
    this.repository = repository;
  }


}
