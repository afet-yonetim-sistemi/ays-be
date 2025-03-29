package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.config.AysApplicationConfigurationParameter;
import org.ays.auth.model.AysUser;
import org.ays.auth.service.AysUserMailService;
import org.ays.common.model.AysMail;
import org.ays.common.model.enums.AysMailTemplate;
import org.ays.common.service.AysMailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link AysUserMailService} interface for sending user-related emails.
 * <p>
 * This service is responsible for composing and sending emails to users, specifically for scenarios
 * like password creation. It uses the {@link AysMailService} to handle the actual sending of emails
 * and interacts with the parameter read port to fetch necessary configurations.
 * </p>
 */
@Service
@RequiredArgsConstructor
class AysUserMailServiceImpl implements AysUserMailService {

    private final AysMailService mailService;
    private final AysApplicationConfigurationParameter applicationConfigurationParameter;


    /**
     * Sends an email to the user to create a password.
     * <p>
     * This method composes an email with a link for the user to create their password. It fetches
     * the front-end URL from the configuration parameters and uses it to generate the complete URL
     * included in the email. The email template used is {@link AysMailTemplate#CREATE_PASSWORD}.
     * </p>
     *
     * @param user The user to whom the password creation email will be sent.
     */
    @Override
    public void sendPasswordCreateEmail(AysUser user) {

        final String userFullName = user.getFirstName() + " " + user.getLastName();
        final String url = applicationConfigurationParameter.getFeUrl()
                .concat("/create-password/").concat(user.getPassword().getId());

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

}
