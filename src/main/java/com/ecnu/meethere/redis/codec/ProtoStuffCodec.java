package com.ecnu.meethere.redis.codec;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.joor.Reflect;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProtoStuffCodec implements RedisCodec {

    private final ThreadLocal<LinkedBuffer> buffer = new ThreadLocal<>();

    private LinkedBuffer getLinkedBuffer() {
        LinkedBuffer linkedBuffer = buffer.get();
        if (linkedBuffer != null)
            return linkedBuffer;
        LinkedBuffer newBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        buffer.set(newBuffer);
        return newBuffer;
    }

    @Override
    public <T> byte[] encode(T o) {
        Objects.requireNonNull(o);
        byte[] bytes = null;
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(o.getClass());
        LinkedBuffer buffer = getLinkedBuffer();
        bytes = ProtostuffIOUtil.toByteArray(o, schema, buffer);
        buffer.clear();
        return bytes;
    }

    @Override
    public <T> Map<String, byte[]> encodeFields(T o) {
        Objects.requireNonNull(o);
        Map<String, Reflect> fields = Reflect.on(o).fields();
        Map<String, byte[]> res = new HashMap<>(fields.size());
        for (Map.Entry<String, Reflect> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue().get();
            if (fieldValue != null)
                res.put(fieldName, encode(fieldValue));
        }
        return res;
    }

    @Override
    public <T> Map<byte[], byte[]> encodeFieldsWithByteKeys(T o) {
        Objects.requireNonNull(o);
        Map<String, Reflect> fields = Reflect.on(o).fields();
        Map<byte[], byte[]> res = new HashMap<>(fields.size());
        for (Map.Entry<String, Reflect> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue().get();
            if (fieldValue != null)
                res.put(fieldName.getBytes(Charset.defaultCharset()), encode(fieldValue));
        }
        return res;
    }

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        Objects.requireNonNull(clazz);
        if (bytes == null)
            return null;
        T o = null;
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        o = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, o, schema);
        return o;
    }

    @Override
    public <T> T decodeFields(Map<String, byte[]> bytesMap, Class<T> clazz) {
        if (CollectionUtils.isEmpty(bytesMap))
            return null;
        Objects.requireNonNull(clazz);
        Reflect reflect = Reflect.onClass(clazz).create();
        for (Map.Entry<String, byte[]> entry : bytesMap.entrySet()) {
            String fieldName = entry.getKey();
            byte[] bytes = entry.getValue();
            if (bytes != null)
                reflect.set(fieldName, decode(bytes, reflect.field(fieldName).type()));
        }
        return reflect.get();
    }

    @Override
    public <T> T decodeFieldsWithByteKeys(Map<byte[], byte[]> m, Class<T> clazz) {
        if (CollectionUtils.isEmpty(m))
            return null;
        Objects.requireNonNull(clazz);
        Reflect reflect = Reflect.onClass(clazz).create();
        for (Map.Entry<byte[], byte[]> entry : m.entrySet()) {
            String fieldName = new String(entry.getKey(), Charset.defaultCharset());
            byte[] bytes = entry.getValue();
            if (bytes != null)
                reflect.set(fieldName, decode(bytes, reflect.field(fieldName).type()));
        }
        return reflect.get();
    }

}
