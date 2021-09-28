package com.ohayoo.whitebird.data.client;

import com.ohayoo.whitebird.compoent.LoggerUtil;
import com.ohayoo.whitebird.data.IDBService;
import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.reactive.stage.Stage.SessionFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-26
 */
@Slf4j
public class RxHibernateService implements IDBService {
    SessionFactory factory ;

    public void init() {
        try {
            factory = createEntityManagerFactory( "mysql" )
                            .unwrap(Stage.SessionFactory.class);
        } catch (Exception e) {
            LoggerUtil.error(e.getMessage());
            throw new RuntimeException("mysql init fail!");
        }
    }

    @Override
    public void stop() {
        factory.close();
    }

    public SessionFactory getClient() {
        return factory;
    }

    //TODO
    public void queryById(long key, Class clazz
            , Handler<Object> success, Handler<Object> fail){
        factory.withSession(
                session -> session
                        .find(clazz, key)
                        .thenAccept(obj -> {
                            success.handle(obj);
                        })
                        .exceptionally(fn->{
                            fail.handle(fn);
                            return null;
                        })
        ).toCompletableFuture().join();
    }

    //TODO
    public void saveObj( Object obj, Handler<Void> complete){
        factory
                .withSession(session -> session.persist( obj ))
                .toCompletableFuture().join();
    }

}
