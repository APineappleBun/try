package com.example.orienteering.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.Chat;
import com.example.orienteering.service.ChatService;
import com.example.orienteering.service.WebSocketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ServerEndpoint("/websocket/{senderId}")
@Component
public class WebSocketServiceImpl implements WebSocketService {


    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServiceImpl> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收SenderId
     */
    private String senderId = "";

//    @Autowired
    private static ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService) {
        WebSocketServiceImpl.chatService = chatService;
    }

//    public static ChatMapper chatMapper = null;


    /**
     * 连接建立成功调用的方法
     * <p>
     * 1.用map存 每个客户端对应的MyWebSocket对象
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("senderId") String senderId) {
        this.session = session;
        this.senderId = senderId;
        if (webSocketMap.containsKey(senderId)) {
            webSocketMap.remove(senderId);
            webSocketMap.put(senderId, this);
            //加入set中
        } else {
            webSocketMap.put(senderId, this);
            //加入set中
        }
    }

//    @OnMessage


    /**
     * 报错
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 实现服务器推送到对应的客户端
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 自定义 指定的SenderId服务端向客户端发送消息
     */
    public static void sendInfo(Chat chat) {
//        QueryWrapper<Chat> queryWrapper = new QueryWrapper();
//        List<Chat> chats = chatMapper.selectList(queryWrapper.lambda()
//                .eq(Chat::getReceiverId, chat.getReceiverId()).or().eq(Chat::getSenderId, chat.getReceiverId()).or().eq(Chat::getReceiverId, chat.getSenderId()).or().eq(Chat::getSenderId, chat.getSenderId()));
        System.out.println("chat:" + chat);
//        List<Chat> chats = chatService.list(new QueryWrapper<Chat>().eq("receiver_id", chat.getReceiverId()).eq("sender_id", chat.getSenderId()));
//        List<Chat> chats2 = chatService.list(new QueryWrapper<Chat>().eq("receiver_id", chat.getSenderId()).eq("sender_id", chat.getReceiverId()));
//        chats.addAll(chats2);
        int receiverId = chat.getReceiverId();
        int senderId = chat.getSenderId();
        List<Chat> chats = chatService.list(new QueryWrapper<Chat>().and(wrapper->wrapper.eq("receiver_id", receiverId).eq("sender_id", senderId)).or(wrapper->wrapper.eq("receiver_id", senderId).eq("sender_id", receiverId)).orderByAsc("create_time"));

        // log.info("发送消息到:"+SenderId+"，报文:"+message);
        if (!StringUtils.isEmpty(chat.getReceiverId().toString()) && webSocketMap.containsKey(chat.getReceiverId().toString())) {
            webSocketMap.get(chat.getSenderId().toString()).sendMessage(JSONObject.toJSONString(chats));
            webSocketMap.get(chat.getReceiverId().toString()).sendMessage(JSONObject.toJSONString(chats));
        } else {
            webSocketMap.get(chat.getSenderId().toString()).sendMessage(JSONObject.toJSONString(chats));
        }
    }

    /**
     * 自定义关闭
     *
     * @param senderId
     */
    public static void close(String senderId) {
        if (webSocketMap.containsKey(senderId)) {
            webSocketMap.remove(senderId);
        }
    }

    /**
     * 获取在线用户信息
     *
     * @return
     */
    public static Map getOnlineUser() {
        return webSocketMap;
    }

}