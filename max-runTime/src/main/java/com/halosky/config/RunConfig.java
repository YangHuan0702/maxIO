package com.halosky.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * packageName com.halosky.config
 *
 * @author huan.yang
 * @className RunConfig
 * @date 2026/4/14
 * @description runTime配置类
 */
@Slf4j
public record RunConfig(String nacosServer, String nacosUserName, String password, String namespace) {


    public static RunConfig readConfig(String configFilePath) {
        Properties prop = new Properties();

        try (InputStream in = RunConfig.class.getClassLoader().getResourceAsStream(configFilePath)) {
            if (Objects.isNull(in)) throw new NullPointerException(configFilePath + " not found");
            prop.load(in);
        } catch (IOException e) {
            log.error("[RunConfig] open target config file {} error.", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return new RunConfig(prop.getProperty(ConfigConstant.NACOS_SERVER), prop.getProperty(ConfigConstant.NACOS_USERNAME),
                prop.getProperty(ConfigConstant.NACOS_PASSWORD), prop.getProperty(ConfigConstant.NACOS_NAMESPACE));
    }

}
