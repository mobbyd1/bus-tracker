package br.com.ruhan.sddl.producer;

import br.com.ruhan.sddl.model.GPSBus;
import br.com.ruhan.sddl.stream.GPSBusStream;
import com.google.gson.Gson;
import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.groups.Group;
import lac.cnclib.net.groups.GroupCommunicationManager;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by ruhan on 02/11/16.
 */
public class BusNode implements NodeConnectionListener {

    private static final String gatewayIP = "ubuntu-1";
    private static final int gatewayPort  = 5500;
    private GroupCommunicationManager groupManager;
    private GPSBus bus;
    private Group grupo;

    public BusNode(GPSBus bus) throws Exception {
        InetSocketAddress address = new InetSocketAddress(gatewayIP, gatewayPort);
        try {
            MrUdpNodeConnection connection = new MrUdpNodeConnection();
            connection.addNodeConnectionListener(this);
            connection.connect(address);

            this.bus = bus;

            grupo = new Group(0, Integer.valueOf( bus.getLinha() ));

            Thread.sleep(1000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {

        if( args.length == 0 ) {
            throw new InvalidParameterException("Número de parâmetros inválidos");
        }

        final String linha = args[0];

        final List<GPSBus> list = GPSBusStream.build();
        final List<GPSBus> busFiltered =
                list.stream()
                        .filter(bus -> bus.getLinha().equals(linha))
                        .collect(Collectors.toList());

        for( final GPSBus bus : busFiltered ) {
            new BusNode( bus );
        }
    }

    public void connected(NodeConnection remoteCon) {
        groupManager = new GroupCommunicationManager(remoteCon);

        try {
            final Gson gson = new Gson();

            ApplicationMessage appMsg = new ApplicationMessage();

            final String json = gson.toJson(bus);
            appMsg.setSenderID(UUID.randomUUID());
            appMsg.setContentObject(json);

            groupManager.sendGroupcastMessage(appMsg, grupo);

            String mensagem = String.format("Mensagem enviada: %s", json);
            System.out.println(mensagem);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reconnected(NodeConnection nodeConnection, SocketAddress socketAddress, boolean b, boolean b1) {

    }

    public void disconnected(NodeConnection nodeConnection) {

    }

    public void newMessageReceived(NodeConnection nodeConnection, Message message) {
        System.out.println("Group sender also received: " + message.getContentObject());
    }

    public void unsentMessages(NodeConnection nodeConnection, List<Message> list) {

    }

    public void internalException(NodeConnection nodeConnection, Exception e) {

    }

}
