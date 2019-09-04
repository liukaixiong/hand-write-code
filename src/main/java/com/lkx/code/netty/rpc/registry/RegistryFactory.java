package com.lkx.code.netty.rpc.registry;

import com.lkx.code.netty.rpc.registry.protocol.RegistryRemoteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心存储工厂
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 15:35
 */
public class RegistryFactory {
    private Logger logger = LoggerFactory.getLogger(RegistryFactory.class);
    /**
     * 项目对应的注册地址
     */
    private Map<String, List<RegistryRemoteData>> remoteInfoMap = new ConcurrentHashMap<>();
    /**
     * 注册地址对应的项目
     */
    private Map<InetSocketAddress, String> hostMap = new ConcurrentHashMap<InetSocketAddress, String>();


    private int timeOut = 10;

    public RegistryFactory(int timeOut) {
        this.timeOut = timeOut;
    }

    public void add(String project, String ip, int port) {
        List<RegistryRemoteData> registryRemoteDataList = remoteInfoMap.get(project);
        RegistryRemoteData registryRemoteData = putList(project, ip, port);
        if (registryRemoteDataList == null) {
            registryRemoteDataList = new ArrayList<>();
        } else if (registryRemoteDataList.contains(registryRemoteData)) {
            logger.info(" 重复注册  : " + project + " 地址 : " + ip + ":" + port);
            return;
        }
        registryRemoteDataList.add(registryRemoteData);
        remoteInfoMap.put(project, registryRemoteDataList);
        InetSocketAddress hostInfo = InetSocketAddress.createUnresolved(ip, port);
        hostMap.put(hostInfo, project);
        logger.info(" 新注册一个服务 : " + project + " 地址 : " + ip + ":" + port);
    }

    private RegistryRemoteData putList(String project, String ip, int port) {
        RegistryRemoteData remoteInfo = new RegistryRemoteData();
        remoteInfo.setProject(project);
        remoteInfo.setIp(ip);
        remoteInfo.setPort(port);
        return remoteInfo;
    }

    public List<RegistryRemoteData> getList(String project, Integer type) {
        List<RegistryRemoteData> registryRemoteDataList = remoteInfoMap.get(project);
        for (int i = 0; i < registryRemoteDataList.size(); i++) {
            RegistryRemoteData registryRemoteData = registryRemoteDataList.get(i);
            registryRemoteData.setType(type);
        }
        return registryRemoteDataList;
    }

    public String getHostByProject(String ip, int port) {
        InetSocketAddress hostInfo = InetSocketAddress.createUnresolved(ip, port);
        return hostMap.get(hostInfo);
    }

}
