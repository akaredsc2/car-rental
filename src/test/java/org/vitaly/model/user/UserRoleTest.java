package org.vitaly.model.user;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by vitaly on 2017-05-04.
 */
public class UserRoleTest {
    @Test
    public void roleOfStringThatIsPartOfEnumReturnsOptionalWithCorrespondingRole() throws Exception {
        UserRole role = UserRole.CLIENT;
        String roleString = role.toString();

        UserRole actualRole = UserRole.of(roleString).orElseThrow(AssertionFailedError::new);

        assertEquals(role, actualRole);
    }

    @Test
    public void stateOfStringThatNotIsPartOfEnumReturnsEmptyOptional() throws Exception {
        String roleString = "random";

        boolean isPresent = UserRole.of(roleString).isPresent();

        assertFalse(isPresent);
    }
}