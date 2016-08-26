package ayp.aug.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ayp.aug.alarmclock.ClockDbSchema.ClockTable;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class ClockBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE = "clockBase.db";
    public ClockBaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ClockTable.NAME
                + "("
                + "_id integer primary key autoincrement , "
                + ClockTable.Cols.UUID + " , "
                + ClockTable.Cols.TITLE + " , "
                + ClockTable.Cols.TIME + " , "
                + ClockTable.Cols.TICK + " , "
                + ClockTable.Cols.DESC + " )"
        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
