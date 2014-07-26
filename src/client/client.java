package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client {

    private ArrayList<Actiu> ifaceUp;
    private ArrayList<NetworkInterface> ifaceDown;
    public static final int port = 8888;
    private static String name = "Halfonso";
    public static final int DEBUG = 1;
    public static final int RELEASE = 0;
    public static int mode;

    private class Actiu {

        public NetworkInterface iface;
        public Listener list;

        public Actiu(NetworkInterface iface) {
            this.iface = iface;
        }

    }

    public static void main(String args[]) {
        if (args.length >= 1 && args[0].equalsIgnoreCase("DEBUG")) {
            mode = DEBUG;
        } else {
            mode = RELEASE;
        }
        if (args.length >= 2) {
            name = args[1];
        } else {
            try {
                name = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ex) {
                name = name + (int)(Math.random() * 100.0);
            }
        }
        try {
            new client().start();
        } catch (SocketException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start() throws SocketException {
        ifaceUp = new ArrayList<>();
        ifaceDown = new ArrayList<>();
        classifyInterfaces();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        if (s.equalsIgnoreCase("STOP")){
            for (Actiu actiu : ifaceUp) {
                System.out.println("Stopping \t" + actiu.iface.getDisplayName());
                actiu.list.stop();
            }
        }
    }

    private void classifyInterfaces() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces;
        networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces != null) {
            System.out.println("Interfaces: ");
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface iface = networkInterfaces.nextElement();
                if (iface.isUp() && !iface.isLoopback()) {
                    add(ifaceUp, iface);
                    if (ifaceDown.contains(iface)) {
                        ifaceDown.remove(iface);
                    }
                } else {
                    if (!ifaceDown.contains(iface)) {
                        ifaceDown.add(iface);
                    }
                    remove(ifaceUp, iface);
                }
            }
        }
    }

    private boolean add(ArrayList<Actiu> llista, NetworkInterface element) {
        for (Actiu elemLlista : llista) {
            if (elemLlista.iface.equals(element)) {
                return false;
            }
        }
        Enumeration<InetAddress> enume = element.getInetAddresses();
        while(enume.hasMoreElements()){
            InetAddress nextElement = enume.nextElement();
            if(nextElement.getClass().equals(Inet4Address.class)){
                System.out.println("\t" + element.getDisplayName() + "|---> " + nextElement.getHostAddress());
                Actiu act = new Actiu(element);
                llista.add(act);
                act.list = new Listener(nextElement, port, name);
                act.list.start();
                return true;
            }
        }
        return false;
    }

    private boolean remove(ArrayList<Actiu> llista, NetworkInterface element) {
        for (Actiu elemLlista : llista) {
            if (elemLlista.iface.equals(element)) {
                llista.remove(elemLlista);
                if(elemLlista.list != null)
                    elemLlista.list.stop();
                return true;
            }
        }
        return false;
    }
}