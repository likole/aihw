package com.likole.aihw;

import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

public class MainSetup implements Setup {
    @Override
    public void init(NutConfig nutConfig) {
        Ioc ioc= nutConfig.getIoc();
        Dao dao=ioc.get(Dao.class);
        Daos.createTablesInPackage(dao,"com.likole.aihw.bean",false);
        Daos.migration(dao,"com.likole.aihw.bean",true,false,true);
    }

    @Override
    public void destroy(NutConfig nutConfig) {

    }
}
