package tinyurl.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Abstract repository class is the parent for all the business object repository to extend
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractRepository<T, ID extends Serializable> {

    protected IDynamoDBMapper mapper;
    protected Class<T> entityClass;

    protected AbstractRepository(IDynamoDBMapper dynamoDBMapper) {
        setMapper(dynamoDBMapper);
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        // This entityClass refers to the actual entity class in the subclass declaration.
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public void save(T t) {
        mapper.save(t);
    }

    public T findOne(ID id) {
        return mapper.load(entityClass, id);
    }

    public void setMapper(IDynamoDBMapper dynamoDBMapper) {
        this.mapper = dynamoDBMapper;
    }

}