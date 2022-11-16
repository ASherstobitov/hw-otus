package ru.otus.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class AppConfig {

    private static final int THREAD_POOL_SIZE = 4;

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE,
                new ThreadFactory() {

                    private final AtomicLong treadIdGenerator = new AtomicLong(0);

                    @Override
                    public Thread newThread(@NonNull Runnable task) {

                        var thread = new Thread(task);
                        thread.setName("server-thread-" + treadIdGenerator.incrementAndGet());
                        return thread;
                    }
                });

        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(THREAD_POOL_SIZE, new ThreadFactory() {
            private final AtomicInteger threadIdGenerator = new AtomicInteger(0);

            @Override
            public Thread newThread(@NonNull Runnable task) {
                var tread = new Thread(task);

                tread.setName("client-thread-" + threadIdGenerator.incrementAndGet());
                return tread;
            }
        });

        ReactorResourceFactory reactorResourceFactory = new ReactorResourceFactory();
        reactorResourceFactory.setLoopResources(b -> eventLoopGroup);
        reactorResourceFactory.setUseGlobalResources(false);
        return reactorResourceFactory;
    }

    @Bean
    public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory resourceFactory) {
        return new ReactorClientHttpConnector(resourceFactory, mapper -> mapper);
    }
}
