package util;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Time {
    private YearMonth time;
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("LLLL u");

    public Time(int year) {
        time = YearMonth.of(year, 1);
    }

    public Time(YearMonth ym) {
        time = ym;
    }

    public void pass() {
        time = time.plusMonths(1);
    }

    public Time plus(long l) {
        return new Time(time.plusMonths(l));
    }

    @Override
    public String toString() {
        return time.format(format);
    }
}
