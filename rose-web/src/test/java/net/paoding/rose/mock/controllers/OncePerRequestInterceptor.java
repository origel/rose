package net.paoding.rose.mock.controllers;

import java.lang.reflect.Method;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Interceptor;

@Interceptor(oncePerRequest = true)
public class OncePerRequestInterceptor extends ControllerInterceptorAdapter {

    int count;

    @Override
    protected boolean isForAction(Method actionMethod, Class<?> controllerClazz) {
        return OncePerRequestController.class == controllerClazz;
    }

    @Override
    public Object before(Invocation inv) throws Exception {
        if (count > 0) {
            throw new IllegalArgumentException("onceperrequest");
        }
        count++;
        return super.before(inv);
    }
}
