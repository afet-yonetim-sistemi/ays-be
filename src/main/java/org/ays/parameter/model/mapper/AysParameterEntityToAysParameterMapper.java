package org.ays.parameter.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.model.entity.AysParameterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AysParameterEntityToAysParameterMapper is an interface that defines the mapping between an {@link AysParameterEntity} and an {@link AysParameter}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysParameterEntityToAysParameterMapper extends BaseMapper<AysParameterEntity, AysParameter> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysParameterEntityToAysParameterMapper initialize() {
        return Mappers.getMapper(AysParameterEntityToAysParameterMapper.class);
    }

}
