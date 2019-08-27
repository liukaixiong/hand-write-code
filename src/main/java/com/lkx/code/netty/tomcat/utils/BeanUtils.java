package com.lkx.code.netty.tomcat.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    protected static final Log logger = LogFactory.getLog(BeanUtils.class);

    private BeanUtils() {
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if
     * cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or <code>Object.class</code> if
     * cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Field getDeclaredField(Object object, String propertyName)
            throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Field getDeclaredField(Class clazz, String propertyName)
            throws NoSuchFieldException {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName()
                + '.' + propertyName);
    }

    /**
     * 暴力获取对象变量值,忽略private,protected修饰符的限制.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static Object forceGetProperty(Object object, String propertyName)
            throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);

        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.info("error wont' happen");
        }
        field.setAccessible(accessible);
        return result;
    }

    /**
     * 暴力设置对象变量值,忽略private,protected修饰符的限制.
     *
     * @throws NoSuchFieldException 如果没有该Field时抛出.
     */
    public static void forceSetProperty(Object object, String propertyName,
                                        Object newValue) throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {
            logger.info("Error won't happen");
        }
        field.setAccessible(accessible);
    }

    /**
     * 暴力调用对象函数,忽略private,protected修饰符的限制.
     *
     * @throws NoSuchMethodException 如果没有该Method时抛出.
     */
    @SuppressWarnings("unchecked")
    public static Object invokePrivateMethod(Object object, String methodName,
                                             Object... params) throws NoSuchMethodException {
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }

        Class clazz = object.getClass();
        Method method = null;
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                method = superClass.getDeclaredMethod(methodName, types);
                break;
            } catch (NoSuchMethodException e) {
                // 方法不在当前类定义,继续向上转型
            }
        }

        if (method == null) {
            throw new NoSuchMethodException("No Such Method:"
                    + clazz.getSimpleName() + methodName);
        }

        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(object, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        method.setAccessible(accessible);
        return result;
    }

    /**
     * 按Filed的类型取得Field列表.
     */
    public static List<Field> getFieldsByType(Object object, Class type) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 按FiledName获得Field的类型.
     */
    public static Class getPropertyType(Class type, String name)
            throws NoSuchFieldException {
        return getDeclaredField(type, name).getType();
    }

    /**
     * 获得field的getter函数名称.
     */
    public static String getGetterName(Class type, String fieldName) {

        if (type.getName().equals("boolean")) {
            return "is" + StringUtils.capitalize(fieldName);
        } else {
            return "get" + StringUtils.capitalize(fieldName);
        }
    }

    /**
     * 获得field的getter函数,如果找不到该方法,返回null.
     */
    @SuppressWarnings("unchecked")
    public static Method getGetterMethod(Class type, String fieldName) {
        try {
            String getterName = getGetterName(type, fieldName);
            return type.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Object invokegetGetterMethod(Object obj, String fieldName) {
        try {
            Method method = obj.getClass().getMethod(getGetterName(obj.getClass(), fieldName));
            Object result = method.invoke(obj);
            return result;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object invoke(String className, String methodName,
                                Class[] argsClass, Object[] args) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class cl = Class.forName(className);
        Method method = cl.getMethod(methodName, argsClass);
        return method.invoke(cl.newInstance(), args);
    }

    @SuppressWarnings("unchecked")
    public static Object invoke(Object oldObject, String methodName,
                                Class[] argsClass, Object[] args) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Class cl = oldObject.getClass();
        Method method = cl.getMethod(methodName, argsClass);
        return method.invoke(oldObject, args);
    }

    /**
     * 获取所有字段属性
     *
     * @param cl 对象类型
     * @return
     * @throws Exception
     */
    public static String[] getFieldsName(Class cl) throws Exception {
        Field[] fields = cl.getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取所有对象的属性转化成list
     *
     * @param cl
     * @return
     */
    public static List<String> getAllFieldName(Class cl) {
        List<String> list = new ArrayList<String>();
        Field[] fields = cl.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            list.add(field.getName());
        }
        while (true) {
            cl = cl.getSuperclass();
            if (cl == Object.class) {
                break;
            }
            list.addAll(getAllFieldName(cl));
        }
        return list;
    }

    public static List<Method> getSetter(Class cl) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = cl.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (!methodName.startsWith("set")) {
                continue;
            }
            list.add(method);
        }
        while (true) {
            cl = cl.getSuperclass();
            if (cl == Object.class) {
                break;
            }
            list.addAll(getSetter(cl));
        }
        return list;
    }

    public static List<Method> getGetter(Class cl) {
        List<Method> list = new ArrayList<Method>();
        Method[] methods = cl.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
                continue;
            }
            list.add(method);
        }
        while (true) {
            cl = cl.getSuperclass();
            if (cl == Object.class) {
                break;
            }
            list.addAll(getGetter(cl));
        }
        return list;
    }

    /**
     * Returns the classname without the package. Example: If the input class is
     * java.lang.String than the return value is String.
     *
     * @param cl The class to inspect
     * @return The classname
     */
    public static String getClassNameWithoutPackage(Class cl) {
        String className = cl.getName();
        int pos = className.lastIndexOf('.') + 1;
        if (pos == -1) {
            pos = 0;
        }

        String name = className.substring(pos);
        return name;
    }

    /**
     * 把DTO对象转成字符串
     *
     * @param obj DTO对象
     * @return 带属性名和值的字符串
     */
    public static String beanToString(Object obj) {
        return JSON.toJSONString(obj);
    }


    /**
     * 通过ClassName获取类的泛型
     *
     * @param className 类的全称
     * @return
     */
    public static Class getClassGenricType(String className) {
        int startIdnex = className.indexOf("<") + 1;
        int endIndex = className.indexOf(">");
        if (startIdnex == -1 || endIndex == -1) {
            return null;
        }
        String classType = className.substring(startIdnex, endIndex);
        // 排除为null和构造参数不确定的情况  如 T , ?
        if (StringUtils.isEmpty(classType) || classType.length() == 1) {
            return null;
        }
        return getClassByString(classType);
    }

    /**
     * 根据路径获取对象
     *
     * @param packagePath
     * @return
     */
    public static Class getClassByString(String packagePath) {
        if (!StringUtils.isEmpty(packagePath)) {
            try {
                return classForName(packagePath);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        } catch (Exception e) {
            // Ignore. Failsafe below.
        }
        if (clazz == null) {
            clazz = Class.forName(className);
        }
        return clazz;
    }

    private static ClassLoader getClassLoader() {
//        if (defaultClassLoader != null) {
//            return defaultClassLoader;
//        } else {
        return Thread.currentThread().getContextClassLoader();
//        }
    }

    /**
     * 获取泛型名称
     *
     * @param className
     * @return
     */
    public static List<String> getClassGenricTypeString(String className) {
        int startIdnex = className.indexOf("<") + 1;
        int endIndex = className.indexOf(">");
        if (startIdnex == -1 || endIndex == -1) {
            return null;
        }
        String classType = className.substring(startIdnex, endIndex);
        int indexOf = classType.indexOf(",");
        String[] split = null;
        List<String> list = new ArrayList<String>();
        if (indexOf > 0) {
            split = classType.split(",");
            for (int i = 0; i < split.length; i++) {
                list.add(split[i].trim());
            }
        } else {
            list.add(classType);
        }
        return list;
    }

    public static void copyBeanProperties(Class<?> type, Object sourceBean, Object destinationBean) {
        for (Class parent = type; parent != null; parent = parent.getSuperclass()) {
            Field[] fields = parent.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                try {
                    field.setAccessible(true);
                    field.set(destinationBean, field.get(sourceBean));
                } catch (Exception var10) {
                    ;
                }
            }
        }
    }

    public static Class<?> getListType(Type type) {
        Class<?> genericClazz = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            //得到泛型里的class类型对象
            genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
        }
        return genericClazz;
    }

}


