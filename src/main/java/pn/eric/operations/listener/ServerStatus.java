package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @author Shadow
 * @date
 */
public class ServerStatus implements DataListener<String> {
    public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
        System.out.println("received command: " + s);
    }
}
