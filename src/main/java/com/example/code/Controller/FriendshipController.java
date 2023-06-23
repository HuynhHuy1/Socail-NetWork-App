package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.FriendshipService;

@RestController
@RequestMapping("api/friendships")
public class FriendshipController {

    @Autowired
    FriendshipService friendshipService;

    @GetMapping("requests")
    public ResponseEntity<ResponseDTO> getFriendshipRequests(@RequestAttribute("userID") int userID) {
        List<FriendShipDTO> listRequest = friendshipService.getFriendShipRequests(userID);
        return ResponseEntity.ok()
                .body(new ResponseDTO("Success", "Lấy danh sách lời mời kết bạn thành công", listRequest));
    }

    @PutMapping("requests/{user2-id}")
    public ResponseEntity<ResponseDTO> updateFriendShipRequest(@RequestAttribute("userID") int userID,
            @PathVariable("user2-id") int user2ID) {
        friendshipService.updateStatusFriendRequest(userID, user2ID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Phản hồi thành công", ""));
    }

    @GetMapping()
    public ResponseEntity<ResponseDTO> getFriend(@RequestAttribute("userID") int userID) {
        List<UserDTO> listFriend = friendshipService.getFriend(userID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Lấy danh sách bạn thành công", listFriend));
    }

    @PostMapping("{user2-id}")
    public ResponseEntity<ResponseDTO> addFriend(@RequestAttribute("userID") int userID,
            @PathVariable("user2-id") int user2ID) {
        friendshipService.addFriend(userID, user2ID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "thêm bạn bè thành công", ""));
    }

    @DeleteMapping("{user2ID}")
    public ResponseEntity<ResponseDTO> deleteFriend(@RequestAttribute("userID") int userID,
            @PathVariable("user2ID") int user2ID) {
        friendshipService.deleteFriend(userID, user2ID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Xoá bạn thành công", ""));
    }
}
