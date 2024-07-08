package org.github.termomix.controller;

import lombok.AllArgsConstructor;
import org.github.termomix.services.ScrapperService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class RegisterController {

    private ScrapperService scrapperService;

    @RequestMapping("/scrap")
    public void scrap() {
        scrapperService.scrap();
    }
}
