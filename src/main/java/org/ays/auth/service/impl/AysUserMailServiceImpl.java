package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.service.AysUserMailService;
import org.ays.common.model.AysMail;
import org.ays.common.model.enums.AysMailTemplate;
import org.ays.common.service.AysMailService;
import org.ays.institution.model.Institution;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link AysUserMailService} interface for sending user-related emails.
 * <p>
 * This service is responsible for composing and sending emails to users, specifically for scenarios
 * like password creation. It uses the {@link AysMailService} to handle the actual sending of emails.
 * </p>
 */
@Service
@RequiredArgsConstructor
class AysUserMailServiceImpl implements AysUserMailService {

    private final AysMailService mailService;

    /**
     * Sends an email to the user with a link to create their password.
     * <p>
     * This method composes an email containing a secure link that allows the user to set their password.
     * It retrieves the front-end URL associated with the user's institution to construct the complete link.
     * The email is dispatched using the {@link AysMailTemplate#CREATE_PASSWORD} template.
     * </p>
     *
     * @param user The user to whom the password creation email will be sent.
     */
    @Override
    public void sendPasswordCreateEmail(AysUser user) {

        final String userFullName = user.getFirstName() + " " + user.getLastName();
        final String feUrl = this.getInstitutionFeUrl(user);
        final String url = feUrl.concat("/create-password/").concat(user.getPassword().getId());

        final Map<String, Object> parameters = Map.of(
                "userFullName", userFullName,
                "url", url
        );

        final AysMail mail = AysMail.builder()
                .to(List.of(user.getEmailAddress()))
                .template(AysMailTemplate.CREATE_PASSWORD)
                .parameters(parameters)
                .build();

        mailService.send(mail);
    }

    /**
     * Retrieves the front-end URL for the first institution associated with the user.
     *
     * @param user The user whose institution's front-end URL is to be retrieved.
     * @return The front-end URL of the user's institution.
     * @throws IllegalStateException If the user is not associated with any institution.
     */
    private String getInstitutionFeUrl(final AysUser user) {
        final Optional<Institution> institution = user.getInstitutions().stream().findFirst();
        if (institution.isEmpty()) {
            throw new IllegalStateException("User has no institution!");
        }
        return institution.get().getFeUrl();
    }

}
