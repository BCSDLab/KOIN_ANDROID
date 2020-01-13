package in.koreatech.koin.util;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

public class FirebasePerformanceUtil {
    Trace trace;

    public FirebasePerformanceUtil(String name) {
        this.trace = FirebasePerformance.getInstance().newTrace(name);
    }

    public void start() {
        trace.start();
    }

    public void stop() {
        trace.stop();
    }

    public void incrementMetric(String name, long number) {
        trace.incrementMetric(name, number);
    }

}
