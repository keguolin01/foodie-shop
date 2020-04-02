package com.ikgl.pojo.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ItemCommentVO {

    private String userFace;

    private String nickname;

    private Date createdTime;

    private String content;

    private String specName;

    private Integer commentLevel;

}