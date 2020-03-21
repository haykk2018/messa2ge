package com.card.website.socketHandler;

import com.card.website.domain.Node;
import com.card.website.repository.NodeRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Configurable
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SocketHandler.class);
    static WebSocketSession curSession;
    static String parentId = "11";
    List sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private NodeRepository nodeRepository;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        Thread.sleep(1000); // simulated delay

        this.curSession = session;

        Map mapValue = new Gson().fromJson(message.getPayload(), Map.class);

		/*for(WebSocketSession webSocketSession : sessions) {
			Map value = new Gson().fromJson(message.getPayload(), Map.class);
			webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
		}*/
        switch (mapValue.get("device").toString()) {
            case "childes":
                ArrayList receivedList = (ArrayList) mapValue.get("id");
                ArrayList<Node> nodes = new ArrayList<Node>();

                for (Object n : receivedList) {
                    Node node = new Node();
                    node.setChildId(n.toString());
                    node.setParentId(parentId);
                    nodes.add(node);
                    this.nodeRepository.save(node);
                }

                //nodeRepository.saveAll(nodes);
                break;
            case "parent":
                parentId = mapValue.get("id").toString();
                break;
            case "child":
                break;
        }

    }

    @Scheduled(fixedRate = 9000)
    public void sayHello() throws IOException {

        sendMessage("scheduler");
        Iterable<Node> nodes;
        //        first find all which must be open
        nodes = nodeRepository.findAllByStartDailyTimeBeforeAndEndDailyTimeAfterAndOpenedFalse(LocalTime.now(), LocalTime.now());
        for (Node node : nodes) {
            node.setOpened(true);
            nodeRepository.save(node);
            sendMessage("sp" + node.getChildId()+ "Pop" + node.getChildId() + "G" + node.getParentId());
        }
        //        second find all which must to close
        nodes = nodeRepository.findAllByStartDailyTimeAfterOrEndDailyTimeBeforeAndOpenedIsTrue(LocalTime.now(), LocalTime.now());
        for (Node node : nodes) {
            node.setOpened(false);
            nodeRepository.save(node);
            sendMessage("sp" + node.getChildId()+ "Pcl" + node.getChildId() + "G" + node.getParentId());
        }
        LOG.info("Hello from our simple scheduled method");
    }

    public void sendMessage(String message) throws IOException {
        if (curSession != null && curSession.isOpen()) {
            curSession.sendMessage(new TextMessage(message));
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }
}