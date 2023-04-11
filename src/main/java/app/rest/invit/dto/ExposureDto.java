package app.rest.invit.dto;

import java.io.Serializable;
import java.util.List;

public class ExposureDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private String type;

    private List<EventDto> events;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EventDto> getEvents() {
        return this.events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

}
