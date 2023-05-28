package com.github.snake.repositories;

import com.github.snake.models.gameboard.Barrier;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.models.gameboard.enemy.Enemy;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.special.SpecialFood;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Custom, partly generic Repository implementing the common methods for gameboard entities
 */
@ApplicationScoped
public class Repository {

  /**
   * This list keeps every @Entity's class that extends GameboardObject (is part from the
   * gameboard). If new entity is added or removed should be fixed manually. Used to loop gameboard
   * entities for the common methods (like when generating the gameboard we need all the entities
   * from the same gameId or when checking if specific location is already taken)
   */
  // TODO: check if it is possible to scan the project before launch to get it automatically
  private static final List<Class<? extends GameboardObject>> ENTITY_CLASSES =
      Arrays.asList(SnakeHead.class, SnakeBody.class, Exit.class, Barrier.class, NormalFood.class,
          SpecialFood.class, Enemy.class);

  private EntityManager entityManager;

  public Repository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Loops the gameboard entities to find if any of them meets the requirements:
   * 
   * @param gameId - the game ID which we're checking the position
   * @param position - the specific position we're looking for
   * @return the first result (there should be only one but this check is made inside the
   *         <code>findByGameIdAndPosition</code> method) that is not null(meet the requirements),
   *         if none is found - then null
   */
  public GameboardObject findAnyByGameIdAndPosition(Long gameId, Position position) {

    return ENTITY_CLASSES.stream()
        .map(classType -> findByGameIdAndPosition(gameId, position, classType))
        .filter(Objects::nonNull).findFirst().orElse(null);
  }

  /**
   * Loops the gameboard entities to find if any of them meets the requirements:
   * 
   * @param gameId - the game ID which we're looking for connected gameboard entities
   * @return list of gameboard entities (no specific checks are made since if none are found
   *         returned list will be empty)
   */
  public List<GameboardObject> findAllByGameId(Long gameId) {

    return ENTITY_CLASSES.stream()
        .flatMap(classType -> findListByGameId(gameId, classType).stream())
        .collect(Collectors.toList());
  }

  /**
   * @param <T> - represents the expected .class for the entity to be returned (entityClass and <T>
   *        must be the same)
   * @param gameId - the game ID which we're looking for connected gameboard entities
   * @param position - the position to search for
   * @param entityClass - the .class type of entity to search for
   * @return the entity object if there's found one, or null if no match is found
   * @throws IllegalArgumentException if there're more than one result
   */
  public <T extends GameboardObject> T findByGameIdAndPosition(Long gameId, Position position,
      Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1 "
        + "AND obj.rowLocation = ?2 AND obj.colLocation = ?3";

    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);

    query.setParameter(1, gameId).setParameter(2, position.getRow()).setParameter(3,
        position.getCol());

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (NonUniqueResultException e) {
      throw new IllegalArgumentException(
          "Returned entity of type " + entityClass.getName() + " should be single or none.");
    }
  }

  /**
   * @param <T> - represents the expected .class for the entity to be returned (entityClass and <T>
   *        must be the same)
   * @param gameId - the game ID which we're looking for connected gameboard entities
   * @param entityClass - the .class type of entity to search for
   * @return the entity object if there's found one, or null if no match is found
   * @throws IllegalArgumentException if there're more than one result
   */
  public <T extends GameboardObject> T findSingleByGameId(Long gameId, Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1";
    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass).setParameter(1, gameId);

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (NonUniqueResultException e) {
      throw new IllegalArgumentException("Returned entity should be single or none.");
    }
  }

  /**
   * @param <T> - represents the expected .class for the entity to be returned (entityClass and <T>
   *        must be the same)
   * @param gameId - the game ID which we're looking for connected gameboard entities
   * @param entityClass - the .class type of entity to search for
   * @return list of entity objects that matches the requirements or empty list if none are found
   */
  public <T extends GameboardObject> List<T> findListByGameId(Long gameId, Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1";
    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass).setParameter(1, gameId);
    return query.getResultList();
  }

  /**
   * @param <T> - the expected .class for the entity we're working with
   * @param entity - the entity that should be saved if the entity ID is null it is persisted
   *        (created new record with generated ID else if the entity ID is not null it merges it
   *        with the already existing record
   */
  @Transactional
  public <T extends GameboardObject> void save(T entity) {

    if (entity.getId() != null) {
      entityManager.merge(entity);
      return;
    }

    entityManager.persist(entity);
  }

  /**
   * @param <T> - the expected .class for the entity we're working with
   * @param entities - the entity list that should be saved
   */
  public <T extends GameboardObject> void save(List<T> entities) {

    for (T entity : entities) {
      save(entity);
    }
  }

  /**
   * @param <T> - the expected .class for the entity we're working with
   * @param entity - the entity that should be deleted
   */
  @Transactional
  public <T extends GameboardObject> void delete(T entity) {
    entityManager.remove(entityManager.find(entity.getClass(), entity.getId()));
  }

  /**
   * @param gameId - the game ID where all related gameboard entities should be deleted
   */
  public void deleteAll(Long gameId) {
    findAllByGameId(gameId).stream().forEach(this::delete);
  }
}
