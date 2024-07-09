package org.github.termomix.configs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Getter
@Slf4j
public class SeleniumConfig {
    private static final String CHROME_DRIVER_EXE = "chromedriver.exe";
    private static final String WEBDRIVER_CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";
    private static final String BUTTON_COOKIE = "onetrust-accept-btn-handler";
    private static final String[] PATHS = {"", "bin/", "target/classes"};

    private ChromeDriver driver;

    public SeleniumConfig() {
        log.info("[SeleniumConfig] Trying to find local chrome webdriver");
        findDriverInLocalWorkdir();
        initDriver();
    }

    private void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-gpu");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("window-size=1200x600");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems

        driver = new ChromeDriver(options);
    }

    private void findDriverInLocalWorkdir() {
        System.setProperty(WEBDRIVER_CHROME_DRIVER_PROPERTY, findFile());
    }

    private static String findFile() {
        for (String path : PATHS) {
            if (new File(path + CHROME_DRIVER_EXE).exists())
                return path + CHROME_DRIVER_EXE;
        }
        return "";
    }
}
