package org.damiane.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnauthorizedAccessExceptionTest {

    @Test
    public void GivenUnauthorizedAccessExceptionWithMessage_WhenGettingMessage_ThenReturnExpectedMessage() {
        String errorMessage = "Unauthorized access detected";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void GivenUnauthorizedAccessExceptionWithoutMessage_WhenGettingMessage_ThenReturnNull() {
        UnauthorizedAccessException exception = new UnauthorizedAccessException(null);
        assertEquals(null, exception.getMessage());
    }
}
