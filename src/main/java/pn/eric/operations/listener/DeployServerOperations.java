package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import pn.eric.operations.po.WebObject;

/**
 * @author Shadow
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{


    @Override
    public void call(Object... objects) {
        JSONObject msg = (JSONObject)objects[0];
        try{
            System.out.println("form build server " + (String)msg.get("msg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
