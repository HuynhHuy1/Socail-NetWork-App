package com.example.code.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.code.DTO.PostDTO;

@Repository
@Mapper
public interface PostDao {
        @Select(        " SELECT MAX(s.StatusID) as StatusID,u.UserName,s.Content , COUNT(l.StatusID) as LikeCount, COUNT(cm.CommnetID) as CommentCount "
                        +
                        " FROM AddFriend af " +
                        " INNER JOIN Status s on af.UserID = s.UserID " +
                        " INNER JOIN Users u on u.UserID = s.UserID " +
                        " LEFT JOIN Likes l on l.StatusID = s.StatusID " +
                        " LEFT JOIN COMMENT cm on cm.statusID = s.StatusID " +
                        " WHERE af.UserIDSend = #{id} and af.Status = 1 " +
                        " GROUP By s.StatusID, cm.statusID " +
                        " UNION " +
                        " SELECT MAX(s.StatusID) as StatusID,u.UserName,s.Content , COUNT(l.StatusID) as LikeCount, COUNT(cm.CommnetID) as CommentCount "
                        +
                        " FROM AddFriend af " +
                        " INNER JOIN Status s on af.UserIDSend = s.UserID " +
                        " INNER JOIN Users u on u.UserID = s.UserID " +
                        " LEFT JOIN Likes l on l.StatusID = s.StatusID " +
                        " LEFT JOIN COMMENT cm on cm.statusID = s.StatusID " +
                        " WHERE af.UserID = #{id} AND af.Status = 1 " +
                        " GROUP By s.StatusID, cm.statusID ")
        List<PostDTO> getPostFriend(@Param("id") int userID);

        @Select(        " SELECT Image FROM PostDetail " +
                        " WHERE StatusID = #{statusID} ")
        List<String> getPostDetail(@Param("statusID") int statusID);

        @Insert(        " INSERT INTO `Status`(`Content`, `UserID`) " +
                        " VALUES ( #{Content} , #{UserID})")
        void insertPost(@Param("Content") String content,@Param("UserID") int id);

        @Select("SELECT LAST_INSERT_ID()")
        int getLastInsertedStatusID();    

        @Insert(        " INSERT INTO PostDetail(StatusID,Image) " +
                        " VALUES ( #{StatusID}, #{Image} ) ")
        void insertPostDetail(@Param("StatusID") int id,@Param("Image") String image);

        @Update(        " UPDATE `Status` " +
                        " SET Content=#{Content}" +
                        " WHERE StatusID = #{ID}")
        void updatePost(@Param("Content") String content, @Param("ID") int id);


        @Delete(        " DELETE FROM Status " +
                        " WHERE StatusID = #{ID} ")
        void deletePost(@Param("ID") int id);
        
        @Delete(        " DELETE FROM PostDetail " +
                        " WHERE StatusID = #{StatusID}")
        void deletePostDetail(@Param("StatusID") int StatusId);


}
