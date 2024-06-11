package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.ChatDto;
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
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final UserRoomRepository userRoomRepository;
    private final ChatRepository chatRepository;


    @Transactional(readOnly = true)
    @Override
    public Slice<ChatDto.Response> findChatsByRoom(Long roomId, Pageable pageable) {
        // < 채팅방 페이징 정렬 기준 >
        // - 페이징을 할 때, 최신 날짜인 마지막 페이지를 0으로 설정하여 역순으로 페이징.
        // - 한 페이지 내의 채팅은 날짜 오름차순으로 정렬.
        // - 만약 퇴장한 경우에는, 본인 퇴장시각 이하까지의 채팅 내역을 조회.
        // => 알고리즘: DESC로 DB에서 페이징 조회후, 각 페이지별로 날짜 오름차순 정렬.

        // 'UserRoom.room' Eager 로딩 (N+1 문제 해결)
        UserRoom userRoom = userRoomService.findUserRoomWithEagerRoom(SecurityUtil.getCurrentMemberId(), roomId, false, false);
        LocalDateTime leaveTime = userRoom.getLeaveTime();

        // 퇴장한 경우, 본인 퇴장시각 이하까지의 채팅내역 페이징 조회
        Slice<Chat> chatSlice;
        if(userRoom.getIsLeave() == 1) chatSlice = chatRepository.findAllByRoomIdAndCreatedTimeLessThanEqual(roomId, leaveTime, pageable);
        else chatSlice = chatRepository.findAllByRoomId(roomId, pageable);

        // DESC로 가져온 데이터를 다시 오름차순으로 페이지별 정렬
        List<Chat> reversedChatList = new ArrayList<>(chatSlice.getContent());  // 새로운 리스트로 만듦으로써 불변성 해제.
        Collections.reverse(reversedChatList);

        // 사용자 캐싱을 위한 맵 생성 (이미 검색한것은 다시 검색하지않도록 성능 향상)
        Map<Long, User> cacheUserMap = new HashMap<>();

        return new SliceImpl<>(reversedChatList.stream()
                .map(chat -> {
                    Long senderId = chat.getSenderId();
                    User senderUser = cacheUserMap.computeIfAbsent(senderId, id -> userService.findUser(id));  // 만약 캐시에 senderId키의 데이터가 없다면, DB조회하고 캐시에 추가.
                    return new ChatDto.Response(chat, senderUser.getNickname(), senderUser.getImageUrl());
                })
                .collect(Collectors.toList()), pageable, chatSlice.hasNext());
    }

    @Transactional
    @Override
    public ChatDto.Response createChat(ChatDto.SaveRequest saveRequestDto) {
        User user = userService.findUser(saveRequestDto.getSenderId());
        Room room = roomService.findRoom(saveRequestDto.getRoomId());
        boolean isExistUserRoom = userRoomRepository.existsByUserAndRoom(user, room);

        String message = null;
        if(saveRequestDto.getMessageType().equals(MessageType.ENTER)) {  // 방 입장의 경우 (방이 이미 생성되어있다는 전제하에)
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
        else if(saveRequestDto.getMessageType().equals(MessageType.LEAVE)) {  // 방 탈퇴의 경우
            // update isLeave UserRoom
            // (프론트엔드에서 또다른 rest api로 실질적인 퇴장을 진행한 이후에, 이 websocket api를 호출하는것이기때문에, 중복 방지로 이 메소드에서 퇴장 로직은 생략해야함.)
            message = "'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.";
        }
        else if(saveRequestDto.getMessageType().equals(MessageType.TALK)) {  // 방 채팅의 경우
            if(isExistUserRoom == false) throw new Exception400.ChatBadRequest("방에 참가하지않은 사용자는 채팅이 불가능합니다.");
            if(saveRequestDto.getMessage() == null) throw new Exception400.ChatBadRequest("채팅시에는 반드시 message를 함께 보내주어야합니다.");
            message = saveRequestDto.getMessage();
        }
        else if(saveRequestDto.getMessageType().equals(MessageType.RANK)) {
            room.addRanking(user.getId());
        }
        else {  // 잘못된 MessageType
            throw new Exception400.ChatBadRequest("잘못된 MessageType으로 API를 요청하였습니다.");
        }

        Chat chat = saveRequestDto.toEntity(message);
        chatRepository.save(chat);

        return new ChatDto.Response(chat, user.getNickname(), user.getImageUrl());
    }
}
