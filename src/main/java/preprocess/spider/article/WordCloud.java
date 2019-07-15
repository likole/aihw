package preprocess.spider.article;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likole
 */
public class WordCloud {

    public static void generate(String wos,String abstractt){
        List<String> keywords=new ArrayList<>();
        generate(wos,abstractt,keywords);
    }

    public static void generate(String wos,String abstractt, List<String> keywords){
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(4);
        //frequencyAnalyzer.setWordTokenizer(new WhiteSpaceWordTokenizer());
        List<String> lists=new ArrayList<>();
        lists.add(abstractt);
        for (int i=0;i<keywords.size();i++)
        {
            for (int j=0;j<keywords.size()-i+2;j++){
                lists.add(keywords.get(i));
            }
        }
        final List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load(lists);
        Dimension dimension = new Dimension(600,600);
        com.kennycason.kumo.WordCloud wordCloud = new com.kennycason.kumo.WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 20);
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255,255,255));
        wordCloud.setBackground(new CircleBackground(255));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        wordCloud.build(wordFrequencyList);
        wordCloud.writeToFile("src/main/webapp/static/wordcloud/"+wos+".png");
    }
}
