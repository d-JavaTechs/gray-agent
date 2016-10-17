package pn.eric.operations.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @author Shadow
 * @date
 */
public class WebOperations implements DataListener<String>{
    @Override
    public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
        System.out.println("received command webCommand: " + s);
        if(s.equals("listBuildServerBranches")){
            System.out.println("listBuildServerBranches");
        }else if(s.equals("build")){
            System.out.println("build");
        }else if(s.equals("rollBack")){
            
        }else if(s.equals("deploy")){

        }
    }
}
