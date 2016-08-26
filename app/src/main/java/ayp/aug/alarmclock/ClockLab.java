package ayp.aug.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ayp.aug.alarmclock.ClockDbSchema.ClockTable;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class ClockLab {
    private Context context;
    private SQLiteDatabase database;

    private static ClockLab instance;  //Bind with class

    public static ClockLab getInstance(Context context) {
        if (instance == null) {
            instance = new ClockLab(context);
        }
        return instance;
    }

    private ClockLab(Context context) {
        this.context = context.getApplicationContext();
        ClockBaseHelper clockBaseHelper = new ClockBaseHelper(context);
        database = clockBaseHelper.getWritableDatabase();
    }

    public static ContentValues getContentValues(Clock clock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClockTable.Cols.UUID, clock.getUuid().toString());
        contentValues.put(ClockTable.Cols.TITLE, clock.getTitle());
        contentValues.put(ClockTable.Cols.TIME, clock.getTime().getTime()); // type long
        contentValues.put(ClockTable.Cols.DESC, clock.getDescription());
        contentValues.put(ClockTable.Cols.TICK, (clock.isNoti())?1:0);
        return contentValues;
    }

    public void addClock(Clock clock) {
        ContentValues contentValues = getContentValues(clock);
        database.insert(ClockTable.NAME,null,contentValues);
    }

    public void deleteClock(UUID uuid) {
        database.delete(ClockTable.NAME,ClockTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

    }

    public ClockWrapper queryClocks(String whereCause, String[] whereArgs){
        Cursor cursor = database.query(ClockTable.NAME,null,whereCause,whereArgs,null,null,null);
        return new ClockWrapper(cursor);
    }
    public void updateClock(Clock clock){
        String uuidStr = clock.getUuid().toString();
        ContentValues contentValues = getContentValues(clock);
        database.update(ClockTable.NAME,contentValues,ClockTable.Cols.UUID + "= ?",new String[]{uuidStr});
    }

    public List<Clock> getClock() {
        List<Clock> crimes = new ArrayList<>();
        ClockWrapper cursor = queryClocks(null,null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getClock());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return crimes;
    }

    public Clock getClockById(UUID uuid) {
        ClockWrapper cursor = queryClocks(ClockTable.Cols.UUID + " = ?",new String[]{uuid.toString()});

        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getClock();
        }finally {
            cursor.close();
        }

    }

}
