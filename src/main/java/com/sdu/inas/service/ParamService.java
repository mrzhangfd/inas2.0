package com.sdu.inas.service;

import com.sdu.inas.beans.Tuple;

import java.util.List;

public interface ParamService {

    void delParam(String objectId,String key);
    void insertParam(String objectId,String key,String value);
    List<Tuple> getAllParams(String objectId);
}
