package app.rest.invit.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rest.invit.requests.EventCreateRequestModel;
import app.rest.invit.responses.OperationStatusModel;
import app.rest.invit.responses.EventRest;
import app.rest.invit.services.EventServiceInterface;
import app.rest.invit.services.UserServiceInterface;
import app.rest.invit.dto.EventCreationDto;
import app.rest.invit.dto.EventDto;
import app.rest.invit.dto.UserDto;
import app.rest.invit.utils.Exposures;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventServiceInterface eventService;

    @Autowired
    UserServiceInterface userService;

    @Autowired
    ModelMapper mapper;

    @PostMapping
    public EventRest createEvent(@RequestBody @Valid EventCreateRequestModel createRequestModel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getPrincipal().toString();

        EventCreationDto eventCreationDto = mapper.map(createRequestModel, EventCreationDto.class);

        eventCreationDto.setUserEmail(email);

        EventDto eventDto = eventService.createEvent(eventCreationDto);

        EventRest eventToReturn = mapper.map(eventDto, EventRest.class);

        return eventToReturn;
    }

    @GetMapping(path = "/last") // localhost:8080/events/last
    public List<EventRest> lastEvents() {
        List<EventDto> events = eventService.getLastEvents();

        List<EventRest> eventRests = new ArrayList<>();

        for (EventDto event : events) {
            EventRest eventRest = mapper.map(event, EventRest.class);
            eventRests.add(eventRest);
        }

        return eventRests;
    }

    @GetMapping(path = "/{id}") // localhost:8080/events/uuid
    public EventRest getEvent(@PathVariable String id) {

        EventDto eventDto = eventService.getEvent(id);

        EventRest eventRest = mapper.map(eventDto, EventRest.class);

        // VALIDAR SI EL EVENTO ES PRIVADO O SI EL EVENTO YA EXPIRO
        if (eventRest.getExposure().getId() == Exposures.PRIVATE || eventRest.getExpired()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDto user = userService.getUser(authentication.getPrincipal().toString());

            if (user.getId() != eventDto.getUser().getId()) {
                throw new RuntimeException("No tienes permisos para realizar esta accion");
            }
        }

        return eventRest;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteEvent(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDto user = userService.getUser(authentication.getPrincipal().toString());

        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");

        eventService.deleteEvent(id, user.getId());
        operationStatusModel.setResult("SUCCESS");

        return operationStatusModel;
    }

    @PutMapping(path = "/{id}")
    public EventRest updateEvent(@RequestBody @Valid EventCreateRequestModel eventCreateRequestModel,
            @PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDto user = userService.getUser(authentication.getPrincipal().toString());

        EventCreationDto eventUpdateDto = mapper.map(eventCreateRequestModel, EventCreationDto.class);

        EventDto eventDto = eventService.updateEvent(id, user.getId(), eventUpdateDto);

        EventRest updatedEvent = mapper.map(eventDto, EventRest.class);

        return updatedEvent;
    }

}
