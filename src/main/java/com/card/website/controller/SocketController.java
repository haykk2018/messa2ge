package com.card.website.controller;

import com.card.website.domain.Node;
import com.card.website.repository.NodeRepository;
import com.card.website.socketHandler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/socket") // This means URL's start with /demo (after Application path)
public class SocketController {

    @Autowired
    private NodeRepository nodeRepository;

    //      admin panel first page
    @GetMapping(path = "/main")
    public String socketAdmin(Map<String, Object> model) {

        Iterable<Node> nodes = nodeRepository.findAll();

        model.put("nodes", nodes);

        return "socket/main";
    }

    @GetMapping(path = "/get-node")
    public String getNode(@RequestParam Integer id, Map<String, Object> model){

        Optional<Node> node = nodeRepository.findById(id);
        model.put("node", node);
        return "socket/editNode :: edit-node";

    }

    @PostMapping(path = "/save-node")
    public String saveNode(@Valid Node node, BindingResult bindingResult){


        if (node.getStartDailyTime().isAfter(node.getEndDailyTime()) || node.getStartDailyTime().equals(node.getEndDailyTime())) {
            bindingResult.rejectValue("startDailyTime", "timeError", "start time must be before the end time");
        }

        if (bindingResult.hasErrors()) {
            return "socket/editNode :: edit-node";
        } else {
            nodeRepository.save(node);
            //send success message to ajax code: then js code do redirect
            return "socket/editNode :: saveSuccess";
        }

    }

    @GetMapping(path = "/disable")
    String timesSetToZero(@RequestParam Integer id){
    Optional<Node> node = nodeRepository.findById(id);
    node.get().setStartDailyTime(null);
    node.get().setEndDailyTime(null);
    nodeRepository.save(node.get());
    return "redirect:/socket/main";
    }

    @GetMapping(path = "/start")
    void start(@RequestParam String parentId,@RequestParam String childId) throws IOException {
        new SocketHandler().sendMessage("sp" + childId + "Pop" + childId + "G" + parentId);
    }
    @GetMapping(path = "/stop")
    void stop(@RequestParam String parentId,@RequestParam String childId) throws IOException {
        new SocketHandler().sendMessage("sp" + childId + "Pop" + childId + "G" + parentId);
    }

    @GetMapping(path = "/init")
    void init() throws IOException {
            new SocketHandler().sendMessage("startRouting");
    }

    @GetMapping(path = "/init-child")
    void initChild() throws IOException {
        new SocketHandler().sendMessage("initChild");
    }
}