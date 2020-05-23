package tinyurl;

/**
 * Wrap the business level exception
 * @author qian bing
 */
public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }
}
