package app.rest.invit.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.rest.invit.entities.ExposureEntity;
import app.rest.invit.entities.EventEntity;
import app.rest.invit.entities.UserEntity;
import app.rest.invit.repositories.ExposureRepository;
import app.rest.invit.repositories.EventRepository;
import app.rest.invit.repositories.UserRepository;
import app.rest.invit.dto.EventCreationDto;
import app.rest.invit.dto.EventDto;
import app.rest.invit.utils.Exposures;

@Service
public class EventService implements EventServiceInterface {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExposureRepository exposureRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public EventDto createEvent(EventCreationDto event) {

        UserEntity userEntity = userRepository.findByEmail(event.getUserEmail());
        ExposureEntity exposureEntity = exposureRepository.findById(event.getExposureId());

        EventEntity eventEntity = new EventEntity();
        eventEntity.setUser(userEntity);
        eventEntity.setExposure(exposureEntity);
        eventEntity.setTitle(event.getTitle());
        eventEntity.setContent(event.getContent());
        eventEntity.setEventId(UUID.randomUUID().toString());
        eventEntity.setExpiresAt(new Date(System.currentTimeMillis() + (event.getExpirationTime() * 60000)));

        EventEntity createdEvent = eventRepository.save(eventEntity);

        EventDto eventToReturn = mapper.map(createdEvent, EventDto.class);

        return eventToReturn;
    }

    @Override
    public List<EventDto> getLastEvents() {

        List<EventEntity> eventEntities = eventRepository.getLastPublicEvents(Exposures.PUBLIC,
                new Date(System.currentTimeMillis()));

        List<EventDto> eventDtos = new ArrayList<>();

        for (EventEntity event : eventEntities) {
            EventDto eventDto = mapper.map(event, EventDto.class);
            eventDtos.add(eventDto);
        }

        return eventDtos;
    }

    @Override
    public EventDto getEvent(String eventId) {

        EventEntity eventEntity = eventRepository.findByEventId(eventId);
        EventDto eventDto = mapper.map(eventEntity, EventDto.class);
        return eventDto;
    }

    @Override
    public void deleteEvent(String eventId, long userId) {
        EventEntity eventEntity = eventRepository.findByEventId(eventId);
        if (eventEntity.getUser().getId() != userId)
            throw new RuntimeException("No se puede realizar esta accion");

        eventRepository.delete(eventEntity);

    }

    @Override
    public EventDto updateEvent(String eventId, long userId, EventCreationDto eventUpdateDto) {
        EventEntity eventEntity = eventRepository.findByEventId(eventId);
        if (eventEntity.getUser().getId() != userId)
            throw new RuntimeException("No se puede realizar esta accion");

        ExposureEntity exposureEntity = exposureRepository.findById(eventUpdateDto.getExposureId());

        eventEntity.setExposure(exposureEntity);
        eventEntity.setTitle(eventUpdateDto.getTitle());
        eventEntity.setContent(eventUpdateDto.getContent());
        eventEntity.setExpiresAt(new Date(System.currentTimeMillis() + (eventUpdateDto.getExpirationTime() * 60000)));

        EventEntity updatedEvent = eventRepository.save(eventEntity);

        EventDto eventDto = mapper.map(updatedEvent, EventDto.class);

        return eventDto;

    }

}
