package io.github.viakiba.hinx.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.testng.annotations.Test;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-28
 */
public class TestRxJava {

    @Test
    public void testRxJava_1() {
        Observable.create(x ->{
            int count = 1;
            while(true) {
                x.onNext(count);
                count++;
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                System.out.println("o = " + o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
