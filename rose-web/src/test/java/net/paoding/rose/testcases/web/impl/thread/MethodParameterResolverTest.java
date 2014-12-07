package net.paoding.rose.testcases.web.impl.thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.rose.mock.controllers.methodparameter.MethodParameter2Controller;
import net.paoding.rose.mock.controllers.methodparameter.MethodParameter2Controller.User;
import net.paoding.rose.testcases.AbstractControllerTest;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.InvocationUtils;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public class MethodParameterResolverTest extends AbstractControllerTest {

    //
    MockMultipartHttpServletRequest multipartRequest;

    MultipartFile file1;

    MultipartFile file2;

    @Override
    public void innerSetUp() throws Exception {

        //
        multipartRequest = new MockMultipartHttpServletRequest();
        multipartRequest.setMethod("POST");
        file1 = new MockMultipartFile("file1", "originalFileName1", "application/oct-stream",
                new byte[] { 1, 2 });
        file2 = new MockMultipartFile("file2", "originalFileName2", "application/oct-stream",
                new byte[] { 3, 4 });
        multipartRequest.addFile(file1);
        multipartRequest.addFile(file2);
    }

    @Override
    protected void tearDown() throws Exception {
        InvocationUtils.unindRequestFromCurrentThread();
    }

    public void testInvocation() throws Exception {
        Object[] parameters = resolveMethodParameters("inv");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(InvocationUtils.getInvocation(request), parameters[0]);
    }

    public void testRequest() throws Exception {
        Object[] parameters = resolveMethodParameters("request");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(request, parameters[0]);
    }

    public void testRequest2() throws Exception {
        Object[] parameters = resolveMethodParameters("request2");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(request, parameters[0]);
    }

    public void testResponse() throws Exception {
        Object[] parameters = resolveMethodParameters("response");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(response, parameters[0]);
    }

    public void testResponse2() throws Exception {
        Object[] parameters = resolveMethodParameters("response2");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(response, parameters[0]);
    }

    public void testSession() throws Exception {
        Object[] parameters = resolveMethodParameters("session");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(request.getSession(), parameters[0]);
    }

    public void testSession2() throws Exception {
        Object[] parameters = resolveMethodParameters("session2");
        assertNotNull(parameters);
        assertNull(parameters[0]);
    }

    public void testModel() throws Exception {
        Object[] parameters = resolveMethodParameters("model");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(InvocationUtils.getInvocation(request).getModel(), parameters[0]);
    }

    public void testFlash() throws Exception {
        Object[] parameters = resolveMethodParameters("flash");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertSame(InvocationUtils.getInvocation(request).getFlash(), parameters[0]);
    }

    public void testHello() throws Exception {
        request.addParameter("name", "rose");
        request.addParameter("int", "345");
        request.addParameter("bool", "true");

        assertEquals("rose", request.getParameter("name"));

        Object[] parameters = resolveMethodParameters("hello");
        assertNotNull(parameters);
        assertEquals("rose", parameters[0]);
        assertEquals(345, parameters[1]);
        assertEquals(true, parameters[2]);
    }

    public void testMultiPartFile0() throws Exception {
        Object[] parameters = resolveMethodParameters("multipart0");
        assertNotNull(parameters);
        assertNull(parameters[0]);
        assertNull(parameters[1]);
        assertNull(parameters[2]);
    }

    public void testMultiPartFile() throws Exception {
        //        inv.setRequest(multipartRequest);
        //        InvocationUtils.bindRequestToCurrentThread(multipartRequest);
        //        InvocationUtils.bindInvocationToRequest(inv,
        //                new HttpServletRequestWrapper(multipartRequest));
        this.request = multipartRequest;
        Object[] parameters = resolveMethodParameters("multipart");
        assertNotNull(parameters);
        assertSame(file1, parameters[0]);
        assertSame(file2, parameters[1]);
        assertSame(multipartRequest, parameters[2]);
    }

    public void testMultiPartFile2() throws Exception {
        //        inv.setRequest(multipartRequest);
        //        InvocationUtils.bindRequestToCurrentThread(multipartRequest);
        //        InvocationUtils.bindInvocationToRequest(inv,
        //                new HttpServletRequestWrapper(multipartRequest));
        this.request = multipartRequest;
        Object[] parameters = resolveMethodParameters("multipart2");
        assertNotNull(parameters);
        assertSame(multipartRequest, parameters[0]);
        assertSame(file2, parameters[1]);
        assertSame(file1, parameters[2]);
    }

    public void testArray() throws Exception {
        request.addParameter("int", "1");
        request.addParameter("int", "2");
        request.addParameter("int", "3");

        request.addParameter("integer", "4");
        request.addParameter("integer", "5");
        request.addParameter("integer", "6");
        
        request.addParameter("bool", "true");
        request.addParameter("bool", "false");

        Object[] parameters = resolveMethodParameters("array");
        assertNotNull(parameters);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof int[]);
        assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, (int[]) parameters[0]));

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof Integer[]);
        assertTrue(Arrays.equals(new Integer[] { 4, 5, 6 }, (Integer[]) parameters[1]));

        assertNotNull(parameters[2]);
        assertTrue("unexpected type " + parameters[2].getClass().getName(),
                parameters[2] instanceof String[]);
        assertTrue(((String[]) parameters[2]).length == 0);

        assertTrue("unexpected type " + parameters[3].getClass().getName(),
                parameters[3] instanceof boolean[]);
        assertTrue(Arrays.equals(new boolean[] { true, false }, (boolean[]) parameters[3]));
    }

    public void testList() throws Exception {
        request.addParameter("int", "1");
        request.addParameter("int", "2");
        request.addParameter("int", "3");

        request.addParameter("integer", "4");
        request.addParameter("integer", "5");
        request.addParameter("integer", "6");

        request.addParameter("bool", "true");
        request.addParameter("bool", "false");

        Object[] parameters = resolveMethodParameters("list");
        assertNotNull(parameters);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof List);
        assertTrue(Arrays.equals(new Integer[] { 1, 2, 3 }, ((List<?>) parameters[0])
                .toArray(new Integer[0])));

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof List);
        assertTrue(Arrays.equals(new Integer[] { 4, 5, 6 }, ((List<?>) parameters[1])
                .toArray((new Integer[0]))));

        assertNotNull(parameters[2]);
        assertTrue("unexpected type " + parameters[2].getClass().getName(),
                parameters[2] instanceof List);
        assertTrue(((List<?>) parameters[2]).size() == 0);

        assertTrue("unexpected type " + parameters[3].getClass().getName(),
                parameters[3] instanceof List);
        assertTrue(Arrays.equals(new Boolean[] { true, false }, ((List<?>) parameters[3])
                .toArray((new Boolean[0]))));
    }

    public void testSet() throws Exception {
        request.addParameter("int", "1");
        request.addParameter("int", "2");
        request.addParameter("int", "3");

        request.addParameter("integer", "4");
        request.addParameter("integer", "5");
        request.addParameter("integer", "6");

        request.addParameter("bool", "true");
        request.addParameter("bool", "false");

        Object[] parameters = resolveMethodParameters("set");
        assertNotNull(parameters);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof Set);
        assertTrue(Arrays.equals(new Integer[] { 1, 2, 3 }, ((Set<?>) parameters[0])
                .toArray(new Integer[0])));

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof Set);
        assertTrue(Arrays.equals(new Integer[] { 4, 5, 6 }, ((Set<?>) parameters[1])
                .toArray((new Integer[0]))));

        assertNotNull(parameters[2]);
        assertTrue("unexpected type " + parameters[2].getClass().getName(),
                parameters[2] instanceof Set);
        assertTrue(((Set<?>) parameters[2]).size() == 0);

        assertTrue("unexpected type " + parameters[3].getClass().getName(),
                parameters[3] instanceof Set);
        Boolean[] bools = ((Set<?>) parameters[3]).toArray((new Boolean[0]));
        assertEquals(2, bools.length);
        assertTrue(ArrayUtils.contains(bools, true));
        assertTrue(ArrayUtils.contains(bools, false));
    }

    public void testMap() throws Exception {
        request.addParameter("ss:ss1", "1");
        request.addParameter("ss:ss2", "2");
        request.addParameter("ss:ss3", "3");

        request.addParameter("is:1", "11");
        request.addParameter("is:2", "12");
        request.addParameter("is:3", "13");

        request.addParameter("sf:ss1", "1.02");
        request.addParameter("sf:ss2", "2.12");
        request.addParameter("sf:ss3", "13");

        request.addParameter("ib:1", "1");
        request.addParameter("ib:2", "true");
        request.addParameter("ib:3", "false");

        Object[] parameters = resolveMethodParameters("map");
        assertNotNull(parameters);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof Map);
        Map<?, ?> ss = (Map<?, ?>) parameters[0];
        assertEquals("1", ss.get("ss1"));
        assertEquals("2", ss.get("ss2"));
        assertEquals("3", ss.get("ss3"));

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof Map);
        Map<?, ?> is = (Map<?, ?>) parameters[1];
        assertEquals("11", is.get(1));
        assertEquals("12", is.get(2));
        assertEquals("13", is.get(3));

        assertNotNull(parameters[2]);
        assertTrue("unexpected type " + parameters[2].getClass().getName(),
                parameters[2] instanceof Map);
        assertTrue(((Map<?, ?>) parameters[2]).size() == 0);

        assertTrue("unexpected type " + parameters[3].getClass().getName(),
                parameters[3] instanceof Map);
        Map<?, ?> sf = (Map<?, ?>) parameters[3];
        assertEquals(1.02f, sf.get("ss1"));
        assertEquals(2.12f, sf.get("ss2"));
        assertEquals(13f, sf.get("ss3"));

        assertTrue("unexpected type " + parameters[4].getClass().getName(),
                parameters[4] instanceof Map);
        Map<?, ?> ii = (Map<?, ?>) parameters[4];
        assertEquals(true, ii.get(1));
        assertEquals(true, ii.get(2));
        assertEquals(false, ii.get(3));
    }

    public void testDate() throws Exception {
        request.addParameter("d", "2001-04-23 10:00:42");
        request.addParameter("sd", "2001-04-23");
        request.addParameter("t", "10:00:42");
        request.addParameter("ts", "2001-04-23 10:00:42");
        Object[] parameters = resolveMethodParameters("date");
        assertNotNull(parameters);

        assertEquals(Date.class, parameters[0].getClass());
        assertEquals(java.sql.Date.class, parameters[1].getClass());
        assertEquals(java.sql.Time.class, parameters[2].getClass());
        assertEquals(java.sql.Timestamp.class, parameters[3].getClass());

        assertEquals("2001-04-23 10:00:42", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format((Date) parameters[0]));
        assertEquals("2001-04-23", new SimpleDateFormat("yyyy-MM-dd")
                .format((java.sql.Date) parameters[1]));
        assertEquals("10:00:42", new SimpleDateFormat("HH:mm:ss")
                .format((java.sql.Time) parameters[2]));
        assertEquals("2001-04-23 10:00:42", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format((java.sql.Timestamp) parameters[3]));
    }

    public void testDatedef() throws Exception {
        long low = System.currentTimeMillis();
        Object[] parameters = resolveMethodParameters("datedef");
        long high = System.currentTimeMillis();
        assertNotNull(parameters);

        assertEquals(Date.class, parameters[0].getClass());
        assertEquals(java.sql.Date.class, parameters[1].getClass());
        assertNull(parameters[2]);
        assertNull(parameters[3]);

        assertTrue(((Date) parameters[0]).getTime() >= low);
        assertTrue(((Date) parameters[0]).getTime() <= high);
        assertEquals(123456, ((java.sql.Date) parameters[1]).getTime());
    }

    public void testDatePattern11() throws Exception {
        request.addParameter("d", "20010423100042");
        request.addParameter("t", "100042");

        Object[] parameters = resolveMethodParameters("datePattern1");
        assertNotNull(parameters);

        assertEquals(Date.class, parameters[0].getClass());
        assertEquals(java.sql.Time.class, parameters[1].getClass());

        assertEquals("2001-04-23 10:00:42", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format((Date) parameters[0]));
        assertEquals("10:00:42", new SimpleDateFormat("HH:mm:ss")
                .format((java.sql.Time) parameters[1]));
    }

    public void testDatePattern12() throws Exception {
        request.addParameter("d", "010423100042");
        request.addParameter("t", "42.1000");

        Object[] parameters = resolveMethodParameters("datePattern1");
        assertNotNull(parameters);

        assertEquals(Date.class, parameters[0].getClass());
        assertEquals(java.sql.Time.class, parameters[1].getClass());

        assertEquals("2001-04-23 10:00:42", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format((Date) parameters[0]));
        assertEquals("10:00:42", new SimpleDateFormat("HH:mm:ss")
                .format((java.sql.Time) parameters[1]));
    }

    public void testDatePattern21() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2001-04-23 10:00:42");
        request.addParameter("d", "" + date.getTime());
        request.addParameter("t", "" + date.getTime());

        Object[] parameters = resolveMethodParameters("datePattern2");
        assertNotNull(parameters);

        assertEquals(Date.class, parameters[0].getClass());
        assertEquals(java.sql.Time.class, parameters[1].getClass());

        assertEquals("2001-04-23 10:00:42", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format((Date) parameters[0]));
        assertEquals("10:00:42", new SimpleDateFormat("HH:mm:ss")
                .format((java.sql.Time) parameters[1]));
    }

    public void testUserBean() throws Exception {
        request.addParameter("id", "12");
        request.addParameter("name", "rose");
        request.addParameter("age", "20");

        Object[] parameters = resolveMethodParameters("userBean");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof User);
        User user = (User) parameters[0];
        assertEquals(Long.valueOf(12), user.getId());
        assertEquals("rose", user.getName());
        assertEquals(20, user.getAge());

    }

    public void testUserBean2() throws Exception {
        request.addParameter("ua.id", "12");
        request.addParameter("ua.name", "rose");
        request.addParameter("ua.age", "20");

        request.addParameter("ub.id", "13");

        Object[] parameters = resolveMethodParameters("userBean2");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertNotNull(parameters[1]);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof User);
        User ua = (User) parameters[0];
        assertEquals(Long.valueOf(12), ua.getId());
        assertEquals("rose", ua.getName());
        assertEquals(20, ua.getAge());

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof User);
        User ub = (User) parameters[1];
        assertEquals(Long.valueOf(13), ub.getId());
        assertNull(ub.getName());
        assertEquals(0, ub.getAge());

    }

    public void testBindingResult() throws Exception {
        request.addParameter("id", "42");
        request.addParameter("name", "rose");
        request.addParameter("age", "20");

        Object[] parameters = resolveMethodParameters("bindingResult");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertNotNull(parameters[1]);

        assertTrue("unexpected type " + parameters[0].getClass().getName(),
                parameters[0] instanceof User);
        User ua = (User) parameters[0];
        assertEquals(Long.valueOf(42), ua.getId());
        assertEquals("rose", ua.getName());
        assertEquals(20, ua.getAge());

        assertTrue("unexpected type " + parameters[1].getClass().getName(),
                parameters[1] instanceof BindingResult);
    }

    public void testInt() throws Exception {
        Object[] parameters = resolveMethodParameters("innt");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertEquals(Integer.class, parameters[0].getClass());
        assertEquals(0, parameters[0]);
    }

    public void testInteger() throws Exception {
        Object[] parameters = resolveMethodParameters("integer");
        assertNotNull(parameters);
        assertNull(parameters[0]);
    }

    public void testBool() throws Exception {
        Object[] parameters = resolveMethodParameters("bool");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertEquals(Boolean.class, parameters[0].getClass());
        assertEquals(false, parameters[0]);
    }

    public void testBoool() throws Exception {
        Object[] parameters = resolveMethodParameters("boool");
        assertNotNull(parameters);
        assertNull(parameters[0]);
    }

    public void testLoong() throws Exception {
        Object[] parameters = resolveMethodParameters("loong");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertEquals(Long.class, parameters[0].getClass());
        assertEquals(0L, parameters[0]);
    }

    public void testLooong() throws Exception {
        Object[] parameters = resolveMethodParameters("looong");
        assertNotNull(parameters);
        assertNull(parameters[0]);
    }

    public void testString() throws Exception {
        Object[] parameters = resolveMethodParameters("string");
        assertNotNull(parameters);
        assertNull(parameters[0]);
    }

    public void testNullPrimitiveInt() throws Exception, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Object[] parameters = resolveMethodParameters("nullPrimitiveInt");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertEquals(Integer.class, parameters[0].getClass());
        assertEquals(0, parameters[0]);
        Method method = findMethod("nullPrimitiveInt");
        method.invoke(new MethodParameter2Controller(), parameters);
        assertTrue(true);
    }

    public void testNullPrimitiveBool() throws Exception, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Object[] parameters = resolveMethodParameters("nullPrimitiveBool");
        assertNotNull(parameters);
        assertNotNull(parameters[0]);
        assertEquals(Boolean.class, parameters[0].getClass());
        assertEquals(false, parameters[0]);
        Method method = findMethod("nullPrimitiveBool");
        method.invoke(new MethodParameter2Controller(), parameters);
        assertTrue(true);
    }

    public void testNullPrimitiveBoolWrapper() throws Exception, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Object[] parameters = resolveMethodParameters("nullPrimitiveBoolWrapper");
        assertNotNull(parameters);
        assertNull(parameters[0]);
        Method method = findMethod("nullPrimitiveBoolWrapper");
        method.invoke(new MethodParameter2Controller(), parameters);
        assertTrue(true);
    }

    public void testInf() throws Exception, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        request.addParameter("a", "2001");
        request.addParameter("controller.bool", "true");

        Object[] parameters = resolveMethodParameters("inf");
        assertNotNull(parameters);
        assertNull(parameters[0]);
        assertNotNull(parameters[1]);
        assertEquals(2001, parameters[1]);
        assertEquals(true, parameters[2]);
        Method method = findMethod("inf");
        method.invoke(new MethodParameter2Controller(), parameters);
        assertTrue(true);
    }

    public void testInf2() throws Exception, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        request.addParameter("a", "20a01"); // type miss match 不要阻止下一个转化
        request.addParameter("controller.bool", "true");

        Object[] parameters = resolveMethodParameters("inf");
        assertNotNull(parameters);
        assertNull(parameters[0]);
        assertNotNull(parameters[1]);
        assertEquals(0, parameters[1]);
        assertEquals(true, parameters[2]);
        Method method = findMethod("inf");
        method.invoke(new MethodParameter2Controller(), parameters);
        assertTrue(true);
    }

    protected Object[] resolveMethodParameters(String methodName) throws Exception {
        String uri = "/methodparameter/methodParameter2/" + methodName;
        invoke(uri, "GET", "");
        Invocation inv = (Invocation) request.getAttribute("$$paoding-rose.invocation");
        return inv.getMethodParameters();

        //        Method method = findMethod(methodName);
        //        assertNotNull("not found method named: " + methodName, method);
        //        ParameterNameDiscovererImpl parameterNameDiscoverer = new ParameterNameDiscovererImpl();
        //        ResolverFactoryImpl resolverFactory = new ResolverFactoryImpl();
        //        MethodParameterResolver resolver = new MethodParameterResolver(MockController.class,
        //                method, parameterNameDiscoverer, resolverFactory);
        //        return resolver.resolve(inv, paramenterBindingResult);
    }

    private Method findMethod(String name) {
        try {
            Method[] methods = MethodParameter2Controller.class.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(name)) {
                    return method;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
