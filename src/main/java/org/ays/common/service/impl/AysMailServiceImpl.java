package org.ays.common.service.impl;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.model.AysMail;
import org.ays.common.model.enums.AysMailTemplate;
import org.ays.common.service.AysMailService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Implementation of the {@link AysMailService} interface, responsible for sending
 * emails asynchronously with the ability to process email templates and parameters.
 * </p>
 *
 * <p>This service includes functionality to:</p>
 * <ul>
 *   <li>
 *     Prevent sending emails to certain ignored domains.
 *   </li>
 *   <li>
 *     Process an email template by extracting and replacing placeholders
 *     with runtime parameters.
 *   </li>
 *   <li>
 *     Handle email creation and asynchronous sending with a timeout mechanism.
 *   </li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
class AysMailServiceImpl implements AysMailService {

    private final JavaMailSender mailSender;


    private static final List<String> IGNORED_DOMAINS = List.of(
            "@afetyonetimsistemi.test"
    );


    /**
     * Sends the specified email if it does not target any ignored domain. The email
     * is processed asynchronously, and a timeout mechanism is applied to ensure timely
     * delivery or raise a warning in case of delay.
     *
     * @param mail the {@link AysMail} instance containing email details such as recipients,
     *             template, and parameters. If any recipient's domain matches an ignored domain,
     *             the email will not be sent and a warning will be logged.
     */
    @Override
    public void send(final AysMail mail) {

        for (String ignoredDomain : IGNORED_DOMAINS) {
            boolean ignoredMailExists = mail.getTo().stream().anyMatch(to -> to.endsWith(ignoredDomain));
            if (ignoredMailExists) {
                log.warn("Mail sending is ignored for {} with {} template", mail.getTo(), mail.getTemplate());
                return;
            }
        }

        CompletableFuture.runAsync(() -> this.sendEmail(mail))
                .orTimeout(5, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    log.warn("Mail not sent to {} in 5 seconds with {} template", mail.getTo(), mail.getTemplate());
                    throw new AsyncRequestTimeoutException();
                });

    }

    /**
     * Sends an email using the provided {@link AysMail} object. Constructs the email with specified
     * recipients, template, and parameters, and sends it through the configured mail sender.
     * Logs the operation status, including successful and failed send attempts.
     *
     * @param mail the {@link AysMail} instance containing details such as recipients, template,
     *             and parameters. It is expected to include a list of recipients and a predefined
     *             email template.
     */
    private void sendEmail(final AysMail mail) {

        try {

            MimeMessage mimeMessage = this.createMimeMessage(mail);

            mailSender.send(mimeMessage);

            log.trace("Mail sent to {} with {} template", mail.getTo(), mail.getTemplate());

        } catch (Exception exception) {
            log.error("Received error while sending mail to {} with {} template", mail.getTo(), mail.getTemplate(), exception);
        }

    }

    /**
     * Creates a {@link MimeMessage} instance by populating it with the necessary details
     * such as recipients, subject, template content, and parameters.
     *
     * @param mail the {@link AysMail} instance containing details like recipients,
     *             email template, and parameters to populate within the template.
     * @return the constructed {@link MimeMessage} instance ready for sending.
     * @throws IOException if an error occurs when reading the email template file.
     * @throws MessagingException if an error occurs when creating or setting up the
     *                            {@link MimeMessage}.
     */
    private MimeMessage createMimeMessage(final AysMail mail) throws IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String htmlContent = this.findTemplate(mail.getTemplate());

        String title = this.findTitle(htmlContent);
        mimeMessage.setSubject(title);

        String htmlContentWithParameters = this.addParameters(htmlContent, mail.getParameters());
        mimeMessage.setText(htmlContentWithParameters, "UTF-8", "html");

        mimeMessage.setFrom(new InternetAddress("noreply@afetyonetimsistemi.org", "Afet Yönetim Sistemi"));

        for (String to : mail.getTo()) {
            mimeMessage.addRecipients(Message.RecipientType.TO, to);
        }
        return mimeMessage;
    }

    /**
     * Finds and reads the email template file associated with the specified {@link AysMailTemplate}.
     * The file is retrieved from the application's classpath and its content is returned as a string.
     *
     * @param template the {@link AysMailTemplate} identifying the email template file to be loaded.
     *                 This parameter provides the filename of the template to look for.
     * @return the content of the email template file as a string, encoded in UTF-8.
     * @throws IOException if an error occurs while reading the template file.
     */
    private String findTemplate(final AysMailTemplate template) throws IOException {
        final ClassPathResource classPathResource = new ClassPathResource(
                "/mail/template/" + template.getFile(),
                AysMailServiceImpl.class.getClassLoader()
        );
        return new String(
                FileCopyUtils.copyToByteArray(classPathResource.getInputStream()),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Extracts the title from the given HTML content by locating the text enclosed
     * within the <title> and </title> tags.
     *
     * @param htmlContent the HTML content as a string, which is expected to contain
     *                    a <title> element with a valid title.
     * @return the title text extracted from the HTML content, or an
     *         undefined behavior if the content does not contain a valid <title> tag.
     */
    private String findTitle(String htmlContent) {
        return htmlContent.substring(htmlContent.indexOf("<title>") + 7, htmlContent.indexOf("</title>"));
    }

    /**
     * Replaces placeholders in the provided HTML content with the corresponding values from the parameters map.
     * Placeholders are defined in the format {key}, where key is the parameter key in the map.
     *
     * @param htmlContent the HTML content containing placeholders to be replaced
     * @param parameters a map of parameter keys and their corresponding replacement values
     * @return the updated HTML content after replacing all placeholders with the corresponding values
     */
    private String addParameters(String htmlContent, Map<String, Object> parameters) {
        String template = htmlContent;
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            template = template.replace("{" + parameter.getKey() + "}", parameter.getValue().toString());
        }
        return template;
    }

}
