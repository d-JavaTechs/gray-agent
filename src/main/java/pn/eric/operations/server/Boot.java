package pn.eric.operations.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import pn.eric.operations.po.DeployServerObject;
import pn.eric.operations.po.WebObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric
 */
public class Boot {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9095);
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
                }else{
                    socketIOClient.leaveRoom("web");
                    System.out.println(String.format("leave room web  %s", socketIOClient.getSessionId()));
                }
            }
        });

               server.addEventListener("nodeReg",String.class,new DataListener<String>(){
                            @Override
                            public void onData (SocketIOClient client,
                                                String data, AckRequest ackRequest){
                                client.joinRoom("nodes");
                                System.out.println("nodes Reg");
                                String name = data.split(":")[0];
                                String ip = data.split(":")[1];
                                nodesMap.put(client.getSessionId().toString(), new DeployServerObject(name,ip) );
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


                server.addEventListener("webEvent",WebObject.class,new DataListener<WebObject>() {
                    @Override
                    public void onData (SocketIOClient client,
                        WebObject data, AckRequest ackRequest){
                    String command = data.getMsg();
                    String result = handleEvent(command);
                    System.out.println("webEvent: " + command);

                    if (result.equals("route")) {
                        if (server.getRoomOperations("nodes").getClients()
                                .size() > 0) {
                            System.out.println("broadcast messages to room operas");
                            server.getRoomOperations("nodes").sendEvent("webCmd", data);
                        }
                    }
                    ackRequest.sendAckData(true);
                }


            }

            );

            server.start();
            System.out.println("websocket server started at 9095");
            Thread.sleep(Integer.MAX_VALUE);

            server.stop();
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

    private static String handleEvent(String messageFromWeb) {
            String result="";
            switch (messageFromWeb) {
                case "listBuildServerBranches":
                    System.out.println("handleEvent->listBuildServerBranches");
                     break;
                case "build":
                    System.out.println("handleEvent->build");
                    break;
                case "rollBack":
                    result= "route";
                    System.out.println("handleEvent->rollBack");
                    break;
                case "deploy":
                    result= "route";
                    System.out.println("handleEvent->deploy");
                    break;
                default:
                    break;
        }
         return result;
    }
}
