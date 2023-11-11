package com.ays.institution.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionToInstitutionResponseMapper extends BaseMapper<Institution, InstitutionResponse> {

    static InstitutionToInstitutionResponseMapper initialize() {
        return Mappers.getMapper(InstitutionToInstitutionResponseMapper.class);
    }
}
