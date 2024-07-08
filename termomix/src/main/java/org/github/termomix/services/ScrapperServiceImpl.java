package org.github.termomix.services;

import lombok.extern.slf4j.Slf4j;
import org.github.termomix.configs.SeleniumConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class ScrapperServiceImpl implements ScrapperService {
    @Value("${site.url}")
    private String siteUrl;
    private static final String BUTTON_COOKIE = "onetrust-accept-btn-handler";

    private static final String TABLE_CSS_CLASS_NAME = "col-sm-6";
    private static final String REGISTER_BUTTON = "button button--outline pull-right";

    private final SeleniumConfig seleniumConfig;

    public ScrapperServiceImpl(SeleniumConfig seleniumConfig) {
        this.seleniumConfig = seleniumConfig;
    }

    @Override
    public void scrap() {

        ChromeDriver driver = seleniumConfig.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(siteUrl);

        WebElement acceptCookiesButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id(BUTTON_COOKIE)));

        acceptCookiesButton.click();

        try {
            Thread.sleep(500);
            WebElement registerButton = driver.findElement(By.cssSelector("button.button.button--outline.pull-right[data-toggle='collapse']"));

            registerButton.click();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Finish getTeamResult");

    }
}
