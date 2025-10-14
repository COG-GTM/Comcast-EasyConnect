package com.easyconnect.easyconnectapp.app.view.utils;

import java.lang.reflect.Field;

public class SingletonTestHelper {

    public static void injectMockSingleton(Class<?> singletonClass, String instanceFieldName, Object mockInstance) throws Exception {
        Field instanceField = singletonClass.getDeclaredField(instanceFieldName);
        instanceField.setAccessible(true);
        instanceField.set(null, mockInstance);
    }

    public static void resetSingleton(Class<?> singletonClass, String instanceFieldName) throws Exception {
        Field instanceField = singletonClass.getDeclaredField(instanceFieldName);
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }
}
