package com.ecnu.meethere.utils;

import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class ReflectionTestUtils {
    public static <T> boolean isAllFieldsNotNull(T o) {
        if (o == null) return true;

        if (o.getClass().getName().startsWith("java"))
            return true;

        return Reflect.on(o).fields().values().stream()
                .allMatch(f -> f.get() != null);
    }

    public static <T> boolean isCollectionElementsAllFieldsNotNull(Collection<T> list) {
        if (list == null || list.size() == 0)
            return true;
        return list.stream().map(Reflect::on).allMatch(
                o -> {
                    if (o.getClass().getName().startsWith("java"))
                        return true;
                    return o.fields().values().stream().allMatch(f -> f.get() != null);
                }
        );
    }
}
