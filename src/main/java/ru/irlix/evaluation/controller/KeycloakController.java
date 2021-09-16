package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.service.KeycloakService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/keycloak")
@RequiredArgsConstructor
@CrossOrigin
public class KeycloakController {

    private final KeycloakService keycloakService;

    @GetMapping()
    public ResponseEntity<String> getJwt() {
        String jwt = keycloakService.getJwt();
        System.out.println("getJwt: " + jwt);
//        System.out.println("getRoles(): " + keycloakService.getRoles());
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    @GetMapping("/users")
    public void getUsers() {
        keycloakService.getAllUsers().forEach(user -> System.out.println("getAllUsers: имя: " + user.getFirstName() + " " + user.getLastName()));
    }

    @PutMapping("/update")
    public void updateUsers() {
        keycloakService.update();
    }

}
