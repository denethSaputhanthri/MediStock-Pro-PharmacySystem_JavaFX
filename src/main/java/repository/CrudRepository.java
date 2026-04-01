package repository;

import java.util.List;

public interface CrudRepository<T, ID>extends SuperRepository{
    Boolean create(T t);
    Boolean update(T t);
    Boolean deleteById(ID id);
    T searchById(ID id);
    List<T>getAll();
}
