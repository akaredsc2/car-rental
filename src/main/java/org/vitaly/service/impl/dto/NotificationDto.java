package org.vitaly.service.impl.dto;

import org.vitaly.model.notification.NotificationStatus;

import java.time.LocalDateTime;

/**
 * Created by vitaly on 2017-04-20.
 */
public class NotificationDto {
    private long id;
    private LocalDateTime creationDateTime;
    private NotificationStatus status;
    private String header;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
