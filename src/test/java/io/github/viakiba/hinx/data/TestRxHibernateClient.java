package io.github.viakiba.hinx.data;

import io.github.viakiba.hinx.base.TestBase;
import io.github.viakiba.hinx.data.model.GameData;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.testng.annotations.Test;

import static javax.persistence.Persistence.createEntityManagerFactory;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-28
 */
@Slf4j
public class TestRxHibernateClient extends TestBase {

    @Test
    public void test0() throws Exception {
        Stage.SessionFactory factory =
                createEntityManagerFactory( "mysql" )
                        .unwrap(Stage.SessionFactory.class);
        GameData gameData = new GameData();
        gameData.setId(11111111);
        gameData.setName("2222222222222");
        try {
            // obtain a reactive session
            factory.withTransaction(
                    // persist the Authors with their Books in a transaction
                    (session, tx) -> session.persist( gameData )
            )
                    // wait for it to finish
                    .toCompletableFuture().join();

            factory.withSession(session ->
                session
                        .find(GameData.class, 11111111L)
                        .thenAccept(book -> {
                            System.out.println(book.getName());
                        })
                        .exceptionally(fn->{
                            fn.printStackTrace();
                            return null;
                        })
                    ).toCompletableFuture().join();
        }finally {
            Thread.sleep(10000);
            factory.close();
        }
    }
}
