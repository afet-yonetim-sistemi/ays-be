package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.auth.exception.AysUserAlreadyExistsByEmailAddressException;
import org.ays.auth.exception.AysUserAlreadyExistsByPhoneNumberException;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.request.AysMobileUserRegisterRequest;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.auth.service.AysMobileUserRegisterService;
import org.ays.common.model.AysPhoneNumber;
import org.ays.institution.model.Institution;
import org.ays.institution.port.InstitutionReadPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link AysMobileUserRegisterService} that handles mobile
 * user self-registration.
 * This service creates new users with ACTIVE status for mobile app
 * registrations.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AysMobileUserRegisterServiceImpl implements AysMobileUserRegisterService {

        private final AysUserReadPort userReadPort;
        private final AysUserSavePort userSavePort;
        private final AysRoleReadPort roleReadPort;
        private final InstitutionReadPort institutionReadPort;
        private final PasswordEncoder passwordEncoder;

        /**
         * Default institution ID for mobile self-registered users.
         * This is the main AYS institution (Afet Yönetim Sistemi).
         */
        private static final String DEFAULT_MOBILE_USER_INSTITUTION_ID = "c5f9f610-f1c8-48a4-b1e9-1fee95f5a51f";

        /**
         * Default role ID for mobile self-registered users.
         * This role has the 'landing:page' permission required for login.
         */
        private static final String DEFAULT_MOBILE_USER_ROLE_ID = "mobile-user-role-0001-000000000000";

        /**
         * Registers a new mobile user.
         * <p>
         * Validates that the email and phone number are unique, creates a new user
         * with ACTIVE status, encodes the password, assigns the default
         * institution and role, and saves the user.
         * </p>
         *
         * @param registerRequest The registration request containing user details.
         * @throws AysUserAlreadyExistsByEmailAddressException if email already exists.
         * @throws AysUserAlreadyExistsByPhoneNumberException  if phone number already
         *                                                     exists.
         */
        @Override
        public void register(final AysMobileUserRegisterRequest registerRequest) {

                log.trace("Mobile user registration starting for email: {}", registerRequest.getEmailAddress());

                // Validate email uniqueness
                if (userReadPort.existsByEmailAddress(registerRequest.getEmailAddress())) {
                        throw new AysUserAlreadyExistsByEmailAddressException(registerRequest.getEmailAddress());
                }

                // Build phone number
                final AysPhoneNumber phoneNumber = AysPhoneNumber.builder()
                                .countryCode(registerRequest.getPhoneNumber().getCountryCode())
                                .lineNumber(registerRequest.getPhoneNumber().getLineNumber())
                                .build();

                // Validate phone uniqueness
                if (userReadPort.existsByPhoneNumber(phoneNumber)) {
                        throw new AysUserAlreadyExistsByPhoneNumberException(phoneNumber);
                }

                // Get default institution for mobile users
                final Institution institution = institutionReadPort.findById(DEFAULT_MOBILE_USER_INSTITUTION_ID)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Default mobile user institution not found: "
                                                                + DEFAULT_MOBILE_USER_INSTITUTION_ID));

                // Get default role for mobile users (with landing:page permission)
                final AysRole mobileUserRole = roleReadPort.findById(DEFAULT_MOBILE_USER_ROLE_ID)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Default mobile user role not found: " + DEFAULT_MOBILE_USER_ROLE_ID));

                // Create user entity - do not set ID manually, let JPA generate it
                final AysUser user = AysUser.builder()
                                .emailAddress(registerRequest.getEmailAddress())
                                .firstName(registerRequest.getFirstName())
                                .lastName(registerRequest.getLastName())
                                .phoneNumber(phoneNumber)
                                .city(registerRequest.getCity())
                                .status(AysUserStatus.ACTIVE)
                                .institution(institution)
                                .roles(List.of(mobileUserRole))
                                .build();

                // Set encoded password - do not set ID manually, let JPA generate it
                final String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
                user.setPassword(
                                AysUser.Password.builder()
                                                .value(encodedPassword)
                                                .build());

                // Save user
                userSavePort.save(user);

                log.info("Mobile user registered successfully with email: {}", registerRequest.getEmailAddress());
        }

}
