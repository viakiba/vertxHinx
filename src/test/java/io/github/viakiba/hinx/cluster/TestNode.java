package io.github.viakiba.hinx.cluster;

import io.github.viakiba.hinx.base.TestBase;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeInfo;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-02
 */
public class TestNode extends TestBase {
    @Test
    public void testUpdateNodeInfo() {
        GlobalContext.initVertx(handler ->{
            AtomicInteger atomicInteger = new AtomicInteger(1);
            GlobalContext.getVertx().setPeriodic(1000, h ->{
                ClusterManager clusterManager = GlobalContext.getClusterManager();
                NodeInfo nodeInfo = clusterManager.getNodeInfo();
                nodeInfo.metadata().put("info",atomicInteger.incrementAndGet());
                clusterManager.setNodeInfo(nodeInfo, Promise.promise());
                List<String> nodes = clusterManager.getNodes();
                System.out.println(nodes);
                for (int i = 0; i < nodes.size(); i++) {
                    Promise<NodeInfo> promise = Promise.promise();
                    promise.future().onSuccess(ha ->{
                        JsonObject metadata = ha.metadata();
                        System.out.println(metadata.encodePrettily());
                    });
                    clusterManager.getNodeInfo(nodes.get(i),promise );
                }
                clusterManager.getNodeInfo();
            });
        });

    }
}
