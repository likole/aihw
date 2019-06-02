package com.likole.aihw;

import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CrossOriginFilter;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.plugins.apidoc.ApidocUrlMapping;


/**
 * @author likole
 */
@SetupBy(value = MainSetup.class)
@IocBy(type = ComboIocProvider.class, args = {"*js", "ioc/",
        "*anno", "com.likole.aihw",
        "*tx", // 事务拦截 aop
        "*async"}) // 异步执行aop
@Modules
@Ok("json:full")
@Fail("jsp:jsp.500")
@UrlMappingBy(ApidocUrlMapping.class)
@Filters(@By(type = CrossOriginFilter.class))
public class MainModule {

    @At("/test")
    public Object relogin() {
        NutMap re = new NutMap();
        return re.setv("code", 666).setv("msg", "666");
    }
}
