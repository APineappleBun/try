package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.Chat;
import com.example.orienteering.entity.Friend;
import com.example.orienteering.entity.R;
import com.example.orienteering.service.ChatService;
import com.example.orienteering.service.FriendService;
import com.example.orienteering.service.UserService;
import com.example.orienteering.service.impl.WebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private FriendService friendService;

    @RequestMapping("/pushMessage")
    public R pushMessage(@RequestBody Chat message) {
        System.out.println("message: " + message);
        message.setCreateTime(LocalDateTime.now());
        chatService.save(message);
        WebSocketServiceImpl.sendInfo(message);
        return R.ok("MSG SEND SUCCESS");
    }

    @GetMapping("/close")
    public String close(String userId) {
        WebSocketServiceImpl.close(userId);
        return "ok";
    }


    @GetMapping("/getMessage")
    public R getMessage(int senderId, int receiverId) {
        // 查询数据库中跟当前对话相关的信息
        if (friendService.count(new QueryWrapper<Friend>().eq("user_id", senderId).eq("friend_user_id", receiverId)) == 0) {
            Friend friend = new Friend();
            friend.setUserId(senderId);
            friend.setFriendUserId(receiverId);
            friendService.save(friend);
            Friend friend2 = new Friend();
            friend2.setUserId(receiverId);
            friend2.setFriendUserId(senderId);
            friendService.save(friend2);
        }
//        List<Chat> messageList = chatService.list(new QueryWrapper<Chat>().eq("receiver_id", receiverId).eq("sender_id", senderId).orderByAsc("create_time"));
//        List<Chat> messageList2 = chatService.list(new QueryWrapper<Chat>().eq("receiver_id", senderId).eq("sender_id", receiverId));
        List<Chat> messageList = chatService.list(new QueryWrapper<Chat>().and(wrapper->wrapper.eq("receiver_id", receiverId).eq("sender_id", senderId)).or(wrapper->wrapper.eq("receiver_id", senderId).eq("sender_id", receiverId)).orderByAsc("create_time"));
//        messageList.addAll(messageList2);
        Map<String, Object> map = new HashMap<>();
        map.put("messageList", messageList);
        return R.ok(map);
    }


}
