package pn.eric.operations.client;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import pn.eric.operations.common.CmdExecutor;
import pn.eric.operations.listener.DeployServerOperations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author duwupeng
 * @date
 */
public class Boot {
    public static void main(String[] args) throws Exception{
        IO.Options opts = new IO.Options();
        opts.forceNew = true;

        final Socket socket = IO.socket("http://localhost:9095",opts);

        final BlockingQueue queue = new LinkedBlockingQueue();

        CmdExecutor ex = new CmdExecutor(queue);

        new Thread(ex).start();
        System.out.println("Cmd Thread started");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("connected");
                socket.emit("nodeReg", "master:192.168.0.1");
            }

        }).on("webCmd", new DeployServerOperations(queue,socket)).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("disconnected");
            }
        });

        new Thread(new Tailer(socket)).start();
        System.out.println("Tailer Thread started");

        socket.connect();
    }
}
