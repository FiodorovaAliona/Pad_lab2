
package com.home.pad.distrsys.node;


public class NodeConfig {

    private String discoverAddress = "233.3.3.3";
    private int discoverPort = 9999;
    private String dataExchangeAddress = "localhost";
    private int dataExchangePort = 5555;

    public String getDataExchangeAddress() {
        return dataExchangeAddress;
    }

    public void setDataExchangeAddress(String dataExchangeAddress) {
        this.dataExchangeAddress = dataExchangeAddress;
    }

    public int getDataExchangePort() {
        return dataExchangePort;
    }

    public void setDataExchangePort(int dataExchangePort) {
        this.dataExchangePort = dataExchangePort;
    }

    public String getDiscoverAddress() {
        return discoverAddress;
    }

    public void setDiscoverAddress(String discoverAddress) {
        this.discoverAddress = discoverAddress;
    }

    public int getDiscoverPort() {
        return discoverPort;
    }

    public void setDiscoverPort(int discoverPort) {
        this.discoverPort = discoverPort;
    }

}
