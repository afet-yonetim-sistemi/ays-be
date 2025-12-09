package org.ays.institution.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.institution.model.Institution;
import org.ays.institution.model.response.InstitutionsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link InstitutionToInstitutionsResponseMapper} is an interface that defines the mapping between an {@link Institution} and an {@link InstitutionsResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface InstitutionToInstitutionsResponseMapper extends BaseMapper<Institution, InstitutionsResponse> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static InstitutionToInstitutionsResponseMapper initialize() {
        return Mappers.getMapper(InstitutionToInstitutionsResponseMapper.class);
    }
}
