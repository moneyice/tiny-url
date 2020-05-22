package tinyurl.service;

import tinyurl.BusinessException;
import tinyurl.repository.TinyUrlRepository;

public interface TinyUrlService {

    public String shortenLongUrl(String longUrl) throws BusinessException;

    public String getLongUrl(String shortUrl) throws BusinessException;

    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository);
}
