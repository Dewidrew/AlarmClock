package ayp.aug.alarmclock;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class Clock {
    private UUID uuid;
    private String title;
    private String description;
    private Date time;
    private boolean noti;

    public Clock(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isNoti() {
        return noti;
    }

    public void setNoti(boolean noti) {
        this.noti = noti;
    }
}
