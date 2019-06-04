package preprocess.spider.article;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;

/**
 * @author likole
 */
public class Parser {

    public void parse(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        Document document = Jsoup.parse(response.body().string());
        Elements articles = Xsoup.select(document, "//body/table").getElements();
        for (int i = 1; i < articles.size(); i++) {
            try {
                PostProcess.process(Xsoup.select(articles.get(i), "//tbody/tr").getElements());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
