package com.home.pad.distrsys.node;

import com.home.pad.distrsys.model.Employee;
import com.home.pad.distrsys.model.EmployeeDataSource;
import com.home.pad.distrsys.dsl.DslProcessor;
import com.home.pad.distrsys.serializers.JsonSerializer;
import com.home.pad.distrsys.net.Request;
import com.home.pad.distrsys.net.TcpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NodeClientHandler implements Runnable{
    private final Socket nodeClientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private String nodeName;
    private Logger logger = Logger.getLogger(NodeClientHandler.class.getName());

    public NodeClientHandler(Socket nodeClientSocket, String nodeName) {
        this.nodeClientSocket = nodeClientSocket;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(nodeClientSocket.getInputStream()));
            this.printWriter = new PrintWriter(nodeClientSocket.getOutputStream());
            this.nodeName = nodeName;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException", e);
        }
    }

    @Override
    public void run() {
        String mediatorRequest = read(bufferedReader);
        logger.log(Level.INFO, "Got mediator request in JSON. Request: {0}", mediatorRequest);
        Request request = JsonSerializer.fromJson(mediatorRequest, Request.class);
        List<Employee> nodeDataList = new ArrayList<>();
        for(URI u: EmployeeDataSource.INSTANCE.getConnectionsFor(nodeName)){
            try {
                Socket s = new Socket(InetAddress.getByName(u.getHost()), u.getPort());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter printWriter = new PrintWriter(s.getOutputStream());
                write(mediatorRequest, printWriter);
                logger.log(Level.INFO, "Mediator request sent. Request: {1}", mediatorRequest);
                String nodeResponse = read(bufferedReader);
                logger.log(Level.INFO, "Got mediator response in JSON. Response: {0}", nodeResponse);
                TcpResponse tcpResponse = JsonSerializer.fromJson(nodeResponse, TcpResponse.class);
                nodeDataList.addAll(tcpResponse.getEmployees());
                bufferedReader.close();
                printWriter.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Can't send request to peer node", e);
            }
        }
        TcpResponse tcpResponse = new TcpResponse(nodeDataList);
        tcpResponse.getEmployees().addAll(EmployeeDataSource.INSTANCE.getNodeDataListFor(nodeName));
        DslProcessor dslProcessor = new DslProcessor(request, tcpResponse.getEmployees());
        List<Employee> filteredList = dslProcessor.execute();
        tcpResponse.setEmployees(filteredList);
        write(JsonSerializer.toJson(tcpResponse), printWriter);
    }

    private String read(BufferedReader bufferedReader) {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = readLine(bufferedReader)) != null && line.trim().length() > 0){
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }

    private String readLine(BufferedReader bufferedReader) {
        String s = "";
        try {
            s = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void write(String data, PrintWriter printWriter) {
        printWriter.println(data);
        printWriter.println();
        printWriter.flush();
    }
}
