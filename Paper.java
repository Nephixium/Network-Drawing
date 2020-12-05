package com.datagram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;

/** Ritar upp bildpunkter i ett fönster utifrån koordinater mottagna och skickade via UDP paket i Communication-klassen.
 *  @author Nephixium
 *  @version 1.0
 */

public class Paper extends JPanel {
    private Communication communication;
    private HashSet hs = new HashSet();

    public Paper(int local, InetAddress host, int hostPort) {
        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
        this.communication = new Communication(local, host, hostPort,  this);
    }

    /** Ritar upp bildpunkterna, synchronized så två olika trådar inte använder metoden samtidigt, vilket skulle resultera i krockar. */
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        Iterator i = hs.iterator();
        while(i.hasNext()) {
            Point p = (Point)i.next();
            g.fillOval(p.x, p.y, 2, 2); }
        }

    /** Lägger till en bildpunkt, synchronized så bara en tråd åt gången kan lägga till en punkt med hjälp av metoden. */
    public synchronized void addPoint(Point p) {
        hs.add(p);
        repaint();
        this.communication.sendCoordinates(p);
    }

    /** Lägger till en bildpunkt när användaren klickar med musen.
     */
    class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }

    /** Lägger till en bildpunkt när användaren klickar, håller in och drar muspekaren på skärmen.
     */
    class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }
}
