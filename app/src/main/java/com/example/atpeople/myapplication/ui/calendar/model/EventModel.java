package com.example.atpeople.myapplication.ui.calendar.model;

import java.util.Date;

import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.data.entity.property.EventProperties;

/**
 * Create by peng on 2020/3/3
 */
public class EventModel implements Event{
    private long id = System.currentTimeMillis();
    private String title = "Event #" + String.valueOf(id);
    private String description = "";
    private Date originalStartDate = new Date();
    private Date startDate = (Date) originalStartDate.clone();
    private Date endDate = new Date();
    private long st_time=0;
    public EventModel() {

    }

    public EventModel(Event event) {
        this.id = event.getEventId();
        this.title = event.getEventTitle();
        this.description = event.getEventDescription();
        this.originalStartDate = event.getOriginalEventStartDate();
        this.startDate = event.getEventStartDate();
        this.endDate = event.getEventEndDate();
    }


    @Override
    public Long getEventId() {
        return id;
    }

    @Override
    public String getEventTitle() {
        return title;
    }

    @Override
    public String getEventDescription() {
        return description;
    }

    @Override
    public Date getOriginalEventStartDate() {
        return originalStartDate;
    }

    @Override
    public Date getEventStartDate() {
        return startDate;
    }

    @Override
    public Date getEventEndDate() {
        return endDate;
    }

    @Override
    public void setEventStartDate(Date startEventDate) {
        this.startDate = startEventDate;
    }

    @Override
    public void setEventEndDate(Date endEventDate) {
        this.endDate = endEventDate;
    }

    @Override
    public void setCalendarId(Long calendarId) {

    }

    @Override
    public Long getCalendarId() {
        return 0l;
    }

    @Override
    public int getEventRepeatPeriod() {
        return EventProperties.EVERY_WEEK;
    }

    @Override
    public String getEventIconUrl() {
        return "";
    }

    @Override
    public Boolean isEditable() {
        return false;
    }

    @Override
    public Boolean isAllDayEvent() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getEventId() == 0) ? 0 : Long.valueOf(this.getEventId()).hashCode());
        result = prime * result + ((this.getEventStartDate() == null) ? 0 : this.getEventStartDate().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (((Object) this).getClass() != obj.getClass())
            return false;

        EventModel other = (EventModel) obj;

        if (this.getEventId() == 0) {
            if (other.getEventId() != 0)
                return false;
        } else if (getEventId() != other.getEventId())
            return false;

        if (this.getEventStartDate() == null) {
            if (other.getEventStartDate() != null)
                return false;
        } else if (!getEventStartDate().equals(other.getEventStartDate()))
            return false;

        return true;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getSt_time() {
        return st_time;
    }

    public void setSt_time(long st_time) {
        this.st_time = st_time;
    }
}
