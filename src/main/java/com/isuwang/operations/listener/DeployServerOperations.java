package com.isuwang.operations.listener;

import static com.isuwang.operations.contex.SystemParas.*;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;

/**
 * @author duwupeng
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{
    Socket socket;
    BlockingQueue queue;
    public  DeployServerOperations(BlockingQueue queue , Socket socket){
        this.socket = socket;
        this.queue = queue;
    }
    @Override
    public void call(Object... objects) {
        JSONObject msg = (JSONObject)objects[0];
        System.out.println("msg: " + msg);
        try{
            String cmd=((String)msg.get("cmd"));

            if (cmd.equalsIgnoreCase(AllowedCopmmand.DEPLOY.name())||cmd.equalsIgnoreCase(AllowedCopmmand.ROLLBACK.name())){
                cmd = cmd+ COMMAS + msg.getString("branchName");
            }else if(cmd.equalsIgnoreCase(AllowedCopmmand.SERVICERESTART.name())|| cmd.equalsIgnoreCase(AllowedCopmmand.SERVICESTOP.name())){
                cmd = cmd+ COMMAS + msg.getString("serviceName");
            }

            queue.put(cmd);
//            if(cmd.startsWith("serviceStop")||cmd.startsWith("serviceRestart")){
//                queue.put("services");
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
