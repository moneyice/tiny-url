package helloworld;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helloworld.repository.TinyUrlRepository;
import helloworld.service.TinyUrlService;
import helloworld.service.impl.TinyUrlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Handler for requests to Lambda function.
 * @author qian bing
 */
public class App implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
//    private static final Logger logger = Logger.getLogger(App.class);
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public App() {
    }

    /**
     * Lambda entry point
     *
     * @param event
     * @param context
     * @return
     */
    public APIGatewayV2ProxyResponseEvent handleRequest(final APIGatewayV2ProxyRequestEvent event, final Context context) {
//        LambdaLogger logger = context.getLogger();
        APIGatewayV2ProxyResponseEvent responseEvent = new APIGatewayV2ProxyResponseEvent();
        try {
            if (Constant.HTTP_METHOD_GET.equals(event.getHttpMethod())) {
                redirect(event, responseEvent);
            } else if (Constant.HTTP_METHOD_POST.equals(event.getHttpMethod())) {
                shortenUrl(event, responseEvent);
            }
        } catch (BusinessException e) {
            logger.error("business error", e);
            responseEvent.setBody(e.getMessage());
            responseEvent.setStatusCode(Constant.HTTP_STATUS_500);
        } catch (Exception e) {
            logger.error("internal error", e);
            responseEvent.setBody(e.getMessage());
            responseEvent.setStatusCode(Constant.HTTP_STATUS_500);
        }
        return responseEvent;
    }

    private Map<String, String> createResponseHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * Shorten the original long url,
     *
     * @param event
     * @param responseEvent
     * @throws BusinessException
     */
    protected void shortenUrl(APIGatewayV2ProxyRequestEvent event, APIGatewayV2ProxyResponseEvent responseEvent) throws BusinessException {
        //create response headers
        Map<String, String> reponseHeaders = createResponseHeader();

        Map<String, String> responseBodyMap = new HashMap<>();
        reponseHeaders.putAll(System.getenv());


        logger.debug("============= event body = " + event.getBody());
        Map<String, String> requestBodyMap = null;
        try {
            requestBodyMap = jsonMapper.readValue(event.getBody(), Map.class);
        } catch (IOException e) {
            logger.error("processing original url error", e);
            throw new BusinessException("processing original url error");
        }
        //retrieve the long url
        String longUrl = requestBodyMap.get(Constant.URL_KEY);

        TinyUrlService tinyUrlService = getTinyUrlService();
        //shorten and store for the long url
        String shortUrl = tinyUrlService.shortenLongUrl(longUrl);
        //save the short url to response body
        responseBodyMap.put(Constant.URL_KEY, shortUrl);
        String bodyString = null;
        try {
            bodyString = jsonMapper.writeValueAsString(responseBodyMap);
        } catch (JsonProcessingException e) {
            logger.error("generating tiny url error", e);
            throw new BusinessException("generating tiny url error");
        }

        responseEvent.setBody(bodyString);
        responseEvent.setHeaders(reponseHeaders);
        responseEvent.setStatusCode(Constant.HTTP_STATUS_200);
    }


    protected void redirect(APIGatewayV2ProxyRequestEvent event, APIGatewayV2ProxyResponseEvent responseEvent) throws BusinessException {
        //create response headers
        Map<String, String> headers = createResponseHeader();
        Map<String, String> body = new HashMap<>();
        String tinyUrl = event.getPathParameters().get(Constant.PATH_VARIABLE_KEY);

        TinyUrlService tinyUrlService = getTinyUrlService();
        //get the original long url
        String longUrl = tinyUrlService.getLongUrl(tinyUrl);
        body.put(Constant.URL_KEY, generateHtml(longUrl));
        String bodyString = null;
        try {
            bodyString = jsonMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            logger.error("getting original url error", e);
            throw new BusinessException("getting original url error");
        }
        //use http status 301 to redirect to long url
        responseEvent.setBody(bodyString);
        headers.put(Constant.HEADER_LOCATION, longUrl);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(Constant.HTTP_STATUS_301);
    }

    /**
     * Generates the html body of the response.
     *
     * @param dest the destination url
     * @return generated html
     */
    private String generateHtml(String dest) {
        return "<html><a href=\"" + dest + "\">Click here to be redirected.</a></html>";
    }

    protected TinyUrlService getTinyUrlService() {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(Constant.REGION).build();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        TinyUrlRepository tinyUrlRepository = new TinyUrlRepository(mapper);
        TinyUrlService tinyUrlService = new TinyUrlServiceImpl();
        tinyUrlService.setTinyUrlRepository(tinyUrlRepository);
        return tinyUrlService;
    }

}
