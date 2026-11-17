package Util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static LocalDate toLocalDate(Date data) {
        if (data == null) {
            return null;
        }
        if (data instanceof java.sql.Date) {
            return ((java.sql.Date) data).toLocalDate();
        }
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
