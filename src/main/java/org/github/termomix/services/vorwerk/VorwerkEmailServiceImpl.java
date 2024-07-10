package org.github.termomix.services.vorwerk;

import com.mailslurp.apis.InboxControllerApi;
import com.mailslurp.apis.WaitForControllerApi;
import com.mailslurp.clients.ApiClient;
import com.mailslurp.clients.ApiException;
import com.mailslurp.clients.Configuration;
import com.mailslurp.models.Email;
import com.mailslurp.models.InboxDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.github.termomix.services.EmailService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("vorwerkEmailServiceImpl")
@Slf4j
public class VorwerkEmailServiceImpl implements EmailService {

    public static final int DURATION_TIMEOUT_MINUTES = 6;
    public static final String ELEMENT_ID_FOR_CODE_CONFIRMATION = "cdcValidationCode";

    private final ApiClient client;
    private final InboxControllerApi inboxControllerApi;
    private InboxDto inbox;

    @SneakyThrows
    public VorwerkEmailServiceImpl(@Value("${api.access.key.mailslurp}") String apiKey) {
        client = Configuration.getDefaultApiClient();
        client.setApiKey(apiKey);

        int timeOut = (int) TimeUnit.MINUTES.toMillis(DURATION_TIMEOUT_MINUTES);
        client.setConnectTimeout(timeOut);
        client.setWriteTimeout(timeOut);
        client.setReadTimeout(timeOut);

        inboxControllerApi = new InboxControllerApi(client);
    }

    @Override
    public String getEmail() {
        try {
            inbox = inboxControllerApi.createInboxWithDefaults();
        } catch (ApiException e) {
            log.error("[EmailServiceImpl] {}", e.getResponseBody());
            throw new RuntimeException(e);
        }
        log.info("[EmailServiceImpl] indexId: {}", inbox.getId());

        String tempEmailAddress = inbox.getEmailAddress();
        log.info("[EmailServiceImpl] Temporary email address: {}", tempEmailAddress);
        return tempEmailAddress;
    }

    @Override
    public String getResponse() {
        long timeout = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        WaitForControllerApi waitForControllerApi = new WaitForControllerApi(client);
        boolean unreadOnly = true;
        Email email;
        try {
            email = waitForControllerApi.waitForLatestEmail(inbox.getId(), timeout, unreadOnly, null, null, null, null);
        } catch (ApiException e) {
            log.error("[EmailServiceImpl] error when try to retriev emial with confirmation code {}", e.getResponseBody());
            throw new RuntimeException(e);
        }
        String confirmationCode = extractConfirmationCode(email);
        log.info("[EmailServiceImpl] confirmation code: {}", confirmationCode);
        return confirmationCode;
    }

    private String extractConfirmationCode(Email email) {
        Document doc = Jsoup.parse(Objects.requireNonNull(email.getBody()));
        Element codeElement = doc.getElementById(ELEMENT_ID_FOR_CODE_CONFIRMATION);
        if (codeElement != null) {
            return codeElement.text();
        }
        return null;
    }
}

