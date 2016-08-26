package ayp.aug.alarmclock;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.sql.Date;
import java.util.UUID;

import ayp.aug.alarmclock.ClockDbSchema.ClockTable;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class ClockWrapper extends CursorWrapper {
    public ClockWrapper(Cursor cursor) {
        super(cursor);
    }
    public Clock getClock(){
        String uuidString = getString(getColumnIndex(ClockTable.Cols.UUID));
        String title = getString(getColumnIndex(ClockTable.Cols.TITLE));
        long time = getLong(getColumnIndex(ClockTable.Cols.TIME));
        String desc = getString(getColumnIndex(ClockTable.Cols.DESC));
        int tick = getInt(getColumnIndex(ClockTable.Cols.TICK));


        Clock clock = new Clock(UUID.fromString(uuidString));
        clock.setTitle(title);
        clock.setTime(new Date(time));
        clock.setDescription(desc);
        clock.setNoti(tick == 1);
        return clock;
    }
}
