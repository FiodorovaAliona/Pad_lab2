package com.home.pad.distrsys.client;

import com.home.pad.distrsys.model.Employee;
import com.home.pad.distrsys.net.Request;
import com.home.pad.distrsys.net.TcpResponse;
import com.home.pad.distrsys.serializers.JsonSerializer;
import com.home.pad.distrsys.serializers.XmlSerializer;
import com.home.pad.distrsys.serializers.XmlValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private ClientConfig clientConfig;
    private Socket socket;
    private Logger logger = Logger.getLogger(Client.class.getName());
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Client(ClientConfig clientConfig) {

        this.clientConfig = clientConfig;
        try {
            this.socket = new Socket(InetAddress.getByName(clientConfig.getMediatorAddress()), clientConfig.getMediatorPort());
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException", e);
        }

    }


    public List<Employee> sendRequest(String responseType, Request request) {
        String jsonSerializedRequest = JsonSerializer.toJson(request);
        write("Response type: " + responseType + "\n" + jsonSerializedRequest);
        logger.log(Level.INFO, "Request sent. Data: {0}", request);
        String mediatorSerializedResponse = read();
        if (responseType.endsWith(ResponseType.JSON_TYPE.getType())) {
            logger.log(Level.INFO, "Got mediator response in JSON. Response: {0}", mediatorSerializedResponse);
            TcpResponse tcpResponse = JsonSerializer.fromJson(mediatorSerializedResponse, TcpResponse.class);
            return tcpResponse.getEmployees();
        } else {
            logger.log(Level.INFO, "Got mediator response in XML. Response: {0}", mediatorSerializedResponse);
            XmlValidator xmlValidator = new XmlValidator("tcp_response_schema.xsd", TcpResponse.class);
            if (xmlValidator.validate(mediatorSerializedResponse)) {
                TcpResponse tcpResponse = XmlSerializer.fromXml(mediatorSerializedResponse, TcpResponse.class);
                return tcpResponse.getEmployees();
            }
            logger.log(Level.WARNING, "Invalid XML data.");
        }
        return new ArrayList<>();
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    private String read() {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = readLine()) != null && line.trim().length() > 0) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    private String readLine() {
        String s = "";
        try {
            s = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void write(String data) {
        printWriter.println(data);
        printWriter.println();
        printWriter.flush();
    }

    public static void main(String[] args) {
        Client client = new Client(new ClientConfig());

        Request r = Request.newRequest()
                .filterBy("salary < 5000")
                .groupBy("department")
                .orderBy("department")
                .build();

        List<Employee> employees = client.sendRequest(ResponseType.JSON_TYPE.getType(), r);

        employees.forEach(System.out::println);

    }
}
