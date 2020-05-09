package com.card.website.controller;

import com.card.website.domain.Node;
import com.card.website.domain.Parent;
import com.card.website.domain.Regime;
import com.card.website.repository.NodeRepository;
import com.card.website.repository.ParentRepository;
import com.card.website.repository.RegimeRepository;
import com.card.website.socketHandler.SocketHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/socket") // This means URL's start with /demo (after Application path)
public class SocketController {


    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private RegimeRepository regimeRepository;

    //      admin panel first page
    @GetMapping(path = "/main")
    public String socketAdmin(Map<String, Object> model) {

        Iterable<Regime> regimes = regimeRepository.findAll();

        model.put("regimes", regimes);

        return "socket/main";
    }

    //      technical admin  panel first page
    @GetMapping(path = "/technical-main")
    public String technicalMain(Map<String, Object> model) {

        Iterable<Parent> parents = parentRepository.findAll();

        model.put("parents", parents);
        model.put("sessionByParId", SocketHandler.sessionByParId);

        return "socket/technical-main";
    }

    @GetMapping(path = "/mainAjax")
    public String socketAdminAjax(Map<String, Object> model) {

        Iterable<Regime> regimes = regimeRepository.findAll();

        model.put("regimes", regimes);

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

            //send success message to ajax code: then js code do redirect
            return "socket/editParent :: saveSuccess";
        }

    }

    @GetMapping(path = "/get-node")
    public String getNode(@RequestParam Integer id, Map<String, Object> model) {

        Iterable<Regime> regimes = regimeRepository.findAll();
        model.put("regimes", regimes);
        Optional<Node> node = nodeRepository.findById(id);
        model.put("node", node);
        return "socket/editNode :: edit-node";

    }


    @PostMapping(path = "/save-node")
    public String saveNode(@Valid Node node, BindingResult bindingResult) {

//        if (LocalTime.parse(node.getStartDailyTime().toString()).isAfter(LocalTime.parse(node.getEndDailyTime().toString())) || node.getStartDailyTime() == node.getEndDailyTime()) {
//            bindingResult.rejectValue("startDailyTime", "timeError", "start time must be before the end time");
//        }

        if (bindingResult.hasErrors()) {
            return "socket/editNode :: edit-node";
        } else {
            node.getRegime().setParentId(node.getParent().getParentId());
            nodeRepository.save(node);
            try {
                sendRegimes(node.getParent().getParentId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //send success message to ajax code: then js code do redirect
            return "socket/editNode :: saveSuccess";
        }

    }

    @GetMapping(path = "/disable")
    String timesSetToZero(@RequestParam Integer id) {
        Optional<Node> node = nodeRepository.findById(id);
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
        } else {
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
        SocketHandler.sendMessage(parentId, "startRouting");
        return "redirect:/socket/technical-main";
    }

    @GetMapping(path = "/deleteParent")
    String deleteParent(@RequestParam String parentId, @RequestParam Integer id) throws IOException {
        parentRepository.deleteById(id);
        SocketHandler.sessionByParId.remove(parentId);
        return "redirect:/socket/technical-main";
    }

    @GetMapping(path = "/init-child")
    @ResponseBody
    String initChild(@RequestParam String parentId) throws IOException {
        SocketHandler.sendMessage(parentId, "initChild");
        return "ok";
    }

    // regime controllers
    @GetMapping(path = "/get-regime")
    public String getRegime(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {

        Regime regime;

        if (id != null) {
            // if id nul its doing edit
            regime = regimeRepository.findById(id).get();
        } else {
            // if isn't  nul its doing new
            regime = new Regime();
        }

        model.put("regime", regime);
        return "socket/editRegime :: edit-regime";

    }

    @PostMapping(path = "/save-regime")
    public String setRegime(@Valid Regime regime, BindingResult bindingResult) throws IOException{

        if (bindingResult.hasErrors()) {
            return "socket/editRegime :: edit-regime";
        } else {
            regimeRepository.save(regime);
            sendRegimes(regime.getParentId());
            //send success message to ajax code: then js code do redirect
            return "socket/editRegime :: saveSuccess";
        }

    }

    @GetMapping(path = "/delete-regime")
    String deleteRegime(@RequestParam String parentId, @RequestParam Integer id) throws IOException {
        regimeRepository.deleteById(id);
        sendRegimes(parentId);
        return "redirect:/socket/main";
    }

    // find all regimes refereed by regimes and send they data
    void sendRegimes(String parentId) throws IOException {

        Iterable<Regime> regimes = regimeRepository.findAllByParentId(parentId);

        for (Regime r : regimes) {
            for (Node n : r.getNodes()) {
                n.setRegime(null);
                n.setParent(null);
            }
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(regimes);
        SocketHandler.sendMessage(parentId, jsonString);
    }

}