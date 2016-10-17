package pn.eric.operations.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import pn.eric.operations.po.WebObject;

/**
 * @author Eric
 */
public class Boot {
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9095);

        final SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            public void onConnect(SocketIOClient socketIOClient) {
                socketIOClient.joinRoom("operas");
                System.out.println(String.format("join room operas %s", socketIOClient.getSessionId()));
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient socketIOClient) {
                socketIOClient.leaveRoom("leave room operas" + socketIOClient.getSessionId());
                System.out.println(String.format("leave room operas %s", socketIOClient.getSessionId()));
            }
        });

        server.addEventListener("webEvent", WebObject.class, new DataListener<WebObject>() {
            @Override
            public void onData(SocketIOClient client,
                               WebObject data, AckRequest ackRequest) {
                String command = data.getMsg();
                String result = handleEvent(command);
                System.out.println("webEvent: " + command);

                if(result.equals("route")){
                    if (server.getRoomOperations("operas").getClients()
                            .size() > 0) {
                        System.out.println("broadcast messages to room operas");
                        server.getRoomOperations("operas").sendEvent("webCmd", data);
                    }
                }
                ackRequest.sendAckData(true);
            }


        });

//        server.addEventListener("deployEvent", String.class, new DeployServerOperations());

        server.start();
        System.out.println("websocket server started at 9095");
        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
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
