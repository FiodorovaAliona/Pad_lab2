package com.home.pad.distrsys.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.home.pad.distrsys.model.Employee;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder("employees")
@XmlRootElement
public class TcpResponse {
    @JsonProperty("employees")
    private List<Employee> employees = new ArrayList<>();

    public TcpResponse() {
    }

    public TcpResponse(List<Employee> employees) {
        this.employees = employees;
    }

    @JsonProperty("employees")
    public List<Employee> getEmployees() {
        return employees;
    }

    @JsonProperty("employees")
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
