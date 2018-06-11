package edu.nju.util;

import org.jetbrains.annotations.*;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionUtil {
    static Map<List<Object>, Method> methods=new HashMap<>();

    public static <T> T getField(final Object instance, final String fieldName) {
        if (instance instanceof Class) {
            return getField((Class) instance, null, fieldName);
        }
        return getField(instance.getClass(), instance, fieldName);
    }

    public static <T> T getField(final Class cls, @Nullable final Object instance, final String fieldName) {
        try {
            final Field field = cls.getField(fieldName);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T invoke(final Object instance, final String methodName, final Object... params) {
        return invoke(instance, methodName, null, params);
    }

    public static <T> T invoke(final Object instance, final String methodName, @Nullable Class<?>[] paramTypes, final Object... params) {
        if (paramTypes == null) {
            paramTypes = (Class<?>[]) new Class[params.length];
            for (int i = 0; i < paramTypes.length; ++i) {
                if (params[i] == null) {
                    throw new IllegalArgumentException("Cannot use 'null' arguments with this method.");
                }
                paramTypes[i] = params[i].getClass();
            }
        }
        try {
            final boolean instanceIsClass = instance instanceof Class;
            final Class<?> cls = (Class<?>) (instanceIsClass ? instance : instance.getClass());
            final Method method = findMethod(cls, methodName, (Class[]) paramTypes);
            if (instanceIsClass) {
                return (T) method.invoke(null, (Object[]) params);
            }
            return (T) method.invoke(instance, (Object[]) params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized Method findMethod(final Class<?> cls, final String methodName, final Class... paramTypes) {
        final List<Object> key = new ArrayList<Object>();
        key.add(cls);
        key.add(methodName);
        for (final Class type : paramTypes) {
            key.add(type);
        }
        Method method = ReflectionUtil.methods.get(key);
        if (method == null) {
            for (final Method m : cls.getMethods()) {
                Label_0186:
                {
                    if (m.getName().equals(methodName)) {
                        final Class<?>[] mParamTypes = m.getParameterTypes();
                        if (mParamTypes.length == paramTypes.length) {
                            for (int i = 0; i < mParamTypes.length; ++i) {
                                if (!mParamTypes[i].isAssignableFrom(paramTypes[i])) {
                                    break Label_0186;
                                }
                            }
                            method = m;
                            break;
                        }
                    }
                }
            }
            if (method == null) {
                throw new NoSuchMethodError(cls.getName() + "." + methodName + "(" + Arrays.asList((Class[]) paramTypes) + ")");
            }
            ReflectionUtil.methods.put(key, method);
        }
        return method;
    }

}
