package com.example.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.dao.FriendshipDao;
import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.UserDTO;

@Service
public class FriendshipService {
    @Autowired
    FriendshipDao friendshipDao;

    public List<FriendShipDTO> getFriendShipRequests(int user2ID) {
        return friendshipDao.getFriendRequest(user2ID);
    }

    public void addFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);
        friendshipDao.insertFriend(friendShipDto);
    }

    public void updateStatusFriendRequest(int userID, int user2ID) {
        FriendShipDTO friendShipDTO = new FriendShipDTO();
        friendShipDTO.setUserID(userID);
        friendShipDTO.setUser2ID(user2ID);

        friendshipDao.updateStatusFriendRequest(friendShipDTO);
    }

    public List<UserDTO> getFriend(int id) {
        return friendshipDao.getFriend(id);
    }

    public void deleteFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);

        friendshipDao.deleteFriend(friendShipDto);
    }

}
