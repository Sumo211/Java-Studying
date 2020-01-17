package com.leon.experimental;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.utils.FileUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class EmployeeCounter {

    private static Map<String, List<String>> result = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // https://www.geeksforgeeks.org/find-number-of-employees-under-every-manager
        String input = FileUtils.readFileFromResources("users.json");

        ObjectMapper mapper = new ObjectMapper();
        List<Employee> employees = mapper.readValue(input, new TypeReference<>() {
        });

        Map<String, List<String>> tmp = employees.stream().collect(groupingBy(Employee::getLoginName, mapping(Employee::getReportTo, toList())));

        tmp.keySet().forEach(e -> populateResult(e, tmp));
        System.out.println(result);
    }

    private static List<String> populateResult(String manager, Map<String, List<String>> tmp) {
        if (!tmp.containsKey(manager)) {
            result.put(manager, new ArrayList<>());
            return new ArrayList<>();
        }

        List<String> employees;
        if (result.containsKey(manager)) {
            employees = result.get(manager);
        } else {
            List<String> currentEmployees = tmp.get(manager);
            employees = new ArrayList<>(currentEmployees);
            for (String e : currentEmployees) {
                employees.addAll(populateResult(e, tmp));
            }

            result.put(manager, employees);
        }

        return employees;
    }

    @Data
    static class Employee {

        private String loginName;

        private String reportTo;

        public Employee() {

        }

    }

}