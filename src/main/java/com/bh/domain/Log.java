package com.bh.domain;

import java.util.Map;

public class Log {

    public Log() {
    }

    private String cluster;
    private String sevice;
    private String version;
    private String ip;
    private String appID;
    private String erp;
    private String logLevel;
    private String podname;
    private String filepath;
    private String filename;
    private String timestamp;
    private Map<String, String> tags;
    private String content;

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getSevice() {
        return sevice;
    }

    public void setSevice(String sevice) {
        this.sevice = sevice;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getErp() {
        return erp;
    }

    public void setErp(String erp) {
        this.erp = erp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getPodname() {
        return podname;
    }

    public void setPodname(String podname) {
        this.podname = podname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Log{" +
                "cluster='" + cluster + '\'' +
                ", sevice='" + sevice + '\'' +
                ", version='" + version + '\'' +
                ", ip='" + ip + '\'' +
                ", appID='" + appID + '\'' +
                ", erp='" + erp + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", podname='" + podname + '\'' +
                ", filepath='" + filepath + '\'' +
                ", filename='" + filename + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
