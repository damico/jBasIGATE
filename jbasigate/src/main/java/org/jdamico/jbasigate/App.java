package org.jdamico.jbasigate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

import org.jdamico.javax25.ax25.Afsk1200Demodulator;

public class App {
	
    public static void main(String[] args){
        String host = args[2];
        int port = Integer.parseInt(args[3]);
        String callSign = args[0].toUpperCase();
        String passcode = args[1];
        Double lat = Double.parseDouble(args[4]);
        Double lng = Double.parseDouble(args[5]);
        Long lastVisitTime = 0l;
        AprsHelper aprsHelper = new AprsHelper();
        boolean is_logged = false;
        try (
                Socket igateSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(igateSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(igateSocket.getInputStream()));
            ) {
                String fromServer;
                
     
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                    if(!is_logged) {
                    	out.println("user "+callSign+" pass "+passcode+" vers jBasIGATE 0.0.1");
                    	String packetMsg = callSign+">APRS,TCPIP*:>https://github.com/damico/jBasIGATE";
                    	out.println(packetMsg);
                    	packetMsg = callSign+">APRS:"+aprsHelper.getLocMsg(lat,lng, "jBasIGATE https://github.com/damico/jBasIGATE");
                    	out.println(packetMsg);
                    	is_logged = true;
                    }else {
                    	if(Afsk1200Demodulator.receivedPackedMap != null && Afsk1200Demodulator.receivedPackedMap.size() > 0) {
                    		Set<Long> packetsReceived = Afsk1200Demodulator.receivedPackedMap.keySet();
                    		Iterator<Long> packetsReceivedIter = packetsReceived.iterator();
                    		while(packetsReceivedIter.hasNext()) {
                    			Long packetsReceivedTime = packetsReceivedIter.next();
                    			if(packetsReceivedTime > lastVisitTime) {
                    				lastVisitTime = packetsReceivedTime;
                    				out.println(callSign+">"+Afsk1200Demodulator.receivedPackedMap.get(packetsReceivedTime));
                    			}
                    		}
                    	}
                    }

                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + host);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                    host);
            }
    }
}
