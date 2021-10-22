package sh.reece.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Timings {
    
    private final List<TimeRecord> times;
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.000", DecimalFormatSymbols.getInstance(Locale.US));


    public Timings(){
        times = new ArrayList<>();
    }

    public void start() {
        times.clear();
        info("start");
    }

    public void info(final String info) {
        if (!times.isEmpty() || "start".equals(info)) {
            times.add(new TimeRecord(info, System.nanoTime()));
        }
    }

    public String end() {
        final StringBuilder output = new StringBuilder();
        output.append("ServerTools Execution time:\n");
        String mark;
        long time0 = 0;
        long time1 = 0;
        long time2 = 0;
        double duration;

        for (final TimeRecord pair : times) {
            mark = pair.getInfo();
            time2 = pair.getTime();
            if (time1 > 0) {
                duration = (time2 - time1) / 1000000.0;
                output.append(mark).append(": ").append(decimalFormat.format(duration)).append("ms\n");
            } else {
                time0 = time2;
            }
            time1 = time2;
        }
        duration = (time1 - time0) / 1000000.0;
        output.append("STools Total: ").append(decimalFormat.format(duration)).append("ms\n");
        times.clear();
        return output.toString();
    }

    private static class TimeRecord {

        private final String information;
        private final long time;
    
        TimeRecord(final String info, final long time){
            this.information = info;
            this.time = time;
        }

        public String getInfo(){
            return information;
        }

        public long getTime(){
            return time;
        }
    }
}

