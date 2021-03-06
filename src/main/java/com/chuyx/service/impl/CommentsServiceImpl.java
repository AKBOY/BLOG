package com.chuyx.service.impl;

import com.chuyx.mapper.CommentsMapper;
import com.chuyx.pojo.dto.*;
import com.chuyx.pojo.po.Comments;
import com.chuyx.pojo.po.User;
import com.chuyx.service.CommentsService;
import com.chuyx.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    UserService userService;

    @Override
    public int queryCountByBlogId(int id) {
        return commentsMapper.queryCountByBlogId(id);
    }

    @Override
    public boolean addComment(int blogId, int uid, String editorContent) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBlogId(blogId);
        commentDTO.setContent(editorContent);
        commentDTO.setUid(uid);
        commentDTO.setParentId(0);
        return commentsMapper.addCommnet(commentDTO);
    }

    @Override
    public Pager<CommentShowDTO> queryByBlogId(int blogId) {
        Pager<CommentShowDTO> result = new Pager();
        List<Comments> comments = commentsMapper.queryByBlogId(blogId);
        List<CommentShowDTO> commentShowDTOS = new ArrayList();
        Iterator var5 = comments.iterator();

        while (var5.hasNext()) {
            Comments comment = (Comments) var5.next();
            CommentShowDTO commentShowDTO = new CommentShowDTO();
            CommentShowMsgDTO commentShowMsgDTO = toShow(comment);
            commentShowDTO.setParent(commentShowMsgDTO);
            List<Comments> childComments = commentsMapper.queryByComId(comment.getId(), blogId);
            ArrayList<CommentShowMsgDTO> childCommentShowMsgDTOS = new ArrayList();
            Iterator var11 = childComments.iterator();

            while (var11.hasNext()) {
                Comments childComment = (Comments) var11.next();
                CommentShowMsgDTO childCommentShowMsgDTO = toShow(childComment);
                childCommentShowMsgDTOS.add(childCommentShowMsgDTO);
            }

            commentShowDTO.setChilds(childCommentShowMsgDTOS);
            commentShowDTOS.add(commentShowDTO);
        }

        int countSize = commentsMapper.countSize(blogId);
        if (countSize < 5) {
            result.setSize(1);
        } else if (countSize / 5 > 0) {
            result.setSize(countSize / 5 + 1);
        } else {
            result.setSize(countSize / 5);
        }

        result.setTotal((long) countSize);
        result.setRows(commentShowDTOS);
        result.setCataId(0);
        result.setPage(1);
        return result;
    }

    @Override
    public Pager<CommentShowDTO> queryByBlogIdSmallPage(int blogId, int nowPage) {
        Pager<CommentShowDTO> result = new Pager();
        int index = (nowPage - 1) * 5;
        List<Comments> comments = commentsMapper.queryByBlogIdByPage(blogId, index, 5);
        List<CommentShowDTO> commentShowDTOS = new ArrayList();
        Iterator var7 = comments.iterator();

        while (var7.hasNext()) {
            Comments comment = (Comments) var7.next();
            CommentShowDTO commentShowDTO = new CommentShowDTO();
            CommentShowMsgDTO commentShowMsgDTO = toShow(comment);
            commentShowDTO.setParent(commentShowMsgDTO);
            List<Comments> childComments = commentsMapper.queryByComId(comment.getId(), blogId);
            ArrayList<CommentShowMsgDTO> childCommentShowMsgDTOS = new ArrayList();
            Iterator var13 = childComments.iterator();

            while (var13.hasNext()) {
                Comments childComment = (Comments) var13.next();
                CommentShowMsgDTO childCommentShowMsgDTO = toShow(childComment);
                childCommentShowMsgDTOS.add(childCommentShowMsgDTO);
            }

            commentShowDTO.setChilds(childCommentShowMsgDTOS);
            commentShowDTOS.add(commentShowDTO);
        }

        int countSize = commentsMapper.countSize(blogId);
        if (countSize < 5) {
            result.setSize(1);
        } else if (countSize / 5 > 0) {
            result.setSize(countSize / 5 + 1);
        } else {
            result.setSize(countSize / 5);
        }

        result.setTotal((long) countSize);
        result.setRows(commentShowDTOS);
        result.setCataId(0);
        result.setPage(nowPage);
        return result;
    }

    @Override
    public int addChildComment(String targetUserName, int userId, String userParentName, String replyContent, int blogId, int parrentComId) {
        Comments comments = new Comments();
        comments.setUid(userId);
        comments.setContent(replyContent);
        comments.setBlogId(blogId);
        comments.setAuthorOne(targetUserName);
        User user = userService.queryUserById(userId);
        comments.setAuthorTwe(user.getUname());
        comments.setParentId(parrentComId);
        userService.queryUserByUserName(userParentName);
        commentsMapper.addChildComment(comments);
        return 0;
    }

    @Override
    public int delComment(int id) {
        commentsMapper.delComment(id);
        commentsMapper.delCommentChilds(id);
        return 0;
    }

    @Override
    public int addChildComment(int userId, String userParentName, String replyContent, int blogId, int parrentComId) {
        Comments comments = new Comments();
        comments.setUid(userId);
        comments.setContent(replyContent);
        comments.setBlogId(blogId);
        User user = userService.queryUserById(userId);
        comments.setAuthorTwe(user.getUname());
        comments.setParentId(parrentComId);
        LoginUserDTO loginUserDTO = userService.queryUserByUserName(userParentName);
        comments.setParentId(loginUserDTO.getUid());
        commentsMapper.addChildComment(comments);
        return 0;
    }

    @Override
    public int getAllCommentsSize() {
        return commentsMapper.getAllCommentsSize();
    }

    @Override
    public List<Comments> getPageCommentsSize(int page, int size) {
        int index = (page - 1) * size;
        return commentsMapper.queryPageComment(index, size);
    }

    @Override
    public int delCommentByBlogId(int id) {
        return commentsMapper.delCommentBuBlogId(id);
    }

    public CommentShowMsgDTO toShow(Comments comment) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommentShowMsgDTO commentShowMsgDTO = new CommentShowMsgDTO();
        BeanUtils.copyProperties(comment, commentShowMsgDTO);
        User user = userService.queryUserById(comment.getUid());
        commentShowMsgDTO.setAuthor(user.getUname());
        commentShowMsgDTO.setTweHeadPic(user.getHeadPic());
        String format = simpleDateFormat.format(comment.getCreateTime());
        commentShowMsgDTO.setRepleaseDate(format);
        return commentShowMsgDTO;
    }
}
