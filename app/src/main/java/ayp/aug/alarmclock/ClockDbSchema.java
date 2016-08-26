package ayp.aug.alarmclock;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class ClockDbSchema {
    public static final class ClockTable{
        public static final String NAME = "CLOCKDB";

        public static final class Cols{
            public static final String UUID = "UUID";
            public static final String TITLE = "title";
            public static final String TIME = "time";
            public static final String DESC = "desc";
            public static final String TICK = "checking";
        }
    }
}
