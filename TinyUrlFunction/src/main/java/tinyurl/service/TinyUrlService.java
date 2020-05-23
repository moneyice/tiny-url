package tinyurl.service;

import tinyurl.BusinessException;
import tinyurl.repository.TinyUrlRepository;

/**
 * Tiny Url service for save and find url
 * @author qian bing
 */
public interface TinyUrlService {

    /**
     * shorten the original long url to short one
     * @param longUrl
     * @return
     * @throws BusinessException
     */
    public String shortenLongUrl(String longUrl) throws BusinessException;

    /**
     * find the specified original url
     * @param shortUrl
     * @return
     * @throws BusinessException
     */
    public String getLongUrl(String shortUrl) throws BusinessException;

    /**
     * DI for TinyUrlRepository
     * @param tinyUrlRepository
     */
    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository);
}
