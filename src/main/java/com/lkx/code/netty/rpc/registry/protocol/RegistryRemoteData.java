package com.lkx.code.netty.rpc.registry.protocol;

import java.io.Serializable;
import java.util.Objects;

/**
 * 注册数据传输
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 16:31
 */
public class RegistryRemoteData implements Serializable {

    public int type;

    public String project;

    public String ip;

    public int port;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RegistryRemoteData{" +
                "type=" + type +
                ", project='" + project + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistryRemoteData)) return false;
        RegistryRemoteData that = (RegistryRemoteData) o;
        return getPort() == that.getPort() &&
                getProject().equals(that.getProject()) &&
                getIp().equals(that.getIp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProject(), getIp(), getPort());
    }
}
