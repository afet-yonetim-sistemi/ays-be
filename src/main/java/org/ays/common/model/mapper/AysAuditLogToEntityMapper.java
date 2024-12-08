package org.ays.common.model.mapper;

import org.ays.common.model.AysAuditLog;
import org.ays.common.model.entity.AysAuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * {@link AysAuditLogToEntityMapper} is an interface that defines the mapping between an {@link AysAuditLog} and an {@link AysAuditLogEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysAuditLogToEntityMapper extends BaseMapper<AysAuditLog, AysAuditLogEntity> {

    @Override
    @Mapping(target = "requestIpAddress", source = "request.ipAddress")
    @Mapping(target = "requestReferer", source = "request.referer")
    @Mapping(target = "requestPath", source = "request.path")
    @Mapping(target = "requestHttpMethod", source = "request.httpMethod")
    @Mapping(target = "requestHttpHeader", source = "request.httpHeader")
    @Mapping(target = "requestBody", source = "request.body")
    @Mapping(target = "responseHttpStatusCode", source = "response.httpStatusCode")
    @Mapping(target = "responseBody", source = "response.body")
    AysAuditLogEntity map(AysAuditLog source);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysAuditLogToEntityMapper initialize() {
        return Mappers.getMapper(AysAuditLogToEntityMapper.class);
    }

}
