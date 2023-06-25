package com.example.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.code.dao.FriendshipDao;
import com.example.code.dao.UserDao;
import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.UserDTO;
import com.example.code.exception.ExistException;
import com.example.code.staticmessage.ErrorMessage;


@Service
public class FriendshipService {
    @Autowired
    FriendshipDao friendshipDao;
    @Autowired
    UserDao userDao;

    public List<FriendShipDTO> getFriendShipRequests(int user2ID) {
        return friendshipDao.getFriendRequest(user2ID);
    }

    public void addFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        try {
            friendShipDto.setUserID(userID);
            friendShipDto.setUser2ID(user2ID);
            friendshipDao.insertFriend(friendShipDto);
        } catch (DataAccessException e) {
            if(friendshipDao.getFriendshipDtoByID(friendShipDto)!= null){
                throw new ExistException(ErrorMessage.FRIENDSHIP_EXISTS);
            }
            else if(userDao.getUserByID(user2ID) == null){
                throw new ExistException(ErrorMessage.USER_NOT_EXISTS);
            }
        }
    }

    public void updateStatusFriendRequest(int userID, int user2ID) {
        FriendShipDTO friendShipDTO = new FriendShipDTO();
        try {
            friendShipDTO.setUserID(userID);
            friendShipDTO.setUser2ID(user2ID);
            friendshipDao.updateStatusFriendRequest(friendShipDTO);
        } catch (Exception e) {
            if(friendshipDao.getFriendshipDtoByID(friendShipDTO)!= null){
                throw new ExistException(ErrorMessage.FRIENDSHIP_EXISTS);
            }
            else if(userDao.getUserByID(user2ID) == null){
                throw new ExistException(ErrorMessage.USER_NOT_EXISTS);
            }
        }

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
