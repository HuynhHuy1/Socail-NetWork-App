package com.example.code.DAO;

import java.util.List;

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
    @Select(" SELECT u.UserName, s.Content , s.Image , s.Video, COUNT(l.StatusID) as LikeCount, COUNT(cm.CommnetID) as CommentCount " +
            " FROM AddFriend af " +
            " INNER JOIN Status s on af.UserID = s.UserID " +
            " INNER JOIN Users u on u.UserID = s.UserID " +
            " LEFT JOIN Likes l on l.StatusID = s.StatusID " +
            " LEFT JOIN COMMENT cm on cm.statusID = s.StatusID " +
            " WHERE af.UserIDSend = #{id} and af.Status = 1 " +
            " GROUP By s.StatusID, cm.statusID " +
            " UNION " +
            " SELECT u.UserName, s.Content , s.Image , s.Video, COUNT(l.StatusID) as LikeCount, COUNT(cm.CommnetID) as CommentCount " +
            " FROM AddFriend af " +
            " INNER JOIN Status s on af.UserIDSend = s.UserID " +
            " INNER JOIN Users u on u.UserID = s.UserID " + 
            " LEFT JOIN Likes l on l.StatusID = s.StatusID " +
            " LEFT JOIN COMMENT cm on cm.statusID = s.StatusID " +
            " WHERE af.UserID = #{id} AND af.Status = 1 " +
            " GROUP By s.StatusID, cm.statusID ")
    List<PostDTO> getPostFriend (@Param("id") int id);

    @Insert(" INSERT INTO `Status`(`Content`, `Image`, `Video`, `UserID`) " + 
            " VALUES ( #{Content} , #{Image}, #{Video} , #{UserID})" )
    void insertPost (@Param("Content") String content,@Param("Image") String image,@Param("Video") String video,@Param("UserID") int id );

    @Update( " UPDATE `Status` " +  
             " SET Content=#{Content},Image=#{Image},Video=#{Video}" + 
             " WHERE UserID = #{UserID}")
    void updatePost (@Param("Content") String content,@Param("Image") String image,@Param("Video") String video,@Param("UserID") int id);

    // @Delete(" DELETE FROM `Status` WHERE 0")
}
