package ayp.aug.alarmclock;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddDialogFragment.Callback {
    RecyclerView mRecyclerView;
    ClockAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                FragmentManager fm = getSupportFragmentManager();
                AddDialogFragment adf = AddDialogFragment.newInstance();
                adf.show(fm, "Add Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void sendData(Clock clock) {
        AlarmService.setServiceAlarm(this,clock.isNoti(),clock);
        updateUI();
    }

    class ClockAdapter extends RecyclerView.Adapter<ClockHolder> {
        private Context activity;
        private List<Clock> clockList;

        public ClockAdapter(Context activity, List<Clock> clockList) {
            this.activity = activity;
            this.clockList = clockList;
        }

        @Override
        public ClockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(activity).inflate(R.layout.list_item_clock, parent, false);

            return new ClockHolder(v, activity);
        }

        @Override
        public void onBindViewHolder(ClockHolder holder, int position) {
            holder.bind(clockList.get(position));
        }

        @Override
        public int getItemCount() {
            return clockList.size();
        }

        public void setClock(List<Clock> clock) {
            clockList = clock;
        }
    }

    private class ClockHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ClockHolder";
        private TextView title, time;
        private SwitchCompat switchCompat;
        private Context ctx;

        public ClockHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            title = (TextView) itemView.findViewById(R.id.list_title);
            time = (TextView) itemView.findViewById(R.id.list_time);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.list_check);
        }

        public void bind(final Clock clock) {
            title.setText(clock.getTitle());
            time.setText(formatClock(clock.getTime()));
            switchCompat.setChecked(clock.isNoti());
            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    clock.setNoti(b);
                    AlarmService.setServiceAlarm(ctx,b,clock);
                    ClockLab.getInstance(ctx).updateClock(clock);
                }
            });
        }

        private String formatClock(Date time) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            return sdf.format(time);

        }

    }

    public void updateUI() {
        final ClockLab clockLab = ClockLab.getInstance(this);

        List<Clock> clocks = clockLab.getClock(); // create list

        if (_adapter == null) {
            _adapter = new ClockAdapter(this,clocks); // set list to Adapter
            mRecyclerView.setAdapter(_adapter); // set Adapter to recycleview

        } else {
            _adapter.setClock(clockLab.getClock());
            _adapter.notifyDataSetChanged();
        }

    }


}
