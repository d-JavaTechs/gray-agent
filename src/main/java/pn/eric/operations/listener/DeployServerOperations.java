package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.emitter.Emitter;

/**
 * @author Shadow
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{


    @Override
    public void call(Object... objects) {
        System.out.print(objects[0]);
    }
}
