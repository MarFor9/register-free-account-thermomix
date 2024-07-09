package org.github.termomix.configs;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class SeleniumConfig {
    private static final String CHROME_DRIVER_EXE = "chromedriver.exe";
    private static final String WEBDRIVER_CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";

    private static final String[] PATHS = {"", "bin/", "target/classes"};

    public SeleniumConfig() {
        log.info("[SeleniumConfig] Trying to find local chrome webdriver");
        findDriverInLocalWorkdir();
    }

    public ChromeDriver createDriver() {
        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.addArguments("--headless");
        browserOptions.addArguments("--disable-gpu");
        browserOptions.addArguments("disable-infobars");
        browserOptions.addArguments("--disable-extensions");
        browserOptions.addArguments("window-size=1200x600");
        browserOptions.addArguments("--no-sandbox");
        browserOptions.addArguments("--remote-allow-origins=*");
        browserOptions.addArguments("--incognito");

        browserOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        return new ChromeDriver(browserOptions);
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
