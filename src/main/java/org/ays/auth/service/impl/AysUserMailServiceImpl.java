package org.ays.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.enums.AysConfigurationParameter;
import org.ays.auth.service.AysUserMailService;
import org.ays.common.model.AysMail;
import org.ays.common.model.enums.AysMailTemplate;
import org.ays.common.service.AysMailService;
import org.ays.parameter.model.AysParameter;
import org.ays.parameter.port.AysParameterReadPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly = true)
class AysUserMailServiceImpl implements AysUserMailService {

    private final AysMailService mailService;
    private final AysParameterReadPort parameterReadPort;


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

        final Map<String, Object> parameters = Map.of(
                "userFullName", user.getFirstName() + " " + user.getLastName(),
                "url", this.findFeUrl().concat("/create-password/").concat(user.getPassword().getId())
        );

        final AysMail mail = AysMail.builder()
                .to(List.of(user.getEmailAddress()))
                .template(AysMailTemplate.CREATE_PASSWORD)
                .parameters(parameters)
                .build();

        mailService.send(mail);
    }

    /**
     * Retrieves the front-end URL from configuration parameters.
     * <p>
     * This method fetches the value of the front-end URL from the parameter read port. If the parameter is not found,
     * it returns a default value defined by {@link AysConfigurationParameter#FE_URL}.
     * </p>
     *
     * @return The front-end URL as a string.
     */
    private String findFeUrl() {
        return parameterReadPort
                .findByName(AysConfigurationParameter.FE_URL.name())
                .orElse(AysParameter.from(AysConfigurationParameter.FE_URL))
                .getDefinition();
    }

}
