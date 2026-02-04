package org.ays.common.service;

import org.ays.common.model.AysMail;

/**
 * <p>
 * Defines the contract for a mail service responsible for sending emails within the system.
 * </p>
 * <p>
 * This service is used for preparing and sending emails based on the provided
 * {@link AysMail} object, which includes details such as recipients, email template,
 * and any additional parameters.
 * </p>
 * <p>
 * The actual implementation of this service may include
 * features such as asynchronous processing, domain filtering, and logging.
 * </p>
 */
public interface AysMailService {

    /**
     * <p>
     * Sends an email using the provided {@link AysMail} object.
     * </p>
     * <p>
     * This method processes the email details, such as recipients, template, and
     * parameters, as encapsulated in the {@link AysMail} object, and performs the
     * necessary operations to dispatch the email.
     * </p>
     * @param mail The {@link AysMail} object containing email details, including
     *             recipients, template, and additional parameters to personalize
     *             the email.
     */
    void send(AysMail mail);

}
