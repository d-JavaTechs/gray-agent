package pn.eric.operations.common;

import pn.eric.operations.util.ShellInvoker;
import java.util.concurrent.BlockingQueue;

/**
 * Created by duwupeng on 16/10/18.
 */
public class CmdExecutor implements Runnable{
    public BlockingQueue queue;

    public CmdExecutor(BlockingQueue queue) {
        this.queue = queue;
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
