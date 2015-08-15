package pe.test.alarmmanagerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by alumno on 8/15/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public final static String TAG= "AlarmBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case MainActivity.ACTION_ALARM_FIRED:
                Log.d(TAG,"Alarm Fired!");
                Toast.makeText(context, R.string.triggered_alarm, Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d(TAG, "Intent with no action!");
                break;
        }
    }
}
