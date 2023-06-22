package com.example.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.UserDTO;

@Repository
@Mapper
public interface FriendshipDao {
    @Select(        " SELECT u.name as UserName, f.user2_id as UserSend, f.status as Status " +
                    " FROM friendships f " +
                    " INNER JOIN users u ON u.id = f.user2_id " +
                    " WHERE f.user1_id = #{userID} AND f.status = 0 ")
    List<FriendShipDTO> getFriendRequest(int userID);

    @Insert(        " INSERT INTO friendships (user1_id,user2_id,status)" +
                    " VAlUES (#{userID},#{user2ID},0)")
    void insertFriend(FriendShipDTO friendShipDto);

    @Update(        " UPDATE friendships SET status = 1 " +
                    " WHERE user1_id = #{userID} AND user2_id = #{user2ID}")
    void updateStatusFriendRequest(FriendShipDTO friendShipDTO);

    @Select(        " SELECT u.id,u.name,u.avatar,u.address,u.phone,u.email,u.password " +
                    " FROM friendships af " +
                    " INNER JOIN users u ON af.user1_id = u.id " +
                    " WHERE af.user2_id = #{id} and status = 1 " +
                    " UNION " +
                    " SELECT u.id,u.name,u.avatar,u.address,u.phone,u.email,u.password " +
                    " FROM friendships af " +
                    " INNER JOIN users u ON af.user2_id = u.id " +
                    " WHERE af.user1_id = #{id} and status = 1 ")
    List<UserDTO> getFriend(int id);

    @Delete(        " DELETE FROM friendships " +
                    " WHERE user1_id = #{userID} AND user2_id = #{user2ID} OR user2_id = #{userID} AND user1_id = #{user2ID} ")
    void deleteFriend(FriendShipDTO friendShipDTO);
}
