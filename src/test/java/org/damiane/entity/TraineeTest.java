package org.damiane.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TraineeTest {

    @InjectMocks
    private Trainee trainee;

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        Date dateOfBirth = new Date();
        String address = "123 Main St";

        trainee.setId(id);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);

        assertEquals(id, trainee.getId());
        assertEquals(dateOfBirth, trainee.getDateOfBirth());
        assertEquals(address, trainee.getAddress());
    }
}
