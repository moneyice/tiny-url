package mock.demo;

import java.math.BigDecimal;

public class AccountService {

    IPerformanceRepository performanceRepository;
    IWeightRepository weightRepository;

    public BigDecimal calcPerformance(User user){
        BigDecimal[] performanceList = performanceRepository.getPerformance(user.getUserId());
        BigDecimal[] weightList = weightRepository.getWeight(user.getDepartmentId());

        if(performanceList.length!=weightList.length){
            throw new RuntimeException("invalid length");
        }
        BigDecimal total=BigDecimal.ZERO;
        for (int i = 0; i < performanceList.length; i++) {
            total=total.add(performanceList[i].multiply(weightList[i]));
        }
        return total;
    }


    public IPerformanceRepository getPerformanceRepository() {
        return performanceRepository;
    }

    public void setPerformanceRepository(IPerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public IWeightRepository getWeightRepository() {
        return weightRepository;
    }

    public void setWeightRepository(IWeightRepository weightRepository) {
        this.weightRepository = weightRepository;
    }
}
