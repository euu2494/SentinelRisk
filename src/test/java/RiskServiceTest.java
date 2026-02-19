import com.sentinelrisk.sentinelrisk.domain.risk.*;
import com.sentinelrisk.sentinelrisk.infrastructure.kafka.MessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskServiceTest {

    @Mock
    private RiskRepository riskRepository;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private RisksAnalysisService risksAnalysisService;

    @Mock
    private RiskMapper riskMapper;

    @InjectMocks
    private RiskService riskService;

    private Risk sampleRisk;

    @BeforeEach
    void setUp() {
        sampleRisk = new Risk();
        sampleRisk.setTitle("Test Risk");
        sampleRisk.setDescription("High exposure detected");
        sampleRisk.setOwner("Admin");
    }

    @Test
    void whenRiskIsCritical_thenAlertIsSent() {

        when(risksAnalysisService.analyzePriority(anyString(), anyString())).thenReturn(Priority.CRITICAL);
        when(riskRepository.save(any(Risk.class))).thenReturn(sampleRisk);

        riskService.processAndSave(sampleRisk);

        verify(messagePublisher, times(1)).sendMessage(anyString());
        assertEquals(Priority.CRITICAL, sampleRisk.getPriority());
    }

    @Test
    void whenRiskIsNotCritical_thenAlertIsNotSent() {
        when(risksAnalysisService.analyzePriority(anyString(), anyString())).thenReturn(Priority.LOW);
        when(riskRepository.save(any(Risk.class))).thenReturn(sampleRisk);

        riskService.processAndSave(sampleRisk);

        verify(messagePublisher, never()).sendMessage(anyString());
    }
}