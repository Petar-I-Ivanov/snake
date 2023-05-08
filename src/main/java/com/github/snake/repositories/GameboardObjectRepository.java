package com.github.snake.repositories;

import com.github.snake.models.gameboard.Barrier;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.models.gameboard.foods.PoisonousFood;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.special.BorderFood;
import com.github.snake.models.gameboard.foods.special.GrowthFood;
import com.github.snake.models.gameboard.foods.special.ImmunityFood;
import com.github.snake.models.gameboard.poacher.Poacher;
import com.github.snake.models.gameboard.poacher.Trap;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameboardObjectRepository {

  private static final List<Class<? extends GameboardObject>> ENTITY_CLASSES =
      Arrays.asList(SnakeHead.class, SnakeBody.class, Exit.class, Barrier.class, NormalFood.class,
          BorderFood.class, GrowthFood.class, ImmunityFood.class, PoisonousFood.class,
          Poacher.class, Trap.class);

  private EntityManager entityManager;

  public GameboardObjectRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public GameboardObject findAnyByGameIdAndPosition(Long gameId, Position position) {

    return ENTITY_CLASSES.stream()
        .map(classType -> findByGameIdAndPosition(gameId, position, classType))
        .filter(Objects::nonNull).findFirst().orElse(null);
  }

  public List<GameboardObject> findAllByGameId(Long gameId) {

    return ENTITY_CLASSES.stream().flatMap(classType -> findByGameId(gameId, classType).stream())
        .collect(Collectors.toList());
  }

  private <T extends GameboardObject> T findByGameIdAndPosition(Long gameId, Position position,
      Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = :gameId "
        + "AND obj.rowLocation = :rowLocation AND obj.colLocation = :colLocation";

    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass)
        .setParameter("gameId", gameId).setParameter("rowLocation", position.getRow())
        .setParameter("colLocation", position.getCol());

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (NonUniqueResultException e) {
      throw new IllegalArgumentException("Returned entity should be single or none.");
    }
  }

  private <T extends GameboardObject> List<T> findByGameId(Long gameId, Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = :gameId ";

    TypedQuery<T> query =
        entityManager.createQuery(jpql, entityClass).setParameter("gameId", gameId);

    return query.getResultList();
  }
}
