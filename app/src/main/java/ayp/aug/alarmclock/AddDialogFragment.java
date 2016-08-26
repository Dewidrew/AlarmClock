package ayp.aug.alarmclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Hattapong on 8/24/2016.
 */
public class AddDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    private static final String TAG = "AddDialogFragment";

    public static AddDialogFragment newInstance() {

        Bundle args = new Bundle();

        AddDialogFragment fragment = new AddDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callback{
        void sendData(Clock clock);
    }

    private TimePicker _timePicker;
    private EditText title,desc;
    private Callback callback;
    private Clock clock;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_clock,null);
        callback = (Callback)getActivity();
        title = (EditText)v.findViewById(R.id.add_clock_title);
        desc = (EditText)v.findViewById(R.id.add_clock_description);
        clock = new Clock(UUID.randomUUID());
        clock.setTime(new Date());

        _timePicker = (TimePicker)v.findViewById(R.id.time_picker);
        _timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hh, int mm) {
                Log.d(TAG,hh+":"+mm);

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setPositiveButton("OK",this);
        builder.setNegativeButton("CANCAL",null);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendar.set(Calendar.MINUTE,_timePicker.getMinute());
            calendar.set(Calendar.HOUR,_timePicker.getHour());
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.AM_PM,_timePicker.getBaseline());
            Log.d(TAG,calendar.getTime().toString());
            clock.setTime(calendar.getTime());
        }
        clock.setDescription(desc.getText().toString());
        clock.setNoti(true);

        Log.d(TAG,formatClock(clock.getTime()));

        clock.setTitle(title.getText().toString());
        ClockLab.getInstance(getActivity()).addClock(clock);
        callback.sendData(clock);

    }
    private String formatClock(Date time) {
        Log.d(TAG,"Time Before format: " + time.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.US);
        return sdf.format(time);

    }
}
