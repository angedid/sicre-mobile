package cm.mindef.sed.sicre.mobile;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by nkalla on 06/11/17.
 */

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here

        Log.e("LONG JOBBBBBBBBB", "LONG JOB LONG JOB LONG JOB LONG JOB LONG JOB LONG JOB LONG JOB LONG JOB ");

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}