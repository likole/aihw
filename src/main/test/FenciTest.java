import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizer.WhiteSpaceWordTokenizer;
import com.kennycason.kumo.nlp.tokenizers.EnglishWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FenciTest {

    @Test
    public void jieba() {
//        JiebaSegmenter segmenter = new JiebaSegmenter();
//        System.out.println(segmenter.process("Edward Said's concept of Orientalism portrays the high tide of nineteenth-century imperialism as the defining moment in the establishment of a global discursive hegemony, in which European attitudes and concepts gained a universal validity. The idea of \"religion\" was central to the civilizing mission of imperialism, and was shaped by the interests of a number of colonial actors in a way that remains visibly relevant today. In East and Southeast Asia, however, many of the concerns that statecraft, law, scholarship, and conversion had for religion transcended the European impact. Both before and after the period of European imperialism, states used religion to engineer social ethics and legitimate rule, scholars elaborated and enforced state theologies, and the missionary faithful voiced the need for and nature of religious conversion. The real impact of this period was to integrate pre-existing concerns into larger discourses, transforming them in the process. The ideals of national citizenship and of legal and scholarly impartiality recast the state and its institutions with a modernist sacrality, which had the effect of banishing the religious from the public space. At the same time, the missionary discourse of transformative conversion located it in the very personal realm of sincerity and belief. The evolution of colonial-era discourses of religion and society in Asia since the departure of European imperial power demonstrates both their lasting power and the degree of agency that remains implicit in the idea of hegemony.", JiebaSegmenter.SegMode.INDEX).toString());
    }

    @Test
    public void kumo() throws IOException {
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setWordTokenizer(new WhiteSpaceWordTokenizer());
        List<String> lists=new ArrayList<>();
        lists.add("Edward Said's concept of Orientalism portrays the high tide of nineteenth-century imperialism as the defining moment in the establishment of a global discursive hegemony, in which European attitudes and concepts gained a universal validity. The idea of \"religion\" was central to the civilizing mission of imperialism, and was shaped by the interests of a number of colonial actors in a way that remains visibly relevant today. In East and Southeast Asia, however, many of the concerns that statecraft, law, scholarship, and conversion had for religion transcended the European impact. Both before and after the period of European imperialism, states used religion to engineer social ethics and legitimate rule, scholars elaborated and enforced state theologies, and the missionary faithful voiced the need for and nature of religious conversion. The real impact of this period was to integrate pre-existing concerns into larger discourses, transforming them in the process. The ideals of national citizenship and of legal and scholarly impartiality recast the state and its institutions with a modernist sacrality, which had the effect of banishing the religious from the public space. At the same time, the missionary discourse of transformative conversion located it in the very personal realm of sincerity and belief. The evolution of colonial-era discourses of religion and society in Asia since the departure of European imperial power demonstrates both their lasting power and the degree of agency that remains implicit in the idea of hegemony.");
        final List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load(lists);

        Dimension dimension = new Dimension(1920,1080);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 20);
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255,255,255));
        wordCloud.setBackground(new CircleBackground(255));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 45));
        wordCloud.build(wordFrequencyList);
        wordCloud.writeToFile("test.png");


    }
}
