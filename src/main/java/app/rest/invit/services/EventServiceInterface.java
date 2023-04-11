package app.rest.invit.services;

import java.util.List;

import app.rest.invit.dto.EventCreationDto;
import app.rest.invit.dto.EventDto;

public interface EventServiceInterface {
    public EventDto createEvent(EventCreationDto event);

    public List<EventDto> getLastEvents();

    public EventDto getEvent(String eventId);

    public void deleteEvent(String eventId, long userId);

    public EventDto updateEvent(String eventId, long userId, EventCreationDto eventUpdateDto);
}
