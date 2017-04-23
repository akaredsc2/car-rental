package org.vitaly.model.notification;

import org.vitaly.model.Entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by vitaly on 2017-04-06.
 */
public class Notification implements Entity {
    private long id;
    private LocalDateTime creationDateTime;
    private NotificationStatus status;
    private String header;
    private String content;

    public Notification(Builder builder) {
        this.id = builder.id;
        this.creationDateTime = builder.creationDateTime;
        this.status = builder.status;
        this.header = builder.header;
        this.content = builder.content;
    }

    @Override
    public long getId() {
        return id;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Notification that = (Notification) o;

        return creationDateTime.until(that.creationDateTime, ChronoUnit.SECONDS) == 0
                && status == that.status
                && header.equals(that.header)
                && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + header.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", creationDateTime=" + creationDateTime +
                ", status=" + status +
                ", header='" + header + '\'' +
                ", content='" + content + '\'' +
                '}';
    }


    public static class Builder {
        private long id;
        private LocalDateTime creationDateTime;
        private NotificationStatus status;
        private String header;
        private String content;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setCreationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        public Builder setStatus(NotificationStatus status) {
            this.status = status;
            return this;
        }

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
