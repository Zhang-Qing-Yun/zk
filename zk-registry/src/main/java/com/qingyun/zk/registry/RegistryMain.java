package com.qingyun.zk.registry;

import com.alibaba.fastjson.JSON;
import com.qingyun.zk.common.constants.ZKConstants;
import com.qingyun.zk.common.zk.CuratorZKClient;
import com.qingyun.zk.registry.pojo.DBConfig;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import java.nio.charset.StandardCharsets;

/**
 * @description： 模拟zk充当注册中心的情景
 * @author: 張青云
 * @create: 2021-12-17 18:40
 **/
public class RegistryMain {
    public static void main(String[] args) throws Exception {
        /*
        * 思路：
        * 1.首先管理员将配置发布到ZK对应的配置结点上；
        * 2.集群中的每一台机器在启动时，从ZK的配置节点上读取配置信息，并在该结点上注册一个Watcher，来监听数据变更事件；
        * 3.管理员修改ZK配置结点里的配置信息，ZK服务端通知客户端发生了数据变更；
        * 4.客户端从ZK中拉取最新的配置信息；
        * */

        // 充当配置信息的管理员
        CuratorZKClient configManager = new CuratorZKClient("8.140.166.139:2181").init();
        // 充当集群中的一个结点
        CuratorZKClient zkClient = new CuratorZKClient("8.140.166.139:2181").init();

        // 步骤一：发布配置信息
        DBConfig config = new DBConfig().setDriverClassName("com.mysql.cj.jdbc.Driver")
                .setUrl("localhost:3306")
                .setUsername("root")
                .setPassword("123456");
        configManager.createNode(ZKConstants.CONFIGURATION, JSON.toJSONString(config));

        // 步骤二：集群结点初始化时去拉取配置信息，并注册Watcher
        byte[] nodeData = zkClient.getNodeData(ZKConstants.CONFIGURATION);
        DBConfig clientConfig = JSON.parseObject(nodeData, DBConfig.class);
        System.out.println("集群结点初始化了配置信息：" + clientConfig);
        NodeCache nodeCache = new NodeCache(zkClient.getClient(), ZKConstants.CONFIGURATION);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                byte[] data = nodeCache.getCurrentData().getData();
                DBConfig clientConfig = JSON.parseObject(data, DBConfig.class);
                System.out.println("集群结点更新了配置信息：" + clientConfig);
            }
        });
        nodeCache.start();

        // 步骤三：管理员修改配置信息
        config.setUrl("localhost:3307");
        configManager.setNodeData(ZKConstants.CONFIGURATION, JSON.toJSONString(config).getBytes(StandardCharsets.UTF_8));

        Thread.sleep(3000);
        configManager.getClient().close();
        zkClient.getClient().close();
    }
}
