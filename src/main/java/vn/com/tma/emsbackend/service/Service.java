package vn.com.tma.emsbackend.service;

import java.util.Collection;
import java.util.List;

public interface Service<T> {
    List<T> getAll();

    T get(long id);

    T add(T request);

    T update(long id, T request);

    void delete(long id);
}
