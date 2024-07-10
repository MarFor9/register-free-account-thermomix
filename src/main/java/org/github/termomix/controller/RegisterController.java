package org.github.termomix.controller;

import lombok.AllArgsConstructor;
import org.github.termomix.model.User;
import org.github.termomix.services.cookidoo.CookidooScrapperServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class RegisterController {

    private CookidooScrapperServiceImpl scrapperService;

    @RequestMapping("/scrap")
    public User scrap() {
        return scrapperService.scrap();
    }
}
