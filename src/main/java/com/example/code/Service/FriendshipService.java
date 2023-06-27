package com.example.code.service;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.code.dao.FriendshipDao;
import com.example.code.dao.UserDao;
import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
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

    public ResponseDTO addFriend(int userID, int user2ID) {

        if (friendshipDao.getFriendshipDtoByID(userID, user2ID) != null) {
            return new ResponseDTO("Failed", ErrorMessage.FRIENDSHIP_EXISTS, null);
        } else if (userDao.getUserByID(user2ID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.USER_NOT_EXISTS, null);
        }
        FriendShipDTO friendShipDto = new FriendShipDTO();
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);
        friendshipDao.insertFriend(friendShipDto);
        return new ResponseDTO("Success", "Thêm bạn bè thành công", null);
    }

    public ResponseDTO updateStatusFriendRequest(int userID, int user2ID) {
        if (friendshipDao.getFriendshipDtoByID(userID, user2ID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.FRIENDSHIP_NOT_EXISTS, null);
        } else if (userDao.getUserByID(user2ID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.FRIENDSHIP_EXISTS, null);
        }
        FriendShipDTO friendShipDTO = new FriendShipDTO();
        friendShipDTO.setUserID(userID);
        friendShipDTO.setUser2ID(user2ID);
        friendshipDao.updateStatusFriendRequest(friendShipDTO);
        return new ResponseDTO("Success", "Cập nhật trạng thái bạn bè", null);
    }

    public List<UserDTO> getFriend(int id) {
        return friendshipDao.getFriend(id);
    }

    public ResponseDTO deleteFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        if (friendshipDao.getFriendshipDtoByID(userID, user2ID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.FRIENDSHIP_NOT_EXISTS, null);
        } else if (userDao.getUserByID(user2ID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.USER_NOT_EXISTS, null);
        }
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);
        friendshipDao.deleteFriend(friendShipDto);
        return new ResponseDTO("Success", "Xoá bạn bè thành công", null);
    }

}
