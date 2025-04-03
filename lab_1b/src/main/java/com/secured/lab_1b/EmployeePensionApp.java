package com.secured.lab_1b;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.secured.lab_1b.model.Employee;
import com.secured.lab_1b.model.PensionPlan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeePensionApp {
    private static final List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        loadEmployees();
        printAllEmployees();
        printQuarterlyUpcomingEnrollees();
    }

    private static void loadEmployees() {
        employees.add(new Employee(1, "Daniel", "Agar",
                LocalDate.of(2018, 1, 17), 105945.50, null));
        employees.add(new Employee(2, "Benard", "Shaw",
                LocalDate.of(2022, 9, 3), 197750.00,
                new PensionPlan("EX1089", LocalDate.of(2023, 1, 17), 100.00)));
        employees.add(new Employee(3, "Carly", "Agar",
                LocalDate.of(2014, 5, 16), 842000.75,
                new PensionPlan("SM2307", LocalDate.of(2019, 11, 4), 1555.50)));
        employees.add(new Employee(4, "Wesley", "Schneider",
                LocalDate.of(2022, 7, 21), 74500.00, null));
        employees.add(new Employee(5, "Anna", "Wiltord",
                LocalDate.of(2022, 6, 15), 85750.00, null));
        employees.add(new Employee(6, "Yosef", "Tesfalem",
                LocalDate.of(2022, 10, 31), 100000.00, null));
    }

    private static void printAllEmployees() throws Exception {
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparingDouble((Employee e) -> -e.getYearlySalary())
                        .thenComparing(e -> e.getLastName()))
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println("All Employees:");
        System.out.println(objectMapper.writeValueAsString(sortedEmployees));
    }

    private static void printQuarterlyUpcomingEnrollees() throws Exception {
        LocalDate now = LocalDate.now();
        LocalDate nextQuarterStart = now.plusMonths(3 - (now.getMonthValue() - 1) % 3).withDayOfMonth(1);
        LocalDate nextQuarterEnd = nextQuarterStart.plusMonths(3).minusDays(1);

        List<Employee> enrollees = employees.stream()
                .filter(e -> e.getPensionPlan() == null)
                .filter(e -> e.getEmploymentDate().plusYears(3).isAfter(nextQuarterStart.minusDays(1)) &&
                        e.getEmploymentDate().plusYears(3).isBefore(nextQuarterEnd.plusDays(1)))
                .sorted(Comparator.comparing(Employee::getEmploymentDate).reversed())
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println("Quarterly Upcoming Enrollees:");
        System.out.println(objectMapper.writeValueAsString(enrollees));
    }
}