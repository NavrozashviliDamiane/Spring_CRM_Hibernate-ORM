package org.damiane.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthorizedAccessExceptionTest {

    @Test
    public void testUnauthorizedAccessExceptionMessage() {
        String errorMessage = "Unauthorized access detected";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testUnauthorizedAccessExceptionWithoutMessage() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException(null);
        assertEquals(null, exception.getMessage());
    }
}
