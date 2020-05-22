package db;


import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import tinyurl.entity.TinyUrl;
import tinyurl.repository.AbstractRepository;

/**
 * TinyUrl repository for read and insert operation with DynasmoDB
 * @author Qian Bing
 */
public class PersonRepository extends AbstractRepository<PersonRequest, Long> {
    public PersonRepository(IDynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper);
    }
}