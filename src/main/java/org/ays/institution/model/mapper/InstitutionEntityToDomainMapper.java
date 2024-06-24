package org.ays.institution.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link InstitutionEntityToDomainMapper} is an interface that defines the mapping between an {@link InstitutionEntity} and an {@link Institution}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface InstitutionEntityToDomainMapper extends BaseMapper<InstitutionEntity, Institution> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static InstitutionEntityToDomainMapper initialize() {
        return Mappers.getMapper(InstitutionEntityToDomainMapper.class);
    }

}
