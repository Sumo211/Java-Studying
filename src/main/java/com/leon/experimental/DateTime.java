package com.leon.experimental;

import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime {

    public static void main(String[] args) {
        // 1.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).withZone(ZoneId.systemDefault());
        System.out.println(Instant.from(formatter.parse("2013-01-07 15:49:26")).toEpochMilli());

        // 2.
        org.joda.time.DateTime dateTime = org.joda.time.DateTime.now();
        org.joda.time.format.DateTimeFormatter anotherFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(dateTime.toString(anotherFormatter));

        // 3.
        long current = Instant.now().getEpochSecond();
        DateTimeFormatter yetAnotherFormatter = DateTimeFormatter.ISO_DATE_TIME
                .withZone(ZoneOffset.UTC)
                .withLocale(Locale.ENGLISH);
        System.out.println(yetAnotherFormatter.format(Instant.ofEpochSecond(current)));

        // 4.
        org.joda.time.DateTime now = org.joda.time.DateTime.now();
        Date temp = now.toDate();
        System.out.println(now.getMillis() + " : " + temp.getTime());

        // 5.
        System.out.println(ZoneId.of("Europe/Berlin").getRules().getOffset(Instant.now()));

        // 6.
        System.out.println(new Date().getTime());

        // 7.
        OffsetDateTime start = OffsetDateTime.of(2018, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime end = OffsetDateTime.of(2019, 4, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        System.out.println(ChronoUnit.DAYS.between(start, end));
    }

    // 8.
    //yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
