package preprocess.spider.article;

import com.likole.aihw.bean.Article;
import com.likole.aihw.bean.Author;
import com.likole.aihw.bean.AuthorArticle;
import com.likole.aihw.bean.KeywordArticle;
import preprocess.utils.DbUtils;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import us.codecraft.xsoup.Xsoup;

import java.util.List;


/**
 * @author likole
 */
public class PostProcess {

    public static void process(Elements items) {

        Article article = new Article();
        for (org.jsoup.nodes.Element item : items) {
            String key = Xsoup.select(item, "//td[1]//html()").get();
            String value = Xsoup.select(item, "//td[2]//html()").get();
            switch (key) {
                case "PT":
                    article.setType(value);
                    break;
                case "AU":
                    article.setAuthor(Json.toJson(value.split(" <br>")));
                    break;
                case "AF":
                    article.setAuthorFullname(Json.toJson(value.split(" <br>")));
                    break;
                case "DE":
                    article.setKeyword(Json.toJson(value.split("; ")));
                    break;
                case "TI":
                    article.setTitle(value);
                    break;
                case "SO":
                    article.setSource(value);
                    break;
                case "CT":
                    article.setConference(value);
                    break;
                case "AB":
                    article.setAbstractt(value);
                    break;
                case "PY":
                    article.setYear(Integer.parseInt(value));
                    break;
                case "UT":
                    article.setWos(value.substring(4));
                    break;
                default:
            }
        }
//        DbUtils.getDao().insertOrUpdate(article);
//        processWordcloud(article);
//        extractAuthor(article);
        extractKeyword(article);
        System.out.println(article.getWos() + " updated!");
    }

    private static void extractKeyword(Article article) {
        if(article.getKeyword()==null) {
            return;
        }
        List<String> keywords= (List<String>) Json.fromJson(article.getKeyword());
        for(String keyword:keywords){
            KeywordArticle keywordArticle=new KeywordArticle();
            keywordArticle.setKeyword(keyword);
            keywordArticle.setWos(article.getWos());
            keywordArticle.setTitle(article.getTitle());
            DbUtils.getDao().insertOrUpdate(keywordArticle);
        }
    }

    private static void extractAuthor(Article article) {
        List<String> authorFullNames= (List<String>) Json.fromJson(article.getAuthorFullname());
        List<String> authorShortNames= (List<String>) Json.fromJson(article.getAuthor());
        for(int i=0;i<authorFullNames.size();i++){
            //author count
            Author author= DbUtils.getDao().fetch(Author.class,authorFullNames.get(i));
            if(author==null){
                author=new Author();
                author.setAuthorFullName(authorFullNames.get(i));
                author.setAuthorShortName(authorShortNames.get(i));
                author.setArticleNumbers(0);
            }
            author.setFirstYear(article.getYear());
            author.setArticleNumbers(author.getArticleNumbers()+1);
            DbUtils.getDao().insertOrUpdate(author);
            //author article
            AuthorArticle authorArticle=new AuthorArticle();
            authorArticle.setAuthorFullName(authorFullNames.get(i));
            authorArticle.setTitle(article.getTitle());
            authorArticle.setWos(article.getWos());
            DbUtils.getDao().insertOrUpdate(authorArticle);
        }
    }

    private static void processWordcloud(Article article) {
        if(article.getAbstractt()!=null){
            if(article.getKeyword()==null){
                WordCloud.generate(article.getWos(),article.getAbstractt());
            }else{
                WordCloud.generate(article.getWos(),article.getAbstractt(), (List<String>) Json.fromJson(article.getKeyword()));
            }
        }
    }
}
