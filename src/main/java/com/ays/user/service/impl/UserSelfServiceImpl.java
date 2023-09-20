package com.ays.user.service.impl;

import com.ays.assignment.repository.AssignmentRepository;
import com.ays.auth.model.AysIdentity;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.repository.UserRepository;
import com.ays.user.service.UserSelfService;
import com.ays.user.util.exception.AysUserCannotUpdateSupportStatusException;
import com.ays.user.util.exception.AysUserNotExistByIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

/**
 * UserSelfServiceImpl is an implementation of the UserSelfService interface.
 * It manages a user's own operations.
 */
@Service
@RequiredArgsConstructor
class UserSelfServiceImpl implements UserSelfService {

    private final UserRepository userRepository;

    private final AssignmentRepository assignmentRepository;

    private final AysIdentity identity;

    /**
     * Updates the support status of a user.
     *
     * @param updateRequest the request object containing the updated support status
     */
    @Override
    public void updateUserSupportStatus(UserSupportStatusUpdateRequest updateRequest) {

        final String userId = identity.getUserId();
        final UserSupportStatus userSupportStatus = updateRequest.getSupportStatus();

        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AysUserNotExistByIdException(userId));

        this.checkAssignment(userId, userSupportStatus);

        userEntity.updateSupportStatus(userSupportStatus);
        userRepository.save(userEntity);

    }

    /**
     * Checks if a user has an assignment when update supportStatus to specific statuses.
     *
     * @param userId the id of the user to check
     * @param userSupportStatus the user support status of the user to check
     */
    private void checkAssignment(String userId, UserSupportStatus userSupportStatus) {

        final EnumSet<UserSupportStatus> supportStatusesToCheck = EnumSet.of(
                UserSupportStatus.IDLE,
                UserSupportStatus.READY,
                UserSupportStatus.BUSY,
                UserSupportStatus.OFFLINE
        );

        final boolean isAssignmentNeedToCheck = supportStatusesToCheck.contains(userSupportStatus);
        if (isAssignmentNeedToCheck) {
            assignmentRepository
                    .findByUserId(userId)
                    .ifPresent(assignmentEntity -> {
                        throw new AysUserCannotUpdateSupportStatusException(userId, assignmentEntity.getId());
                    });
        }

    }

}
