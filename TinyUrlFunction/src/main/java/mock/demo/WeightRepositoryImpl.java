package mock.demo;

import java.math.BigDecimal;

public class WeightRepositoryImpl implements IWeightRepository {
    @Override
    public BigDecimal[] getWeight(String departmentId) {
        //到数据库里面去获取该部门的考核绩效权重配置
        return new BigDecimal[0];
    }
}
