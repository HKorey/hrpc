package com.hquery.hrpc.core.codec.anthession;

import com.caucho.burlap.io.BurlapRemoteObject;
import com.caucho.hessian.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hquery.huang
 * 2019/3/6 13:45:35
 */
public class SingleClassLoaderSofaSerializerFactory extends SerializerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleClassLoaderSofaSerializerFactory.class);

    private static Deserializer OBJECT_DESERIALIZER = new BasicDeserializer(
            BasicDeserializer.OBJECT);

    private static Map staticSerializerMap;
    private static Map staticDeserializerMap;
    private static Map staticTypeMap;

    protected Serializer defaultSerializer;

    // Additional factories
    protected ArrayList factories = new ArrayList();

    protected CollectionSerializer collectionSerializer;

    private Deserializer hashMapDeserializer;
    private final ConcurrentMap cachedSerializerMap = new ConcurrentHashMap();
    private final ConcurrentMap cachedDeserializerMap = new ConcurrentHashMap();
    private final ConcurrentMap cachedTypeDeserializerMap = new ConcurrentHashMap();

    private boolean isAllowNonSerializable;

    /**
     * Set true if the collection serializer should send the java type.
     */
    @Override
    public void setSendCollectionType(boolean isSendType) {
        if (collectionSerializer == null)
            collectionSerializer = new CollectionSerializer();

        collectionSerializer.setSendJavaType(isSendType);
    }

    /**
     * Adds a factory.
     */
    @Override
    public void addFactory(AbstractSerializerFactory factory) {
        factories.add(factory);
    }

    /**
     * If true, non-serializable objects are allowed.
     */
    @Override
    public void setAllowNonSerializable(boolean allow) {
        isAllowNonSerializable = allow;
    }

    /**
     * If true, non-serializable objects are allowed.
     */
    @Override
    public boolean isAllowNonSerializable() {
        return isAllowNonSerializable;
    }

    /**
     * Returns the serializer for a class.
     *
     * @param cl the class of the object that needs to be serialized.
     * @return a serializer object for the serialization.
     */
    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        Serializer serializer;

        serializer = (Serializer) staticSerializerMap.get(cl);
        if (serializer != null)
            return serializer;

        serializer = (Serializer) cachedSerializerMap.get(cl);

        if (serializer != null)
            return serializer;

        for (int i = 0; serializer == null && factories != null && i < factories.size(); i++) {
            AbstractSerializerFactory factory;

            factory = (AbstractSerializerFactory) factories.get(i);

            serializer = factory.getSerializer(cl);
        }

        if (serializer != null) {
            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debugWithApp(null, "serializer success ");
            }
        } else if (HessianRemoteObject.class.isAssignableFrom(cl))
            serializer = new RemoteSerializer();

        else if (BurlapRemoteObject.class.isAssignableFrom(cl))
            serializer = new RemoteSerializer();

        else if (Map.class.isAssignableFrom(cl))
            serializer = new MapSerializer();

        else if (Collection.class.isAssignableFrom(cl)) {
            if (collectionSerializer == null) {
                collectionSerializer = new CollectionSerializer();
            }

            serializer = collectionSerializer;
        } else if (cl.isArray())
            serializer = new ArraySerializer();

        else if (Throwable.class.isAssignableFrom(cl))
            serializer = new ThrowableSerializer(cl, this.getClassLoader());

        else if (InputStream.class.isAssignableFrom(cl))
            serializer = new InputStreamSerializer();

        else if (Iterator.class.isAssignableFrom(cl))
            serializer = IteratorSerializer.create();

        else if (Enumeration.class.isAssignableFrom(cl))
            serializer = EnumerationSerializer.create();

        else if (Calendar.class.isAssignableFrom(cl))
            serializer = CalendarSerializer.SER;

        else if (Locale.class.isAssignableFrom(cl))
            serializer = LocaleSerializer.create();

        else if (Enum.class.isAssignableFrom(cl))
            serializer = new EnumSerializer(cl);

        if (serializer == null)
            serializer = getDefaultSerializer(cl);

        cachedSerializerMap.put(cl, serializer);

        return serializer;
    }

    /**
     * Returns the default serializer for a class that isn't matched directly.
     * Application can override this method to produce bean-style serialization
     * instead of field serialization.
     *
     * @param cl the class of the object that needs to be serialized.
     * @return a serializer object for the serialization.
     */
    @Override
    protected Serializer getDefaultSerializer(Class cl) {
        if (defaultSerializer != null)
            return defaultSerializer;

        return new JavaSerializer(cl);

        /*if (!Serializable.class.isAssignableFrom(cl) && !isAllowNonSerializable) {
            throw new IllegalStateException("Serialized class " + cl.getName() + " must implement java.io.Serializable");
        }

        return new JavaSerializer(cl);*/
    }

    /**
     * Returns the deserializer for a class.
     *
     * @param cl the class of the object that needs to be deserialized.
     * @return a deserializer object for the serialization.
     */
    @Override
    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        Deserializer deserializer;

        deserializer = (Deserializer) staticDeserializerMap.get(cl);
        if (deserializer != null)
            return deserializer;

        deserializer = (Deserializer) cachedDeserializerMap.get(cl);

        if (deserializer != null)
            return deserializer;

        for (int i = 0; deserializer == null && factories != null && i < factories.size(); i++) {
            AbstractSerializerFactory factory;
            factory = (AbstractSerializerFactory) factories.get(i);

            deserializer = factory.getDeserializer(cl);
        }

        if (deserializer != null) {
            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debugWithApp(null, "serializer success ");
            }
        } else if (Collection.class.isAssignableFrom(cl))
            deserializer = new CollectionDeserializer(cl);

        else if (Map.class.isAssignableFrom(cl))
            deserializer = new MapDeserializer(cl);

        else if (cl.isInterface())
            deserializer = OBJECT_DESERIALIZER;

        else if (cl.isArray())
            deserializer = new ArrayDeserializer(getDeserializer(cl.getComponentType()).getType());

        else if (Enumeration.class.isAssignableFrom(cl))
            deserializer = EnumerationDeserializer.create();

        else if (Enum.class.isAssignableFrom(cl))
            deserializer = new EnumDeserializer(cl);

        else
            deserializer = getDefaultDeserializer(cl);

        cachedDeserializerMap.put(cl, deserializer);

        return deserializer;
    }

    /**
     * Returns the default serializer for a class that isn't matched directly.
     * Application can override this method to produce bean-style serialization
     * instead of field serialization.
     *
     * @param cl the class of the object that needs to be serialized.
     * @return a serializer object for the serialization.
     */
    @Override
    protected Deserializer getDefaultDeserializer(Class cl) {
        return new JavaDeserializer(cl);
    }

    /**
     * Reads the object as a list.
     */
    @Override
    public Object readList(AbstractHessianInput in, int length, String type)
            throws HessianProtocolException,
            IOException {
        Deserializer deserializer = getDeserializer(type);

        if (deserializer != null)
            return deserializer.readList(in, length);
        else
            return new CollectionDeserializer(ArrayList.class).readList(in, length);
    }

    /**
     * Reads the object as a map.
     */
    @Override
    public Object readMap(AbstractHessianInput in, String type) throws HessianProtocolException,
            IOException {
        Deserializer deserializer = getDeserializer(type);

        if (deserializer != null)
            return deserializer.readMap(in);
        else if (hashMapDeserializer != null)
            return hashMapDeserializer.readMap(in);
        else {
            hashMapDeserializer = new MapDeserializer(HashMap.class);

            return hashMapDeserializer.readMap(in);
        }
    }

    /**
     * Reads the object as a map.
     */
    @Override
    public Object readObject(AbstractHessianInput in, String type, String[] fieldNames)
            throws HessianProtocolException,
            IOException {
        Deserializer deserializer = getDeserializer(type);

        if (deserializer != null)
            return deserializer.readObject(in, fieldNames);
        else if (hashMapDeserializer != null)
            return hashMapDeserializer.readObject(in, fieldNames);
        else {
            hashMapDeserializer = new MapDeserializer(HashMap.class);

            return hashMapDeserializer.readObject(in, fieldNames);
        }
    }

    /**
     * Reads the object as a map.
     */
    @Override
    public Deserializer getObjectDeserializer(String type, Class cl)
            throws HessianProtocolException {
        Deserializer reader = getObjectDeserializer(type);

        if (cl == null || cl == reader.getType() || cl.isAssignableFrom(reader.getType())) {
            return reader;
        }

        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debugWithApp(null, "hessian: expected '{0}' at '{1}'", cl.getName(), type);
        }
        return getDeserializer(cl);
    }

    /**
     * Reads the object as a map.
     */
    @Override
    public Deserializer getObjectDeserializer(String type) throws HessianProtocolException {
        Deserializer deserializer = getDeserializer(type);

        if (deserializer != null)
            return deserializer;
        else if (hashMapDeserializer != null)
            return hashMapDeserializer;
        else {
            hashMapDeserializer = new MapDeserializer(HashMap.class);

            return hashMapDeserializer;
        }
    }

    /**
     * Returns a deserializer based on a string type.
     */
    @Override
    public Deserializer getDeserializer(String type) throws HessianProtocolException {
        if (type == null || "".equals(type))
            return null;

        Deserializer deserializer;

        deserializer = (Deserializer) cachedTypeDeserializerMap.get(type);

        if (deserializer != null)
            return deserializer;

        deserializer = (Deserializer) staticTypeMap.get(type);
        if (deserializer != null)
            return deserializer;

        if (type.startsWith("[")) {
            Deserializer subDeserializer = getDeserializer(type.substring(1));
            deserializer = new ArrayDeserializer(subDeserializer.getType());
        } else {
            try {
                ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
                Class<?> cl = Class.forName(type, true, appClassLoader);
                deserializer = getDeserializer(cl);
            } catch (Exception e) {
                if (e instanceof ClassNotFoundException) {
//                    LOGGER.errorWithApp(null, LogCodes.getLog(LogCodes.ERROR_DECODE_CLASS_NOT_FOUND,
//                                    "MultipleClassLoaderSofaSerializerFactory",
//                                    type, Thread.currentThread().getContextClassLoader()));
                } else {
//                    LOGGER.errorWithApp(null, e.toString(), e);
                }
            }
        }

        if (deserializer != null) {
            cachedTypeDeserializerMap.put(type, deserializer);
        }

        return deserializer;
    }

    private static void addBasic(Class cl, String typeName, int type) {
        staticSerializerMap.put(cl, new BasicSerializer(type));

        Deserializer deserializer = new BasicDeserializer(type);
        staticDeserializerMap.put(cl, deserializer);
        staticTypeMap.put(typeName, deserializer);
    }

    static {
        staticSerializerMap = new ConcurrentHashMap();
        staticDeserializerMap = new ConcurrentHashMap();
        staticTypeMap = new ConcurrentHashMap();

        addBasic(void.class, "void", BasicSerializer.NULL);

        addBasic(Boolean.class, "boolean", BasicSerializer.BOOLEAN);
        addBasic(Byte.class, "byte", BasicSerializer.BYTE);
        addBasic(Short.class, "short", BasicSerializer.SHORT);
        addBasic(Integer.class, "int", BasicSerializer.INTEGER);
        addBasic(Long.class, "long", BasicSerializer.LONG);
        addBasic(Float.class, "float", BasicSerializer.FLOAT);
        addBasic(Double.class, "double", BasicSerializer.DOUBLE);
        addBasic(Character.class, "char", BasicSerializer.CHARACTER_OBJECT);
        addBasic(String.class, "string", BasicSerializer.STRING);
        addBasic(Object.class, "object", BasicSerializer.OBJECT);
        addBasic(java.util.Date.class, "date", BasicSerializer.DATE);

        addBasic(boolean.class, "boolean", BasicSerializer.BOOLEAN);
        addBasic(byte.class, "byte", BasicSerializer.BYTE);
        addBasic(short.class, "short", BasicSerializer.SHORT);
        addBasic(int.class, "int", BasicSerializer.INTEGER);
        addBasic(long.class, "long", BasicSerializer.LONG);
        addBasic(float.class, "float", BasicSerializer.FLOAT);
        addBasic(double.class, "double", BasicSerializer.DOUBLE);
        addBasic(char.class, "char", BasicSerializer.CHARACTER);

        addBasic(boolean[].class, "[boolean", BasicSerializer.BOOLEAN_ARRAY);
        addBasic(byte[].class, "[byte", BasicSerializer.BYTE_ARRAY);
        addBasic(short[].class, "[short", BasicSerializer.SHORT_ARRAY);
        addBasic(int[].class, "[int", BasicSerializer.INTEGER_ARRAY);
        addBasic(long[].class, "[long", BasicSerializer.LONG_ARRAY);
        addBasic(float[].class, "[float", BasicSerializer.FLOAT_ARRAY);
        addBasic(double[].class, "[double", BasicSerializer.DOUBLE_ARRAY);
        addBasic(char[].class, "[char", BasicSerializer.CHARACTER_ARRAY);
        addBasic(String[].class, "[string", BasicSerializer.STRING_ARRAY);
        addBasic(Object[].class, "[object", BasicSerializer.OBJECT_ARRAY);
        try {
            staticSerializerMap.put(Class.class, new ClassSerializer());
            staticDeserializerMap.put(Class.class, new ClassDeserializer(Thread.currentThread().getContextClassLoader()));
            staticDeserializerMap.put(Number.class, new BasicDeserializer(BasicSerializer.NUMBER));
            staticSerializerMap.put(BigDecimal.class, new StringValueSerializer());
            staticDeserializerMap.put(BigDecimal.class, new StringValueDeserializer(
                    BigDecimal.class));
            staticSerializerMap.put(File.class, new StringValueSerializer());
            staticDeserializerMap.put(File.class, new StringValueDeserializer(File.class));
            staticSerializerMap.put(java.sql.Date.class, new SqlDateSerializer());
            staticSerializerMap.put(java.sql.Time.class, new SqlDateSerializer());
            staticSerializerMap.put(java.sql.Timestamp.class, new SqlDateSerializer());
            staticSerializerMap.put(InputStream.class, new InputStreamSerializer());
            staticDeserializerMap.put(InputStream.class, new InputStreamDeserializer());
            staticDeserializerMap.put(java.sql.Date.class, new SqlDateDeserializer(
                    java.sql.Date.class));
            staticDeserializerMap.put(java.sql.Time.class, new SqlDateDeserializer(
                    java.sql.Time.class));
            staticDeserializerMap.put(java.sql.Timestamp.class, new SqlDateDeserializer(
                    java.sql.Timestamp.class));
            Class stackTrace = Class.forName("java.lang.StackTraceElement");
            staticDeserializerMap.put(stackTrace, new StackTraceElementDeserializer());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
