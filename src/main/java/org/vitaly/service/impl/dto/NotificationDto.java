package org.vitaly.service.impl.dto;

import org.vitaly.model.notification.NotificationStatus;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDto that = (NotificationDto) o;

        return id == that.id
                && (Objects.equals(creationDateTime, that.creationDateTime))
                && status == that.status
                && Objects.equals(header, that.header)
                && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
