package com.example.code.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int postId;
    private String userName;
    private String content;
    private String image; 
    private int likeCount;
    private int commentCount;
    private String avatar;
    private Timestamp timeCreate;
    private int userID;
    private String timeFormatString = "";
    private Boolean stateLike ;
    private String avatarBase64;
}
