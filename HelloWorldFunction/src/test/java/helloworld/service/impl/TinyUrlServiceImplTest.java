package helloworld.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import helloworld.Constant;
import helloworld.entity.TinyUrl;
import helloworld.repository.TinyUrlRepository;
import helloworld.service.TinyUrlService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
    public void shortenLongUrl() {
        String longUrl = "http://www.bing.com";
        String shortUrl = tinyUrlService.shortenLongUrl(longUrl);

        verify(dynamoDBMapper, times(1)).save(any());

    }

    @Test
    public void getLongUrl() {
        String longUrl = "http://www.baidu.com";

        //  base62(315151157690118144)= NHOWrSHp6e
        TinyUrl returnTinyUrl = new TinyUrl();
        returnTinyUrl.setLongUrl(longUrl);

        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(returnTinyUrl);

        String shortUrl = "NHOWrSHp6e";
        String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);

        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
        assertEquals("getLongUrl", longUrl, resultLongUrl);
    }

    @Test
    public void getLongUrlNotFoundInDB() {
        //if url not found, return Constant.DEFAULT_REDIRECT_URL

        //  base62(315151157690118144)= NHOWrSHp6e

        //not found the tiny url
        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(null);

        String shortUrl = "NHOWrSHp6e";
        String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);

        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
        assertEquals("getLongUrl", Constant.DEFAULT_REDIRECT_URL, resultLongUrl);
    }

    @Test
    public void getLongUrlIsEmpty() {
        //if url is empty, return Constant.DEFAULT_REDIRECT_URL
        //  base62(315151157690118144)= NHOWrSHp6e

        TinyUrl returnTinyUrl = new TinyUrl();
        returnTinyUrl.setLongUrl("");

        //not found the tiny url
        when(dynamoDBMapper.load(TinyUrl.class, 315151157690118144L)).thenReturn(null);

        String shortUrl = "NHOWrSHp6e";
        String resultLongUrl = tinyUrlService.getLongUrl(shortUrl);

        verify(dynamoDBMapper, times(1)).load(any(), eq(315151157690118144L));
        assertEquals("getLongUrl", Constant.DEFAULT_REDIRECT_URL, resultLongUrl);
    }
}