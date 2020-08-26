package mock.demo;

import java.math.BigDecimal;

public interface IWeightRepository {
    BigDecimal[] getWeight(String departmentId);
}
