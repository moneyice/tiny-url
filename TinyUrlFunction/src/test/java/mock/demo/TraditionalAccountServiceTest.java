package mock.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TraditionalAccountServiceTest {
    AccountService service;

    @Before
    public void setUp() throws Exception {
        service = new AccountService();

        IPerformanceRepository performanceRepository = new IPerformanceRepository() {

            @Override
            public BigDecimal[] getPerformance(String userId) {
                return new BigDecimal[]{BigDecimal.valueOf(100), BigDecimal.valueOf(80), BigDecimal.valueOf(60)};
            }
        };

        IWeightRepository weightRepository = new IWeightRepository() {
            @Override
            public BigDecimal[] getWeight(String departmentId) {
                return new BigDecimal[]{BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.2)};
            }
        };

        service.setPerformanceRepository(performanceRepository);
        service.setWeightRepository(weightRepository);


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void calcPerformance() {
        User user=new User();
        user.setUserId("1001");
        user.setDepartmentId("22");
        BigDecimal value = service.calcPerformance(user);
        assertEquals( BigDecimal.valueOf(86.0), value);

    }
}