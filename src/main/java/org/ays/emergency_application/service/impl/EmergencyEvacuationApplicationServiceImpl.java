package org.ays.emergency_application.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToDomainMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.emergency_application.util.exception.EmergencyEvacuationApplicationNotExistException;
import org.ays.institution.model.Institution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class implements the interface {@link EmergencyEvacuationApplicationService}
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 * The {@code @Transactional} annotation ensures that all the methods in this class are executed within a transactional context.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class EmergencyEvacuationApplicationServiceImpl implements EmergencyEvacuationApplicationService {

    private final EmergencyEvacuationApplicationReadPort emergencyEvacuationApplicationReadPort;
    private final EmergencyEvacuationApplicationSavePort emergencyEvacuationApplicationSavePort;
    private final AysIdentity identity;

    private final EmergencyEvacuationApplicationRequestToDomainMapper emergencyEvacuationApplicationRequestToDomainMapper =
            EmergencyEvacuationApplicationRequestToDomainMapper.initialize();


    /**
     * Retrieves a page of emergency evacuation applications based on the provided request parameters.
     *
     * @param listRequest The request parameters for retrieving the emergency evacuation applications. This includes pagination and filtering parameters.
     * @return A page of emergency evacuation applications. Each application includes details such as the ID, status, and other related information.
     */
    @Override
    public AysPage<EmergencyEvacuationApplication> findAll(final EmergencyEvacuationApplicationListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();
        final EmergencyEvacuationApplicationFilter filter = listRequest.getFilter();

        return emergencyEvacuationApplicationReadPort.findAll(aysPageable, filter);
    }


    /**
     * Retrieves an emergency evacuation application by its ID.
     *
     * @param id The ID of the emergency evacuation application.
     * @return the emergency evacuation application corresponding to the given ID.
     */
    @Override
    public EmergencyEvacuationApplication findById(final String id) {
        return emergencyEvacuationApplicationReadPort.findById(id)
                .orElseThrow(() -> new EmergencyEvacuationApplicationNotExistException(id));
    }


    /**
     * Create an emergency evacuation application to the database
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    @Override
    @Transactional
    public void create(final EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {

        EmergencyEvacuationApplication application = emergencyEvacuationApplicationRequestToDomainMapper
                .map(emergencyEvacuationApplicationRequest);

        application.pending();

        emergencyEvacuationApplicationSavePort.save(application);
    }

    // TODO : Javadoc
    // TODO : Write tests
    @Override
    @Transactional
    public void update(final String id,
                       final EmergencyEvacuationApplicationUpdateRequest updateRequest) {

        final EmergencyEvacuationApplication emergencyEvacuationApplication = emergencyEvacuationApplicationReadPort
                .findById(id)
                .orElseThrow(() -> new EmergencyEvacuationApplicationNotExistException(id));

        emergencyEvacuationApplication.setInstitution(
                Institution.builder()
                        .id(identity.getInstitutionId())
                        .build()
        );
        emergencyEvacuationApplication.setSeatingCount(updateRequest.getSeatingCount());
        emergencyEvacuationApplication.setHasObstaclePersonExist(updateRequest.getHasObstaclePersonExist());
        emergencyEvacuationApplication.setStatus(updateRequest.getStatus());

        Optional.ofNullable(updateRequest.getNotes())
                .filter(StringUtils::isNotBlank)
                .ifPresent(emergencyEvacuationApplication::setNotes);

        emergencyEvacuationApplicationSavePort.save(emergencyEvacuationApplication);
    }

}
