package com.leon.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.utils.FileUtils;
import lombok.Data;

import java.util.List;

public class OtpMapper {

    public static void main(String[] args) throws Exception {
        String input = FileUtils.readFileFromResources("otp.json");

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.readValue(input, Wrapper.class));
    }

    @Data
    static class Wrapper {

        private OtpResponse data;

        public Wrapper() {

        }

    }

    @Data
    static class OtpResponse {

        private OtpStop stop;

        public OtpResponse() {

        }

    }

    @Data
    static class OtpStop {

        private String gtfsId;

        private String name;

        private double lat;

        private double lon;

        private OtpCluster cluster;

        private String platformCode;

        private String locationType;

        private String wheelchairBoarding;

        private List<OtpRoute> routes;

        public OtpStop() {

        }

    }

    @Data
    static class OtpCluster {

        private String gtfsId;

        public OtpCluster() {

        }

    }

    @Data
    static class OtpRoute {

        private String type;

        private String shortName;

        private String color;

        private String textColor;

        public OtpRoute() {

        }

    }

}