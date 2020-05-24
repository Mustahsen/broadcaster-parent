package com.mustahsen.broadcaster.resolver;

import com.mustahsen.broadcaster.annotation.BroadcastPair;
import com.mustahsen.broadcaster.exception.InvalidArgumentMapException;
import com.mustahsen.broadcaster.exception.InvalidBroadcastAnnotationException;
import com.mustahsen.broadcaster.exception.NullObjectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseResolver implements IResolver {

    public static final String DOT_REGEX = "\\.";

    @Override
    public List<String> propertyAccessors(String sourceValue) {
        return Arrays.stream(sourceValue.split(DOT_REGEX)).collect(Collectors.toList());
    }

    @Override
    public abstract Object getResolvable(List<String> propertyAccessors,
                                         BroadcastPair broadcastPair,
                                         Map<String, Object> argumentMap,
                                         Object returnValue,
                                         Object object);

    @Override
    public BroadcastPair resolveBroadcastAnnotation(Object... arguments) {
        return (BroadcastPair) arguments[0];
    }

    @Override
    public Map<String, Object> resolveArguments(Object... arguments) {
        Object object = arguments[1];
        if (object instanceof Map) {
            return Map.class.cast(object);
        }
        return Collections.emptyMap();
    }

    @Override
    public Object resolveReturnValue(Object... arguments) {
        return arguments[2];
    }

    @Override
    public Object resolveObject(Object... arguments) {
        if (arguments.length > 3) {
            return arguments[3];
        }
        return null;
    }

    @Override
    public abstract void validateResolverArguments(BroadcastPair broadcastPair,
                                                   Map<String, Object> argumentMap,
                                                   Object returnValue,
                                                   Object object) throws RuntimeException;

    @Override
    public Object resolveProperty(Object object, List<String> propertyAccessors) {
        if (CollectionUtils.isEmpty(propertyAccessors) || Objects.isNull(object)) {
            return object;
        } else if (propertyAccessors.size() == 1) {
            return resolveField(object, propertyAccessors.get(0));
        } else {
            return resolveField(object, propertyAccessors);
        }
    }

    @Override
    public Object resolveField(Object object, List<String> propertyAccessors) {
        Field field = ReflectionUtils.findField(object.getClass(), propertyAccessors.get(0));
        if (Objects.nonNull(field)) {
            field.setAccessible(true);
            try {
                return resolveProperty(field.get(object), propertyAccessors.subList(1, propertyAccessors.size()));
            } catch (IllegalAccessException e) {
                log.info("An error occurred during accessing the field: " + field, e);
            }
        }
        return object;
    }

    @Override
    public Object resolveField(Object object, String propertyAccessor) {
        Field field = ReflectionUtils.findField(object.getClass(), propertyAccessor);
        if (Objects.nonNull(field)) {
            field.setAccessible(true);
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                log.info("An error occurred during accessing the field: " + field, e);
            }
        }
        return object;
    }

}
