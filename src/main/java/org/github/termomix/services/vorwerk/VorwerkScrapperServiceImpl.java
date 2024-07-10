package org.github.termomix.services.vorwerk;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.github.termomix.configs.SeleniumConfig;
import org.github.termomix.model.User;
import org.github.termomix.services.EmailService;
import org.github.termomix.services.ScrapperService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class VorwerkScrapperServiceImpl implements ScrapperService {
    public static final String SPAN_CONTAINS_TEXT_CONFIRM_CODE = "//span[contains(text(), 'Potwierdź kod')]";
    public static final String INPUT_CODE_CONFIRMATION = "label#gigya-textbox-code_label input";
    public static final String SPAN_REGISTER_ACCUNT = "//span[contains(text(), 'Zarejestruj konto tutaj')]";
    public static final String SPAN_CONTAINS_TEXT_I_CONSENT_TO = "//span[contains(text(), 'Wyrażam zgodę na')]";
    public static final String REGISTER_BUTTON = "button.button.button--outline.pull-right[data-toggle='collapse']";
    public static final String EMAIL_INPUT = "gigya-loginID-126291542149967380";
    public static final String PASSWORD_INPUT = "gigya-password-77342656421579710";
    public static final String PASSWORD_RETYPE_INPUT = "gigya-password-122211554012532780";
    public static final String PASSWORD_RODZINKA_PL_123 = "rodzinkaPL2024!";
    public static final String GENDER_RADIO_BUTTON_MEN = "//span[text()='Pan']";
    public static final String FIRST_NAME_INPUT = "gigya-textbox-30426711382413052";
    public static final String LAST_NAME_INPUT = "gigya-textbox-67439711721643270";
    private static final String BUTTON_COOKIE = "onetrust-accept-btn-handler";
    public static final String CONTAIN_LOGOUT = "//a[contains(text(), 'Wyloguj się') and contains(@class, 'js-logout')]";

    @Value("${vorwerk.site.url}")
    private String siteUrl;

    private final Faker faker = new Faker();
    private final EmailService emailService;
    private final ChromeDriver driver;

    public VorwerkScrapperServiceImpl(SeleniumConfig seleniumConfig, @Qualifier("vorwerkEmailServiceImpl") EmailService emailService) {
        this.driver = seleniumConfig.getDriver();
        this.emailService = emailService;
    }

    @SneakyThrows
    @Override
    public User scrap() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(siteUrl);

        acceptCookies();

        Thread.sleep(500);
        log.info("[ScrapperServiceImpl] try to find register button by cssSelector: " + REGISTER_BUTTON);
        WebElement registerButton = driver.findElement(By.cssSelector(REGISTER_BUTTON));
        registerButton.click();

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find email input by id: " + EMAIL_INPUT);
        WebElement emailInput = driver.findElement(By.id(EMAIL_INPUT));
        String email = emailService.getEmail();
        emailInput.sendKeys(email);

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find password input by id: " + PASSWORD_INPUT);
        WebElement passwordInput = driver.findElement(By.id(PASSWORD_INPUT));
        passwordInput.sendKeys(PASSWORD_RODZINKA_PL_123);

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find password retype input by id: " + PASSWORD_RETYPE_INPUT);
        WebElement passwordRetypeInput = driver.findElement(By.id(PASSWORD_RETYPE_INPUT));
        passwordRetypeInput.sendKeys(PASSWORD_RODZINKA_PL_123);

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find gender radio button by xpath: " + GENDER_RADIO_BUTTON_MEN);
        WebElement genderSpan = driver.findElement(By.xpath(GENDER_RADIO_BUTTON_MEN));
        genderSpan.click();

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find first name input by id: " + FIRST_NAME_INPUT);
        WebElement firstNameInput = driver.findElement(By.id(FIRST_NAME_INPUT));
        firstNameInput.sendKeys(faker.name().firstName());

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find last name input by id: " + LAST_NAME_INPUT);
        WebElement lastNameInput = driver.findElement(By.id(LAST_NAME_INPUT));
        lastNameInput.sendKeys(faker.name().lastName());

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find politics check button by xpath: " + SPAN_CONTAINS_TEXT_I_CONSENT_TO);
        WebElement consentSpan = driver.findElement(By.xpath(SPAN_CONTAINS_TEXT_I_CONSENT_TO));
        consentSpan.click();

        Thread.sleep(500);
        log.info("[VorwerkScrapperServiceImpl] try to find register account button by xpath: " + SPAN_REGISTER_ACCUNT);
        WebElement registerAccountButton = driver.findElement(By.xpath(SPAN_REGISTER_ACCUNT));
        registerAccountButton.click();

        Thread.sleep(3000);
        log.info("[VorwerkScrapperServiceImpl] try to find confirm code input by className: " + INPUT_CODE_CONFIRMATION);
        String emailConfirmationCode = emailService.getResponse();
        WebElement codeInputField = driver.findElement(By.cssSelector(INPUT_CODE_CONFIRMATION));
        codeInputField.sendKeys(emailConfirmationCode);

        Thread.sleep(100);
        log.info("[VorwerkScrapperServiceImpl] try to find confirm code button by xpath: " + SPAN_CONTAINS_TEXT_CONFIRM_CODE);
        WebElement confirmCode = driver.findElement(By.xpath(SPAN_CONTAINS_TEXT_CONFIRM_CODE));
        confirmCode.click();

        log.info("[VorwerkScrapperServiceImpl] try to find logout button by xpath: {}", CONTAIN_LOGOUT);
        Thread.sleep(2000);
        WebElement logoutLink = driver.findElement(By.xpath(CONTAIN_LOGOUT));
        logoutLink.click();

        User user = new User(email, PASSWORD_RODZINKA_PL_123);
        log.info("user info: {}", user);
        return user;
    }

    private void acceptCookies() {
        try {
            log.info("[SeleniumConfig] Trying to find and accept cookies");
            log.info("[SeleniumConfig] start scrapping, try to find accept cookies button by id: " + BUTTON_COOKIE);
            WebElement acceptCookiesButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id(BUTTON_COOKIE)));

            Thread.sleep(500);
            log.info("[SeleniumConfig] click: " + BUTTON_COOKIE);
            acceptCookiesButton.click();
            log.info("[SeleniumConfig] Cookies accepted");
        } catch (Exception e) {
            log.error("Failed to accept cookies", e);
        }
    }
}