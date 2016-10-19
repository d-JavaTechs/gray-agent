package pn.eric.operations.listener;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import pn.eric.operations.client.CmdExecutor;
import pn.eric.operations.event.OperateCommand;

/**
 * @author Shadow
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{
    Socket socket;
    CmdExecutor exec;
    public  DeployServerOperations(CmdExecutor exec , Socket socket){
        this.socket = socket;
        this.exec = exec;
    }
//    public static void main(String[] args) {
//        System.out.println("form build server " + OperateCommand.ROLLBACK.name());
//    }
    @Override
    public void call(Object... objects) {
        JSONObject msg = (JSONObject)objects[0];
        try{
            String command = (String)msg.get("msg");
            if(command.equals(OperateCommand.ROLLBACK.name().toLowerCase())){
//                JavaShellUtil.executeShellAndSendMessage(OperateCommand.BRANCH, socket);
                exec.getQueue().put("");
                System.out.println("ROLLBACK");
            } else if(command.equals(OperateCommand.DEPLOY.name().toLowerCase())){
//                JavaShellUtil.executeShellAndSendMessage(OperateCommand.BRANCH, socket);
                exec.getQueue().put("");
                System.out.println("DEPLOY");
            }
            System.out.println("form build server " + (String)msg.get("msg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
