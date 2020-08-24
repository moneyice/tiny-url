package tinyurl.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tinyurl.BusinessException;
import tinyurl.entity.TinyUrl;
import tinyurl.repository.TinyUrlRepository;
import tinyurl.service.TinyUrlService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class TinyUrlServiceImplTest {
    TinyUrlService tinyUrlService;
    IDynamoDBMapper dynamoDBMapper;

    @Before
    public void setUp() throws Exception {
        tinyUrlService = new TinyUrlServiceImpl();
        dynamoDBMapper = mock(IDynamoDBMapper.class);
        TinyUrlRepository repository = new TinyUrlRepository(dynamoDBMapper);
        tinyUrlService.setTinyUrlRepository(repository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shortenLongUrl() throws BusinessException {
        String longUrl = "http://www.bing.com";
        //执行
        String shortUrl = tinyUrlService.shortenLongUrl(longUrl);
        //验证
        assertEquals("ok", 10, shortUrl.length());
        String pipeRegex ="^[a-zA-Z0-9]+$";
        Pattern pipePattern = Pattern.compile(pipeRegex, Pattern.CASE_INSENSITIVE);

        Matcher pipeMatcher = pipePattern.matcher(shortUrl);

        assertEquals("all charactors", true,pipeMatcher.matches());
        verify(dynamoDBMapper, times(1)).save(any());


    }

    @Test
    public void getLongUrl() throws BusinessException {
        String longUrl = "http://www.baidu.com";

        //  base62(315151157690118144)= NHOWrSHp6e
        TinyUrl returnTinyUrl = new TinyUrl();
        returnTinyUrl.setLongUrl(longUrl);

        //假设
        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(returnTinyUrl);

        //执行
        String shortUrl = "NHOWrSHp6e";
        String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);

        //验证
        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
        assertEquals("getLongUrl", longUrl, resultLongUrl);
    }

    @Test
    public void getLongUrlNotFoundInDB() throws BusinessException {
        //if url not found, return Constant.DEFAULT_REDIRECT_URL

        //  base62(315151157690118144)= NHOWrSHp6e
        //假设
        //not found the tiny url
        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(null);

        //执行
        String shortUrl = "NHOWrSHp6e";
        try {
            String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);
            fail();
        }catch (BusinessException e){
            assertEquals("tiny url is invalid","tiny url is invalid",e.getMessage());
        }
        //验证
        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
    }

    @Test
    public void getLongUrlIsEmpty() {
        //if url is empty, return Constant.DEFAULT_REDIRECT_URL
        //  base62(315151157690118144)= NHOWrSHp6e

        TinyUrl returnTinyUrl = new TinyUrl();
        returnTinyUrl.setLongUrl("");

        //假设
        //not found the tiny url
        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(returnTinyUrl);

        String shortUrl = "NHOWrSHp6e";

        //执行
        try {
            String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);
            fail();
        }catch (BusinessException e){
            assertEquals("tiny url is invalid","tiny url is invalid",e.getMessage());
        }
        //验证
        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
    }
}