package org.ays.common.model.mapper;

import java.util.Collection;
import java.util.List;

/**
 * Interface for mapping between source and target types.
 * <p>
 * The {@code BaseMapper} interface provides a contract for converting objects of one type into another,
 * as well as converting collections of such objects. This interface is useful for implementing data
 * transformation layers between different representations, such as mapping between two objects.
 * </p>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * public class UserToEntityMapper implements BaseMapper<User, UserEntity> {
 *
 *     static AdminRegistrationApplicationCompleteRequestToUserMapper initialize() {
 *         return Mappers.getMapper(UserToEntityMapper.class);
 *     }
 *
 * }
 * }</pre>
 *
 * <p>
 * Implementations of this interface should provide specific mapping logic for transforming a source type
 * into a target type. The default method for mapping collections leverages the single object mapping method.
 * </p>
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public interface BaseMapper<S, T> {

    /**
     * Maps a single source object to a target object.
     * <p>
     * This method defines the logic for converting an instance of the source type {@code S}
     * into an instance of the target type {@code T}. Implementations should handle the specific
     * transformation required between the source and target.
     * </p>
     *
     * @param source the source object to map
     * @return the mapped target object
     */
    T map(S source);

    /**
     * Maps a collection of source objects to a list of target objects.
     * <p>
     * This method provides a default implementation for converting a collection of source objects
     * into a list of target objects using the single object mapping method {@link #map(Object)}.
     * Implementations may override this method if a different collection mapping logic is needed.
     * </p>
     *
     * @param sources the collection of source objects to map
     * @return the list of mapped target objects
     */
    List<T> map(Collection<S> sources);

}
