package com.chuyx.service.impl;

import com.chuyx.pojo.dto.*;
import com.chuyx.pojo.po.Blog;
import com.chuyx.pojo.po.Category;
import com.chuyx.pojo.po.Comments;
import com.chuyx.pojo.po.User;
import com.chuyx.service.*;
import com.chuyx.utils.BlogUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    BlogService blogService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EmailService emailService;

    @Override
    public AdminIndexMsgDTO toAdmin() {
        int countUserSize = userService.getCountUserSize();
        int allBlogSize = blogService.getAllBlogSize();
        int allCommentsSize = commentsService.getAllCommentsSize();
        int countAuthorSize = userService.getCountAuthorSize();
        AdminIndexMsgDTO result = new AdminIndexMsgDTO();
        result.setUserSize(countUserSize);
        result.setBlogSize(allBlogSize);
        result.setCommentsSize(allCommentsSize);
        result.setAuthorSize(countAuthorSize);
        return result;
    }

    @Override
    public Pager<BlogDTO> blog() {
        Pager<BlogDTO> result = blogService.queryBlogByPage(1, 10);
        return toTenPager(result);
    }

    @Override
    public int delBlog(int id) {
        return blogService.deleteBlog(id);
    }

    @Override
    public Pager<BlogDTO> adminBlogPage(int page) {
        Pager<BlogDTO> result = blogService.queryBlogByPage(page, 10);
        return toTenPager(result);
    }

    @Override
    public Pager<LoginUserDTO> allWaitPassAuthor(int page, int size) {
        List<LoginUserDTO> waitAuthorPage = userService.getWaitAuthorPage(page, size);
        Pager<LoginUserDTO> pager = new Pager();
        pager.setRows(waitAuthorPage);
        pager.setPage(page);
        int total = userService.getCountWaitAuthor();
        pager.setTotal((long) total);
        if (total <= size) {
            pager.setSize(1);
        } else if (total % size > 0) {
            pager.setSize(total / size + 1);
        } else {
            pager.setSize(total / size);
        }

        return pager;
    }

    @Override
    public int passAuthor(int uid) {
        userService.passAuthor(uid);
        emailService.sentToUser(uid);
        return 0;
    }

    @Override
    public Pager<AdminComments> getAllCommentsPage(int page, int size) {
        Pager<AdminComments> result = new Pager();
        List<Comments> pageCommentsSize = commentsService.getPageCommentsSize(page, 10);
        List<AdminComments> adminComments = listCommenttoPager(pageCommentsSize);
        result.setRows(adminComments);
        result.setPage(page);
        int count = commentsService.getAllCommentsSize();
        result.setTotal((long) count);
        if (count <= size) {
            result.setSize(1);
        } else if (count % size > 0) {
            result.setSize(count / size + 1);
        } else {
            result.setSize(count / size);
        }

        return result;
    }

    @Override
    public int delComment(int id) {
        return commentsService.delComment(id);
    }

    @Override
    public Pager<AdminUser> getAllUserPage(int page, int size) {
        Pager<AdminUser> pager = new Pager();
        List<User> allUser = userService.getAllUser(page, size);
        List<AdminUser> adminUsers = userToAdminUser(allUser);
        pager.setRows(adminUsers);
        pager.setPage(page);
        int count = userService.getCountUserSize();
        pager.setTotal((long) count);
        if (count <= size) {
            pager.setSize(1);
        } else if (count % size > 0) {
            pager.setSize(count / size + 1);
        } else {
            pager.setSize(count / size);
        }

        return pager;
    }

    @Override
    public int delUser(int id) {
        return userService.delUser(id);
    }

    public List<AdminUser> userToAdminUser(List<User> users) {
        ArrayList<AdminUser> adminUsers = new ArrayList();
        Iterator var3 = users.iterator();

        while (var3.hasNext()) {
            User user = (User) var3.next();
            AdminUser adminUser = new AdminUser();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            adminUser.setBrith(format.format(user.getBrith()));
            adminUser.setUid(user.getUid());
            adminUser.setCapacity(user.getCapacity());
            adminUser.setEmail(user.getEmail());
            adminUser.setUname(user.getUname());
            adminUsers.add(adminUser);
        }

        return adminUsers;
    }

    public List<AdminComments> listCommenttoPager(List<Comments> list) {
        List<AdminComments> adminComments = new ArrayList();
        Iterator var3 = list.iterator();

        while (var3.hasNext()) {
            Comments comments = (Comments) var3.next();
            AdminComments adminComments1 = new AdminComments();
            adminComments1.setId(comments.getId());
            adminComments1.setAuthor(userService.queryUserById(comments.getUid()).getUname());
            adminComments1.setBlogName(blogService.queryBlogById(comments.getBlogId()).getTitle());
            adminComments1.setConment(comments.getContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            adminComments1.setTime(dateFormat.format(comments.getCreateTime()));
            adminComments.add(adminComments1);
        }

        return adminComments;
    }

    public List<BlogDTO> pageBlogUtil(List<Blog> blogs) {
        List<BlogDTO> blogDTOS = new ArrayList();
        Iterator var3 = blogs.iterator();

        while (var3.hasNext()) {
            Blog blog = (Blog) var3.next();
            BlogDTO blogDTO = new BlogDTO();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.format(blog.getReleaseDate());
            blogDTO = BlogUtils.BolgDateToYMD(blog, blogDTO);
            BeanUtils.copyProperties(blog, blogDTO);
            Category category = categoryService.getCategoryById(blog.getCategoryId());
            blogDTO.setCatecoty(category.getName());
            int count = commentsService.queryCountByBlogId(blog.getId());
            blogDTO.setCount(count);
            User user = userService.queryUserById(blog.getUid());
            blogDTO.setAuthor(user.getUname());
            blogDTOS.add(blogDTO);
        }

        return blogDTOS;
    }

    public Pager<BlogDTO> toTenPager(Pager<BlogDTO> page) {
        int count = blogService.queryAllBlogSize();
        page.setTotal((long) count);
        page.setPage(page.getPage());
        if (count <= 10) {
            page.setSize(1);
        } else if (count % 10 > 0) {
            page.setSize(count / 10 + 1);
        } else {
            page.setSize(count / 10);
        }

        return page;
    }
}
