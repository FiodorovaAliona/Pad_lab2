package com.home.pad.distrsys.net;


public class UdpResponse implements Comparable<UdpResponse> {
    private int dataSize;
    private int numberOfConnections;
    private String address;
    private int port;

    public UdpResponse() {
    }

    public UdpResponse(int dataSize, int numberOfConnections, String address, int port) {
        this.dataSize = dataSize;
        this.numberOfConnections = numberOfConnections;
        this.address = address;
        this.port = port;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public void setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "UdpResponse{" +
                "dataSize=" + dataSize +
                ", numberOfConnections=" + numberOfConnections +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public int compareTo(UdpResponse other) {
        int result = Integer.compare(other.numberOfConnections, this.numberOfConnections);
        return result != 0 ? result : Integer.compare(other.dataSize, this.dataSize);
    }
}
