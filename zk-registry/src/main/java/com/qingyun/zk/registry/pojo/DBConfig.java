package com.qingyun.zk.registry.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description： 模拟数据库的配置信息
 * @author: 張青云
 * @create: 2021-12-17 18:40
 **/
@Data
@Accessors(chain = true)
public class DBConfig {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
