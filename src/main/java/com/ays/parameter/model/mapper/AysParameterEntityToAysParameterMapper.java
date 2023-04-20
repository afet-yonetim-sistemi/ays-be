package com.ays.parameter.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.parameter.model.AysParameter;
import com.ays.parameter.model.entity.AysParameterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AysParameterEntityToAysParameterMapper extends BaseMapper<AysParameterEntity, AysParameter> {

    static AysParameterEntityToAysParameterMapper initialize() {
        return Mappers.getMapper(AysParameterEntityToAysParameterMapper.class);
    }

}
