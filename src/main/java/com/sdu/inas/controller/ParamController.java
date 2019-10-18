package com.sdu.inas.controller;

import com.sdu.inas.beans.Category;
import com.sdu.inas.beans.Edge;
import com.sdu.inas.beans.Node;
import com.sdu.inas.beans.Tuple;
import com.sdu.inas.beans.*;
import com.sdu.inas.service.ParamService;
import com.sdu.inas.service.RawinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ParamController {

    @Autowired
    ParamService paramService;

    @Autowired
    RawinfoService rawinfoService;

    @RequestMapping("/param/{objectId}")
    public String getParams(@PathVariable String objectId,Model model){
        List<Tuple> allParams = paramService.getAllParams(objectId);
        String realname = rawinfoService.findRealName(objectId);
        String testNmae="234";
        model.addAttribute("objectId",objectId);
        model.addAttribute("realName",realname);
        model.addAttribute("allParams",allParams);
        model.addAttribute("testName",testNmae);
        return "param";
    }

    @RequestMapping("/deleteparam/{obkey}")
    public String deleteParam(@PathVariable String obkey) throws UnsupportedEncodingException {
        String[] split = obkey.split("@@");

        String objectId = split[0];
        String key = split[1];
        //String objectId="曹操T3HRGAS9";
        //String key="性别";
        paramService.delParam(objectId,key);
        return "redirect:/param/" + URLEncoder.encode(objectId,"UTF-8");
    }

    @RequestMapping(value = "/reconsparam", method = RequestMethod.POST)
    public String saveNewParam(String objectId, String key,String value) throws UnsupportedEncodingException {
        paramService.insertParam(objectId,key,value);
        return "redirect:/param/" + URLEncoder.encode(objectId,"UTF-8");
    }

    @RequestMapping(value = "/param.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ArrayList> getForceGraphy(String objectId) throws Exception {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        HashMap<String, ArrayList> map = new HashMap<>();
        categories.add(new Category("核心词"));
        categories.add(new Category("属性组"));
        String name = objectId.substring(0,objectId.length()-8);
        Node node = new Node();
        node.setId("0");
        node.setColor("red");
        node.setSize(40);
        node.setSerial(0);
        node.setLabel(name);
        nodes.add(node);
        List<Tuple> allParams = paramService.getAllParams(objectId);
        int i = 1;
        for (Tuple tuple:allParams){
            Node tupleNode = new Node();
            tupleNode.setSize(20);
            tupleNode.setLabel(tuple.getValue());
            tupleNode.setColor("blue");
            tupleNode.setId(String.valueOf(i++));
            tupleNode.setSerial(1);
            nodes.add(tupleNode);
            Edge edge = new Edge("0", tupleNode.getId(), tuple.getKey());
            edges.add(edge);
        }
        map.put("nodes", nodes);
        map.put("edges", edges);
        map.put("categories", categories);
        return map;
    }

    public static void main(String[] args) {
        String a = "曹操abcdefgh";
        System.out.println(a.substring(0,a.length()-8));
    }
}
