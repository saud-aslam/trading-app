package ca.jrvs.apps.trading.dao;

public interface CrudRepository<E, ID> {
    E save(E entity);

    E findById(ID id);

    boolean existsById(ID id);

    boolean deleteById(ID id);
}
