package app.rest.invit.responses;

import java.util.Date;

public class EventRest {
    private String eventId;

    private String title;

    private String content;

    private Date expiresAt;

    private Date createdAt;

    private boolean expired;

    private UserRest user;

    private ExposureRest exposure;

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

    public boolean getExpired() {
        this.expired = getExpiresAt().compareTo(new Date(System.currentTimeMillis())) < 0;
        return this.expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public UserRest getUser() {
        return this.user;
    }

    public void setUser(UserRest user) {
        this.user = user;
    }

    public ExposureRest getExposure() {
        return this.exposure;
    }

    public void setExposure(ExposureRest exposure) {
        this.exposure = exposure;
    }

}
