package org.github.termomix.configs;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Slf4j
public class SeleniumConfig {
    private static final String BUTTON_COOKIE = "onetrust-accept-btn-handler";
    private ChromeDriver driver;

    public SeleniumConfig() {
        log.info("[SeleniumConfig] Initializing Chrome WebDriver");
        initDriver();
    }

    private void initDriver() {
        // Automatically manages ChromeDriver version
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-gpu");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("window-size=1200x600");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage"); // For limited resource environments

        driver = new ChromeDriver(options);
    }
}
