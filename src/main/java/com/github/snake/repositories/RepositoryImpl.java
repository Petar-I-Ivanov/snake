package com.github.snake.repositories;

import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Inheritance;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Custom, partly generic Repository implementing the common methods for gameboard entities
 */
@ApplicationScoped
public class RepositoryImpl implements Repository {

  /**
   * This list keeps every @Entity's class that extends GameboardObject (is part from the
   * gameboard).Used to loop gameboard entities for the common methods (like when generating the
   * gameboard we need all the entities from the same gameId or when checking if specific location
   * is already taken)
   */
  private List<Class<? extends GameboardObject>> entityClasses;
  private EntityManager entityManager;

  public RepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.entityClasses = extractEntityClasses();
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
  @Override
  public GameboardObject findAnyByGameIdAndPosition(Long gameId, Position position) {

    return entityClasses.stream()
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
  @Override
  public List<GameboardObject> findAllByGameId(Long gameId) {

    return entityClasses.stream().flatMap(classType -> findListByGameId(gameId, classType).stream())
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
  @Override
  public <T extends GameboardObject> T findByGameIdAndPosition(Long gameId, Position position,
      Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1 "
        + "AND obj.rowLocation = ?2 AND obj.colLocation = ?3";

    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);

    query.setParameter(1, gameId).setParameter(2, position.getRow()).setParameter(3,
        position.getCol());

    try {
      return query.getSingleResult();
    }

    catch (NoResultException e) {
      return null;
    }

    catch (NonUniqueResultException e) {
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
  @Override
  public <T extends GameboardObject> T findSingleByGameId(Long gameId, Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1";
    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass).setParameter(1, gameId);

    try {
      return query.getSingleResult();
    }

    catch (NoResultException e) {
      return null;
    }

    catch (NonUniqueResultException e) {
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
  @Override
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
  @Override
  public <T extends GameboardObject> void save(T entity) {

    if (entity.getId() != null) {
      entityManager.merge(entity);
      return;
    }

    entityManager.persist(entity);
  }

  /**
   * @param <T> - the expected .class for the entity we're working with
   * @param entity - the entity that should be deleted
   */
  @Override
  public <T extends GameboardObject> void delete(T entity) {
    entityManager.remove(entityManager.find(entity.getClass(), entity.getId()));
  }

  /**
   * checks entityManager's metamodel entities to find the ones part from the gameboard and find
   * their Class
   * 
   * @return Entity classes part from the Gameboard
   */
  private List<Class<? extends GameboardObject>> extractEntityClasses() {

    return entityManager.getMetamodel().getEntities().stream().map(EntityType::getJavaType)
        .filter(this::isGameboardObject)
        .map(entityClass -> (Class<? extends GameboardObject>) entityClass)
        .collect(Collectors.toList());
  }

  /**
   * @param entityClass represents the entity class
   * @return true if the class is annotated with entity, not annotated with Inheritance (because
   *         like NormalFood matches the requirements but is only used to map the
   *         subclasses @OneToOne with Game and store them in single table) and is subclass of
   *         GameboardObject
   * @returns false otherwise
   */
  private boolean isGameboardObject(Class<?> entityClass) {

    return entityClass.isAnnotationPresent(Entity.class)
        && !entityClass.isAnnotationPresent(Inheritance.class)
        && GameboardObject.class.isAssignableFrom(entityClass);
  }
}
