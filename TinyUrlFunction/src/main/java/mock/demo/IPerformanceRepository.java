package mock.demo;

import java.math.BigDecimal;

public interface IPerformanceRepository {
        BigDecimal[] getPerformance(String userId);
}
