package br.com.ruhan.sddl.client;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.ruhan.sddl.model.GPSBus;
import org.openstreetmap.gui.jmapviewer.*;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

/**
 *
 * Demonstrates the usage of {@link JMapViewer}
 *
 * @author Jan Peter Stotz
 *
 */
public class Map extends JFrame {

    private static final long serialVersionUID = 1L;

    private static ConcurrentHashMap<String, MapMarkerDot> busesPosition
            = new ConcurrentHashMap<>();

    private static final double RADIUS_IN_METERS = 557.9;
    private static final double HOME_LATITUDE = -22.863171;
    private static final double HOME_LONGITUDE = -43.255901;

    private JMapViewer map;

    private Object lock = new Object();

    public Map() {
        super("JMapViewer Map");
        map = new JMapViewer();

        drawMap();
    }

    private void drawMap() {
        setSize(400, 400);

        // final JMapViewer map = new JMapViewer(new MemoryTileCache(),4);
        // map.setTileLoader(new OsmFileCacheTileLoader(map));
        // new DefaultMapController(map);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();

        JPanel helpPanel = new JPanel();
        add(panel, BorderLayout.NORTH);
        add(helpPanel, BorderLayout.SOUTH);
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpPanel.add(helpLabel);

        add(map, BorderLayout.CENTER);

        final MapMarkerDot markerDot = new MapMarkerDot(Color.BLACK, HOME_LATITUDE, HOME_LONGITUDE);
        markerDot.setBackColor( Color.GREEN );
        map.addMapMarker(markerDot);

        final MapMarkerCircle mapMarkerCircle = new MapMarkerCircle(HOME_LATITUDE, HOME_LONGITUDE, 0.005);
        map.addMapMarker(mapMarkerCircle);

    }

    public synchronized void addPoint( GPSBus gpsBus ) {

        final double latitude = gpsBus.getLatitude();
        final double longitude = gpsBus.getLongitude();

        final String ordem = gpsBus.getOrdem();

        final MapMarkerDot oldPosition = busesPosition.get( ordem );
        if( oldPosition != null ) {
            synchronized ( lock ) {
                map.removeMapMarker(oldPosition);
            }
        }

        final double distance = distance(HOME_LATITUDE, latitude, HOME_LONGITUDE, longitude);

        final MapMarkerDot newPosition = new MapMarkerDot(latitude, longitude);
        if( distance <= RADIUS_IN_METERS ) {
            newPosition.setBackColor( Color.RED );
            newPosition.setColor( Color.RED );

        } else {
            newPosition.setBackColor( Color.BLUE );
            newPosition.setColor( Color.BLUE );
        }

        busesPosition.put( ordem, newPosition );
        synchronized ( lock ) {
            map.addMapMarker(newPosition);
        }
    }

    private double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

}