package com.sdu.inas.service;

import com.sdu.inas.beans.Event;
import com.sdu.inas.beans.DetailedInfo;
import com.sdu.inas.beans.Timenode;

import java.util.List;

/**
 * Created by J on  17-10-27.
 */

public interface TimelineService {

    List<Timenode> findTimelineByObjectId(String objectId);

    Event findEventByEventId(String eventId);

    DetailedInfo findDetailinfoByTimepoint(String objectId, String timePoint) throws Exception;

    void insetTimenode(String objectId,Timenode timenode);

    void insertEvent(String objectId, Event event);

    void delTimenodeByTimepoint(String objectId,String timePoint);

    void delEvent(Event event);

}
