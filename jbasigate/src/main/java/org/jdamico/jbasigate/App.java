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
        
        IgateComponent igateComponent = new IgateComponent();
        igateComponent.controller(host, port, callSign, passcode, lat, lng);
        
    }
}
