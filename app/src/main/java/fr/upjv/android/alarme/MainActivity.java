package fr.upjv.android.alarme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import org.joda.time.Duration;

public class MainActivity extends Activity {

    private NumberPicker minutesPicker;

    private NumberPicker secondsPicker;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        minutesPicker = findViewById(R.id.minutesPicker);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(10);
        minutesPicker.setValue(1);

        secondsPicker = findViewById(R.id.secondsPicker);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setValue(30);

        spinner = findViewById(R.id.spinner);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AlarmActivity.class)
                        .putExtra(AlarmActivity.EXTRA_DELAY, parseMinutes().plus(parseSeconds()))
                        .putExtra(AlarmActivity.EXTRA_MODE, (String) spinner.getSelectedItem()));
            }
        });
    }

    public Duration parseMinutes() {
        return Duration.standardMinutes(minutesPicker.getValue());
    }

    public Duration parseSeconds() {
        return Duration.standardSeconds(secondsPicker.getValue());
    }
}
