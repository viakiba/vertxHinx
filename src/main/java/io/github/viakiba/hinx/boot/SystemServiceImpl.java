package io.github.viakiba.hinx.boot;

import java.io.IOException;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-23
 */
public interface SystemServiceImpl {

    void start() throws IOException;

    default void stop(){}

}
