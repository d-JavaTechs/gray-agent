package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.emitter.Emitter;
import pn.eric.operations.event.OperateCommand;

/**
 * @author Shadow
 * @date
 */
public class BuildServerOperations implements DataListener<String>{
    @Override
    public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
        System.out.println("received command: " + s);
        socketIOClient.sendEvent("operas", OperateCommand.BRANCH);
    }


}
