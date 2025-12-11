package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.List;

import com.smartbill.fibonacci.ClientState;
import com.smartbill.fibonacci.service.SequenceGeneratorServiceImpl;
import com.smartbill.fibonacci.storage.ClientStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SequenceGeneratorServiceImplTest {

    @Mock
    private ClientStorage storage;

    @InjectMocks
    private SequenceGeneratorServiceImpl fibonacciService;

    @Test
    void next_ShouldComputeFibonacciCorrectly() {
        String clientId = "client1";
        ClientState realState = new ClientState();

        when(storage.getOrCreate(clientId)).thenReturn(realState);

        BigInteger first = fibonacciService.next(clientId);  // 1
        BigInteger second = fibonacciService.next(clientId); // 1
        BigInteger third = fibonacciService.next(clientId);  // 2
        BigInteger fourth = fibonacciService.next(clientId); // 3

        assertEquals(4, realState.getPosition());

        // verificăm că storage.save a fost apelat pentru fiecare next
        verify(storage, times(4)).save(eq(clientId), any(ClientState.class));
    }

    @Test
    void prev_ShouldThrow_WhenPositionTooLow() {
        String clientId = "client1";
        ClientState realState = new ClientState();
        realState.setPosition(1);

        when(storage.get(clientId)).thenReturn(realState);

        assertThrows(IllegalStateException.class, () -> fibonacciService.prev(clientId));
    }
}
