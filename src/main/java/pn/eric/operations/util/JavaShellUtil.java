package pn.eric.operations.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Eric on 2016-08-10
 */
public class JavaShellUtil {

    public static String executeShell(String shellCommand) throws IOException {
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
    public static void main(String[] args) {
        try {
           System.out.println(executeShell(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

