package com.isuwang.operations.po;

/**
 * @author duwupeng
 * @date
 */
public class WebObject {
    String branchName;
    String cmd;
    String nodeName;
    String ip;
    boolean footSelected;
    boolean bodySelected;
    String sessionId;
    String serviceName;

    public WebObject() {

    }

    public WebObject(String nodeName, String ip,boolean footSelected,boolean bodySelected,String sessionId) {
        this.nodeName = nodeName;
        this.ip = ip;
        this.footSelected=footSelected;
        this.bodySelected=bodySelected;
        this.sessionId=sessionId;
    }


    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String msg) {
        this.cmd = msg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isFootSelected() {
        return footSelected;
    }

    public void setFootSelected(boolean footSelected) {
        this.footSelected = footSelected;
    }

    public boolean isBodySelected() {
        return bodySelected;
    }

    public void setBodySelected(boolean bodySelected) {
        this.bodySelected = bodySelected;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "WebObject{" +
                "branchName='" + branchName + '\'' +
                ", cmd='" + cmd + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", ip='" + ip + '\'' +
                ", footSelected=" + footSelected +
                ", bodySelected=" + bodySelected +
                ", sessionId='" + sessionId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
