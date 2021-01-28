package com.chuyx.controller;

import com.chuyx.api.ViewJumpApi;
import com.chuyx.constant.NormalConstant;
import com.chuyx.pojo.dto.LoginUserDTO;
import com.chuyx.pojo.dto.Pager;
import com.chuyx.pojo.model.Blog;
import com.chuyx.pojo.vo.BlogBaseVO;
import com.chuyx.service.BlogService;
import com.chuyx.service.ViewJumpService;
import com.chuyx.utils.DozerUtil;
import com.chuyx.service.UserService;
import com.chuyx.wrapper.BlogWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yasir.chu
 * @date 2021/1/27
 **/
@Controller
@Slf4j
public class ViewJumpController implements ViewJumpApi {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private ViewJumpService viewJumpService;

    @Override
    public String index() {
        return "index";
    }

    @Override
    public String blogPage(Model model) {
        BlogWrapper.QueryPageDTO queryPageDTO = new BlogWrapper.QueryPageDTO();
        queryPageDTO.setSize(NormalConstant.TOP_SIZE);
        queryPageDTO.setPage(NormalConstant.ONE);
        Pager<BlogBaseVO> result = blogService.queryPageBlog(queryPageDTO);
        model.addAttribute("blogDTOS", result);
        return "ordinary/article";
    }

    @Override
    public String blogPage(int categoryId, int page, Model model) {
        BlogWrapper.QueryPageDTO queryPageDTO = new BlogWrapper.QueryPageDTO();
        queryPageDTO.setSize(NormalConstant.TOP_SIZE);
        queryPageDTO.setPage(page);
        if (categoryId > 0){
            queryPageDTO.setOrdinaryId(categoryId);
        }
        Pager<BlogBaseVO> result = blogService.queryPageBlog(queryPageDTO);
        model.addAttribute("blogDTOS", result);
        return "ordinary/article";
    }

    @Override
    public String readBlog(int id, Model model) {
        BlogBaseVO result = viewJumpService.readBlog(id);
        model.addAttribute("blog", result);
        return "ordinary/read";
    }

    @Override
    public String signOut(HttpSession session, Model model) {
        session.removeAttribute("userMsg");
        return blogPage(model);
    }


    @Override
    public String login(HttpSession session, HttpServletRequest request) {
        session.setAttribute("beforeSignin", request.getHeader("Referer"));
        return "ordinary/signin";
    }

    @Override
    public String signUp() {
        return "ordinary/signup";
    }


}