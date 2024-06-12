package org.ays.auth.model.mapper;

import org.ays.auth.model.AysToken;
import org.ays.auth.model.dto.response.AysTokenResponse;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link AysTokenToAysTokenResponseMapper} is an interface that defines the mapping between a {@link AysToken} and an {@link AysTokenResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysTokenToAysTokenResponseMapper extends BaseMapper<AysToken, AysTokenResponse> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysTokenToAysTokenResponseMapper initialize() {
        return Mappers.getMapper(AysTokenToAysTokenResponseMapper.class);
    }

}
