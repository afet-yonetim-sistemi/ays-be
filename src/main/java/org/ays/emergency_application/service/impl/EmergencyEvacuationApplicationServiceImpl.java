package org.ays.emergency_application.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.ays.auth.model.AysIdentity;
import org.ays.common.model.AysPage;
import org.ays.emergency_application.exception.EmergencyEvacuationApplicationNotExistException;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.filter.EmergencyEvacuationApplicationFilter;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationRequestToDomainMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Emergency evacuation application service to perform emergency evacuation related operations
 */
@Service
@RequiredArgsConstructor
class EmergencyEvacuationApplicationServiceImpl implements EmergencyEvacuationApplicationService {

    private final EmergencyEvacuationApplicationReadPort emergencyEvacuationApplicationReadPort;
    private final EmergencyEvacuationApplicationSavePort emergencyEvacuationApplicationSavePort;
    private final AysIdentity identity;

    private final EmergencyEvacuationApplicationRequestToDomainMapper applicationRequestToDomainMapper = EmergencyEvacuationApplicationRequestToDomainMapper.initialize();


    /**
     * Retrieves a page of emergency evacuation applications based on the provided request parameters.
     *
     * @param listRequest The request parameters for retrieving the emergency evacuation applications. This includes pagination and filtering parameters.
     * @return A page of emergency evacuation applications. Each application includes details such as the ID, status, and other related information.
     */
    @Override
    public AysPage<EmergencyEvacuationApplication> findAll(final EmergencyEvacuationApplicationListRequest listRequest) {

        Optional.ofNullable(listRequest.getFilter())
                .ifPresentOrElse(filter -> filter.setInstitutionId(identity.getInstitutionId()),
                        () -> {
                            EmergencyEvacuationApplicationFilter filter = EmergencyEvacuationApplicationFilter.builder()
                                    .institutionId(identity.getInstitutionId())
                                    .build();

                            listRequest.setFilter(filter);
                        });

        return emergencyEvacuationApplicationReadPort
                .findAll(listRequest.getPageable(), listRequest.getFilter());
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
                .filter(application -> application.hasNotInstitution() || application.isInstitutionOwner(identity.getInstitutionId()))
                .orElseThrow(() -> new EmergencyEvacuationApplicationNotExistException(id));
    }


    /**
     * Create an emergency evacuation application to the database
     *
     * @param emergencyEvacuationApplicationRequest The emergency evacuation request containing application information
     */
    @Override
    public void create(final EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest) {

        EmergencyEvacuationApplication application = applicationRequestToDomainMapper
                .map(emergencyEvacuationApplicationRequest);

        application.pending();

        emergencyEvacuationApplicationSavePort.save(application);
    }


    /**
     * Updates an existing Emergency Evacuation Application with the provided details
     *
     * @param id            the unique identifier of the Emergency Evacuation Application to be updated
     * @param updateRequest the request object containing the details to update the Emergency Evacuation Application
     * @throws EmergencyEvacuationApplicationNotExistException if the application with the specified ID does not exist
     */
    @Override
    @Transactional
    public void update(final String id,
                       final EmergencyEvacuationApplicationUpdateRequest updateRequest) {

        final EmergencyEvacuationApplication emergencyEvacuationApplication = emergencyEvacuationApplicationReadPort
                .findById(id)
                .filter(application -> application.hasNotInstitution() || application.isInstitutionOwner(identity.getInstitutionId()))
                .orElseThrow(() -> new EmergencyEvacuationApplicationNotExistException(id));

        if (emergencyEvacuationApplication.hasNotInstitution()) {
            emergencyEvacuationApplication.setInstitutionId(identity.getInstitutionId());
        }

        emergencyEvacuationApplication.setSeatingCount(updateRequest.getSeatingCount());
        emergencyEvacuationApplication.setHasObstaclePersonExist(updateRequest.getHasObstaclePersonExist());
        emergencyEvacuationApplication.setStatus(updateRequest.getStatus());

        Optional.ofNullable(updateRequest.getNotes())
                .filter(StringUtils::isNotBlank)
                .ifPresent(emergencyEvacuationApplication::setNotes);

        emergencyEvacuationApplicationSavePort.save(emergencyEvacuationApplication);
    }

}
