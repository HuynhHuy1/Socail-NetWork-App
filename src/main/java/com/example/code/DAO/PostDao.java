package com.example.code.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.example.code.DTO.PostDTO;

@Repository
@Mapper
public interface PostDao {
    @Select(" SELECT u.UserName, s.Content , s.Image , s.Video " +
            " FROM AddFriend af " +
            " INNER JOIN Status s on af.UserID = s.UserID " +
            " INNER JOIN Users u on u.UserID = s.UserID " +
            " WHERE af.UserIDSend = #{id} and af.Status = #{id} " +
            " UNION " +
            " SELECT u.UserName, s.Content , s.Image , s.Video " +
            " FROM AddFriend af " +
            " INNER JOIN Status s on af.UserIDSend = s.UserID " +
            " INNER JOIN Users u on u.UserID = s.UserID " + 
            " WHERE af.UserID = #{id} AND af.Status = #{id} ")
    List<PostDTO> getPostFriend (@Param("id") int id);

}
