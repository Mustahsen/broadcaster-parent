package com.mustahsen.broadcaster.resolver;

import com.mustahsen.broadcaster.annotation.BroadcastPair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConstantResolver extends BaseResolver {

    @Override
    public Object getResolvable(List<String> propertyAccessors,
                                BroadcastPair broadcastPair,
                                Map<String, Object> argumentMap,
                                Object returnValue,
                                Object object) {

        return broadcastPair.value();
    }

    @Override
    public void validateResolverArguments(BroadcastPair broadcastPair, Map<String, Object> argumentMap, Object returnValue, Object object)
            throws RuntimeException {
        validateBroadcastAnnotation(broadcastPair);
        validateObject(broadcastPair.value(), "broadcastPair.value");
    }
}
