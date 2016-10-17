package pn.eric.operations.util;

import com.corundumstudio.socketio.SocketIOClient;
import io.socket.client.Socket;
import pn.eric.operations.event.OperateCommand;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Eric on 2016-08-10
 */
public class JavaShellUtil {


    public static String executeShell(String shellCommand) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            Process pid = null;
            Calendar cal  =  Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String[] cmd = { "/bin/sh", "-c", shellCommand};
            // 执行Shell命令
            pid = Runtime.getRuntime().exec(cmd);
            if (pid != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                // 读取Shell的输出内容，并添加到stringBuffer中
                while (bufferedReader != null
                        && (line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\r\n");
                }
//              pid.waitFor();
            } else {
                stringBuffer.append("没有pid\r\n");
            }
            stringBuffer.append("Shell命令执行完毕\r\n执行结果为：\r\n");
        } catch (Exception ioe) {
            stringBuffer.append("执行命令发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
        }
        return stringBuffer.toString();
    }


    public static String executeShellAndSendMessage(OperateCommand operateCommand,Socket Socket) {
        String[] shellCommands=null;
        if (operateCommand==OperateCommand.DEPLOY){
            shellCommands=new String[]{"cd ~/scompose","git checkout master & git pull","./scompose up -d"};
        }else if(operateCommand==OperateCommand.ROLLBACK){
            shellCommands=new String[]{"./scompose s-rollback F_XHT_1.9.5"};
        }else{
            shellCommands=new String[]{"ls"};
        }
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        for(String shellCommand: shellCommands){
            String line = null;
            try {
                Process pid = null;
                Calendar cal  =  Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                String[] cmd = { "/bin/sh", "-c", shellCommand};
                // 执行Shell命令
                pid = Runtime.getRuntime().exec(cmd);
                if (pid != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                    // 读取Shell的输出内容，并添加到stringBuffer中
                    while (bufferedReader != null
                            && (line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("\r\n");
                    }
                    //pid.waitFor();
                } else {
                    stringBuffer.append("没有pid\r\n");
                }
                stringBuffer.append("Shell命令执行完毕\r\n执行结果为：\r\n");
            } catch (Exception ioe) {
                stringBuffer.append("执行命令发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
            }
        }

        return stringBuffer.toString();
    }


    public static String executeShellAndSendMessage(OperateCommand operateCommand,SocketIOClient client) {

            String[] shellCommands=null;
            if (operateCommand==OperateCommand.BRANCH){
                shellCommands=new String[]{"git branch"};
            }else if(operateCommand==OperateCommand.BUILD){
                shellCommands=new String[]{"cd ~/scompose",
                                            " & git pull",
                                            " & git checkout test",
                                            "./scompose s-pull",
                                            " ./scompose s-diff",
                                            "./scompose s-log",
                                            "./scompose s-rebuild",
                                            "./scompose s-docker-push"};
            }else{
                shellCommands=new String[]{"ls"};
            }

            StringBuffer stringBuffer = new StringBuffer();
            for(String shellCommand: shellCommands){
                BufferedReader bufferedReader = null;
                String line = null;
                try {
                    Process pid = null;
                    Calendar cal  =  Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    String[] cmd = { "/bin/sh", "-c", shellCommand};
                    // 执行Shell命令
                    stringBuffer.append(shellCommand).append("\r\n");
                    pid = Runtime.getRuntime().exec(cmd);
                    if (pid != null) {
                        bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
                        // 读取Shell的输出内容，并添加到stringBuffer中
                        while (bufferedReader != null
                                && (line = bufferedReader.readLine()) != null) {
                            stringBuffer.append(line).append("\r\n");
                        }
                        //pid.waitFor();
                    } else {
                        stringBuffer.append("没有pid\r\n");
                    }
                    stringBuffer.append("Shell命令执行完毕\r\n执行结果为：\r\n");
                } catch (Exception ioe) {
                    stringBuffer.append("执行命令发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
                }
            }
        client.sendEvent("branchEvent",stringBuffer.toString());

        return stringBuffer.toString();
    }


     public static void main(String[] args) {
           System.out.println(executeShell(args[0]));
    }
}

