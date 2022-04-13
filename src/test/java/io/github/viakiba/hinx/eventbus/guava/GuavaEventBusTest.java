package io.github.viakiba.hinx.eventbus.guava;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.Executors;

class LoginEvent {
    private String username;
    private String age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

class LoginEventHandler {
     public LoginEventHandler(){}
    @Subscribe
    public void LoginEventHandler(LoginEvent msg) {
        System.out.println("String msg: " + msg.getUsername());
    }
}

class LoginEventHandler1 {
    public LoginEventHandler1(){}
    @Subscribe
    public void LoginEventHandler(LoginEvent msg) {
        System.out.println("1String msg: " + msg.getUsername());
    }
}

public class GuavaEventBusTest {
    private static EventBus eventBus = new EventBus();

    private static EventBus asyncEventBus = new AsyncEventBus("xxx", Executors.newSingleThreadExecutor());

    public static void main(String[] args) throws Exception {
        test1();
        test2();
        System.exit(0);
    }

    private static void test1() {
        eventBus.register(new LoginEventHandler());
        eventBus.register(new LoginEventHandler1());
        asyncEventBus.register(new LoginEventHandler());
        asyncEventBus.register(new LoginEventHandler1());
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setAge("12");
        loginEvent.setUsername("viakiba");
        asyncEventBus.post(loginEvent);
        eventBus.post(loginEvent);
    }

    private static void test2() throws Exception {
        org.reflections.Reflections reflections = new org.reflections.Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("io.github.viakiba.hinx.eventbus.guava"))
                        .addScanners(Scanners.MethodsAnnotated));
        Set<Method> annotatedWith = reflections.getMethodsAnnotatedWith(Subscribe.class);
        for (Method p : annotatedWith) {
            Object o = p.getDeclaringClass().getConstructor().newInstance();
            eventBus.register(o);
            asyncEventBus.register(o);
        }
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setAge("12");
        loginEvent.setUsername("viakiba");
        asyncEventBus.post(loginEvent);
        eventBus.post(loginEvent);
    }
}

