import com.pupu.es.entity.Article;
import com.pupu.es.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * 初始化spring容器
 *
 * @author : lipu
 * @since : 2020-08-15 16:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataElasticSearchTest {

    //报错的原因：这个bean是xml配置的，在运行的时候才会有，这里找不到，所以报错。
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ElasticsearchTemplate template;


    @Test
    public void createIndex() throws Exception {
        //创建索引并配置映射关系
        template.createIndex(Article.class);
//        template.putMapping(Article.class);
    }

    //修改和增加是一个
    @Test
    public void addDocument() throws Exception {
        Article article = new Article();
        article.setId(2);
        article.setTitle(" Maven 教程 Maven 翻译为“专家”、“内行”,是 Apache 下的一个纯2 ");
        article.setContent("2018年9月30日 - Maven 教程 Maven 翻译为“专家”、“内行”,是 Apache 下的一个纯 Java 开发的开源项目。基于项目对象模型(缩写:POM)概念,Maven利用一个中央信息片");
        //添加document
        articleRepository.save(article);
    }

    @Test
    public void deleteDocument() throws Exception {
        //添加document
        articleRepository.deleteById(2L);
    }

    @Test
    public void addDocuments() throws Exception {
        for (int i = 10; i < 20; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle(" 十四五”规划编制工作开展网上意见征求 " + i);
            article.setContent("iPhone 12巨省电？小电池+苹果A14，续航比上一代还长三小时习近平总书记近日对“十四五”规划编制工作作出重要指示强调，把加强顶层设计和坚持问计于民统一起来，齐心协力把“十四五”规划编制好。为贯彻落实习近平总书记重要指示精神，“十四五”规划编制工作自16日起开" + i);
            //添加document
            articleRepository.save(article);
        }
    }

    @Test
    public void findAll() throws Exception {
        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(a -> System.out.println(a));

    }

    @Test
    public void finById() throws Exception {
        Optional<Article> article = articleRepository.findById(2L);
        System.out.println(article.get());

    }

    @Test
    public void finByTitle() throws Exception {
        List<Article> articles = articleRepository.findByTitle("意见征求");
        articles.forEach(a -> System.out.println(a));

    }


    @Test
    public void finByTitleOrContent() throws Exception {
        List<Article> articles = articleRepository.findByTitleOrContent("你好","续航");
        articles.forEach(a -> System.out.println(a));

    }

    //默认是对搜索条件先分词，在查询，分过的词是and关系，必须都出现
    @Test
    public void finByTitleOrContentPage() throws Exception {
        PageRequest pageable = PageRequest.of(4, 5);
        List<Article> articles = articleRepository.findByTitleOrContent("你好","续航",pageable);
        articles.forEach(a -> System.out.println(a));

    }


    //想实现不是and的关系，需要用到原生查询
    @Test
    public void testNativeSearchQuery() throws Exception {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("11征求22意见222")
                .defaultField("title"))
                .withPageable(PageRequest.of(0, 5)).build();

        List<Article> articles = template.queryForList(query, Article.class);
        articles.forEach(a -> System.out.println(a));

    }
}
