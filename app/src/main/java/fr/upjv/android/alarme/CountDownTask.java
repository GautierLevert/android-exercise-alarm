package fr.upjv.android.alarme;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Period;

public class CountDownTask extends AsyncTask<Duration, Period, Void> {

    public interface CountDownListener {
        void onCountDownUpdate(Period periodUntilFinish);

        void onCountDownDone();
    }

    private Instant start;

    private CountDownListener listener;

    public void setListener(CountDownListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Duration... params) {
        Instant end = start.plus(params[0]);
        try {
            while (!isCancelled() && end.isAfterNow()) {
                publishProgress(new Period(Instant.now(), end));
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Log.i("COUNT_DOWN", "interrupted");
        } catch (Exception e) {
            Log.e("COUNT_DOWN", "error", e);
        }
        return null;
    }

    @Override
    //le execute dans le thread principale
    protected void onPreExecute() {
        super.onPreExecute();
        start = Instant.now();
    }

    @Override
    protected void onProgressUpdate(Period... values) {
        super.onProgressUpdate(values);
        if (listener != null) {
            listener.onCountDownUpdate(values[0]);
        }
    }

    @Override
    //l'execute dans le thread principale
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null) {
            listener.onCountDownDone();
        }
    }
}
