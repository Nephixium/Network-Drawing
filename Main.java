package com.datagram;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;

/** Startar upp ett grafiskt ritprogram av klassen Paper med portparametrar och hostaddress från argumenten som programmet startas med.
 * Programmet startas enligt formatet: -Lokal port- -Hostaddress- -Hostport-
 *  @author Nephixium
 *  @version 1.0
 */

public class Main extends JFrame {
    private Paper p;

    /** Startar programmet enligt formatet: -Lokal port- -Hostaddress- -Hostport- som anges som argument.
     */
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            InetAddress host = InetAddress.getByName(args[1]);
            int hostPort = Integer.parseInt(args[2]);
            System.out.println(port + " " + host + " " + hostPort);
            new Main(port,host,hostPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Could not resolve connection to host, confirm host address and port");
        }
    }

    /** Initierar ett objekt av Paper-klassen utifrån local, host och hostPort parametrarna och skapar ett fönster som objektet kan använda.
     * @param local Lokal port
     * @param host Host address
     * @param hostPort Host port
     */
    public Main(int local, InetAddress host, int hostPort) {
        this.p = new Paper(local,host,hostPort);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(this.p, BorderLayout.CENTER);

        setSize(640, 480);
        setVisible(true);
    }
}

