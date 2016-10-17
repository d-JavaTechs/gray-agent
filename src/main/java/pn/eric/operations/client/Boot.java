package pn.eric.operations.client;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import pn.eric.operations.listener.DeployServerOperations;

/**
 * @author duwupeng
 * @date
 */
public class Boot {
    public static void main(String[] args) throws Exception{
        final Socket socket = IO.socket("http://localhost:9095");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("connected");
                socket.emit("operas", "hi , I am duwupeng");
            }

        }).on("webCmd", new DeployServerOperations()).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("disconnected");
            }
        });
        socket.connect();
    }
}
