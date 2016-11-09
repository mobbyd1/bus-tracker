package br.com.ruhan.sddl.client;

import br.com.ruhan.sddl.model.GPSBus;
import com.google.gson.Gson;
import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.groups.Group;
import lac.cnclib.net.groups.GroupCommunicationManager;
import lac.cnclib.net.groups.GroupMembershipListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/**
 * Created by ruhan on 02/11/16.
 */
public class BusClientNode implements NodeConnectionListener, GroupMembershipListener {

    private static final String gatewayIP = "ubuntu-1";
    private static final int gatewayPort  = 5500;
    private GroupCommunicationManager   groupManager;
    private Group grupo;

    private Object lock = new Object();

    public BusClientNode(int linha ) {
        InetSocketAddress address = new InetSocketAddress(gatewayIP, gatewayPort);
        try {
            MrUdpNodeConnection connection = new MrUdpNodeConnection();
            connection.connect(address);
            connection.addNodeConnectionListener(this);

            grupo = new Group(0, linha);

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    public void connected(NodeConnection remoteCon) {
        groupManager = new GroupCommunicationManager(remoteCon);
        groupManager.addMembershipListener(this);

        try {
            groupManager.joinGroup(grupo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnected(NodeConnection nodeConnection, SocketAddress socketAddress, boolean b, boolean b1) {

    }

    public void disconnected(NodeConnection nodeConnection) {

    }

    public void newMessageReceived(NodeConnection nodeConnection, Message message) {

        try {
            final Serializable contentObject = message.getContentObject();
            final String json = contentObject.toString();

            if( json != null ) {

                String mensagem = String.format("Mensagem recebida: %s", json);
                System.out.println(mensagem);

                final Gson gson = new Gson();
                final GPSBus gpsBus = gson.fromJson(json, GPSBus.class);

                if (MainClient.MAP != null) {
                    MainClient.MAP.addPoint(gpsBus);
                }
            }

        } catch ( Throwable throwable ) {
            throwable.printStackTrace();
        }

    }

    public void unsentMessages(NodeConnection nodeConnection, List<Message> list) {

    }

    public void internalException(NodeConnection nodeConnection, Exception e) {

    }

    public void enteringGroups(List<Group> list) {
        System.out.println("Entered in the group");
    }

    public void leavingGroups(List<Group> list) {

    }
}