package com.likole.aihw.preprocess.spider.article;

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

    private void parse(String url) throws IOException {
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

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.parse("http://127.0.0.1:8082/static/1.html");
        parser.parse("http://127.0.0.1:8082/static/2.html");
        parser.parse("http://127.0.0.1:8082/static/3.html");
    }

}
