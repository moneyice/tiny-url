package mock.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tinyurl.Constant;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class MockAccountServiceTest {
    AccountService service;
    private IPerformanceRepository performanceRepository;
    private IWeightRepository weightRepository;


    @Before
    public void setUp() throws Exception {
        service = new AccountService();
        performanceRepository = mock(IPerformanceRepository.class);
        weightRepository = mock(IWeightRepository.class);
        service.setPerformanceRepository(performanceRepository);
        service.setWeightRepository(weightRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void calcPerformanceOK() {
        //准备
        User user = new User();
        user.setUserId("1001");
        user.setDepartmentId("22");


        //假设
        when(performanceRepository.getPerformance(eq("1001"))).thenReturn(new BigDecimal[]{BigDecimal.valueOf(100), BigDecimal.valueOf(80), BigDecimal.valueOf(60)});
        when(weightRepository.getWeight(eq("22"))).thenReturn(new BigDecimal[]{BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.2)});

        //运行
        BigDecimal value = service.calcPerformance(user);

        //检查
        verify(performanceRepository, times(1)).getPerformance(anyString());
        verify(weightRepository, times(1)).getWeight(anyString());
        assertEquals(BigDecimal.valueOf(86.0), value);

    }
    @Test
    public void calcPerformanceError() {
        //准备
        User user = new User();
        user.setUserId("1001");
        user.setDepartmentId("22");


        //假设
        when(performanceRepository.getPerformance(eq("1001"))).thenReturn(new BigDecimal[]{BigDecimal.valueOf(100)});
        when(weightRepository.getWeight(eq("22"))).thenReturn(new BigDecimal[]{BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.2)});

        //运行
        try{
            BigDecimal value = service.calcPerformance(user);
            fail();
        }catch (RuntimeException e){

        }
        //检查
        verify(performanceRepository, times(1)).getPerformance(anyString());
        verify(weightRepository, times(1)).getWeight(anyString());



    }




}