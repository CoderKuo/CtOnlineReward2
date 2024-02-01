package cn.ctcraft.ctonlinereward.common.pojo.dao;

import org.bukkit.configuration.serialization.SerializableAs;

import java.beans.Transient;
import java.io.Serializable;

public class OnlineDataDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;
    private Long startTime;
    private Long endTime;

    public OnlineDataDao(String uuid, Long startTime, Long endTime) {
        this.uuid = uuid;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OnlineDataDao{" +
                "uuid='" + uuid + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
