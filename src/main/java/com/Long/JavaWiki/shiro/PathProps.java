package com.Long.JavaWiki.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * URL拦截规则
 */
@ConfigurationProperties(prefix = "shiro")
public class PathProps {
    private List<String> pathList;

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
