package com.datagram;

import java.awt.*;
import java.io.IOException;
import java.net.*;

/** Tar emot, skickar och behandlar koordinater som ritas upp i ett grafiskt ritprogram i Paper-klassen med hjälp av UDP paket som formateras till koordinater.
 *  @author Nephixium
 *  @version 1.0
 */

public class Communication extends Thread {

    private DatagramSocket client;
    int localPort;
    int hostPort;
    InetAddress host;
    private Paper paper;

    public Communication(int localPort, InetAddress inetHost, int hostPort, Paper paper) {
        this.localPort = localPort;
        this.host = inetHost;
        this.hostPort = hostPort;
        this.paper = paper;
        try {
            this.client = new DatagramSocket(localPort);
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Unable to resolve connection to client");
        }
        this.start();
    }

    /**
     * Skapar en temporär buffer med datan som ska tas emot och anger maxstorlek 8192 bytes.
     * UDP paketet sammanställs enligt den angivna bufferstorleken och tas emot av klienten för att sedan omvandlas till koordinater i en sträng som används som referens för att rita en bildpunkt.
     */
    public void run() {
        try {
            while (true) {
                byte[] data = new byte[8192];
                DatagramPacket response = new DatagramPacket(data, data.length);
                this.client.receive(response);

                String responseReceived = new String(response.getData(), 0, response.getLength());
                String responseCoordinates[] = responseReceived.split(" ");
                Point responsePoint = new Point(Integer.parseInt(responseCoordinates[0]), Integer.parseInt(responseCoordinates[1]));
                this.paper.addPoint(responsePoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in run method");
        }
    }

    /**
     * Bildpunkter tas emot från metoden addPoint() i Paper-objektet, omvandlas till en sträng, sparas i en buffer (data) och skickas till en annan klient med hjälp av parametrarna host och hostPort.
     *
     * @param p Ett bildpunkt objekt
     */
    public void sendCoordinates(Point p) {
        try {
            String coordinatesForReply = p.x + " " + p.y;
            byte[] data = coordinatesForReply.getBytes();
            DatagramPacket replyPacket = new DatagramPacket(data, data.length, this.host, this.hostPort);
            this.client.send(replyPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in send method");
        }
    }
}
