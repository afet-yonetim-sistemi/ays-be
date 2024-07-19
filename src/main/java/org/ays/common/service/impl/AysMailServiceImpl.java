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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
class AysMailServiceImpl implements AysMailService {

    private final JavaMailSender mailSender;

    @Override
    public void send(final AysMail mail) {

        CompletableFuture.runAsync(() -> this.sendEmail(mail))
                .orTimeout(5, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    log.warn("Mail not sent to {} in 5 seconds with {} template", mail.getTo(), mail.getTemplate());
                    throw new AsyncRequestTimeoutException();
                });

    }

    private void sendEmail(final AysMail mail) {

        try {

            MimeMessage mimeMessage = this.createMimeMessage(mail);

            mailSender.send(mimeMessage);

            log.trace("Mail sent to {} with {} template", mail.getTo(), mail.getTemplate());

        } catch (Exception exception) {
            log.error("Received error while sending mail to {} with {} template", mail.getTo(), mail.getTemplate(), exception);
        }

    }

    private MimeMessage createMimeMessage(final AysMail mail) throws IOException, MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        String htmlContent = this.findTemplate(mail.getTemplate());

        String title = this.findTitle(htmlContent);
        mimeMessage.setSubject(title);

        String htmlContentWithParameters = this.addParameters(htmlContent, mail.getParameters());
        mimeMessage.setText(htmlContentWithParameters, "UTF-8", "html");

        mimeMessage.setFrom(new InternetAddress("info@afetyonetimsistemi.org", "Afet Yönetim Sistemi"));

        for (String to : mail.getTo()) {
            mimeMessage.addRecipients(Message.RecipientType.TO, to);
        }
        return mimeMessage;
    }

    private String findTemplate(AysMailTemplate template) throws IOException {
        ClassPathResource resource = new ClassPathResource("mail/template/" + template.getFile());
        byte[] binaryData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(binaryData, StandardCharsets.UTF_8);
    }

    private String findTitle(String htmlContent) {
        return htmlContent.substring(htmlContent.indexOf("<title>") + 7, htmlContent.indexOf("</title>"));
    }

    private String addParameters(String htmlContent, Map<String, Object> parameters) {
        String template = htmlContent;
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            template = template.replace("{" + parameter.getKey() + "}", parameter.getValue().toString());
        }
        return template;
    }

}
