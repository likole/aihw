package preprocess.spider.reference;

import com.likole.aihw.bean.ArticleReference;
import preprocess.DbUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * @author likole
 */
public class DbPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        String fromWOS=resultItems.get("fromWOS");
        String fromTitle=resultItems.get("fromTitle");
        List<String> toWOSs=resultItems.get("toWOSs");
        List<String> toTitles=resultItems.get("toTitles");
        for (int i=0;i<toWOSs.size();i++){
            ArticleReference articleReference =new ArticleReference();
            articleReference.setFromWOS(fromWOS);
            articleReference.setFromTitle(fromTitle);
            articleReference.setToWOS(toWOSs.get(i).substring(toWOSs.get(i).indexOf("WOS:")+4));
            articleReference.setToTitle(toTitles.get(i));
            DbUtils.getDao().insertOrUpdate(articleReference);
        }
    }
}
