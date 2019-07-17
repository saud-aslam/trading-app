package ca.jrvs.apps.trading.dao;

public interface CrudRepository<E, ID> {
    E save(E entity);

    E findById(ID id);

    boolean existsById(ID id);
    //a

    void deleteByID(ID id);
}
