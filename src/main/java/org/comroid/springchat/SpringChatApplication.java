package org.comroid.springchat;

import org.comroid.api.io.FileHandle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"org.comroid.springchat.config", "org.comroid.springchat.controller"})
@EntityScan(basePackages = {"org.comroid.springchat.entity"})
public class SpringChatApplication {
    public static final FileHandle PATH_BASE = new FileHandle("/srv/chat/", true); // server path base
    public static final FileHandle OAUTH_FILE = PATH_BASE.createSubFile("oauth2.json");
    public static void main(String[] args) {
        SpringApplication.run(SpringChatApplication.class, args);
    }
}

