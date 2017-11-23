package com.home.pad.distrsys.dsl;

import com.home.pad.distrsys.model.Employee;
import com.home.pad.distrsys.net.Request;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class DslProcessor {
    private Logger logger = Logger.getLogger(DslProcessor.class.getName());

    private final Request request;
    private List<Employee> employees;

    public DslProcessor(Request request, List<Employee> employees) {
        this.request = request;
        this.employees = employees;
    }

    public List<Employee> execute() {
        try {
            filter();
            group();
            sort();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "DSL parsing error", e);
        }
        return employees;
    }

    public void sort() {
        if (request.getOrderBy() != null && !request.getOrderBy().isEmpty()) {
            switch (request.getOrderBy().toLowerCase()) {
                case "firstname":
                    employees.sort(Comparator.comparing(Employee::getFirstName));
                    break;
                case "lastname":
                    employees.sort(Comparator.comparing(Employee::getLastName));
                    break;
                case "salary":
                    employees.sort(Comparator.comparing(Employee::getSalary));
                    break;
                case "department":
                    employees.sort(Comparator.comparing(Employee::getDepartment));
                    break;
            }
        }
    }

    public void group() {
        if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
            switch (request.getGroupBy().toLowerCase()) {
                case "firstname": {
                    Map<String, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::getFirstName));
                    employees.clear();
                    for (Map.Entry<String, List<Employee>> e : collect.entrySet()) {
                        employees.addAll(e.getValue());
                    }
                    break;
                }
                case "lastname": {
                    Map<String, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::getLastName));
                    employees.clear();
                    for (Map.Entry<String, List<Employee>> e : collect.entrySet()) {
                        employees.addAll(e.getValue());
                    }
                    break;
                }
                case "department": {
                    Map<String, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
                    employees.clear();
                    for (Map.Entry<String, List<Employee>> e : collect.entrySet()) {
                        employees.addAll(e.getValue());
                    }
                    break;
                }
                case "salary": {
                    Map<Double, List<Employee>> collect = employees.stream().collect(Collectors.groupingBy(Employee::getSalary));
                    employees.clear();
                    for (Map.Entry<Double, List<Employee>> e : collect.entrySet()) {
                        employees.addAll(e.getValue());
                    }
                    break;
                }
            }
        }
    }


    public void filter() {
        Pattern pattern = Pattern.compile("(firstname|salary|lastname|department)+\\s*(==|!=|>|<|>=|<=)\\s*(\\w+|'.+')");
        if (request.getFilterBy() != null && !request.getFilterBy().isEmpty()) {
            Matcher m = pattern.matcher(request.getFilterBy());
            if(m.find()){
                String field = m.group(1);
                String operation = m.group(2);
                String value = m.group(3);
                switch (field.toLowerCase()) {
                    case "firstname": {
                        filterByName(operation, value.substring(1, value.length() - 1));
                        break;
                    }
                    case "lastname": {
                        filterByLastName(operation, value.substring(1, value.length() - 1));
                        break;
                    }
                    case "salary": {
                        filterBySalary(operation, value);
                        break;
                    }
                    case "department": {
                        filterByDepartment(operation, value);
                        break;
                    }
                }
            }
        }

    }

    private void filterBySalary(String operation, String value) {
        switch (operation) {
            case "<":
                employees = employees.stream().filter((employee) -> employee.getSalary() < Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case ">":
                employees = employees.stream().filter((employee) -> employee.getSalary() > Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case ">=":
                employees = employees.stream().filter((employee) -> employee.getSalary() >= Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case "<=":
                employees = employees.stream().filter((employee) -> employee.getSalary() <= Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case "==":
                employees = employees.stream().filter((employee) -> employee.getSalary() == Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case "!=":
                employees = employees.stream().filter((employee) -> employee.getSalary() != Double.parseDouble(value)).collect(Collectors.toList());
                break;
        }
    }

    private void filterByLastName(String operation, String value) {
        switch (operation) {
            case "<":
                employees = employees.stream().filter((employee) -> employee.getLastName().compareTo(value) < 0).collect(Collectors.toList());
                break;
            case ">":
                employees = employees.stream().filter((employee) -> employee.getLastName().compareTo(value) > 0).collect(Collectors.toList());
                break;
            case ">=":
                employees = employees.stream().filter((employee) -> employee.getLastName().compareTo(value) >= 0).collect(Collectors.toList());
                break;
            case "<=":
                employees = employees.stream().filter((employee) -> employee.getLastName().compareTo(value) <= 0).collect(Collectors.toList());
                break;
            case "==":
                employees = employees.stream().filter((employee) -> employee.getLastName().equals(value)).collect(Collectors.toList());
                break;
            case "!=":
                employees = employees.stream().filter((employee) -> !employee.getLastName().equals(value)).collect(Collectors.toList());
                break;
        }
    }

    private void filterByName(String operation, String value) {
        switch (operation) {
            case "<":
                employees = employees.stream().filter((employee) -> employee.getFirstName().compareTo(value) < 0).collect(Collectors.toList());
                break;
            case ">":
                employees = employees.stream().filter((employee) -> employee.getFirstName().compareTo(value) > 0).collect(Collectors.toList());
                break;
            case ">=":
                employees = employees.stream().filter((employee) -> employee.getFirstName().compareTo(value) >= 0).collect(Collectors.toList());
                break;
            case "<=":
                employees = employees.stream().filter((employee) -> employee.getFirstName().compareTo(value) <= 0).collect(Collectors.toList());
                break;
            case "==":
                employees = employees.stream().filter((employee) -> employee.getFirstName().equals(value)).collect(Collectors.toList());
                break;
            case "!=":
                employees = employees.stream().filter((employee) -> !employee.getFirstName().equals(value)).collect(Collectors.toList());
                break;
        }
    }

    private void filterByDepartment(String operation, String value) {
        switch (operation) {
            case "<":
                employees = employees.stream().filter((employee) -> employee.getDepartment().compareTo(value) < 0).collect(Collectors.toList());
                break;
            case ">":
                employees = employees.stream().filter((employee) -> employee.getDepartment().compareTo(value) > 0).collect(Collectors.toList());
                break;
            case ">=":
                employees = employees.stream().filter((employee) -> employee.getDepartment().compareTo(value) >= 0).collect(Collectors.toList());
                break;
            case "<=":
                employees = employees.stream().filter((employee) -> employee.getDepartment().compareTo(value) <= 0).collect(Collectors.toList());
                break;
            case "==":
                employees = employees.stream().filter((employee) -> employee.getDepartment().equals(value)).collect(Collectors.toList());
                break;
            case "!=":
                employees = employees.stream().filter((employee) -> !employee.getDepartment().equals(value)).collect(Collectors.toList());
                break;
        }
    }


}
