package com.lkx.code.netty.rpc.registry.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程信息
 *
 * @author ： liukx
 * @time ： 2019/8/27 - 15:57
 */
public class RegisterServerData {

    private int timeOut;

    public RegisterServerData(int timeOut) {
        this.timeOut = timeOut;
    }

    private Map<String, Long> remoteMap = new ConcurrentHashMap<>();

    public void add(String key) {
        remoteMap.put(key, new Date().getTime());
    }


    public boolean validTime(String key) {
        Long time = remoteMap.get(key);

        if (time != null) {
            long currentTime = new Date().getTime();

            long outTime = time + timeOut;

            if (currentTime > outTime) {
                remoteMap.remove(key);
                return false;
            }
        }
        return true;
    }

    public List<String> getList() {
        List<String> list = new ArrayList<>();
        if (remoteMap.size() > 0) {
            remoteMap.forEach((K, V) -> {
                if (validTime(K)) {
                    list.add(K);
                }
            });
        }
        return list;
    }

}
