package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.evaluation.service.KeycloakService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/keycloak")
@RequiredArgsConstructor
@CrossOrigin
public class KeycloakController {

    private final KeycloakService keycloakService;

    @GetMapping()
    public void getJwt() {
        System.out.println("getJwt: " + keycloakService.getJwt());
//        System.out.println("getRoles(): " + keycloakService.getRoles());
    }
}
