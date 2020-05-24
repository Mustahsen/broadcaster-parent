package com.mustahsen.broadcaster.resolver;

import com.mustahsen.broadcaster.annotation.BroadcastPair;
import com.mustahsen.broadcaster.exception.InvalidArgumentMapException;
import com.mustahsen.broadcaster.exception.InvalidBroadcastAnnotationException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ArgumentResolver extends BaseResolver {

    @Override
    public Object getResolvable(List<String> propertyAccessors,
                                BroadcastPair broadcastPair,
                                Map<String, Object> argumentMap,
                                Object returnValue,
                                Object object) {

        if (CollectionUtils.isEmpty(propertyAccessors)) {
            return null;
        } else if (propertyAccessors.size() == 1) {
            Object resolvable = argumentMap.getOrDefault(propertyAccessors.get(0), null);
            propertyAccessors.clear();
            return resolvable;
        } else if (propertyAccessors.size() > 1) {
            Object resolvable = argumentMap.getOrDefault(propertyAccessors.get(0), null);
            propertyAccessors.remove(0);
            return resolvable;
        } else {
            return returnValue;
        }
    }

    @Override
    public void validateResolverArguments(BroadcastPair broadcastPair, Map<String, Object> argumentMap, Object returnValue, Object object)
            throws RuntimeException {
        validateBroadcastAnnotation(broadcastPair);
        validateArgumentMap(argumentMap);
    }
}