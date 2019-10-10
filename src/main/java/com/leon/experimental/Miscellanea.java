package com.leon.experimental;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Miscellanea {

    public static void main(String[] args) throws IOException, ParseException {
        // 1.
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        int numberOfParts = 3;
        List<List<Integer>> result = new ArrayList<>(numberOfParts);
        int size = list.size();
        int batchSize = (int) Math.ceil(((double) size) / numberOfParts);
        int leftElements = size;
        int i = 0;
        while (i < size && numberOfParts != 0) {
            result.add(list.subList(i, i + batchSize));
            i = i + batchSize;
            leftElements = leftElements - batchSize;
            batchSize = (int) Math.ceil(((double) leftElements) / --numberOfParts);
        }
        result.forEach(System.out::println);

        // 2.
        System.out.println("\uD835\uDD38");

        // 3.
        String[] temp = "SWITZERLAND33808,\"Antrona, paese\",46.060558,8.113673,,,\"1300001\"".split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String s : temp) {
            System.out.println(s);
        }

        // 4.
        Calendar cal = Calendar.getInstance();
        String dateStr;
        try (ZipFile gtfsFile = new ZipFile("F:\\Transit Data\\GTFS\\ito-ch\\av_switzerland_gtfs_20190416.zip")) {
            ZipEntry zipEntry;
            for (Enumeration<? extends ZipEntry> e = gtfsFile.entries(); e.hasMoreElements(); ) {
                zipEntry = e.nextElement();
                cal.setTimeInMillis(zipEntry.getTime());
                dateStr = String.format("%tc", cal);
                System.out.println(zipEntry.getName() + " " + dateStr);
            }
        }

        // 5.
        SimpleDateFormat completeDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat compactDateFormat = new SimpleDateFormat("yyyyMMdd");

        Document document = Jsoup.connect("http://download.geofabrik.de/europe/switzerland.html").get();
        Element table = document.selectFirst("#details > table");
        Elements rows = table.select("tr");
        rows.remove(0);

        Elements cols;
        for (Element row : rows) {
            cols = row.select("td");

            if ("switzerland-latest.osm.pbf".equalsIgnoreCase(cols.get(0).text())) {
                System.out.println(
                        compactDateFormat.format(
                                completeDateFormat.parse(cols.get(1).text()).getTime()
                        )
                );
            }
        }

        // 6.
        String input = "LINESTRING(6.142455 46.21064,6.093893 46.220833,6.076914 46.222393,6.065739 46.221325,6.037505 46.214283,6.017554 46.187836,5.961157 46.146084,5.82597 46.11092)";
        Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group(1).replaceAll(",", ";").replaceAll(" ", ","));
        }
    }

}
