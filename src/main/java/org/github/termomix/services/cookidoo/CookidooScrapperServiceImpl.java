package org.github.termomix.services.cookidoo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.github.termomix.configs.SeleniumConfig;
import org.github.termomix.model.User;
import org.github.termomix.services.EmailService;
import org.github.termomix.services.ScrapperService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class CookidooScrapperServiceImpl implements ScrapperService {

    public static final String NAV_LINK_REGISTER = "//a[contains(text(), 'Zarejestruj konto') and contains(@class, 'core-nav__link')]";
    public static final String EMAIL_ID = "uEmail";
    public static final String PASSWORD_ID = "password";
    public static final String PASSWORD_RODZINKA_PL_2024 = "rodzinkaPL2024!";
    public static final String ACCEPT_TOS_ID = "acceptTos1";
    public static final String CSS_CLASS_REGISTER_YOURSELF = "//button[text()='Zarejestruj się']";

    @Value("${cookido.site.url}")
    private String siteUrl;

    private final EmailService emailService;
    private final ChromeDriver driver;

    public CookidooScrapperServiceImpl(SeleniumConfig seleniumConfig, @Qualifier("cookidooEmailServiceImpl") EmailService emailService) {
        this.driver = seleniumConfig.getDriver();
        this.emailService = emailService;
    }

    @SneakyThrows
    @Override
    public User scrap() {
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(siteUrl);

        Thread.sleep(200);
        log.info("[CookidooEmailServiceImpl] try to find register button by xpath: " + NAV_LINK_REGISTER);
        WebElement registerLink = driver.findElement(By.xpath(NAV_LINK_REGISTER));
        registerLink.click();

        Thread.sleep(2000);
        log.info("[CookidooEmailServiceImpl] try to find email input by id: " + EMAIL_ID);
        WebElement emailField = driver.findElement(By.id(EMAIL_ID));
        String email = emailService.getEmail();
        emailField.sendKeys(email);

        Thread.sleep(500);
        log.info("[CookidooEmailServiceImpl] try to find password input by id: " + PASSWORD_ID);
        WebElement passwordInput = driver.findElement(By.id(PASSWORD_ID));
        passwordInput.sendKeys(PASSWORD_RODZINKA_PL_2024);

        Thread.sleep(500);
        log.info("[CookidooEmailServiceImpl] try to find accept tos radio button by id: " + ACCEPT_TOS_ID);
        WebElement genderSpan = driver.findElement(By.id(ACCEPT_TOS_ID));
        genderSpan.click();

        Thread.sleep(500);
        log.info("[CookidooEmailServiceImpl] try to find register account button by cssSelector: " + CSS_CLASS_REGISTER_YOURSELF);
        WebElement registerAccountButton = driver.findElement(By.xpath(CSS_CLASS_REGISTER_YOURSELF));
        registerAccountButton.click();

        handleConfirmationAccount();

        User user = new User(email, PASSWORD_RODZINKA_PL_2024);
        log.info("user info: {}", user);
        return user;
    }

    private void handleConfirmationAccount() {
        String confirmationLink = emailService.getResponse();
        driver.get(confirmationLink);
    }
}
