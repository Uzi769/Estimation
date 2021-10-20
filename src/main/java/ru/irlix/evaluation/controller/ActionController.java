package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.evaluation.dto.response.ActionResponse;
import ru.irlix.evaluation.service.ActionService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/actions")
@RequiredArgsConstructor
@CrossOrigin
@Log4j2
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public List<ActionResponse> findAllRoles() {
        log.info(UrlConstants.RECEIVED_NO_ARGS);
        return actionService.findAllActions();
    }
}
