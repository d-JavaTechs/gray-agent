package pn.eric.operations.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import pn.eric.operations.event.OperateCommand;
import pn.eric.operations.po.DeployServerObject;
import pn.eric.operations.po.WebObject;
import pn.eric.operations.util.JavaShellUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric
 */
public class Boot {
    final static int port = 9095;
    final static String hostName = "localhost";
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname(hostName);
        config.setPort(port);
        Map<String,DeployServerObject> nodesMap = new ConcurrentHashMap<String,DeployServerObject> ();
        final SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println(String.format("join room %s", socketIOClient.getSessionId()));
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient socketIOClient) {
                if (nodesMap.containsKey(socketIOClient.getSessionId().toString())) {
                    socketIOClient.leaveRoom("nodes");
                    nodesMap.remove(socketIOClient.getSessionId().toString());

                    System.out.println(String.format("leave room  nodes %s", socketIOClient.getSessionId()));
                    notifyWebClients(nodesMap, server);
                } else {
                    socketIOClient.leaveRoom("web");
                    System.out.println(String.format("leave room web  %s", socketIOClient.getSessionId()));
                }
            }
        });

       server.addEventListener("nodeReg", String.class, new DataListener<String>() {
                   @Override
                   public void onData(SocketIOClient client,
                                      String data, AckRequest ackRequest) {
                       client.joinRoom("nodes");
                       System.out.println("nodes Reg");
                       String name = data.split(":")[0];
                       String ip = data.split(":")[1];
                       nodesMap.put(client.getSessionId().toString(), new DeployServerObject(name, ip));
                       notifyWebClients(nodesMap, server);
                   }
               }

       );

        server.addEventListener("webReg",WebObject.class,new DataListener<WebObject>() {
            @Override
            public void onData (SocketIOClient client,
                WebObject data, AckRequest ackRequest){

                client.joinRoom("web");
                System.out.println("webReg");

                notifyWebClients(nodesMap, server);
            }
        }

        );

        server.addEventListener("nodeEvent",String.class,new DataListener<String>() {
                    @Override
                    public void onData (SocketIOClient client,
                                        String data, AckRequest ackRequest){
                        notifyWebClientsNodeEvent(nodesMap, server, data);
                    }
        });



        server.addEventListener("webEvent",WebObject.class,new DataListener<WebObject>() {
            @Override
            public void onData (SocketIOClient client,
                WebObject data, AckRequest ackRequest){
            String command = data.getMsg();
            System.out.println("webEvent: " + command);

            switch (command) {
                case "branches":
                    System.out.println("handleEvent->listBuildServerBranches");
                    JavaShellUtil.executeShellAndSendMessage(OperateCommand.BRANCH, client);
                    break;
                case "build":
                    System.out.println("handleEvent->build");
                    JavaShellUtil.executeShellAndSendMessage(OperateCommand.BUILD, client);
                    break;
                case "rollBack":
                    System.out.println("handleEvent->rollBack");
                    if (server.getRoomOperations("nodes").getClients()
                            .size() > 0) {
                        System.out.println("broadcast messages to room operas");
                        server.getRoomOperations("nodes").sendEvent("webCmd", data);
                    }
                    break;
                case "deploy":
                    System.out.println("handleEvent->deploy");
                    if (server.getRoomOperations("nodes").getClients()
                            .size() > 0) {
                        System.out.println("broadcast messages to room operas");
                        server.getRoomOperations("nodes").sendEvent("webCmd", data);
                    }
                    break;
                default:
                    break;
            }
            ackRequest.sendAckData(true);
        }
        }
      );

            server.start();
            System.out.println("websocket server started at "+port);
            Thread.sleep(Integer.MAX_VALUE);

            server.stop();
        }

    private static void notifyWebClientsNodeEvent(Map<String, DeployServerObject> map, SocketIOServer server,String data) {
        if (server.getRoomOperations("web").getClients().size() > 0) {
            server.getRoomOperations("web").sendEvent("serverList", data);
        }
    }

    private static void notifyWebClients(Map<String, DeployServerObject> map, SocketIOServer server) {
        ArrayList ar = new ArrayList();
        Iterator<Map.Entry<String, DeployServerObject>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, DeployServerObject> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            ar.add(entry.getValue());
        }


        if (ar.size()>= 0&& server.getRoomOperations("web").getClients().size() > 0) {
            server.getRoomOperations("web").sendEvent("serverList", ar);
        }
    }
}
