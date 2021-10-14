package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.service.EventService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/events")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventResponse> getAll(){
        return eventService.getAllEvents();
    }
}
