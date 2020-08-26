package mock.demo;

import java.math.BigDecimal;

public class PerformanceRepositoryImpl implements IPerformanceRepository {
    @Override
    public BigDecimal[] getPerformance(String userId) {
        //到数据库里面去获取该员工的绩效考评结果
        return new BigDecimal[0];
    }
}
