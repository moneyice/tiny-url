package helloworld.service;

import helloworld.BusinessException;
import helloworld.Constant;
import helloworld.entity.TinyUrl;
import helloworld.repository.TinyUrlRepository;
import helloworld.util.Base62;
import helloworld.util.SnowflakeSequence;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;

public interface TinyUrlService {

    public String shortenLongUrl(String longUrl) throws BusinessException;

    public String getLongUrl(String shortUrl);

    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository);
}
