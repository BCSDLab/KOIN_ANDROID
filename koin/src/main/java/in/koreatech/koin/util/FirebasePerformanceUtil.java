package in.koreatech.koin.util;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public class FirebasePerformanceUtil {
    Trace mTrace;

    public FirebasePerformanceUtil(String name) {
        this.mTrace = FirebasePerformance.getInstance().newTrace(name);
    }

    public void start() {
        mTrace.start();
    }

    public void stop() {
        mTrace.stop();
    }

    public void incrementMetric(String name, long number) {
        mTrace.incrementMetric(name, number);
    }

}
