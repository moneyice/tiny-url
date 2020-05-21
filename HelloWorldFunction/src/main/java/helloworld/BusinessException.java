package helloworld;

/**
 * Wrap the business level exception
 */
public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }
}
