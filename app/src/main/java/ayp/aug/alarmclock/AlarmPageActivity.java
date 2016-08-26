package ayp.aug.alarmclock;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Hattapong on 8/26/2016.
 */
public class AlarmPageActivity extends AppCompatActivity{
    private static final String UUID_KEY = "UUID_KEY";
    private static final String TAG = "AlarmPageActivity";
    protected Ringtone ringtone;
    private Button stopBtn;
    private TextView time;
    private Clock clock;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_page);
        Log.d(TAG,"onCreate");

        UUID uuid = (UUID) getIntent().getExtras().getSerializable(UUID_KEY);
        clock = ClockLab.getInstance(this).getClockById(uuid);
        context = this;


        time = (TextView)findViewById(R.id.alarm_time);
        time.setText(formatClock(clock.getTime()));
        stopBtn = (Button)findViewById(R.id.alarm_stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ringtone != null){
                    ringtone.stop();
                    clock.setNoti(false);
                    AlarmService.setServiceAlarm(context,false,clock);
                    ClockLab.getInstance(context).updateClock(clock);
                    startActivity(new Intent(context,MainActivity.class));
                }
            }
        });
        Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if(notif==null){
            notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if(notif==null){
                notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notif);
        ringtone.play();
    }

    public static Intent newIntent(Context alarmService,UUID uuid) {
        Intent i =  new Intent(alarmService,AlarmPageActivity.class);
        i.putExtra(UUID_KEY,uuid);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }
    private String formatClock(Date time) {
        Log.d(TAG,"Time Before format: " + time.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.US);
        return sdf.format(time);

    }
}
