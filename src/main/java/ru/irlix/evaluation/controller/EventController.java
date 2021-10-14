package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.repository.EventRepository;
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
        List<EventResponse> list = eventService.getAllEvents();
        return list;

    }
}
