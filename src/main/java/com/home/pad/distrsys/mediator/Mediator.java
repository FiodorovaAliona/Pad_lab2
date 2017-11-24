package com.home.pad.distrsys.mediator;

import com.home.pad.distrsys.net.UdpResponse;
import com.home.pad.distrsys.serializers.JsonSerializer;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Mediator implements Runnable {

    private MediatorConfig config;
    private volatile Maven maven;
    private Logger logger = Logger.getLogger(Mediator.class.getName());
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private ServerSocket serverSocket;

    public Mediator(MediatorConfig config) {
        MavenDetector mavenDetector = new MavenDetector();
        this.config = config;
        mavenDetector.discoverNodes();
        logger.log(Level.INFO, "Nodes: {0}", mavenDetector.getUdpResponses());
        logger.log(Level.INFO, "Maven detected: {0}", mavenDetector.getMaven());
        try {
            serverSocket = new ServerSocket(config.getMediatorPort());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException", e);
        }
        executorService.submit(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                MediatorClientHandler mediatorClientHandler = new MediatorClientHandler(clientSocket, maven);
                executorService.submit(mediatorClientHandler);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediatorConfig getConfig() {
        return config;
    }

    public void setConfig(MediatorConfig config) {
        this.config = config;
    }

    private class MavenDetector implements Runnable {
        private ExecutorService executorService = Executors.newFixedThreadPool(3);
        private List<UdpResponse> udpResponses = Collections.synchronizedList(new ArrayList<>());


        private DatagramSocket socket;


        public void discoverNodes() {
            try {
                socket = new DatagramSocket();
                final byte[] buf = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                packet.setAddress(InetAddress.getByName(config.getNodeDiscoverAddress()));
                packet.setPort(config.getNodeDiscoverPort());
                packet.setData("SYN".getBytes());
                socket.send(packet);
                executorService.submit(this);
                Thread.sleep(config.getTimeout());
                executorService.shutdownNow();
                socket.close();
            } catch (IOException | InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
            detectMaven();
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < config.getTimeout()) {
                final byte[] buf = new byte[4096];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                    udpResponses.add(JsonSerializer.fromJson(new String(packet.getData()), UdpResponse.class));
                    logger.log(Level.INFO, "Nodes: " + udpResponses);
                } catch (Exception ex) {
                    logger.log(Level.INFO, "Reached time out limit. Socket closed");
                }
            }
        }

        private void detectMaven() {
            Optional<Maven> maven = udpResponses.stream().sorted().map(Maven::new).findFirst();
            maven.ifPresent(m -> Mediator.this.maven = m);
            if (!maven.isPresent())
                logger.log(Level.WARNING, "Maven not found.");
        }

        public Maven getMaven() {
            return maven;
        }

        public List<UdpResponse> getUdpResponses() {
            return udpResponses;
        }

        public void setUdpResponses(List<UdpResponse> udpResponses) {
            this.udpResponses = udpResponses;
        }

    }

    public static void main(String[] args) {
        Mediator mediator = new Mediator(new MediatorConfig());
    }

}
