package app.rest.invit.dto;

import java.io.Serializable;
import java.util.Date;

public class EventDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private String eventId;

    private String title;

    private String content;

    private Date expiresAt;

    private Date createdAt;

    private UserDto user;

    private ExposureDto exposure;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDto getUser() {
        return this.user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ExposureDto getExposure() {
        return this.exposure;
    }

    public void setExposure(ExposureDto exposure) {
        this.exposure = exposure;
    }

    public Date getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
