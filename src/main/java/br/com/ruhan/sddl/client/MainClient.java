package br.com.ruhan.sddl.client;

import org.openstreetmap.gui.jmapviewer.Demo;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

/**
 * Created by ruhan on 02/11/16.
 */
public class MainClient {

    public static Map MAP = null;

    public static void main(String args[]) {

        if( args.length == 0 ) {
            throw new InvalidParameterException("Número de parâmetros inválidos");
        }


        final Thread threadBusClient = new Thread() {
            public void run() {
                final String linha = args[0];
                new BusClientNode(Integer.valueOf( linha ) );
            }
        };

        threadBusClient.start();

        final Thread threadMap = new Thread() {
            public void run() {
                MAP = new Map();
                MAP.setVisible(true);
            }
        };

        threadMap.start();
    }

}
