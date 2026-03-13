package com.brainwave.core.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用於標記需要稽核的操作。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    /**
     * 操作名稱，例如 CREATE_USER、UPDATE_CONFIG。
     */
    String action();

    /**
     * 資源名稱，例如 USER、SYSTEM_CONFIG。
     */
    String resource() default "";
}

