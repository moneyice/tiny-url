package tinyurl.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import tinyurl.Constant;
import tinyurl.util.LocalDateTimeConverter;

import java.time.LocalDateTime;

/**
 * Bean to be used to interact with DynamoDB
 */
@DynamoDBTable(tableName = Constant.DYNAMODB_TABLE_NAME)
public class TinyUrl {

    private Long id;
    private String longUrl;
    private String tinyUrl;
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;
    private Long userId;

    public TinyUrl() {
    }

    @DynamoDBHashKey
    public Long getId() {
        return id;
    }

    @DynamoDBAttribute
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    @DynamoDBAttribute
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }
}