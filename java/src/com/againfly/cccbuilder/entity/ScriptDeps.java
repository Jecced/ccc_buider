package com.againfly.cccbuilder.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

public class ScriptDeps {
    @JSONField(serialize = false)
    private String path;

    private String file;

    @JSONField(serialize = false)
    private String name;

    @JSONField(serialize = false)
    private int index;

    private boolean isNodeModule = false;

    private Map<String, Integer> deps = new HashMap<>();

    public ScriptDeps(){

    }

    public ScriptDeps(String path, int index){
        this.path = path;

        String file = "preview-scripts/" + path.substring(path.indexOf("quick-scripts") + "quick-scripts".length());
        this.file = file;

        this.index = index;
        this.name = file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isNodeModule() {
        return isNodeModule;
    }

    public void setNodeModule(boolean nodeModule) {
        isNodeModule = nodeModule;
    }

    public Map<String, Integer> getDeps() {
        return deps;
    }

    public void setDeps(Map<String, Integer> deps) {
        this.deps = deps;
    }

    @Override
    public String toString() {
        return "ScriptDeps{" +
                "path='" + path + '\'' +
                ", file='" + file + '\'' +
                ", name='" + name + '\'' +
                ", index=" + index +
                ", isNodeModule=" + isNodeModule +
                ", deps=" + deps +
                '}';
    }
}
