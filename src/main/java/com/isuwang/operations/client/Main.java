package com.isuwang.operations.client;

import com.isuwang.operations.contex.SystemParas;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import com.isuwang.operations.listener.DeployServerOperations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author duwupeng
 * @date
 */
public class Main {
    public static void main(String[] args) throws Exception{
//        if(args!=null&&args.length==3){
//            System.err.println("参数不正确");
//            System.exit(-1);
//        }
        System.out.println("args[0]: " + args[0]);
        System.out.println("args[1]: " + args[1]);
        System.out.println("args[2]: " + args[2]);

        System.out.println("....................");
        IO.Options opts = new IO.Options();
        opts.forceNew = true;

        final Socket socket = IO.socket("http://"+ args[0]+":9095",opts);

        final BlockingQueue queue = new LinkedBlockingQueue();

        CmdExecutor ex = new CmdExecutor(queue,socket);

        new Thread(ex).start();
        System.out.println("Cmd Thread started");

        String hostName = args[1];
        String ip = args[2];

        System.out.println(String.format("hostName:%s  ip:%s", hostName,ip));

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("connected");
                socket.emit("nodeReg", String.format("%s:%s",hostName,ip));
            }

        }).on("webCmd", new DeployServerOperations(queue,socket)).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("disconnected");
            }
        });

//        System.out.println("begin to connect server" +args[0] );
        socket.connect();
    }
}
