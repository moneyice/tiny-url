package tinyurl.service.impl;

import tinyurl.App;
import tinyurl.BusinessException;
import tinyurl.Constant;
import tinyurl.entity.TinyUrl;
import tinyurl.repository.TinyUrlRepository;
import tinyurl.service.TinyUrlService;
import tinyurl.util.Base62;
import tinyurl.util.SnowflakeSequence;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TinyUrlServiceImpl implements TinyUrlService {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private final UrlValidator urlValidator = new UrlValidator();
    private final SnowflakeSequence sequence = new SnowflakeSequence();

    private TinyUrlRepository tinyUrlRepository;

    @Override
    public String shortenLongUrl(String longUrl) throws BusinessException {
        logger.debug("============= longUrl = " + longUrl);
        if(!urlValidator.isValid(longUrl)){
            throw new BusinessException("original url is invalid");
        }

        long nextId = sequence.nextValue();
        String tinyUrl = Base62.base62Encode(nextId);
        logger.debug("============= tinyUrl = " + tinyUrl);
        saveToDynamoDB(nextId, longUrl,tinyUrl);
        return tinyUrl;
    }

    private void saveToDynamoDB(long nextId, String longUrl, String tinyUrl) {
        TinyUrl tinyUrlObj = new TinyUrl();
        tinyUrlObj.setId(nextId);
        tinyUrlObj.setLongUrl(longUrl);
        tinyUrlObj.setTinyUrl(tinyUrl);
        tinyUrlObj.setCreatedTime(LocalDateTime.now());
        tinyUrlRepository.save(tinyUrlObj);
    }

    @Override
    public String getLongUrl(String shortUrl) throws BusinessException {
        long id = Base62.base62Decode(shortUrl);
        TinyUrl tinyUrl = tinyUrlRepository.findOne(id);

        String longUrl=null;
        if(tinyUrl==null|| StringUtils.isEmpty(tinyUrl.getLongUrl())){
            throw new BusinessException("tiny url is invalid");
        }else{
            longUrl = tinyUrl.getLongUrl();
        }
        return longUrl;
    }


    @Override
    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository) {
        this.tinyUrlRepository = tinyUrlRepository;
    }


}
