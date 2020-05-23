package tinyurl;

import com.amazonaws.regions.Regions;


/**
 * All the constants for the project
 * @author qian bing
 */
public class Constant {
//    public static final String DOMAIN="https://ula4dsy2ee.execute-api.ap-northeast-2.amazonaws.com/Prod/";
    public static final String DOMAIN="https://api.qianyitian.com/";
    public static final String PATH_VARIABLE_KEY="tinyurl";
    public static final String URL_KEY="url";
    public static final String HEADER_LOCATION = "Location";

    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";

    public static final int HTTP_STATUS_200 = 200;
    public static final int HTTP_STATUS_301 = 301;
    public static final int HTTP_STATUS_500 = 500;

    public static final String DYNAMODB_TABLE_NAME = "TINY_URL";
    public static final Regions REGION = Regions.AP_NORTHEAST_2;

    public static final String DEFAULT_REDIRECT_URL = "http://www.bing.com";


}
