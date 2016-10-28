package com.isuwang.operations.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.isuwang.operations.contex.SystemParas;
import com.isuwang.operations.po.WebObject;
import static com.isuwang.operations.contex.SystemParas.*;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author duwupeng
 */
public class Main {
    final static int port = 9095;
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setPort(port);

        config.setAllowCustomRequests(true);

        Map<String,WebObject> nodesMap = new ConcurrentHashMap<String,WebObject> ();

        final SocketIOServer server = new SocketIOServer(config);
        final BlockingQueue queue = new LinkedBlockingQueue();

        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println(String.format(socketIOClient.getRemoteAddress()+ " --> join room %s", socketIOClient.getSessionId()));
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
                       nodesMap.put(client.getSessionId().toString(), new WebObject(name, ip,false,false,client.getSessionId().toString()));
                       notifyWebClients(nodesMap, server);
                   }
               }

       );

        server.addEventListener("webReg", WebObject.class, new DataListener<WebObject>() {
                    @Override
                    public void onData(SocketIOClient client,
                                       WebObject data, AckRequest ackRequest) {

                        client.joinRoom("web");
                        System.out.println("webReg ack: "+data);

                        ArrayList ar = new ArrayList();
                        Iterator<Map.Entry<String, WebObject>> entries = nodesMap.entrySet().iterator();
                        while (entries.hasNext()) {
                            Map.Entry<String, WebObject> entry = entries.next();
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            ar.add(entry.getValue());
                        }
                        client.sendEvent("serverList", ar);
//                        notifyWebClients(nodesMap, server);
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

            System.out.println("web data: "+ data);
            String command = data.getCmd();
                if(BUILDSERVERCOMMANDS.contains(command)){
                    try {
                        queue.put(command+" "+data.getBranchName());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    System.out.println("3: " +data.getSessionId() +":end");
                    if (server.getRoomOperations("nodes").getClients().size() > 0) {
                        String[] sessionIds = data.getSessionId().split("\\s+");
                        for(String sessionId:sessionIds){
                            System.out.println("sessionId: " + sessionId.trim() +":end");
                            SocketIOClient socketIOClient = server.getClient(UUID.fromString(sessionId.trim()));
                            System.out.println("socketIOClient: " + socketIOClient+" sending with command: " +command);
                            socketIOClient.sendEvent("webCmd", data);
                        }
                        server.getRoomOperations("web").sendEvent("roomEvent", data.getCmd()+";elId:"+data.getSessionId());
                    }
                }
            ackRequest.sendAckData(true);
        }
        }
      );

            server.start();
            System.out.println("websocket server started at " + port);

            CmdExecutor ex = new CmdExecutor(queue,server);
            System.out.println("CmdExecutor Thread started");


            new Thread(ex).start();
            Thread.sleep(Integer.MAX_VALUE);

            server.stop();
        }

    private static void notifyWebClientsNodeEvent(Map<String, WebObject> map, SocketIOServer server,String data) {
        String cmdRsp=data.substring(0,data.indexOf(":"));
        if (server.getRoomOperations("web").getClients().size() > 0) {
            server.getRoomOperations("web").sendEvent(cmdRsp+"Event", data.substring(data.indexOf(":")));
        }
    }

    private static void notifyWebClients(Map<String, WebObject> map, SocketIOServer server) {
        ArrayList ar = new ArrayList();
        Iterator<Map.Entry<String, WebObject>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, WebObject> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            ar.add(entry.getValue());
        }


        if (ar.size()>= 0&& server.getRoomOperations("web").getClients().size() > 0) {
            server.getRoomOperations("web").sendEvent("serverList", ar);
        }
    }
}
