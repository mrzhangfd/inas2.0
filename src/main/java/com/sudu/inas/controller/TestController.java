package com.sudu.inas.controller;


import com.sudu.inas.beans.*;
import com.sudu.inas.service.*;
import com.sudu.inas.util.CommonUtil;

import org.apache.hadoop.mapreduce.ID;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
public class TestController {

    @Autowired
    ObjectService objectService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    RawinfoService rawinfoService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    RelevanceService relevanceService;

    @RequestMapping(value = "/graphtest.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getSingLineGraph(String objectId) throws Exception {
        Map<String, ArrayList> map = singLineGraphforId(objectId, 0, null);
        return map;
    }

    @RequestMapping(value = "/graphforconn.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getConnLine(String objectId, String selectedId, int num) throws Exception {
        System.out.println(objectId);
        System.out.println(selectedId);
        System.out.println(num);
        Map<String, ArrayList> map = singLineGraphforId(selectedId, num, objectId);
        return map;
    }


    @RequestMapping(value = "/searchtest.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> getDesLlist(String prefix) throws Exception {
        HashMap<String, String> desList = new HashMap<>();
//        List<Entity> objectsByPrefix = objectService.findObjectsByPrefix(prefix);
        List<RealEntity> entitiesByPrefix = objectService.findEntitiesByPrefix(prefix);
        for (RealEntity entity : entitiesByPrefix) {
            desList.put(entity.getObjectId(), entity.getRealName());
        }
        return desList;
    }

    @RequestMapping(value = "/saveNewEntitytest.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> saveNewEntity(String name, String description, String prefix) throws Exception {

        String objectId = name + CommonUtil.genRandomNum();
        String realName = name + " " + description;
        rawinfoService.insertRealName(realName, objectId);
        UUID uuid=UUID.randomUUID();
        String  eventId = uuid.toString();
        timelineService.insertEvent(objectId,new Event(eventId,objectId,"2050-01-01","",realName,""));

        HashMap<String, String> desList = new HashMap<>();
        List<RealEntity> objectsByPrefix = objectService.findEntitiesByPrefix(prefix);
        for (RealEntity entity : objectsByPrefix) {
            desList.put(entity.getObjectId(), entity.getRealName());
        }
        return desList;
    }

    public Map<String, ArrayList> singLineGraphforId(String objectId, int num, String reEntityId) throws Exception {
        HashMap<String, ArrayList> map = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        int xBegin = 5;
        int yBegin = 5;
        int x = xBegin;
        int y = yBegin + num * 5;
        RealEntity realEntity = objectService.findEntityByIdFromEs(objectId);
        if (null == realEntity) {
            return null;
        }
        ArrayList<Event> events = realEntity.getEvents();
        int change = 2;
        for (Event event : events) {
            Node node = new Node();
            if (num == 0) {
                node.setColor("red");
            } else {
                node.setColor("blue");
            }
            node.setSize(20);
            node.setSerial(CommonUtil.toDays(event.getTs()));
            if (!(change % 2 == 0)) {
                node.setLabel("\n" + event.pack());
            }else {
                node.setLabel(event.pack());
            }
            change++;
//            node.setLabel(event.pack());
            if (event.getTs().equals("2050-01-01")) {
                node.setLabel("\n"+realEntity.getRealName());
                node.setSerial(0);
            }

            node.setId(objectId + ":" + event.getEventId());
            nodes.add(node);
        }

        Collections.sort(nodes, (o1, o2) -> {
            if (o1.getSerial() > o2.getSerial()) {
                return 1;
            }
            if (o1.getSerial() == o2.getSerial()) {
                return 0;
            }
            return -1;
        });

        if (nodes.size() > 1) {
            for (int i = 0; i < nodes.size() - 1; i++) {
                Node sourceNode = nodes.get(i);
                String source = sourceNode.getId();
                sourceNode.setX(x + i * 10);
                sourceNode.setY(y);
                Node targetNode = nodes.get(i + 1);
                String target = targetNode.getId();
                targetNode.setX(x + (i + 1) * 10);
                targetNode.setY(y);
                Edge edge = new Edge(source, target, "");
                edges.add(edge);
            }
        }

        if (reEntityId != null) {
            List<Relevance> outRele = relevanceService.getRelevancesByEntityIds(objectId, reEntityId);
            for (Relevance relevance : outRele) {
                if (relevance != null) {
                    String source = objectId + ":" + relevance.getSourceEventId();
                    String target = reEntityId + ":" + relevance.getTargetEventId();
                    Edge edge = new Edge(source, target, relevance.getDescription());
                    edges.add(edge);
                }
            }

            List<Relevance> inRele = relevanceService.getRelevancesByEntityIds(reEntityId, objectId);
            for (Relevance relevance : inRele) {
                if (relevance != null) {
                    String source = reEntityId + ":" + relevance.getSourceEventId();
                    String target = objectId + ":" + relevance.getTargetEventId();
                    Edge edge = new Edge(source, target, relevance.getDescription());
                    edges.add(edge);
                }
            }

        }
        map.put("nodes", nodes);
        map.put("edges", edges);
        return map;
    }

    @RequestMapping(value = "/commit.do", method = RequestMethod.POST)
    public @ResponseBody
    String commitEdit(String newNodes, String newEdges, String rawinfo,String id) {
        try {
            JSONArray nodeArray = new JSONArray(newNodes);
            for (int i = 0; i < nodeArray.length(); i++) {
                JSONObject node = (JSONObject) nodeArray.get(i);
                String pointId = node.getString("id");
                String[] ids = pointId.split(":");
                String objectId = ids[0];
                String eventId = ids[1];
                Event event = new Event(eventId, objectId, node.getString("timepoint"), node.getString("location"), node.getString("description"), "");
                System.out.println(event);
                timelineService.insertEvent(objectId, event);
            }
            JSONArray edgeArray = new JSONArray(newEdges);
            for (int i = 0; i < edgeArray.length(); i++) {
                JSONObject edge = (JSONObject) edgeArray.get(i);
                String sourceID = edge.getString("sourceID");
                String[] sids = sourceID.split(":");
                String sourceObjectId = sids[0];
                String sourceEventId = sids[1];
                String targetID = edge.getString("targetID");
                String[] tids = targetID.split(":");
                String targetObjectId = tids[0];
                String targetEventId = tids[1];
                Relevance relevance = new Relevance(CommonUtil.getUUID(), "", edge.getString("note"), sourceEventId, sourceObjectId, targetEventId, targetObjectId);
                System.out.println(relevance);
                relevanceService.addRelevance(relevance);

            }

//            String rawText = rawinfoService.findRawText(id);
//            rawinfoService.addRawText(rawText+rawinfo,id);
            System.out.println(id);
            System.out.println(rawinfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "OK";
    }

    public static void main(String[] args) {
    }

    @RequestMapping("/gggg")
    public String getSingleGraph() {
        return "singlegraph";
    }

    @RequestMapping("/demo")
    public String getDemoGraph() {
        return "demo";
    }


}
