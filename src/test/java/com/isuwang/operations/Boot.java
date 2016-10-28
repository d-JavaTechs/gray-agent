package com.isuwang.operations;

import com.isuwang.operations.po.WebObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import com.isuwang.operations.client.CmdExecutor;
import com.isuwang.operations.listener.DeployServerOperations;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author duwupeng
 * @date
 */
public class Boot {
    public static void main(String[] args) throws Exception{
        List<String> commands = Arrays.asList("branch", "build", "deploy", "rollback", "docker", "services", "serviceRestart", "serviceStop");
        System.out.println(commands.contains("branch1"));
//        IO.Options opts = new IO.Options();
//        opts.forceNew = true;
//
//        final Socket socket = IO.socket("http://127.0.0.1:9095",opts);
//
//        final BlockingQueue queue = new LinkedBlockingQueue();

//        CmdExecutor ex = new CmdExecutor(queue);

//        new Thread(ex).start();

//
//
//        System.out.println("Cmd Thread started");
//
//        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            public void call(Object... args) {
//                System.out.println("connected");
//                //socket.emit("webEvent", "branches");
//                //socket.emit("webEvent", "build");
//                //socket.emit("webEvent", "rollBack");
//                //socket.emit("webEvent", "deploy");
//               try{
//                   JSONObject wb = new JSONObject();
//                   wb.put("msg","123");
//                   socket.emit("branches", wb);
//               }catch (Exception e){
//
//               }
//
//            }
//
//        }).on("webCmd", new DeployServerOperations(queue,socket)).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//            public void call(Object... args) {
//                System.out.println("disconnected");
//            }
//        });
//
//
//        socket.connect();
    }
}
