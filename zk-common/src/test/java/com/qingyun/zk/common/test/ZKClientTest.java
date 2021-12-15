package com.qingyun.zk.common.test;

import com.qingyun.zk.common.constants.ZKConstants;
import com.qingyun.zk.common.zk.CuratorZKClient;
import org.junit.Before;
import org.junit.Test;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-12-16 02:01
 **/
public class ZKClientTest {
    private CuratorZKClient zkClient;

    @Before
    public void init() {
        zkClient = new CuratorZKClient("8.140.166.139:2181").init();
    }

    @Test
    public void createNodeTest() throws Exception {
        zkClient.createNode(ZKConstants.ZK_APPLICATION, null);
    }

    @Test
    public void deleteNodeTest() throws Exception {
        zkClient.deleteNode(ZKConstants.ZK_APPLICATION);
    }
}
