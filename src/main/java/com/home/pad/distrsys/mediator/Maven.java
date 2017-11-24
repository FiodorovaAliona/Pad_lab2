package com.home.pad.distrsys.mediator;

import com.home.pad.distrsys.net.UdpResponse;

public class Maven {

    private String address;
    private int port;

    public Maven(UdpResponse udpResponse) {
        this.address = udpResponse.getAddress();
        this.port = udpResponse.getPort();
    }

    public Maven(String address, int port) {
        this.address = address;
        this.port = port;
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
        return "Maven{" +
                "address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
