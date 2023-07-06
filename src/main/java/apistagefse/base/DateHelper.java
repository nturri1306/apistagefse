package apistagefse.base;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

/**
 * Version: 1.0.1 (Connectathon 2019 improvements)
 *
 * @author b.amoruso
 */
public class DateHelper {

    public static final String DATE_PATTERN = "yyyyMMdd";
    public static final String TIME_PATTERN = "HHmmss";
    public static final String DATETIME_SHORT_PATTERN = DATE_PATTERN + "HHmm";
    public static final String DATETIME_PATTERN = DATE_PATTERN + TIME_PATTERN;
    public static final String DATETIME_PATTERN_SECONDFRACTION = DATETIME_PATTERN + "SSSSSS";
    public static final String DATE_TIME_ZONE_PATTERN = DATETIME_PATTERN + "Z";
    public static final String ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss:SSSZ";

    public static String toString(Date date) {
        return toString(date, DATETIME_PATTERN);
    }

    public static String toString(Date date, String pattern) {
        return date != null && pattern != null ? new SimpleDateFormat(pattern).format(date) : null;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return (date != null) ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    public static LocalDate toLocalDate(Date date) {
        return (date != null) ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    /**
     * Convert a string to {@link LocalDate} first by processing a whole
     * DATE, second YEAR * MONTH and, finally, a DATE with the YEAR only.
     * The format of these values is defined as the following regular expression:
     * YYYY[MM[DD]]
     */
    public static LocalDate toLocalDate(String date) {
        try {
            return toLocalDate(date, DATE_PATTERN);
        } catch (DateTimeParseException ex1) {
            try {
                // Try by supposing month + year...
                return toLocalDate(date + "01", DATE_PATTERN);
            } catch (DateTimeParseException ex2) {
                // Try by supposing year only...
                return toLocalDate(date + "0101", DATE_PATTERN);
            }
        }
    }

    public static LocalDate toLocalDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return date != null && date.length() > 0 ? LocalDate.parse(date, formatter) : null;
    }

    /**
     * Convert a string to {@link LocalDateTime} first by processing a whole
     * DATE + TIME, second DATE + TIME without seconds, third by
     * processing a {@link toLocalDate} with hours, minutes and seconds
     * equals to zero
     * The format of these values is defined as the following regular expression:
     * YYYY[MM[DD[hh[mm[ss]]]]]
     */
    public static LocalDateTime toLocalDateTime(String date) {
        try {
            return toLocalDateTime(date, DATETIME_PATTERN);
        } catch (DateTimeParseException ex1) {
            try {
                // Try zulu time...
                return toLocalDateTime(date, DATE_TIME_ZONE_PATTERN);
            } catch (DateTimeParseException ex2) {
                try {
                    // Try just by removing seconds...
                    return toLocalDateTime(date, DATETIME_SHORT_PATTERN);
                } catch (DateTimeParseException ex3) {
                    // Try just the date...
                    return LocalDateTime.of(toLocalDate(date), LocalTime.of(0, 0, 0));
                }
            }
        }
    }

    public static LocalDateTime toLocalDateTime(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return (date != null && date.length() > 0) ? LocalDateTime.parse(date, formatter) : null;
    }

    public static String toString(LocalDate value) {
        return toString(value, (String) null);
    }

    public static String toString(LocalDate value, String pattern) {
        return value == null ? "" : value.format(
                pattern == null ? DateTimeFormatter.ISO_LOCAL_DATE :
                        DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(LocalDate value, Locale locale) {
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDate(
                FormatStyle.LONG).withLocale(locale);

        return value == null ? "" : value.format(f);
    }

    public static String toString(LocalDateTime value) {
        return toString(value, (String) null);
    }

    public static String toString(LocalDateTime value, String pattern) {
        return value == null ? "" : value.format(
                pattern == null ? DateTimeFormatter.ISO_LOCAL_DATE_TIME :
                        DateTimeFormatter.ofPattern(pattern));
    }

    public static String toString(ZonedDateTime value) {
        return value == null ? null :
                value.withZoneSameInstant(ZoneOffset.UTC).toString();
    }

    public static ZonedDateTime toZonedDateTime(String value) {
        if (value != null) {
            ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(value);
//				DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

            return zdtWithZoneOffset.withZoneSameInstant(ZoneOffset.UTC);
        }

        return null;
    }

    public static String toString(LocalDateTime value, Locale locale, String zone) {
        ZonedDateTime nowWithTimeZone = ZonedDateTime.of(value, ZoneId.of(zone));

        return DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.LONG).withLocale(locale).format(nowWithTimeZone);
    }

    public static Date toDate(LocalDate date) {
        return date != null ?
                Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static Date toDate(LocalDateTime date) {
        return date != null ?
                Date.from(date.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

}
