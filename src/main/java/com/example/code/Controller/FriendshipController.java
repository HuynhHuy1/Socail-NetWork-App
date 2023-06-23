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
        try {
            List<FriendShipDTO> listRequest = friendshipService.getFriendShipRequests(userID);
            return ResponseEntity.ok()
                    .body(new ResponseDTO("True", "Lấy danh sách lời mời kết bạn thành công", listRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO("False", "Lấy danh sách lời mời kết bạn thất bại", ""));
        }
    }

    @PutMapping("requests/{user2-id}")
    public ResponseEntity<ResponseDTO> updateFriendShipRequest(@RequestAttribute("userID") int userID,
                                                           @PathVariable("user2-id") int user2ID) {
        try {
            friendshipService.updateStatusFriendRequest(userID, user2ID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Phản hồi thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseDTO("False", "Phản hồi thất bại", ""));
        }
    }
    
    @GetMapping()
    public ResponseEntity<ResponseDTO> getFriend(@RequestAttribute("userID") int userID) {
        try {
            List<UserDTO> listFriend = friendshipService.getFriend(userID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Lấy danh sách bạn thành công", listFriend));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO("False", "Lấy danh sách bạn thất bại", ""));
        }
    }

    @PostMapping("{user2-id}")
    public ResponseEntity<ResponseDTO> addFriend(@RequestAttribute("userID") int userID,
                                                 @PathVariable("user2-id") int user2ID) {
        try {
            friendshipService.addFriend(userID, user2ID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "thêm bạn bè thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO("False", "thêm bạn bè thất bại", ""));
        }
    }

    @DeleteMapping("{user2ID}")
    public ResponseEntity<ResponseDTO> deleteFriend(@RequestAttribute("userID") int userID,
                                                    @PathVariable("user2ID") int user2ID) {
        try {
            friendshipService.deleteFriend(userID, user2ID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Xoá bạn thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO("False", "Lấy danh sách bạn thất bại", ""));
        }
    }
}
