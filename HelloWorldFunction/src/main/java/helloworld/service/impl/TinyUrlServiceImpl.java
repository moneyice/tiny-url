package helloworld.service.impl;

import helloworld.App;
import helloworld.BusinessException;
import helloworld.Constant;
import helloworld.entity.TinyUrl;
import helloworld.repository.TinyUrlRepository;
import helloworld.service.TinyUrlService;
import helloworld.util.Base62;
import helloworld.util.SnowflakeSequence;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TinyUrlServiceImpl implements TinyUrlService {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
//    private static final Logger logger = Logger.getLogger(TinyUrlServiceImpl.class);
    private final UrlValidator urlValidator = new UrlValidator();
    private final SnowflakeSequence sequence = new SnowflakeSequence();

    private TinyUrlRepository tinyUrlRepository;

    public String shortenLongUrl(String longUrl) throws BusinessException {
        logger.debug("============= longUrl = " + longUrl);
        if(!urlValidator.isValid(longUrl)){
            throw new BusinessException("original url is invalid");
        }

        long nextId = sequence.nextValue();
        String tinyUrl = Base62.base62Encode(nextId);
        logger.debug("============= tinyUrl = " + tinyUrl);
        saveToDynamoDB(nextId, longUrl);
        return tinyUrl;
    }

    private void saveToDynamoDB(long nextId, String longUrl) {
        TinyUrl tinyUrl = new TinyUrl();
        tinyUrl.setId(nextId);
        tinyUrl.setLongUrl(longUrl);
        tinyUrl.setCreatedTime(LocalDateTime.now());
        tinyUrlRepository.save(tinyUrl);
    }

    public String getLongUrl(String shortUrl) {
//        long id = Base62.base62Decode(shortUrl);
        long id = Base62.base62Decode("NHOWrSHp6e");
        TinyUrl tinyUrl = tinyUrlRepository.findOne(id);

        String longUrl=null;
        if(tinyUrl==null|| StringUtils.isEmpty(tinyUrl.getLongUrl())){
            longUrl = Constant.DEFAULT_REDIRECT_URL;
        }else{
            longUrl = tinyUrl.getLongUrl();
        }
        return longUrl;
    }


    public void setTinyUrlRepository(TinyUrlRepository tinyUrlRepository) {
        this.tinyUrlRepository = tinyUrlRepository;
    }


}
