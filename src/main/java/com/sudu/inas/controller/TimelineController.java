package com.sudu.inas.controller;

import com.sudu.inas.beans.*;
import com.sudu.inas.service.ConnectionService;
import com.sudu.inas.service.RelevanceService;
import com.sudu.inas.service.TimelineService;
import com.sudu.inas.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on  17-10-27.
 */

@Controller
public class TimelineController {

    @Autowired
    TimelineService timelineService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    RelevanceService relevanceService;

    @RequestMapping(value = "/timenode/{eventId}", method = RequestMethod.GET)
    public String getTimeNode(@PathVariable String eventId, Model model) throws Exception {

        Event event = timelineService.findEventByEventId(eventId);
        model.addAttribute("event", event);
        List<Relevance> relevances = relevanceService.getRelevancesByEventId(eventId);
        ArrayList<Event> reledEvents = new ArrayList<>();
        for (Relevance relevance : relevances) {
            if (eventId.equals(relevance.getSourceEventId())) {
                Event event1 = timelineService.findEventByEventId(relevance.getTargetEventId());
                event1.setEventId(relevance.getrId());
                reledEvents.add(event1);
            } else {
                Event event1 = timelineService.findEventByEventId(relevance.getSourceEventId());
                event1.setEventId(relevance.getrId());
                reledEvents.add(event1);
            }
        }
        model.addAttribute("reledEvents", reledEvents);
        return "showtimenode";
    }

    @RequestMapping(value = "/edittimenode/{eventId}", method = RequestMethod.GET)
    public String editTimeNode(@PathVariable String eventId, Model model) {
        Event event = new Event();
        try {
            event = timelineService.findEventByEventId(eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("event", event);
        return "editnodeform";
    }

    @RequestMapping(value = "/edittimenode/{eventId}", method = RequestMethod.POST)
    public String saveEditedNode(@PathVariable String eventId, Event event) throws UnsupportedEncodingException {
        Event oldEvent = timelineService.findEventByEventId(eventId);
        oldEvent.setSite(event.getSite());
        oldEvent.setDetails(event.getDetails());
        oldEvent.setAffect(event.getAffect());
        timelineService.insertEvent(oldEvent.getObjectId(), oldEvent);
        String id = oldEvent.getObjectId();
        return "redirect:/object/" + URLEncoder.encode(id,"UTF-8");
    }

    @RequestMapping("/deletenode/{eventId}")
    public String deleteTimeNode(@PathVariable String eventId) throws UnsupportedEncodingException {

        Event event = timelineService.findEventByEventId(eventId);
        timelineService.delEvent(event);
        return "redirect:/object/" + URLEncoder.encode(event.getObjectId(),"UTF-8");

    }


    @RequestMapping(value = "/newtimenode/{objectId}", method = RequestMethod.GET)
    public String newtimenode(@PathVariable String objectId, Model model) {
        model.addAttribute("objectId", objectId);
        return "newtimenode";
    }

    @RequestMapping(value = "/recons", method = RequestMethod.POST)
    public String saveObjectTimenode(String objectId, String time, String location, String description, String result) throws UnsupportedEncodingException {
        Event event = new Event();
        event.setObjectId(objectId);
        event.setEventId(CommonUtil.getUUID());
        event.setTs(time);
        event.setSite(location);
        event.setDetails(description);
        event.setAffect(result);
        timelineService.insertEvent(objectId, event);
        return "redirect:/object/" + URLEncoder.encode(objectId,"UTF-8");
    }

}
