package com.cakeshop.api_main.service.id;

import com.cakeshop.api_main.model.ReuseId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class IdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try{
            ReuseId reuseId = (ReuseId) o;
            if(reuseId.getReusedId()!=null){
                return reuseId.getReusedId();
            }
        }catch (Exception e){
            //e.printStackTrace();
        }


//        String createQuery = "CREATE SEQUENCE IF NOT EXISTS MY_CUSTOM_SEQENCE_NAME"
//        sharedSessionContractImplementor.createSQLQuery(createQuery).executeUpdate();
//        String nextValQuery = "SELECT NEXTVAL('MY_CUSTOM_SEQENCE_NAME')";
//        BigInteger newId = (BigInteger) sharedSessionContractImplementor.createSQLQuery(nextValQuery).getSingleResult();

        return SnowFlakeIdService.getInstance().nextId();
    }
}

