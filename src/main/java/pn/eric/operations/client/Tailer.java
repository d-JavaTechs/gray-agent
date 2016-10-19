package pn.eric.operations.client;

import com.corundumstudio.socketio.SocketIOServer;
import com.sun.corba.se.spi.activation.Server;
import io.socket.client.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by duwupeng on 16/10/18.
 */
public class Tailer implements Runnable{
    private static Process process;
    private static InputStream inputStream;
    private static BufferedReader reader;
    Socket socket;
    public Tailer(){
    }
    public Tailer(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {

        String line;
        try {
            process = Runtime.getRuntime().exec("tail -f /home/isuwang/gray/gray-agent/agent.log");
            inputStream = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while((line = reader.readLine()) != null) {
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                System.out.println(line);
                socket.emit("res", line);
                while((line = reader.readLine()) != null) {
                    // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                    // socket.emit("res", line);
//                    if(line.equals("cmd: ./scompose.sh")){
//                        currentEvent = "branch";
//                    }else if(line.equals("cmd: ")) {
//                        currentEvent = "build";
//                    }
//                    System.out.println("current cmd: " + currentEvent);
//
//                    if(currentEvent.equals("branch")){
//                        server.getRoomOperations("web").sendEvent("branchEvent", line);
//                    }else if(currentEvent.equals("build")){
//                        server.getRoomOperations("web").sendEvent("buildEvent", line);
//                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new Thread(new Tailer()).start();
    }
}
