package org.ays.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.common.model.AysPage;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
import org.ays.user.model.AdminRegistrationApplication;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequest;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.user.model.mapper.AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper;
import org.ays.user.repository.AdminRegistrationApplicationRepository;
import org.ays.user.repository.UserRepositoryV2;
import org.ays.user.service.AdminRegistrationApplicationService;
import org.ays.user.util.exception.AysAdminRegistrationApplicationNotExistByIdException;
import org.ays.user.util.exception.AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException;
import org.ays.user.util.exception.AysAdminRegistrationApplicationSummaryNotExistByIdException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class implements the {@link AdminRegistrationApplicationService} interface and provides verification operations for admin users.
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 */
@Service
@RequiredArgsConstructor
public class AdminRegistrationApplicationServiceImpl implements AdminRegistrationApplicationService {

    private final AdminRegistrationApplicationRepository adminRegistrationApplicationRepository;
    private final UserRepositoryV2 userRepository;
    private final InstitutionRepository institutionRepository;

    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();
    private final AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper adminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper = AdminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper.initialize();


    /**
     * Retrieves a paginated list of admin registration applications based on the specified filters
     *
     * @param listRequest covering page, pageSize and filters
     * @return a paginated list of admin registration applications
     */
    @Override
    public AysPage<AdminRegistrationApplication> findAll(AdminRegistrationApplicationListRequest listRequest) {
        final Specification<AdminRegistrationApplicationEntity> filters = listRequest
                .toSpecification(AdminRegistrationApplicationEntity.class);

        final Page<AdminRegistrationApplicationEntity> registerApplicationEntities = adminRegistrationApplicationRepository
                .findAll(filters, listRequest.toPageable());

        final List<AdminRegistrationApplication> registerApplications = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(registerApplicationEntities.getContent());

        return AysPage.of(
                listRequest.getFilter(),
                registerApplicationEntities,
                registerApplications
        );
    }

    /**
     * Retrieves an admin register application by id.
     *
     * @param id The id of the register application.
     * @return An admin register application.
     */
    @Override
    public AdminRegistrationApplication findById(String id) {
        final AdminRegistrationApplicationEntity registerApplicationEntity = adminRegistrationApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdException(id));

        return adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Retrieves an admin register application by id and checks if it is waiting.
     * If it is not waiting, throws an exception.
     *
     * @param id registration application id
     * @return An admin register application.
     */
    @Override
    public AdminRegistrationApplication findAllSummaryById(String id) {
        final AdminRegistrationApplicationEntity registerApplicationEntity = adminRegistrationApplicationRepository
                .findById(id)
                .filter(AdminRegistrationApplicationEntity::isWaiting)
                .orElseThrow(() -> new AysAdminRegistrationApplicationSummaryNotExistByIdException(id));

        return adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Creates a new admin register application.
     *
     * @param request The request object containing the register application details.
     * @return A response object containing the created register application.
     */
    @Override
    public AdminRegistrationApplication create(AdminRegistrationApplicationCreateRequest request) {
        boolean isInstitutionExists = institutionRepository.existsActiveById(request.getInstitutionId());
        if (!isInstitutionExists) {
            throw new AysInstitutionNotExistException(request.getInstitutionId());
        }

        AdminRegistrationApplicationEntity registerApplicationEntity = adminRegistrationApplicationCreateRequestToAdminRegistrationApplicationEntityMapper.mapForSaving(request);
        adminRegistrationApplicationRepository.save(registerApplicationEntity);

        return adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Approves a new admin register application.
     *
     * @param id The id of the register application.
     */
    @Override
    @Transactional
    public void approve(String id) {
        final AdminRegistrationApplicationEntity registerApplicationEntity = adminRegistrationApplicationRepository
                .findById(id)
                .filter(AdminRegistrationApplicationEntity::isCompleted)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException(id, AdminRegistrationApplicationStatus.COMPLETED));
        final UserEntityV2 userEntity = registerApplicationEntity.getUser();

        registerApplicationEntity.verify();
        adminRegistrationApplicationRepository.save(registerApplicationEntity);

        userEntity.activate();
        userRepository.save(userEntity);
    }

    /**
     * Rejects an admin register application by id.
     *
     * @param id      The id of the register application.
     * @param request The request object containing the rejection details.
     */
    @Override
    @Transactional
    public void reject(String id, AdminRegistrationApplicationRejectRequest request) {
        final AdminRegistrationApplicationEntity registerApplicationEntity = adminRegistrationApplicationRepository
                .findById(id)
                .filter(AdminRegistrationApplicationEntity::isCompleted)
                .orElseThrow(() -> new AysAdminRegistrationApplicationNotExistByIdOrStatusNotWaitingException(id, AdminRegistrationApplicationStatus.WAITING));
        final UserEntityV2 userEntity = registerApplicationEntity.getUser();

        registerApplicationEntity.reject(request.getRejectReason());
        adminRegistrationApplicationRepository.save(registerApplicationEntity);

        userEntity.passivate();
        userRepository.save(userEntity);
    }

}
