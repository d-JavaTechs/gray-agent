package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import pn.eric.operations.event.OperateCommand;
import pn.eric.operations.po.WebObject;
import pn.eric.operations.util.JavaShellUtil;

/**
 * @author Shadow
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{
    Socket socket;

    public  DeployServerOperations(Socket socket){
        this.socket = socket;
    }
    public static void main(String[] args) {
        System.out.println("form build server " + OperateCommand.ROLLBACK.name());
    }
    @Override
    public void call(Object... objects) {
        JSONObject msg = (JSONObject)objects[0];
        try{
            String command = (String)msg.get("msg");
            if(command.equals(OperateCommand.ROLLBACK.name().toLowerCase())){
                JavaShellUtil.executeShellAndSendMessage(OperateCommand.BRANCH, socket);
                System.out.println("ROLLBACK");
            } else if(command.equals(OperateCommand.DEPLOY.name().toLowerCase())){
                JavaShellUtil.executeShellAndSendMessage(OperateCommand.BRANCH, socket);
                System.out.println("DEPLOY");
            }
            System.out.println("form build server " + (String)msg.get("msg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
