package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.service.EventService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/events")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class EventController {

    private final EventService eventService;

    @GetMapping
    public Page<EventResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return eventService.getAllEvents(pageable);
    }
}
