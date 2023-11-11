package org.jdamico.jbasigate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import org.jdamico.javax25.ax25.Packet;

public class App {
    public static void main(String[] args){
        String host = args[2];
        int port = Integer.parseInt(args[3]);
        String callSign = args[0].toUpperCase();
        String passcode = args[1];
        SocketClient sc =  new SocketClient();
        
        AprsHelper aprsHelper = new AprsHelper();
        String msg = aprsHelper.getDateTimeLocMsg(new Date(), -3, -23.5429886,-46.7575315);
        
		Packet packet = new Packet("APRS",
				callSign,
				new String[] {},
				Packet.AX25_CONTROL_APRS,
				Packet.AX25_PROTOCOL_NO_LAYER_3,
				msg.getBytes());
		
		System.out.println(packet.toString());
        boolean is_logged = false;
        try (
                Socket kkSocket = new Socket(host, port);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));
            ) {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                String fromServer;
                String fromUser;
     
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                     
                    //fromUser = stdIn.readLine();
                    if(!is_logged) {
                    	out.println("user "+callSign+" pass "+passcode+" vers jBasIGATE 0.0.1");
                    	System.out.println("PU2LVM-10>APRS:"+aprsHelper.getLocMsg(-23.5429886,-46.7575315, "jBasIGATE https://github.com/damico/jBasIGATE"));
                    	String packetMsg = "PU2LVM-10>APRS,TCPIP*:>https://github.com/damico/jBasIGATE";
                    	out.println(packetMsg);
                    	out.println("PU2LVM-10>APRS,TCPIP*:"+aprsHelper.getLocMsg(-23.5429886,-46.7575315, "jBasIGATE\r"));
                    	
                    	is_logged = true;
                    }

                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + host);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                    host);
                System.exit(1);
            }
        
        
        
        
        
        
        
        
        
        
        
//        try {
//			sc.startConnection(host, port);
//			sc.sendMessage("user "+callSign+" pass "+passcode+" vers jBasIGATE 0.0.1\r");
//			sc.sendMessage(callSign+">APRS,TCPIP*:>This is a test packet from jBasIGATE\r");
//			
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}
