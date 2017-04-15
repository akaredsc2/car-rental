package org.vitaly.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.vitaly.model.Entity;

/**
 * Created by vitaly on 2017-04-15.
 */
public class EntityIdMatcher extends BaseMatcher<Entity> {
    private final long expectedId;

    private EntityIdMatcher(long expectedId) {
        this.expectedId = expectedId;
    }

    @Override
    public boolean matches(Object entity) {
        final Entity actual = (Entity) entity;
        return expectedId == actual.getId();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Entity with id ").appendValue(expectedId);
    }

    public static EntityIdMatcher hasId(long expectedId) {
        return new EntityIdMatcher(expectedId);
    }
}
