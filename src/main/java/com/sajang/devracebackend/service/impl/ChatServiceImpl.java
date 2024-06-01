package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.chat.ChatResponseDto;
import com.sajang.devracebackend.dto.chat.ChatRequestDto;
import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.response.exception.Exception400;
import com.sajang.devracebackend.service.ChatService;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import com.sajang.devracebackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final UserRoomRepository userRoomRepository;
    private final ChatRepository chatRepository;


    @Transactional
    @Override
    public ChatResponseDto createChat(ChatRequestDto chatRequestDto) {
        User user = userService.findUser(chatRequestDto.getSenderId());
        Room room = roomService.findRoom(chatRequestDto.getRoomId());
        boolean isExistUserRoom = userRoomRepository.existsByUserAndRoom(user, room);

        String message = null;
        if(chatRequestDto.getMessageType().equals(MessageType.ENTER)) {  // 방 입장의 경우 (방이 이미 생성되어있다는 전제하에)
            if(isExistUserRoom == false) {  // 이와 반대로, 이미 방에 입장해있는 사용자의 경우에는 메세지를 전송하지 않는다.
                // create UserRoom
                UserRoom userRoom = UserRoom.UserRoomSaveBuilder()
                        .user(user)
                        .room(room)
                        .build();
                userRoomRepository.save(userRoom);
                message = "'" + user.getNickname() + "'님이 방에 참가하였습니다.";
            }
        }
        else if(chatRequestDto.getMessageType().equals(MessageType.LEAVE)) {  // 방 탈퇴의 경우
            // update isLeave UserRoom
            // (프론트엔드에서 또다른 rest api로 실질적인 퇴장을 진행한 이후에, 이 websocket api를 호출하는것이기때문에, 중복 방지로 이 메소드에서 퇴장 로직은 생략해야함.)
            message = "'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.";
        }
        else if(chatRequestDto.getMessageType().equals(MessageType.TALK)) {  // 방 채팅의 경우
            if(isExistUserRoom == false) throw new Exception400.ChatBadRequest("방에 참가하지않은 사용자는 채팅이 불가능합니다.");
            if(chatRequestDto.getMessage() == null) throw new Exception400.ChatBadRequest("채팅시에는 반드시 message를 함께 보내주어야합니다.");
            message = chatRequestDto.getMessage();
        }
        else if(chatRequestDto.getMessageType().equals(MessageType.RANK)) {
            room.addRanking(user.getId());
        }
        else {  // 잘못된 MessageType
            throw new Exception400.ChatBadRequest("잘못된 MessageType으로 API를 요청하였습니다.");
        }

        Chat chat = chatRequestDto.toEntity(message);
        chatRepository.save(chat);

        return new ChatResponseDto(chat, user.getNickname(), user.getImageUrl());
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<ChatResponseDto> findChatsByRoom(Long roomId, Pageable pageable) {
        UserRoom userRoom = userRoomService.findUserRoom(SecurityUtil.getCurrentMemberId(), roomId);
        LocalDateTime leaveTime = userRoom.getLeaveTime();

        // 본인 퇴장시각 이하까지의 채팅 내역 조회
        Slice<Chat> chatSlice = chatRepository.findAllByRoomIdAndCreatedTimeLessThanEqual(roomId, leaveTime, pageable);

        // 사용자 캐싱을 위한 맵 생성 (이미 검색한것은 다시 검색하지않도록 성능 향상)
        Map<Long, User> cacheUserMap = new HashMap<>();

        return chatSlice.map(chat -> {
            Long senderId = chat.getSenderId();
            User senderUser = cacheUserMap.computeIfAbsent(senderId, id -> userService.findUser(id));  //  만약 캐시에 senderId키의 데이터가 없다면, DB조회하고 캐시에 추가.
            return new ChatResponseDto(chat, senderUser.getNickname(), senderUser.getImageUrl());
        });
    }
}
