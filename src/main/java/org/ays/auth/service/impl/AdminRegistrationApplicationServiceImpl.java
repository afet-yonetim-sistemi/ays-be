package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.exception.AysAdminRegistrationApplicationAlreadyApprovedException;
import org.ays.auth.exception.AysAdminRegistrationApplicationAlreadyRejectedException;
import org.ays.auth.exception.AysAdminRegistrationApplicationInCompleteException;
import org.ays.auth.exception.AysAdminRegistrationApplicationNotExistByIdAuthException;
import org.ays.auth.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationFilter;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.mapper.AdminRegistrationApplicationCreateRequestToDomainMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.port.AdminRegistrationApplicationReadPort;
import org.ays.auth.port.AdminRegistrationApplicationSavePort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AdminRegistrationApplicationService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageable;
import org.ays.institution.exception.AysInstitutionNotExistException;
import org.ays.institution.model.Institution;
import org.ays.institution.port.InstitutionReadPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements the {@link AdminRegistrationApplicationService} interface and provides verification operations for admin users.
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 */
@Service
@RequiredArgsConstructor
class AdminRegistrationApplicationServiceImpl implements AdminRegistrationApplicationService {

    private final AdminRegistrationApplicationReadPort adminRegistrationApplicationReadPort;
    private final AdminRegistrationApplicationSavePort adminRegistrationApplicationSavePort;

    private final AysUserSavePort userSavePort;
    private final InstitutionReadPort institutionReadPort;


    private final AdminRegistrationApplicationCreateRequestToDomainMapper adminRegistrationApplicationCreateRequestToEntityMapper = AdminRegistrationApplicationCreateRequestToDomainMapper.initialize();


    /**
     * Retrieves a paginated list of admin registration applications based on the specified filters
     *
     * @param listRequest covering page, pageSize and filters
     * @return a paginated list of admin registration applications
     */
    @Override
    public AysPage<AdminRegistrationApplication> findAll(final AdminRegistrationApplicationListRequest listRequest) {

        final AysPageable aysPageable = listRequest.getPageable();
        final AdminRegistrationApplicationFilter filter = listRequest.getFilter();

        return adminRegistrationApplicationReadPort.findAll(aysPageable, filter);
    }

    /**
     * Retrieves an admin register application by id.
     *
     * @param id The id of the register application.
     * @return An admin register application.
     */
    @Override
    public AdminRegistrationApplication findById(String id) {
        return adminRegistrationApplicationReadPort.findById(id)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdException(id));
    }

    /**
     * Retrieves an admin register application by id and checks if it is waiting.
     * If it is not waiting, throws an exception.
     *
     * @param id registration application id
     * @return An admin register application.
     */
    @Override
    public AdminRegistrationApplication findSummaryById(String id) {

        return adminRegistrationApplicationReadPort.findById(id)
                .filter(AdminRegistrationApplication::isWaiting)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdAuthException(id));
    }

    /**
     * Creates a new admin register application.
     *
     * @param request The request object containing the register application details.
     * @return A response object containing the created register application.
     */
    @Override
    @Transactional
    public AdminRegistrationApplication create(AdminRegistrationApplicationCreateRequest request) {

        final Set<String> institutionIds = new LinkedHashSet<>();
        if (request.getInstitutionIds() != null) {
            institutionIds.addAll(request.getInstitutionIds());
        }
        if (request.getInstitutionId() != null && !request.getInstitutionId().isBlank()) {
            institutionIds.add(request.getInstitutionId());
        }

        for (String institutionId : institutionIds) {
            if (!institutionReadPort.existsByIdAndIsStatusActive(institutionId)) {
                throw new AysInstitutionNotExistException(institutionId);
            }
        }

        final AdminRegistrationApplication registrationApplication = adminRegistrationApplicationCreateRequestToEntityMapper
                .map(request);
        final List<Institution> institutions = institutionIds.stream()
                .map(this::institutionReference)
                .toList();
        registrationApplication.setInstitution(institutions.get(0));
        registrationApplication.setInstitutions(institutions);

        registrationApplication.waiting();

        return adminRegistrationApplicationSavePort.save(registrationApplication);
    }

    private Institution institutionReference(final String institutionId) {
        return Institution.builder()
                .id(institutionId)
                .build();
    }

    /**
     * Approves a new admin register application.
     *
     * @param id The id of the register application.
     */
    @Override
    @Transactional
    public void approve(String id) {
        final AdminRegistrationApplication registrationApplication = adminRegistrationApplicationReadPort
                .findById(id)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdException(id));

        this.checkApplicationStatus(registrationApplication);

        registrationApplication.approve();
        adminRegistrationApplicationSavePort.save(registrationApplication);

        final AysUser user = registrationApplication.getUser();
        user.activate();
        userSavePort.save(user);
    }

    /**
     * Rejects an admin register application by id.
     *
     * @param id            The id of the register application.
     * @param rejectRequest The request object containing the rejection details.
     */
    @Override
    @Transactional
    public void reject(final String id, final AdminRegistrationApplicationRejectRequest rejectRequest) {
        final AdminRegistrationApplication registrationApplication = adminRegistrationApplicationReadPort
                .findById(id)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdException(id));

        this.checkApplicationStatus(registrationApplication);

        registrationApplication.reject(rejectRequest.getRejectReason());
        adminRegistrationApplicationSavePort.save(registrationApplication);

        final AysUser user = registrationApplication.getUser();
        user.passivate();
        userSavePort.save(user);
    }

    /**
     * Checks the status of the provided {@link AdminRegistrationApplication} object and throws
     * corresponding exceptions if the application status is waiting, approved, or rejected.
     *
     * @param registrationApplication The admin registration application to check.
     * @throws AysAdminRegistrationApplicationInCompleteException      If the application status is waiting.
     * @throws AysAdminRegistrationApplicationAlreadyApprovedException If the application status is approved.
     * @throws AysAdminRegistrationApplicationAlreadyRejectedException If the application status is rejected.
     */
    private void checkApplicationStatus(final AdminRegistrationApplication registrationApplication) {

        if (registrationApplication.isWaiting()) {
            throw new AysAdminRegistrationApplicationInCompleteException(
                    registrationApplication.getId(),
                    registrationApplication.getStatus()
            );
        }

        if (registrationApplication.isApproved()) {
            throw new AysAdminRegistrationApplicationAlreadyApprovedException(
                    registrationApplication.getId(),
                    registrationApplication.getStatus()
            );
        }

        if (registrationApplication.isRejected()) {
            throw new AysAdminRegistrationApplicationAlreadyRejectedException(
                    registrationApplication.getId()
            );
        }
    }

}
