package preprocess;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;

/**
 * @author likole
 */
public class DbUtils {
    private static Dao dao;


    public static Dao getDao() {
        if (dao == null) {
            SimpleDataSource dataSource = new SimpleDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1/aihw");
            dataSource.setUsername("aihw");
            dataSource.setPassword("2CNctX4ht6Rhe6yp");
            dao = new NutDao(dataSource);
        }
        return dao;
    }


}
