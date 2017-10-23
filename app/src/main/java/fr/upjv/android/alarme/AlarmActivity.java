package fr.upjv.android.alarme;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class AlarmActivity extends Activity {

    private static final PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
            .appendMinutes()
            .appendSeparator(":")
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendSeconds()
            .appendSeparator(".")
            .appendMillis3Digit()
            .toFormatter();

    public static final String EXTRA_DELAY = "fr.upjv.android.alarme.EXTRA_DELAY";

    public static final String EXTRA_MODE = "fr.upjv.android.alarme.EXTRA_MODE";

    private TextView textView;

    private CountDownTask countDownTask;

    private MediaPlayer mediaPlayer = null;

    private Duration delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        long millisDelay;

        millisDelay = getIntent().getLongExtra(EXTRA_DELAY, -1L);

        if (millisDelay < 0L) {
            finish();
            return;
        }

        delay = new Duration(millisDelay);

        setContentView(R.layout.activity_alarm);

        textView = findViewById(R.id.textView);

        displayRemainingTime(delay.toPeriod());

        countDownTask = new CountDownTask();
        countDownTask.setListener(new CountDownTask.CountDownListener() {
            @Override
            public void onCountDownUpdate(Period periodUntilFinish) {
                displayRemainingTime(periodUntilFinish);
            }

            @Override
            public void onCountDownDone() {
                displayRemainingTime(Period.ZERO);
                alarm();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        countDownTask.execute(delay);
    }

    @Override
    protected void onStop() {
        super.onStop();
        countDownTask.cancel(true);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void displayRemainingTime(Period periodUntilFinish) {
        textView.setText(PERIOD_FORMATTER.print(periodUntilFinish));
    }

    private void alarm() {
        switch (getIntent().getStringExtra(EXTRA_MODE)) {
            case "Toast":
                Toast.makeText(this, "C'est l'heure", Toast.LENGTH_LONG).show();
                break;
            case "Vibration":
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(new long[]{500, 500, 500, 500, 500}, -1);
                break;
            case "Musique":
                mediaPlayer = MediaPlayer.create(this, R.raw.happiness);
                mediaPlayer.start();
                break;
        }
    }
}
