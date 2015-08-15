package pe.test.alarmmanagerapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView alarmStatusTextVIew;
    private AlarmManager alarmManager;
    private AlarmBroadcastReceiver alarmBroadcastReceiver;

    private class AlarmBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
    public static final String ACTION_ALARM_FIRED = "pe.test.alarmmanagerapp.ALARM_FIRED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmStatusTextVIew = (TextView) findViewById(R.id.textview_alarm_status);
        setAlarmStatusTextVIew(isAlarmSet());
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmBroadcastReceiver = new AlarmBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ALARM_FIRED);
        registerReceiver(alarmBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(alarmBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_alarm:
                //tiene que saber si la alarma está desactivada
                if (isAlarmSet()) {
                    stopAlarm();
                    setAlarmStatusTextVIew(false);
                    item.setTitle(R.string.set_alarm);
                } else {
                    setAlarm(10);
                    setAlarmStatusTextVIew(true);
                    item.setTitle(R.string.cancel_alarm);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setAlarm(final int timeInSeconds) {

        //atento al flag
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, getAlarmIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        final long realtimeTriggerAtMillis = SystemClock.elapsedRealtime() + timeInSeconds * 1000;
        //QUeremos que nuestra alarma se ejecute de forma exacta, para esto tenemos
        //dos alternativas:
        //-Podemos poner al Target SDK a una versión menor al API 29 (KITKAT) a partir de esta versión
        //se cambió el comportamiento  del método set() del AlarmManager.
        //
        //-sin embargo vamos a mantener el targetsdk a la última vertsion posible y
        //vamos a preguntar qué versión estamos ejecutando ahora para seleccionar
        //el mètodo adecuado, esto con la finalidad de evitar que la aplicación se ejecute en modo
        //compatibilidad en las versiones más nuevas

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, realtimeTriggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, realtimeTriggerAtMillis, pendingIntent);
        }

    }

    private void stopAlarm() {

        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, getIntent(), PendingIntent.FLAG_CANCEL_CURRENT));

    }

    private boolean isAlarmSet() {
        return PendingIntent.getBroadcast(this, 0, getIntent(), PendingIntent.FLAG_NO_CREATE) != null;
    }

    private Intent getAlarmIntent() {
        return new Intent(ACTION_ALARM_FIRED);

    }

    private void setAlarmStatusTextVIew(final boolean isAlarmSet) {
        if (isAlarmSet) {
            alarmStatusTextVIew.setText(getString(R.string.alarm_status, getString(R.string.alarm_status_set)));
        } else {
            alarmStatusTextVIew.setText(getString(R.string.alarm_status, getString((R.string.alarm_status_not_set))));
        }
    }


}
