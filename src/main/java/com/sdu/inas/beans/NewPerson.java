package com.sdu.inas.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 人物表
 *
 * @author icatzfd
 * Created on 2020/5/21 15:33.
 */
@Data
public class NewPerson {
    @JSONField(ordinal = 0)
    private String _id;

    @JSONField(ordinal = 1)
    private String figureName;

    @JSONField(ordinal = 2)
    private String intro;

    @JSONField(ordinal = 3)
    private ArrayList<String> events = new ArrayList<>();
}
