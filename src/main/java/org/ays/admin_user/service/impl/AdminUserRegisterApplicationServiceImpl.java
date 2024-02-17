package org.ays.admin_user.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.admin_user.model.AdminUserRegisterApplication;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import org.ays.admin_user.repository.AdminUserRegisterApplicationRepository;
import org.ays.admin_user.repository.AdminUserRepository;
import org.ays.admin_user.service.AdminUserRegisterApplicationService;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdAndStatusException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationNotExistByIdException;
import org.ays.admin_user.util.exception.AysAdminUserRegisterApplicationSummaryNotExistByIdException;
import org.ays.common.model.AysPage;
import org.ays.institution.repository.InstitutionRepository;
import org.ays.institution.util.exception.AysInstitutionNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class implements the {@link AdminUserRegisterApplicationService} interface and provides verification operations for admin users.
 * It is annotated with {@code @Service} to indicate that it is a service component in the application.
 * The class is also annotated with {@code @RequiredArgsConstructor} to automatically generate a constructor based on the declared final fields.
 */
@Service
@RequiredArgsConstructor
public class AdminUserRegisterApplicationServiceImpl implements AdminUserRegisterApplicationService {

    private final AdminUserRegisterApplicationRepository adminUserRegisterApplicationRepository;
    private final AdminUserRepository adminUserRepository;
    private final InstitutionRepository institutionRepository;


    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();
    private final AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper adminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper = AdminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper.initialize();


    /**
     * Retrieves a paginated list of admin user registration applications based on the specified filters
     *
     * @param listRequest covering page, pageSize and filters
     * @return a paginated list of admin user registration applications
     */
    @Override
    public AysPage<AdminUserRegisterApplication> getRegistrationApplications(AdminUserRegisterApplicationListRequest listRequest) {
        final Specification<AdminUserRegisterApplicationEntity> filters = listRequest
                .toSpecification(AdminUserRegisterApplicationEntity.class);

        final Page<AdminUserRegisterApplicationEntity> registerApplicationEntities = adminUserRegisterApplicationRepository
                .findAll(filters, listRequest.toPageable());

        final List<AdminUserRegisterApplication> registerApplications = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntities.getContent());

        return AysPage.of(
                listRequest.getFilter(),
                registerApplicationEntities,
                registerApplications
        );
    }

    /**
     * Retrieves an admin user register application by id.
     *
     * @param id The id of the register application.
     * @return An admin user register application.
     */
    @Override
    public AdminUserRegisterApplication getRegistrationApplicationById(String id) {
        final AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationNotExistByIdException(id));

        return adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Retrieves an admin user register application by id and checks if it is waiting.
     * If it is not waiting, throws an exception.
     *
     * @param id registration application id
     * @return An admin user register application.
     */
    @Override
    public AdminUserRegisterApplication getRegistrationApplicationSummaryById(String id) {
        final AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationRepository
                .findById(id)
                .filter(AdminUserRegisterApplicationEntity::isWaiting)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationSummaryNotExistByIdException(id));

        return adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Creates a new admin user register application.
     *
     * @param request The request object containing the register application details.
     * @return A response object containing the created register application.
     */
    @Override
    public AdminUserRegisterApplication createRegistrationApplication(AdminUserRegisterApplicationCreateRequest request) {
        boolean isInstitutionExists = institutionRepository.existsActiveById(request.getInstitutionId());
        if (!isInstitutionExists) {
            throw new AysInstitutionNotExistException(request.getInstitutionId());
        }

        AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationCreateRequestToAdminUserRegisterApplicationEntityMapper.mapForSaving(request);
        adminUserRegisterApplicationRepository.save(registerApplicationEntity);

        return adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(registerApplicationEntity);
    }

    /**
     * Approves a new admin user register application.
     *
     * @param id The id of the register application.
     */
    @Override
    @Transactional
    public void approveRegistrationApplication(String id) {
        final AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationRepository
                .findById(id)
                .filter(AdminUserRegisterApplicationEntity::isCompleted)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationNotExistByIdAndStatusException(id, AdminUserRegisterApplicationStatus.COMPLETED));
        final AdminUserEntity adminUser = registerApplicationEntity.getAdminUser();

        registerApplicationEntity.verify();
        adminUserRegisterApplicationRepository.save(registerApplicationEntity);

        adminUser.activate();
        adminUserRepository.save(adminUser);
    }

    /**
     * Rejects an admin user register application by id.
     *
     * @param id      The id of the register application.
     * @param request The request object containing the rejection details.
     */
    @Override
    @Transactional
    public void rejectRegistrationApplication(String id, AdminUserRegisterApplicationRejectRequest request) {
        final AdminUserRegisterApplicationEntity registerApplicationEntity = adminUserRegisterApplicationRepository
                .findById(id)
                .filter(AdminUserRegisterApplicationEntity::isCompleted)
                .orElseThrow(() -> new AysAdminUserRegisterApplicationNotExistByIdAndStatusException(id, AdminUserRegisterApplicationStatus.WAITING));
        final AdminUserEntity adminUser = registerApplicationEntity.getAdminUser();

        registerApplicationEntity.reject(request.getRejectReason());
        adminUserRegisterApplicationRepository.save(registerApplicationEntity);

        adminUser.passivate();
        adminUserRepository.save(adminUser);
    }

}
