package com.halosky;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.halosky.config.ConfigConstant;
import com.halosky.config.RunConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

/**
 * packageName com.halosky
 *
 * @author huan.yang
 * @className Catalog
 * @date 2026/4/14
 * @description 目录管理
 */
@Slf4j
public class Catalog {

    private final RunConfig config;

    private ConfigService configService;


    public Catalog(String configFileName) {
        config = RunConfig.readConfig(configFileName);
        initNacosServer();
    }

    public Catalog() {
        config = RunConfig.readConfig(ConfigConstant.DEFAULT_CONFIG_NAME);
        initNacosServer();
    }


    private void initNacosServer() {
        Properties configProps = new Properties();
        configProps.put(ConfigConstant.NACOS_SERVER_ADDR, config.nacosServer());
        configProps.put(ConfigConstant.NACOS_SERVER_NAMESPACE, config.namespace());
        configProps.put(ConfigConstant.NACOS_SERVER_USERNAME, config.nacosUserName());
        configProps.put(ConfigConstant.NACOS_SERVER_PASSWORD, config.password());

        try {
            configService = NacosFactory.createConfigService(configProps);
        } catch (Exception e) {
            log.error("[Catalog] init Nacos Server error : {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public void close() {
        try {
            if (Objects.nonNull(configService)) {
                this.configService.shutDown();
            }
        } catch (Exception e) {
            log.error("[Catalog] close Nacos Server error : {}", e.getMessage(), e);
        }
    }
}
