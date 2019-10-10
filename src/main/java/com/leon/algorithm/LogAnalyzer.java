package com.leon.algorithm;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer {

    private static final String TIMESTAMP_REGEX = "(?<timestamp>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})"; // "(?<timestamp>\\d{2}:\\d{2}:\\d{2}.\\d{3})"

    private static final String THREAD_REGEX = "\\[(?<thread>[^]]+)]";

    private static final String LEVEL_REGEX = "(?<level>INFO|ERROR|WARN|TRACE|DEBUG|FATAL)";

    private static final String CLASS_REGEX = "(?<class>[^)]+)"; // "\\((?<class>[^)]+)\\)"

    private static final String TEXT_REGEX = "(?<text>.*?)(?=\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}|\\Z)";

    private static final Pattern LOG_PATTERN = Pattern.compile(TIMESTAMP_REGEX + " " + THREAD_REGEX + " " + LEVEL_REGEX + "\\s+" + CLASS_REGEX + " - " + TEXT_REGEX, Pattern.DOTALL);

    public static void main(String[] args) {
        try (FileInputStream inputStream = new FileInputStream("C:\\Users\\ntcong\\Downloads\\transit-shapes-job-build-shapes-ito-ch-latest-32_log.txt");
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {

            //Map<Integer, Integer> summaries = new TreeMap<>();
            int timeout = 0, onTime = 0, totalTime = 0;

            Matcher matcher;
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                matcher = LOG_PATTERN.matcher(line);
                if (matcher.find()) {
                    if (matcher.group("text").contains("to complete the routing request")) {
                        int time = Integer.parseInt(matcher.group("text").replaceAll("\\D+", ""));
                        //summaries.merge(time, 1, Integer::sum);
                        if (time >= 300000) {
                            timeout++;
                        } else {
                            onTime++;
                            totalTime += time;
                        }
                    }
                }
            }

            //System.out.println("Result: " + summaries);
            //System.out.println("Total: " + summaries.values().stream().mapToInt(Integer::intValue).sum());
            System.out.println("Timeout: " + timeout);
            System.out.println("OnTime: " + onTime);
            System.out.println("TotalTime: " + totalTime);
            System.out.println("Avg: " + (totalTime * 1.0) / onTime);

            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
