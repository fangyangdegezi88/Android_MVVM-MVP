package com.focustech.android.components.mt.sdk.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.logback.core.joran.util.Introspector;
import ch.qos.logback.core.joran.util.PropertyDescriptor;

/**
 * 反射工具
 *
 * @author zhangxu
 */
public class ReflectionUtil {
    private static final Map<String, PropertyDescriptor[]> CACHE = new ConcurrentHashMap<>();

    private static final Class<?>[] PRIMITIVE = new Class[]{
            boolean.class,
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class,
    };

    private static final Class<?>[] WRAPPER = new Class[]{
            Boolean.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
    };

    /**
     * Copy the property values of the given source bean into the target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     *
     * @param source the source bean
     * @param target the target bean
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null, null);
    }

    /**
     * Copy the property values of the given source bean into the given target bean,
     * only setting properties defined in the given "editable" class (or interface).
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     *
     * @param source   the source bean
     * @param target   the target bean
     * @param editable the class (or interface) to restrict property setting to
     */
    public static void copyProperties(Object source, Object target, Class<?> editable) {
        copyProperties(source, target, editable, null);
    }

    /**
     * Copy the property values of the given source bean into the given target bean,
     * ignoring the given "ignoreProperties".
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     *
     * @param source           the source bean
     * @param target           the target bean
     * @param ignoreProperties array of property names to ignore
     */
    public static void copyProperties(Object source, Object target, String[] ignoreProperties) {
        copyProperties(source, target, null, ignoreProperties);
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     *
     * @param source           the source bean
     * @param target           the target bean
     * @param editable         the class (or interface) to restrict property setting to
     * @param ignoreProperties array of property names to ignore
     */
    private static void copyProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties) {

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null &&
                    (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }

                        Object value = readMethod.invoke(source);

                        if (null == value || value.equals("")) { // 忽略空字符串
                            continue;
                        } else if (value.getClass().isArray() && Array.getLength(value) == 0) {
                            continue;
                        } else if (value.getClass().isAssignableFrom(Collection.class) && ((Collection) value).size() == 0) {
                            continue;
                        } else if (value.getClass().isAssignableFrom(Map.class) && ((Map) value).size() == 0) {
                            continue;
                        }

                        Class sourceClazz = value.getClass();
                        Class targetClazz = targetPd.getWriteMethod().getParameterTypes()[0];

                        if (sourceClazz != targetClazz) {
                            int indexOfSourcePrimitive = indexOfPrimitive(sourceClazz);
                            int indexOfTargetPrimitive = indexOfPrimitive(targetClazz);

                            if (-1 != indexOfSourcePrimitive && -1 != indexOfTargetPrimitive) {
                                value = convertWrapper(value.toString(), targetClazz);
                            }
                        }

                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                    }
                }
            }
        }
    }

    private static Object convertWrapper(String value, Class clazz) {
        if (clazz == Boolean.class || clazz == boolean.class) {
            return Boolean.valueOf(value);
        } else if (clazz == Byte.class || clazz == byte.class) {
            return Byte.valueOf(value);
        } else if (clazz == Short.class || clazz == short.class) {
            return Short.valueOf(value);
        } else if (clazz == Integer.class || clazz == int.class) {
            return Integer.valueOf(value);
        } else if (clazz == Long.class || clazz == long.class) {
            return Long.valueOf(value);
        } else if (clazz == Float.class || clazz == float.class) {
            return Float.valueOf(value);
        } else if (clazz == Double.class || clazz == double.class) {
            return Double.valueOf(value);
        }


        return value;
    }

    private static int indexOfPrimitive(Class clazz) {
        int index = indexOf(clazz, PRIMITIVE);
        return -1 == index ? indexOf(clazz, WRAPPER) : index;
    }

    private static int indexOf(Class clazz, Class[] classes) {
        int index = -1;

        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == clazz) {
                index = i;
                break;
            }
        }

        return index;
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        String key = clazz.getCanonicalName();

        if (!CACHE.containsKey(key)) {
            CACHE.put(key, getPropertyDescriptors0(clazz));
        }

        return CACHE.get(key);
    }

    private static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) {
        PropertyDescriptor[] targetPds = getPropertyDescriptors(clazz);

        for (PropertyDescriptor pd : targetPds) {
            if (pd.getName().equals(name)) {
                return pd;
            }
        }

        return null;
    }

    /**
     * Gets a class's property descriptors. All properties have methods whose name
     * begins with "set" or "get". The setters must have a single parameter and
     * getters must have none.
     *
     * @param clazz class to be evaluated
     * @return property descriptors
     */
    public static PropertyDescriptor[] getPropertyDescriptors0(Class<?> clazz) {
        final String SETTER_PREFIX = "set";
        final String GETTER_PATTERN = "^((get)|(is)).+";
        final int LEN_PREFIX = SETTER_PREFIX.length();

        Map<String, PropertyDescriptor> map = new HashMap<>();
        for (Method m : clazz.getMethods()) {
            PropertyDescriptor pd = null;
            String mName = m.getName();

            boolean isGet = mName.matches(GETTER_PATTERN);
            boolean isSet = (mName.startsWith(SETTER_PREFIX) && mName.length() > LEN_PREFIX);

            if (isGet || isSet) {
                String propName = getDecapitalize(mName);

                pd = map.get(propName);
                if (pd == null) {
                    pd = new PropertyDescriptor(propName);
                    map.put(propName, pd);
                }

                Class<?>[] paramTypes = m.getParameterTypes();
                if (isSet) {
                    if (paramTypes.length == 1) { // we only want the single-parm setter
                        pd.setWriteMethod(m);
                        pd.setPropertyType(paramTypes[0]);
                    }
                } else if (isGet) {
                    if (paramTypes.length == 0) { // we only want the zero-parm getter
                        pd.setReadMethod(m);

                        // let setter's type take priority
                        if (pd.getPropertyType() == null) {
                            pd.setPropertyType(m.getReturnType());
                        }
                    }
                }
            }
        }

        return map.values().toArray(new PropertyDescriptor[0]);
    }

    private static String getDecapitalize(String mName) {
        return Introspector.decapitalize(mName.replaceAll("^((get)|(is)|(set))", ""));
    }
}
