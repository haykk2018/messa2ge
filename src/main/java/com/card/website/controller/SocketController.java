package com.card.website.controller;

import com.card.website.domain.Node;
import com.card.website.domain.Parent;
import com.card.website.repository.NodeRepository;
import com.card.website.repository.ParentRepository;
import com.card.website.socketHandler.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/socket") // This means URL's start with /demo (after Application path)
public class SocketController {

    private static final Logger LOG = LoggerFactory.getLogger(SocketHandler.class);

    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private ParentRepository parentRepository;

    //      admin panel first page
    @GetMapping(path = "/main")
    public String socketAdmin(Map<String, Object> model) {

        Iterable<Parent> parents = parentRepository.findAll();

        model.put("parents", parents);

        return "socket/main";
    }

    @GetMapping(path = "/mainAjax")
    public String socketAdminAjax(Map<String, Object> model) {

        Iterable<Parent> parents = parentRepository.findAll();

        model.put("parents", parents);

        return "socket/main :: all-nodes";
    }

    @GetMapping(path = "/get-parent")
    public String getParent(@RequestParam Integer id, Map<String, Object> model) {

        Parent parent = parentRepository.findById(id).get();
        model.put("parent", parent);
        return "socket/editParent :: edit-parent";

    }

    @PostMapping(path = "/save-parent")
    public String saveParent(@Valid Parent parent, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "socket/editParent :: edit-parent";
        } else {
            parentRepository.save(parent);
            LOG.info("node saved, node:" + parent);
            //send success message to ajax code: then js code do redirect
            return "socket/editParent :: saveSuccess";
        }

    }

    @GetMapping(path = "/get-node")
    public String getNode(@RequestParam Integer id, Map<String, Object> model) {

        Optional<Node> node = nodeRepository.findById(id);
        model.put("node", node);
        return "socket/editNode :: edit-node";

    }


    @PostMapping(path = "/save-node")
    public String saveNode(@Valid Node node, BindingResult bindingResult) {

        if (LocalTime.parse(node.getStartDailyTime().toString()).isAfter(LocalTime.parse(node.getEndDailyTime().toString())) || node.getStartDailyTime() == node.getEndDailyTime()) {
            bindingResult.rejectValue("startDailyTime", "timeError", "start time must be before the end time");
        }

        if (bindingResult.hasErrors()) {
            return "socket/editNode :: edit-node";
        } else {
            nodeRepository.save(node);
            LOG.info("node saved, node:" + node);
            //send success message to ajax code: then js code do redirect
            return "socket/editNode :: saveSuccess";
        }

    }

    @GetMapping(path = "/disable")
    String timesSetToZero(@RequestParam Integer id) {
        Optional<Node> node = nodeRepository.findById(id);
        node.get().setStartDailyTime(null);
        node.get().setEndDailyTime(null);
        nodeRepository.save(node.get());
        return "redirect:/socket/main";
    }

    @GetMapping(path = "/start")
    void start(@RequestParam String parentId, @RequestParam String childId) throws IOException {
        // set new message que only with new message
        ArrayList<String> newMessagesQue = new ArrayList<String>(Arrays.asList("sp" + childId + "Pop" + childId + "G" + parentId));
        ArrayList<String> oldMessagesQue = new SocketHandler().messagesQueListByParId.get(parentId);
        if (oldMessagesQue == null) {
            // if condition true so we must say to begin without waiting response
            new SocketHandler().statusForEveryParId.put(parentId, true);
        }else{
            newMessagesQue.addAll(oldMessagesQue);
        }
        new SocketHandler().messagesQueListByParId.put(parentId, newMessagesQue);
    }

    @GetMapping(path = "/stop")
    void stop(@RequestParam String parentId, @RequestParam String childId) throws IOException {
        // set new message que only with new message
        new SocketHandler().messagesQueListByParId.put(parentId, new ArrayList<String>(Arrays.asList("sp" + childId + "Pop" + childId + "G" + parentId)));
    }

    @GetMapping(path = "/init")
    String initAll(@RequestParam String parentId) throws IOException {
        Parent p = parentRepository.findParentByParentId(parentId);
        if (p.getNodes() != null) {
            p.getNodes().removeAll(p.getNodes());
            parentRepository.save(p);
        }
        // set new message que only with new message
        new SocketHandler().messagesQueListByParId.put(parentId, new ArrayList<String>(Arrays.asList("startRouting")));
        return "redirect:/socket/main";
    }

    @GetMapping(path = "/deleteParent")
    String deleteParent(@RequestParam String parentId, @RequestParam Integer id) throws IOException {
        parentRepository.deleteById(id);
        new SocketHandler().messagesQueListByParId.remove(parentId);
        return "redirect:/socket/main";
    }

    @GetMapping(path = "/init-child")
    @ResponseBody
    String initChild(@RequestParam String parentId) throws IOException {
        new SocketHandler().sendMessage(parentId, "initChild");
        return "ok";
    }
}