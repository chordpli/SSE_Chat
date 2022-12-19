package com.ll.exam.app_2022_12_19__1.controller;

import com.ll.exam.app_2022_12_19__1.ChatMessage;
import com.ll.exam.app_2022_12_19__1.RsData;
import com.ll.exam.app_2022_12_19__1.SseEmitters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SseEmitters sseEmitter;

    private List<ChatMessage> chatMessages = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    public static class writeMessageResponse {
        private final long id;
    }

    @AllArgsConstructor
    @Getter
    public static class writeMessageRequest {
        private final String authorName;
        private final String content;
    }


    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeMessageResponse> writeMessage(@RequestBody writeMessageRequest dto) {
        // 채팅 메세지 생성
        ChatMessage message = new ChatMessage(dto.getAuthorName(), dto.getContent());
        // 채팅 메세지가 들어오면 저장.
        chatMessages.add(message);

        sseEmitter.noti("chat__messageAdded");

        return new RsData("S-1", "메세지가 작성되었습니다.", new writeMessageResponse(message.getId()));
    }

    @AllArgsConstructor
    @Getter
    public static class MessagesResponse {
        private final List<ChatMessage> messages;
        private final long count;
    }

    @AllArgsConstructor
    @Getter
    public static class MessagesRequest {
        private final Long fromId;
    }

    @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> readMessage(MessagesRequest req) {
        List<ChatMessage> messages = chatMessages;

        // 번호가 입력 되었을 경우.
        if (req.fromId != null) {
            // 해당 번호의 채팅 메세지가 전체 리스트에서의 배열 인덱스 번호를 구함.
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == (req.fromId))
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                // 만약에 index가 있으면, 0번부터 index 번까지 제거한 리스트를 만든다.
                messages = messages.subList(index + 1, messages.size());
            }


        }
        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages, messages.size())
        );
    }


    @GetMapping("/room")
    public String showRoom(){
        return "chat/room";
    }

}

    /*@GetMapping("/messages")
    @ResponseBody
    public RsData<List<ChatMessage>> readMessage(@RequestParam(required = false) Long fromId){

        if (fromId == null) {
            return new RsData( "S-1", "메세지가 작성되었습니다.", new MessagesResponse(chatMessages, chatMessages.size()));
        }else{
            List<ChatMessage> fromIdChatMessage = new ArrayList<>();
            for (int i = 0; i < chatMessages.size() ; i++) {
                if (chatMessages.get(i).getId() > fromId) {
                    fromIdChatMessage.add(chatMessages.get(i));
                }
            }
            return new RsData( "S-1", "메세지가 작성되었습니다.", new MessagesResponse(fromIdChatMessage, fromIdChatMessage.size()));
        }
    }*/
