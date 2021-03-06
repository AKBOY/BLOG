package com.chuyx.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShowDTO {

    private CommentShowMsgDTO parent;

    private List<CommentShowMsgDTO> childs;

}
