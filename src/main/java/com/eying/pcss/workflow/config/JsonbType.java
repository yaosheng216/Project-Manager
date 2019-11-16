package com.eying.pcss.workflow.config;

import com.eying.pcss.core.exception.SystemException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json转换类型，用来实现json类型与Postgre类型的转换
 */
public class JsonbType implements UserType, ParameterizedType {

    private final ObjectMapper mapper = new ObjectMapper ().registerModule (new JavaTimeModule ())
            // 处理java8 Date/Time类型问题
            .disable (SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            // 反序列化的时候如果多了其他属性,不抛出异常
            .disable (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static final ClassLoaderService CLASS_LOADER_SERVICE = new ClassLoaderServiceImpl ();

    public static final String CLASS = "CLASS";

    private Class<?> jsonClassType;

    private String listEntity(String value) {
        String regex = "\\[\\{(.*?)}]";
        Pattern p = Pattern.compile (regex);
        Matcher m = p.matcher (value);
        StringBuilder sb = new StringBuilder ();
        String[] a = value.split ("\\[\\{");
        List<String> stringList = new ArrayList<> ();
        while (m.find ()) {
            String str1 = m.group (1).replace ("},", "####-1-2-####,");
            stringList.add (str1);
        }
        int i = 0;
        for (String a1 : a) {
            if (a1.contains ("}]")) {
                sb.append ("[{");
                sb.append (stringList.get (i));
                sb.append ("}]");
                sb.append (a1.split ("}]")[1]);
                i++;
            } else {
                sb.append (a1);
            }
        }
        return sb.toString ();
    }

    /**
     * Retrieve an instance of the mapped class from a JDBC resultset
     *
     * @param resultSet a JDBC result set
     * @param strings   the column names
     * @param session
     * @param object    the containing entity  @return Object
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings,
                              SharedSessionContractImplementor session, Object object) throws HibernateException, SQLException {
//        PGobject oo = (PGobject) resultSet.getObject(strings[0]);
        //此处直接转换成字符串即可
        String value = String.valueOf (resultSet.getObject (strings[0]));
        if (value == null) {
            return null;
        }
//        String value = oo.getValue();
        if (value != null) {
            try {
                // 判断是否是数组json串
                if (value.startsWith ("[") && value.endsWith ("]")) {
                    List<Object> objectList = new ArrayList<> ();
                    // 去除[和]
                    String subValue = value.substring (1, value.length () - 1);
                    // 判断是否是空数组
                    if (StringUtils.isNotBlank (subValue)) {
                        // 如果以{开始，以}结尾，则表示是对象数组
                        if (subValue.startsWith ("{") && subValue.endsWith ("}")) {
                            String objectStr = listEntity (subValue.substring (0, subValue.length () - 1));
                            // 分隔对象
                            String[] values = objectStr.split ("},");
                            for (String v : values) {
                                v = v.replace ("####-1-2-####", "}");
                                objectList.add (mapper.readValue (v + "}", jsonClassType));
                            }
                        } else {
                            // 字符串或者数字数组
                            String[] values = subValue.split (",");
                            for (String v : values) {
                                objectList.add (mapper.readValue (v, jsonClassType));
                            }
                        }
                    }
                    return objectList;
                } else {
                    return mapper.readValue (value, jsonClassType);
                }
            } catch (IOException e) {
                throw new SystemException (e);
            }
        }
        return null;
    }

    /**
     * Write an instance of the mapped class to a prepared statement.
     *
     * @param preparedStatement a JDBC prepared statement
     * @param object            the object to write
     * @param index             statement parameter index
     * @param session
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object object, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (object == null) {
            preparedStatement.setNull (index, Types.OTHER);
        } else {
            try {
                preparedStatement.setObject (index, mapper.writeValueAsString (object), Types.OTHER);
            } catch (IOException e) {
                throw new SystemException (e);
            }
        }
    }

    /**
     * Return a deep copy of the persistent state, stopping at entities and at
     * collections. It is not necessary to copy immutable objects, or null
     * values, in which case it is safe to simply return the argument.
     *
     * @param originalValue the object to be cloned, which may be null
     * @return Object a copy
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object deepCopy(Object originalValue) throws HibernateException {
        if (originalValue != null) {
            try {
                // 如果Object是List，则需要转换成json数组
                if (originalValue instanceof List) {
                    String content = mapper.writeValueAsString (originalValue);
                    return mapper.readValue (content, new TypeReference<List<Object>> () {
                    });
                } else {
                    return mapper.readValue (mapper.writeValueAsString (originalValue), returnedClass ());
                }
            } catch (IOException e) {
                throw new SystemException (e);
            }
        }
        return null;
    }


    /**
     * Transform the object into its cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable.
     *
     * @param value the object to be cached
     * @return a cachable representation of the object
     * @throws HibernateException
     */
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object copy = deepCopy (value);

        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }

        throw new SerializationException (
                String.format ("Cannot serialize '%s', %s is not Serializable.", value, value.getClass ()), null);
    }

    /**
     * Reconstruct an object from the cacheable representation. At the very least this
     * method should perform a deep copy if the type is mutable.
     *
     * @param cached the object to be cached
     * @param owner  the owner of the cached object
     * @return a reconstructed object from the cachable representation
     * @throws HibernateException
     */
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy (cached);
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to
     * with a new (original) value from the detached entity we are merging. For immutable
     * objects, or null values, it is safe to simply return the first parameter. For
     * mutable objects, it is safe to return a copy of the first parameter. For objects
     * with component values, it might make sense to recursively replace component values.
     *
     * @param original the value from the detached entity being merged
     * @param target   the value in the managed entity
     * @return the value to be merged
     */
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy (original);
    }

    /**
     * Are objects of this type mutable?
     *
     * @return boolean
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }

        return x.hashCode ();
    }


    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals (x, y);
    }

    /**
     * The class returned by <tt>nullSafeGet()</tt>.
     *
     * @return Class
     */
    @Override
    public Class<?> returnedClass() {
        return jsonClassType;
    }

    /**
     * Return the SQL type codes for the columns mapped by this type. The
     * codes are defined on <tt>java.sql.Types</tt>.
     *
     * @return int[] the typecodes
     * @see java.sql.Types
     */
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public void setParameterValues(Properties parameters) {
        final String clazz = (String) parameters.get (CLASS);
        jsonClassType = CLASS_LOADER_SERVICE.classForName (clazz);
    }
}