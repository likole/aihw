package com.likole.aihw.utils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizer.WhiteSpaceWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likole
 */
public class WordCloudUtils {

    public static List<WordFrequency> frequence(String abstractt, List<String> keywords){
        if(keywords==null){
            keywords=new ArrayList<>();
        }
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(4);
        frequencyAnalyzer.setWordTokenizer(new WhiteSpaceWordTokenizer());
        List<String> lists=new ArrayList<>();
        lists.add(abstractt);
        for (int i=0;i<keywords.size();i++)
        {
            for (int j=0;j<keywords.size()-i+2;j++){
                lists.add(keywords.get(i));
            }
        }
        return frequencyAnalyzer.load(lists);
    }
}
