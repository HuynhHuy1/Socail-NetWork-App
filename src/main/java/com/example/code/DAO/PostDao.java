package com.example.code.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.DTO.Friend_Request;
import com.example.code.DTO.PostDTO;
import com.example.code.Model.User;

@Repository
@Mapper
public interface PostDao {
        @Select(        " SELECT  p.PostID as PostID, u.UserName as UserName, p.Content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.TimeCreate as TimeCreate  " +
                        " FROM Posts p " +
                        " INNER JOIN Friends f ON f.UserIDSend = p.UserID " +
                        " INNER JOIN Users u ON u.UserID = p.UserID " +
                        " LEFT JOIN ( " +
                        " SELECT PostID, COUNT(PostID) as LikeNumber " +
                        " FROM Post_likes " +
                        " GROUP BY PostID " +
                        " ) l ON p.PostID = l.PostID " +
                        " LEFT JOIN( " +
                        " SELECT PostID, COUNT(PostID) as CommentNumber " +
                        " FROM Post_Comment " + 
                        " GROUP BY PostID " +
                        " ) cm ON cm.PostID = p.PostID " + 
                        " WHERE f.UserID = #{id} AND f.Status = 1 " +
                        " UNION " +
                        " SELECT  p.PostID as PostID, u.UserName as UserName, p.Content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.TimeCreate as TimeCreate  " +
                        " FROM Posts p " +
                        " INNER JOIN Friends f ON f.UserID = p.UserID " +
                        " INNER JOIN Users u ON u.UserID = p.UserID " +
                        " LEFT JOIN ( " +
                        " SELECT PostID, COUNT(PostID) LikeNumber " +
                        " FROM Post_likes " +
                        " GROUP BY PostID " +
                        " ) l ON p.PostID = l.PostID " +
                        " LEFT JOIN( " +
                        " SELECT PostID, COUNT(PostID) CommentNumber " +
                        " FROM Post_Comment " +
                        " GROUP BY PostID " +
                        " ) cm ON cm.PostID = p.PostID " +
                        " WHERE f.UserIDSend = #{id} AND f.Status = 1 ")
        List<PostDTO> getPostFriend(@Param("id") int userID);

        @Select(        " SELECT post_file FROM Post_files " +
                        " WHERE PostID = #{PostID} ")
        List<String> getPostDetail(@Param("PostID") int PostID);

        @Insert(        " INSERT INTO `Posts`(`Content`, `UserID`) " +
                        " VALUES ( #{Content} , #{UserID})")
        void insertPost(@Param("Content") String content,@Param("UserID") int id);

        @Select("SELECT LAST_INSERT_ID()")
        int getLastInsertedPostID();    

        @Insert(        " INSERT INTO Post_files(PostID,Post_file) " +
                        " VALUES ( #{PostID}, #{Image} ) ")
        void insertPostDetail(@Param("PostID") int id,@Param("Image") String image);

        @Update(        " UPDATE `Posts` " +
                        " SET Content=#{Content}" +
                        " WHERE PostID = #{ID}")
        void updatePost(@Param("Content") String content, @Param("ID") int id);


        @Delete(        " DELETE FROM Posts " +
                        " WHERE PostID = #{ID} AND UserID = #{UserID} ")
        void deletePost(@Param("ID") int id, @Param("UserID") int userID);
        
        @Delete(        " DELETE FROM PostDetail " +
                        " WHERE PostID = #{PostID}")
        void deletePostDetail(@Param("PostID") int PostId);

        @Select(        " SELECT  p.PostID as PostID, u.UserName as UserName, p.Content as Content , cm.CommentNumber as CommentCount , l.LikeNumber as LikeCount, p.TimeCreate as TimeCreate  " +
                        " FROM Posts p " +
                        " INNER JOIN Users u ON u.UserID = p.UserID " +
                        " LEFT JOIN ( " +
                        " SELECT PostID, COUNT(PostID) as LikeNumber " +
                        " FROM Post_likes " +
                        " GROUP BY PostID " +
                        " ) l ON p.PostID = l.PostID " +
                        " LEFT JOIN( " +
                        " SELECT PostID, COUNT(PostID) as CommentNumber " +
                        " FROM Post_Comment " + 
                        " GROUP BY PostID " +
                        " ) cm ON cm.PostID = p.PostID " + 
                        " WHERE u.UserID = #{userID}" )
        List<PostDTO> getProFileByID(@Param("userID") int id);

        @Select(        " SELECT u.UserName as UserName, f.UserIDSend as UserIDSend, f.Status as Status " +
                        " FROM Friends f " +
                        " INNER JOIN Users u ON u.UserID = f.UserIDSend " +
                        " WHERE f.UserID = #{UserID} AND f.Status = 0 ")
        List<Friend_Request> getFriendRequest(@Param("UserID") int userID);

        @Insert(        " INSERT INTO Friends (UserID,UserIDSend,Status)" +
                        " VAlUES (#{UserID},#{UserSendID},0)" )
        void insertFriends(@Param("UserID")int userID,@Param("UserSendID") int userSendID);
                
        @Update(        " UPDATE Friends SET Status = #{Status} " +
                        " WHERE UserID = #{userID} AND UserIDSend = #{userSendID}")
        void updateStatusFriendRequest(@Param("userID")int userID,@Param("userSendID") int userSendID,@Param("Status") int status);

        @Select(        " SELECT u.UserID,u.UserName,u.Avatar,u.UserAddress,u.UserPhone,u.Email,u.PASSWORD " +
                        " FROM Friends af " +
                        " INNER JOIN Users u ON af.UserID = u.UserID " +
                        " WHERE af.UserIDSend = #{id} and Status = 1 " +
                        " UNION " +
                        " SELECT u.UserID,u.UserName,u.Avatar,u.UserAddress,u.UserPhone,u.Email,u.PASSWORD " +
                        " FROM Friends af " +
                        " INNER JOIN Users u ON af.UserIDSend = u.UserID " +
                        " WHERE af.UserID = #{id} and Status = 1 " )
        List<User> getFriend(@Param("id") int id);

        @Update(        " UPDATE `Users` " +
                        " SET `UserName`= #{UserName},`Avatar`=#{Avatar},`UserAddress`=#{UserAddress},`UserPhone`=#{UserPhone}" +  
                        " WHERE Users.UserID = #{UserID} ")
        void updateUser(User user);
        
        @Update(" UPDATE sers " + 
        " SET PASSWORD = #{password} " +
        " WHERE UserID = #{userID} "
        )
        void updatePassWord(@Param("password") String password,@Param("userID")  int userID);

        @Delete(" DELETE FROM `Friends` " +
                " WHERE UserID = #{UserID} AND UserIDSend = #{UserIDSend} OR UserIDSend = #{UserID} AND UserID = #{UserIDSend} ")
        void deleteFriend(int UserID, int UserIDSend);
}
