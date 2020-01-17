package com.leon.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.math.DoubleMath;
import com.leon.utils.FileUtils;
import lombok.*;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class JsonComparator {

    public static void main(String[] args) throws Exception {
        String v2Response = FileUtils.readFileFromResources("stops/ch-v2-stops.json");
        String v3Response = FileUtils.readFileFromResources("stops/ch-v3-stops.json");

        ObjectMapper objectMapper = new ObjectMapper();

        Wrapper v2Wrapper = objectMapper.readValue(v2Response, Wrapper.class);
        Wrapper v3Wrapper = objectMapper.readValue(v3Response, Wrapper.class);

        List<Map<String, Object>> v2RawStops = v2Wrapper.getData().getStops();
        List<Map<String, Object>> v3RawStops = v3Wrapper.getData().getStops();

        //System.out.println(v2RawStops.get(0));
        //System.out.println(v3RawStops.get(0));

        Set<Stop> v2Stops = v2RawStops.stream().map(JsonComparator::mapToStop).collect(toSet());
        Set<Stop> v3Stops = v3RawStops.stream().map(JsonComparator::mapToStop).collect(toSet());

        //System.out.println(v2Stops.size() + " || " + v3Stops.size());

        Set<Stop> tmp = v2Stops.stream().filter(stop -> !v3Stops.contains(stop)).collect(toSet());
        System.out.println(tmp.size());
        //tmp.stream().limit(10).forEach(System.out::println);

        //Stop tmp1 = Stop.builder().lat(46.2038703959152).lng(6.14862676084605).name("Genève, Molard").sourceId("8592862").supportedTransitModes(List.of("LIGHTRAIL", "BUS")).build();
        //Stop tmp2 = Stop.builder().lat(46.2038688659668).lng(6.148626804351807).name("Genève, Molard").sourceId("8592862").supportedTransitModes(List.of("BUS", "LIGHTRAIL")).build();
        //System.out.println(tmp1.equals(tmp2));

        //findByName(v2Stops, "Basel, Bahnhof St. Johann").ifPresent(System.out::println);
        //findByName(v3Stops, "Basel, Bahnhof St. Johann").ifPresent(System.out::println);

        //System.out.println(countBy(v2RawStops, "accessibility"));
        //System.out.println(countBy(v3RawStops, "accessibility"));
    }

    private static Map<String, Long> countBy(List<Map<String, Object>> source, String fieldName) {
        return source.stream()
                .map(entry -> entry.get(fieldName).toString())
                .collect(groupingBy(identity(), counting()));
    }

    @SuppressWarnings("unchecked")
    private static Stop mapToStop(Map<String, Object> source) {
        List<String> transitModes = Optional.ofNullable(source.get("supportedTransitModes"))
                .map(obj -> (List<String>) obj)
                .map(unsorted -> unsorted.stream().sorted().collect(toList()))
                .orElse(new ArrayList<>());

        List<ServiceDetails> serviceDetails = Optional.ofNullable(source.get("supportedServices"))
                .map(obj -> (List<Map<String, Object>>) obj)
                .map(obj -> obj.stream().map(container ->
                        ServiceDetails.builder()
                                .transitMode(container.getOrDefault("transitMode", "").toString())
                                .serviceShortName(container.getOrDefault("serviceShortName", "").toString())
                                .color(container.getOrDefault("color", "").toString())
                                .textColor(container.getOrDefault("textColor", "").toString())
                                .build())
                        .sorted(Comparator.comparing(ServiceDetails::getTransitMode).thenComparing(ServiceDetails::getServiceShortName))
                        .collect(toList()))
                .orElse(new ArrayList<>());

        return Stop.builder()
                .lat((double) source.get("lat"))
                .lng((double) source.get("lon"))
                .name(source.get("name").toString())
                .sourceId(source.get("sourceId").toString())
                .supportedTransitModes(transitModes)
                .groupOfServiceDetails(serviceDetails)
                .build();
    }

    private static Optional<Stop> findByName(Set<Stop> source, String name) {
        return source.stream()
                .filter(stop -> name.equals(stop.getName()))
                .findFirst();
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class Wrapper {

        private boolean success;

        private Payload data;

    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class Payload {

        private List<Map<String, Object>> stops;

    }

    @Data
    @Builder
    static class Stop {

        private double lat;

        private double lng;

        private String name;

        private String sourceId;

        private List<String> supportedTransitModes;

        private List<ServiceDetails> groupOfServiceDetails;

        @Override
        public boolean equals(Object o) {
            double tolerance = 1e-5;
            if (o == this) return true;
            if (!(o instanceof Stop)) return false;
            Stop stop = (Stop) o;
            return DoubleMath.fuzzyEquals(stop.getLat(), this.lat, tolerance) &&
                    DoubleMath.fuzzyEquals(stop.getLng(), this.lng, tolerance) &&
                    stop.getName().equals(this.name) &&
                    stop.getSourceId().equals(this.sourceId) &&
                    stop.getSupportedTransitModes().equals(this.getSupportedTransitModes()) &&
                    stop.getGroupOfServiceDetails().equals(this.groupOfServiceDetails);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, sourceId, supportedTransitModes);
        }

    }

    @Data
    @Builder
    static class ServiceDetails {

        private String transitMode;

        private String serviceShortName;

        private String color;

        private String textColor;

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof ServiceDetails)) return false;
            ServiceDetails serviceDetails = (ServiceDetails) o;
            return serviceDetails.getTransitMode().equals(this.transitMode) &&
                    serviceDetails.getServiceShortName().equals(this.serviceShortName) &&
                    serviceDetails.getColor().equals(this.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(transitMode, serviceShortName, color);
        }

    }

}