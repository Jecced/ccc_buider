package com.againfly.cccbuilder.entity;

public class FileInfo {
    private String filePath;
    private String name;
    private String outJsPath;
    private String uuid;
    private String newTs;
    private String jsPath;
    private String jsContent;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutJsPath() {
        return outJsPath;
    }

    public void setOutJsPath(String outJsPath) {
        this.outJsPath = outJsPath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNewTs() {
        return newTs;
    }

    public void setNewTs(String newTs) {
        this.newTs = newTs;
    }

    public String getJsPath() {
        return jsPath;
    }

    public void setJsPath(String jsPath) {
        this.jsPath = jsPath;
    }

    public String getJsContent() {
        return jsContent;
    }

    public void setJsContent(String jsContent) {
        this.jsContent = jsContent;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "filePath='" + filePath + '\'' +
                ", name='" + name + '\'' +
                ", outJsPath='" + outJsPath + '\'' +
                ", uuid='" + uuid + '\'' +
                ", newTs='" + newTs + '\'' +
                ", jsPath='" + jsPath + '\'' +
                ", jsContent='" + jsContent + '\'' +
                '}';
    }
}
