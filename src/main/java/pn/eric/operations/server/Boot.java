package pn.eric.operations.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import pn.eric.operations.listener.BuildServerOperations;
import pn.eric.operations.listener.ServerStatus;

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
                System.out.println(String.format("join room operas %s" , socketIOClient.getSessionId()));
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            public void onDisconnect(SocketIOClient socketIOClient) {
                socketIOClient.leaveRoom("leave room operas" + socketIOClient.getSessionId());
                System.out.println(String.format("leave room operas %s", socketIOClient.getSessionId()));
            }
        });

        server.addEventListener("operas", String.class, new BuildServerOperations());
        server.addEventListener("status", String.class, new ServerStatus());

        server.start();
        System.out.println("websocket server started at 9095");
        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

}
