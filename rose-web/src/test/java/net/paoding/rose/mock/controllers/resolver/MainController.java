package net.paoding.rose.mock.controllers.resolver;

import net.paoding.rose.mock.resolvers.Bean;
import net.paoding.rose.mock.resolvers.BeanEx;
import net.paoding.rose.mock.resolvers.Interface;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.context.ApplicationContext;

public class MainController {

    @Get
    public String index(@Param("phone") Phone phone) {
        return phone.getId();
    }

    public String intf(Interface intf) {
        return intf.get();
    }

    public ApplicationContext ctx(ApplicationContext ctx) {
        return ctx;
    }

    public String bean(Bean bean) {
        return bean.get();
    }

    public Object beanex(BeanEx bean) {
        return bean;
    }
}
