package pn.eric.operations.po;

/**
 * @author Shadow
 * @date
 */
public class ServerObject {
    String name;
    String ip;

    public ServerObject(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
