package com.pupu.es.repositories;

import com.pupu.es.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *
 * lasticsearchRepository<Article,Long>
 *      第一个参数：要操作的对象
 *      第二个参数：主键类型
 *
 * @author : lipu
 * @since : 2020-08-15 16:27
 */
public interface ArticleRepository extends ElasticsearchRepository<Article,Long> {

    List<Article> findByTitle(String title);
    List<Article> findByTitleOrContent(String title,String content);
    List<Article> findByTitleOrContent(String title, String content, Pageable pageable);
}
