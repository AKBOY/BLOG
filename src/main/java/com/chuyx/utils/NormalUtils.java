package com.chuyx.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chuyx.pojo.dto.Pager;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @author yasir.chu
 * @date 2021/1/27
 **/

public class NormalUtils {


    /**
     * 设置页面数据
     *
     * @param iPage MP的分页信息
     * @param rows  需要的数据
     * @param <T>   分页信息的数据类
     * @param <V>   需要的数据的类
     * @return pager的分页信息
     */
    public static <T, V> Pager<V> pagerRows(IPage<T> iPage, List<V> rows) {
        Pager<V> vPager = new Pager<>();
        int total = Integer.parseInt(String.valueOf(iPage.getTotal()));
        int pageSize = Integer.parseInt(String.valueOf(iPage.getSize()));
        int size = total/pageSize;
        if (total%pageSize > 0){
            size += 1;
        }
        vPager.setSize(size);
        vPager.setTotal(iPage.getTotal());
        vPager.setRows(rows);
        vPager.setPage(Integer.parseInt(String.valueOf(iPage.getCurrent())));
        return vPager;
    }

    /**
     * 密码加密
     * @param password 加密前密码
     * @return 加密后密码
     */
    public static String encodePassword(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * 比较两个密码是否相同
     * @param password 输入密码
     * @param sqlPassword 数据库保存的加密密码
     * @return 结果
     */
    public static Boolean comparePassword(String password, String sqlPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, sqlPassword);
    }
}
