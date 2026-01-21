package com.brainwave.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Brainwave Service Configuration
 * 服務層模組配置，供外部模組透過 @Import 方式引入使用
 */
@Configuration
@ComponentScan("com.brainwave.service")
public class BrainwaveServiceConfiguration {

}
