package com.sdu.inas.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;

/**
 * 对应Mongodb中的event
 *
 * @author icatzfd
 * Created on 2020/5/21 16:30.
 */
@Data
public class NewEvent {

    @JSONField(ordinal = 0)
    private String _id;
    @JSONField(ordinal = 1)
    private String obj_id;
    @JSONField(ordinal = 2)
    private String date;
    @JSONField(ordinal = 3)
    private String eventIntro;
    @JSONField(ordinal = 4)
    private String eventDetail;
    @JSONField(ordinal = 5)
    private ArrayList<String> pName = new ArrayList<>();
    @JSONField(ordinal = 6)
    private ArrayList<String> sName = new ArrayList<>();



}
