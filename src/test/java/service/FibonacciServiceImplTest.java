package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.smartbill.fibonacci.ClientState;
import com.smartbill.fibonacci.service.FibonacciServiceImpl;
import com.smartbill.fibonacci.storage.ClientStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FibonacciServiceImplTest {

    @Mock
    private ClientStorage storage;

    @InjectMocks
    private FibonacciServiceImpl fibonacciService;

    @Test
    void next_ShouldComputeFibonacciCorrectly() {
        String clientId = "client1";
        ClientState realState = new ClientState();

        when(storage.getOrCreate(clientId)).thenReturn(realState);

        Long first = fibonacciService.next(clientId);  // 1
        Long second = fibonacciService.next(clientId); // 1
        Long third = fibonacciService.next(clientId);  // 2
        Long fourth = fibonacciService.next(clientId); // 3

        assertEquals(List.of(1L, 1L, 2L, 3L), realState.getValues());
        assertEquals(4, realState.getPosition());

        // verificăm că storage.save a fost apelat pentru fiecare next
        verify(storage, times(4)).save(eq(clientId), any(ClientState.class));
    }

    @Test
    void prev_ShouldGoBackCorrectly() {
        String clientId = "client1";
        ClientState realState = new ClientState();
        realState.addValue(1L);
        realState.addValue(1L);
        realState.addValue(2L);
        realState.setPosition(3);

        when(storage.get(clientId)).thenReturn(realState);

        fibonacciService.prev(clientId);

        assertEquals(List.of(1L, 1L), realState.getValues());
        assertEquals(2, realState.getPosition());
        verify(storage).save(eq(clientId), eq(realState));
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
