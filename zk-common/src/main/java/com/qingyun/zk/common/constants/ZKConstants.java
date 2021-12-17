package com.qingyun.zk.common.constants;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-12-16 01:58
 **/
public interface ZKConstants {
    /**
     * ZK应用相关结点在服务端的路径
     */
    String ZK_APPLICATION = "/zk-application";

    /**
     * ZK充当注册中心使用的结点
     */
    String CONFIGURATION = ZK_APPLICATION + "/configuration";
}
