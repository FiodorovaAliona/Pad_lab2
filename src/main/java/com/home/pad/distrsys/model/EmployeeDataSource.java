package com.home.pad.distrsys.model;

import com.home.pad.distrsys.node.NodeNames;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public enum EmployeeDataSource {
    INSTANCE();

    private HashMap<String, List<Employee>> employees = new HashMap<>();
    private HashMap<String, URI> nodeAdresses = new HashMap<>();
    private HashMap<String, Set<URI>> connections = new HashMap<>();


    EmployeeDataSource() {
        supplyNodeEmployees();
        supplyNodePorts();
        supplyNodeConnections();

    }

    private void supplyNodePorts() {
        try {
            nodeAdresses.put(NodeNames.NODE_1.name(), new URI("tcp://localhost:4001"));
            nodeAdresses.put(NodeNames.NODE_2.name(), new URI("tcp://localhost:4002"));
            nodeAdresses.put(NodeNames.NODE_3.name(), new URI("tcp://localhost:4003"));
            nodeAdresses.put(NodeNames.NODE_4.name(), new URI("tcp://localhost:4004"));
            nodeAdresses.put(NodeNames.NODE_5.name(), new URI("tcp://localhost:4005"));
            nodeAdresses.put(NodeNames.NODE_6.name(), new URI("tcp://localhost:4006"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void supplyNodeEmployees() {
        employees.put(NodeNames.NODE_1.name(), getFirstDataList());
        employees.put(NodeNames.NODE_2.name(), getSecondDataList());
        employees.put(NodeNames.NODE_3.name(), getThirdDataList());
        employees.put(NodeNames.NODE_4.name(), getForthDataList());
        employees.put(NodeNames.NODE_5.name(), getFifthDataList());
        employees.put(NodeNames.NODE_6.name(), getSixthDataList());
    }

    private void supplyNodeConnections() {
        try {
            connections.put(NodeNames.NODE_1.name(),new HashSet<>());
            connections.put(NodeNames.NODE_2.name(),
                    new HashSet<>(Arrays.asList(
                            new URI("tcp://localhost:4003"),
                            new URI("tcp://localhost:4004"),
                            new URI("tcp://localhost:4005"))));
            connections.put(NodeNames.NODE_3.name(),
                    new HashSet<>(Arrays.asList(
                            new URI("tcp://localhost:4005"))));
            connections.put(NodeNames.NODE_4.name(),
                    new HashSet<>(Arrays.asList(
                            new URI("tcp://localhost:4005"))));
            connections.put(NodeNames.NODE_5.name(), new HashSet<>());
            connections.put(NodeNames.NODE_6.name(), new HashSet<>());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private List<Employee> getFirstDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Cherilyn", "Bins", "Sales", 3900.00));
        list.add(new Employee("Hebert", "Fisher", "Accounting", 3200.00));
        list.add(new Employee("Odis", "Fisher", "Information Technology", 4500.00));
        return list;
    }

    private List<Employee> getSecondDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Earlie", "Rempel", "Sales",3200.00));
        list.add(new Employee("Aili", "Cormier", "Sales",700.00));
        list.add(new Employee("Delwin", "Grant", "Information Technology",7600.00));
        return list;
    }

    private List<Employee> getThirdDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Earley", "Nikolaus", "Production Department",4300.00));
        list.add(new Employee("Moira", "Williamson", "Human Resources",5300.00));
        list.add(new Employee("Tennessee", "Heller", "Public Relations",2000.00));
        list.add(new Employee("Villa", "Bartoletti", "Human Resources",3200.00));
        return list;
    }

    private List<Employee> getForthDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Henretta", "Harber", "Information Technology",6800.00));
        list.add(new Employee("Kami", "Lehner", "Public Relations",1800.00));
        list.add(new Employee("Melony", "Boehm", "Production Department",8100.00));
        return list;
    }

    private List<Employee> getFifthDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Clyda", "McKenzie", "Information Technology",5100.00));
        list.add(new Employee("Iver", "Johnston", "Information Technology",6100.00));
        list.add(new Employee("Carleen", "Bins", "Information Technology",7300.00));
        return list;
    }

    private List<Employee> getSixthDataList() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Terence", "Simonis", "Administration",8400.00));
        list.add(new Employee("Kaylin", "Hessel", "Administration",8400.00));
        list.add(new Employee("Landin", "Walsh", "Production Department",2400.00));
        return list;
    }

    public int getNodeDataListSizeFor(String nodeName) {
        return this.employees.get(nodeName).size();
    }

    public int getNodePortFor(String nodeName) {
        return this.nodeAdresses.get(nodeName).getPort();
    }

    public String getNodeAddressFor(String nodeName) {
        return this.nodeAdresses.get(nodeName).getHost();
    }

    public List<Employee> getNodeDataListFor(String nodeName) {
        return this.employees.get(nodeName);
    }

    public int getNodeNumberOfConnectionsFor(String nodeName) {
        return this.connections.get(nodeName).size();
    }

    public Set<URI> getConnectionsFor(String nodeName) {return this.connections.get(nodeName);}
}