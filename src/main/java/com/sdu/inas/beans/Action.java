package com.sdu.inas.beans;

import com.google.common.base.MoreObjects;
import lombok.Data;

/**
 * 事件类，就是实体在一个时间节点发生的事情。
 * 包括实体，时间点，位置，描述。
 * @author jgy
 */
@Data
public class Action {

    private String entityName;
    private String timepoint;
    private String location;
    private String description;

    public Action(String entityName, String timepoint, String location, String description) {
        this.entityName = entityName;
        this.timepoint = timepoint;
        this.location = location;
        this.description = description;
    }

    public Action() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entityName", entityName)
                .add("timepoint", timepoint)
                .add("location", location)
                .add("description", description)
                .toString();
    }
}
