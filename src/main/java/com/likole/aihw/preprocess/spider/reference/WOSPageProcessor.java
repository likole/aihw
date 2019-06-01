package com.likole.aihw.preprocess.spider.reference;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class WOSPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).addHeader("cookie", "dotmatics.elementalKey=SLsLWlMhrHnTjDerSrlG; _sp_ses.630e=*; bm_sz=9820EF1B1FF986102D5E6F2A8B7FFBC8~YAAQnVcyuMQM6OtqAQAAzz0gDQMOCsKn2OaUrs/61smv82Mqv+TvG7pqC+HVh/HQr/Ovm2FjlpqaLOhDj/sFw3cQKxtPjxP5GrqPO1oH9lYpcuQPGqdVZeqUyIPSJhYg1q9VxeqBVYqYNAKAg5s5toRn1RkZZ9+ebpLZgkbV6Yp0ct2kwN6imq2L5p6cbDIsrZTreO+IROE=; JSESSIONID=75A2D62F9BEE59658EDA2F21500D3D15; ak_bmsc=E5CF5BA55CFA54E5C560C21529F79FA1B832575DD71D000076F6F05C9295AB66~pllKFdFFjT93YEPr9zvCVuFBruXJvB/ZtDNgUwNcN55lCiLVWKqqNYpljASIcXbbJKe4uflg6PdsZdJJ+N/t3UXQwhfU58YNwoS5uaoRz8W0YIFx6wEzIUyG/2qKFBL+Y4cxeHmcddX5dN07FKwxQC5aMjtZ5jzEMHmWYmvbCz6SkB847s2uVk3SOWdE8BcElQ9xrM6lVHgNLFBR+noMJcO5vuEEXwHi3jAlhgsIA9G2QQAmE1icSINZLYkGb9V6pJ; _sp_id.630e=40277f83-5642-4fb4-9c15-49995d55da83.1559133153.3.1559295608.1559280465.27f5302c-4685-4cde-a5df-0d60a02adaee; _abck=65BF7F716E45F6B23CE1953497896010B83257556D7B0000DC7BEE5C579F144F~0~jmB64Nvw8vWmfyU9saL9sySLg6/NVLHOsPQ+MirICVc=~-1~-1");

    @Override
    public void process(Page page) {
        //wos
        String wos=page.getHtml().xpath("//input[@name='00N70000002n880']/@value").get();
        if(wos.length()==19){
            page.putField("fromWOS",wos.substring(4));
        }

        //title
        page.putField("fromTitle", page.getHtml().xpath("//div[@class='title']/allText()").get());

        //reference documents
        page.putField("toTitles", page.getHtml().xpath("//a[@class='smallV110 snowplow-full-record']/allText()").all());
        page.putField("toWOSs", page.getHtml().xpath("//a[@class='smallV110 snowplow-full-record']/@href").all());

        //skip
        if (page.getResultItems().get("fromWOS") == null) {
            page.setSkip(true);
        }
        page.addTargetRequest(page.getHtml().xpath("//form[@id='paginationForm']//a[2]").links().get());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.setProperty("selenuim_config", "/home/likole/tmp/selenuim/config.ini");
        Spider.create(new WOSPageProcessor())
                .addUrl("http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=33&SID=6BYRHPnTGnonsmCbQU6&page=1&doc=500")
                .setDownloader(new SeleniumDownloader())
                .addPipeline(new DbPipeline())
                .addPipeline(new ConsolePipeline())
                .thread(5)
                .run();
    }
}