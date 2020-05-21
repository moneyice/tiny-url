package helloworld;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.service.TinyUrlService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class AppTest {
    private final ObjectMapper jsonMapper = new ObjectMapper();
    App testApp;
    TinyUrlService tinyUrlService;

    @Before
    public void setUp() throws Exception {
        tinyUrlService = mock(TinyUrlService.class);
        testApp = new App() {
            @Override
            protected TinyUrlService getTinyUrlService() {
                return tinyUrlService;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void handleRequest() {
    }

    @Test
    public void shortenUrl() throws IOException {
        //  base62(315151157690118144)= NHOWrSHp6e
        //准备
        APIGatewayV2ProxyRequestEvent reqEvent = new APIGatewayV2ProxyRequestEvent();
        Context context = mock(Context.class);
        reqEvent.setHttpMethod(Constant.HTTP_METHOD_POST);

        reqEvent.setBody("{\"url\":\"http://www.baidu.com\"}");
        //假设
        when(tinyUrlService.shortenLongUrl(eq("http://www.baidu.com"))).thenReturn("NHOWrSHp6e");

        //运行
        APIGatewayV2ProxyResponseEvent responseEvent = testApp.handleRequest(reqEvent, context);

        //检查
        assertEquals("shortURL", Constant.HTTP_STATUS_200, responseEvent.getStatusCode());
        String bodyString = responseEvent.getBody();
        Map map = jsonMapper.readValue(bodyString, Map.class);
        assertEquals("shortURL", "NHOWrSHp6e", map.get(Constant.URL_KEY));
        verify(tinyUrlService, times(1)).shortenLongUrl(eq("http://www.baidu.com"));

    }

    @Test
    public void redirect() {
        //  base62(315151157690118144)= NHOWrSHp6e
        //准备
        APIGatewayV2ProxyRequestEvent reqEvent = new APIGatewayV2ProxyRequestEvent();
        Context context = mock(Context.class);
        reqEvent.setHttpMethod(Constant.HTTP_METHOD_GET);
        Map<String, String> pathParams = new HashMap<String, String>() {{
            put(Constant.PATH_VARIABLE_KEY, "NHOWrSHp6e");
        }};
        reqEvent.setPathParameters(pathParams);

        //假设
        when(tinyUrlService.getLongUrl(eq("NHOWrSHp6e"))).thenReturn("http://www.baidu.com");

        //运行
        APIGatewayV2ProxyResponseEvent responseEvent = testApp.handleRequest(reqEvent, context);

        //检查
        assertEquals("redirection", Constant.HTTP_STATUS_301, responseEvent.getStatusCode());
        String bodyString = responseEvent.getBody();
        assertTrue("redirection", bodyString.contains("http://www.baidu.com"));

        assertEquals("redirection", "http://www.baidu.com", responseEvent.getHeaders().get(Constant.HEADER_LOCATION));

        verify(tinyUrlService, times(1)).getLongUrl(eq("NHOWrSHp6e"));

    }


    @Test
    public void shortenUrlWithRuntimeException() throws IOException {
        //  base62(315151157690118144)= NHOWrSHp6e
        //准备
        APIGatewayV2ProxyRequestEvent reqEvent = new APIGatewayV2ProxyRequestEvent();
        Context context = mock(Context.class);
        reqEvent.setHttpMethod(Constant.HTTP_METHOD_POST);

        reqEvent.setBody("{\"url\":\"http://www.baidu.com\"}");
        //假设
        when(tinyUrlService.shortenLongUrl(eq("http://www.baidu.com"))).thenThrow(new RuntimeException("test runtime exception"));

        //运行
        APIGatewayV2ProxyResponseEvent responseEvent = testApp.handleRequest(reqEvent, context);

        //检查
        assertEquals("shortURL", Constant.HTTP_STATUS_500, responseEvent.getStatusCode());
        String bodyString = responseEvent.getBody();
        assertEquals("shortURL", "test runtime exception", bodyString);
        verify(tinyUrlService, times(1)).shortenLongUrl(eq("http://www.baidu.com"));

    }

    @Test
    public void shortenUrlWithBusinessException() throws IOException {
        //  base62(315151157690118144)= NHOWrSHp6e
        //准备
        APIGatewayV2ProxyRequestEvent reqEvent = new APIGatewayV2ProxyRequestEvent();
        Context context = mock(Context.class);
        reqEvent.setHttpMethod(Constant.HTTP_METHOD_POST);

        reqEvent.setBody("some messed data here");
        //假设
        when(tinyUrlService.shortenLongUrl(eq("http://www.baidu.com"))).thenReturn("NHOWrSHp6e");

        //运行
        APIGatewayV2ProxyResponseEvent responseEvent = testApp.handleRequest(reqEvent, context);

        //检查
        assertEquals("shortURL", Constant.HTTP_STATUS_500, responseEvent.getStatusCode());
        String bodyString = responseEvent.getBody();
        assertEquals("shortURL", "processing original url error", bodyString);
        verify(tinyUrlService, times(0)).shortenLongUrl(eq("http://www.baidu.com"));

    }

    @Test
    public void getTinyUrlService() {
    }
}