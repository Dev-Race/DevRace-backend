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
import com.sajang.devracebackend.service.ChatService;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        LocalDateTime createdTime = LocalDateTime.now();  // 시간을 수동으로 직접 넣어줌. (실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서임.)
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
            // delete UserRoom
            UserRoom userRoom = userRoomService.findUserRoom(user, room);
            userRoomRepository.delete(userRoom);
            message = "'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.";
        }
        else if(chatRequestDto.getMessageType().equals(MessageType.TALK)) {  // 방 채팅의 경우
            if(isExistUserRoom == false) throw new RuntimeException("ERROR - 방에 참가하지않은 사용자는 채팅이 불가능합니다.");
            if(chatRequestDto.getMessage() == null) throw new RuntimeException("ERROR - 채팅시에는 반드시 message을 함께 보내주어야합니다.");
            message = chatRequestDto.getMessage();
        }
        else {  // 랭킹 상승의 경우 (MessageType.RANK 일때)
            room.addRanking(user.getId());
        }

        Chat chat = chatRequestDto.toEntity(user.getNickname(), user.getImageUrl(), message, createdTime);
        chatRepository.save(chat);

        return new ChatResponseDto(chat);
    }
}
