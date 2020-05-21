package helloworld.service;

import helloworld.BusinessException;
import helloworld.repository.TinyUrlRepository;

public interface TinyUrlService {

    public String shortenLongUrl(String longUrl) throws BusinessException;

    public String getLongUrl(String shortUrl);

    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository);
}
