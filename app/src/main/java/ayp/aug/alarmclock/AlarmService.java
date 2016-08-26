package ayp.aug.alarmclock;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Hattapong on 8/26/2016.
 */
public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    private static final String UUID_KEY = "UUID_KEY";


    public static Intent newIntent(Context context,UUID uuid){
        Intent i = new Intent(context,AlarmService.class);
        i.putExtra(UUID_KEY,uuid);
        return i;
    }

    public AlarmService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UUID uuid = (UUID)intent.getExtras().getSerializable(UUID_KEY);
        Log.d(UUID_KEY,"ID :"+uuid);
        Intent i = AlarmPageActivity.newIntent(this,uuid);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this);
        notiBuilder.setTicker("Alarm");
        notiBuilder.setSmallIcon(android.R.drawable.ic_menu_report_image);
        notiBuilder.setContentTitle("Alarm");
        notiBuilder.setContentText("Alarm");
        notiBuilder.setContentIntent(pi);
        notiBuilder.setContentInfo("Hello");
        notiBuilder.setAutoCancel(true);

        Notification notification = notiBuilder.build();
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(0,notification);

        PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);

        if(!powerManager.isScreenOn()) {
            PowerManager.WakeLock wl = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "SCREEN");
            wl.acquire();

            KeyguardManager km=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl=km.newKeyguardLock("ShowEvent");
            kl.disableKeyguard();
        }
        startActivity(AlarmPageActivity.newIntent(this,uuid));

    }
    public static void setServiceAlarm(Context c,Boolean isOn,Clock clocks){
        Intent i = AlarmService.newIntent(c,clocks.getUuid());
        PendingIntent pi = PendingIntent.getService(c,0,i,0);

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        if(isOn){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP,                 // MODE
                        clocks.getTime().getTime(),                   // Start Time
                                                                        // Interval
                        pi);                                          // Pending action(Intent)
                Log.d(TAG,"Run by Alarm Manager");
            }else {
                c.startService(AlarmService.newIntent(c,clocks.getUuid()));
                Log.d(TAG,"Run by Scheduler");
            }
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                am.cancel(pi);
                pi.cancel();
            }else{
                c.startService(AlarmService.newIntent(c,clocks.getUuid()));
            }
        }
    }

}
