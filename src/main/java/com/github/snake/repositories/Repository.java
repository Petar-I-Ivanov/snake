package com.github.snake.repositories;

import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class Repository {

  private EntityManager entityManager;

  public Repository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public <T extends GameboardObject> T findByGameIdAndPosition(Long gameId, Position position,
      Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName()
        + " obj WHERE obj.game.id = ?1 AND obj.rowLocation = ?2 AND obj.colLocation = ?3";

    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
    query.setParameter(1, gameId).setParameter(2, position.getRow()).setParameter(3,
        position.getCol());

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (NonUniqueResultException e) {
      throw new IllegalArgumentException("Returned entity should be single or none.");
    }
  }

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

  public <T extends GameboardObject> List<T> findListByGameId(Long gameId, Class<T> entityClass) {

    String jpql = "SELECT obj FROM " + entityClass.getName() + " obj WHERE obj.game.id = ?1";
    TypedQuery<T> query = entityManager.createQuery(jpql, entityClass).setParameter(1, gameId);
    return query.getResultList();
  }

  public <T extends GameboardObject> void save(T entity) {

    if (entity.getId() != null) {
      entityManager.merge(entity);
      return;
    }

    entityManager.persist(entity);
  }

  public <T extends GameboardObject> void save(List<T> entities) {

    for (T entity : entities) {
      save(entity);
    }
  }

  public <T extends GameboardObject> void delete(T entity) {
    entityManager.remove(entityManager.find(entity.getClass(), entity.getId()));
  }
}
