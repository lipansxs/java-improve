package com.lipanre.snakeyaml.config;


import com.lipanre.snakeyaml.annotation.PropertiesConfiguration;
import com.lipanre.snakeyaml.annotation.PropertiesField;

/**
 * @author Layton
 * @date 2022/4/24 14:53
 */
@PropertiesConfiguration
public class MqttConfiguration {

    @PropertiesField(value = "mqtt.host")
    private String host = "120.24.31.121";

    @PropertiesField(value = "mqtt.port", type = Integer.class)
    private Integer port = 3883;

    @PropertiesField(value = "mqtt.username")
    private String username = "admin1";

    @PropertiesField(value = "mqtt.password")
    private String password = "8wUhdgc4VD";


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
