package pn.eric.operations.common;

import pn.eric.operations.util.ShellInvoker;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by eric on 16/10/18.
 */
public class CmdExecutor implements Runnable{
    final BlockingQueue queue = new LinkedBlockingQueue();
    public BlockingQueue getQueue() {
        return queue;
    }
    @Override
    public void run() {
            while(true) {
                try {
                    System.out.println("Consumed: "+ queue.take());
                    String cmd  = (String)queue.take();
                    ShellInvoker.executeShell(cmd);
                } catch (InterruptedException ex) {
                   ex.printStackTrace();
                }
            }
    }
}
