package com.ays.common.model.mapper;

import java.util.Collection;
import java.util.List;

/**
 * A generic interface for mapping objects of type {@code S} to objects of type {@code T}.
 * This interface provides two methods: one for mapping a single object and another for mapping a collection of objects.
 * <p>
 * Implementations of this interface should provide an implementation for the {@link #map(Object)} method that takes an
 * object of type {@code S} and returns an object of type {@code T}. The {@link #map(Collection)} method, which takes
 * a collection of objects of type {@code S}, should be implemented by calling the {@code map} method on each element
 * of the collection and returning a list of the resulting objects of type {@code T}.
 * <p>
 * The purpose of this interface is to provide a generic way of mapping objects from one type to another, allowing for
 * reuse of code and reduction of duplication.
 *
 * @param <S> the type of the source object to be mapped
 * @param <T> the type of the target object to be mapped to
 */
public interface BaseMapper<S, T> {

    /**
     * Maps the specified source object to an object of type {@code T}.
     *
     * @param source the source object to be mapped
     * @return the resulting object of type {@code T}
     */
    T map(S source);

    /**
     * Maps the specified collection of source objects to a list of objects of type {@code T}.
     *
     * @param sources the collection of source objects to be mapped
     * @return the list of resulting objects of type {@code T}
     */
    List<T> map(Collection<S> sources);

}
