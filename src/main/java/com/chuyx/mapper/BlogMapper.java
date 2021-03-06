package com.chuyx.mapper;

import com.chuyx.pojo.dto.NewBlogDTO;
import com.chuyx.pojo.po.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {

    /**
     * 查找所有博客列表
     *
     * @return 博客列表
     */
    List<Blog> queryAllBlog();

    /**
     * 统计博客总数量
     *
     * @return 博客总数量
     */
    int queryAllBlogSize();

    /**
     * 根据类别id找到对应博客
     *
     * @param categoryId 类别id
     * @return 对应类别id的博客
     */
    List<Blog> queryBlogByCateId(int categoryId);

    /**
     * 查找访问量前10的博客
     *
     * @return 博客列表
     */
    List<Blog> queryHotBlog();

    /**
     * 查找最新的10篇博客
     *
     * @return 博客列表
     */
    List<Blog> queryNewBlog();

    /**
     * 根据博客id查找博客
     *
     * @return 博客对象
     */
    Blog queryBlogById(int id);

    /**
     * 更新博客访问量
     */
    void updateBlogVisitCount(Blog blog);

    List<Blog> queryCapacityBlog(int capaId);

    List<Blog> queryBlogByPage(int index, int size);

    int countSize();

    List<Blog> queryBlogByPageCata(int cataId, int index, int size);

    int countSizeCata(int id);

    List<Blog> queryBlogSearch(String name);

    int addBlog(NewBlogDTO newBlogDTO);

    List<Blog> queryBlogByAuthorId(int uid);

    int queryBlogByAuthorIdCount(int uid);

    List<Blog> queryBlogByAuthorIdPage(int uid, int index, int pageSize);

    int delBlog(int id);

    int updateBlog(NewBlogDTO newBlogDTO);
}
