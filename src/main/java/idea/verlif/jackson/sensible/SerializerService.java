package idea.verlif.jackson.sensible;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/26 11:20
 */
public class SerializerService {

    private final Map<Class<?>, SerializeHandler<?>> serializerMap;

    public SerializerService() {
        serializerMap = new HashMap<>();
    }

    public boolean addOrReplace(SerializeHandler<?> serializer) {
        Class<?>[] cls = serializer.match();
        if (cls.length == 0) {
            return false;
        }
        for (Class<?> match : cls) {
            serializerMap.put(match, serializer);
        }
        return true;
    }

    public SerializeHandler<?> getSerializer(Class<?> cl) {
        return serializerMap.get(cl);
    }

    /**
     * 处理数据
     *
     * @param o   原始数据
     * @param <T> 原始数据类型
     * @return 处理后的数据
     */
    public <T> Object handle(Object o) {
        SerializeHandler<T> handler = (SerializeHandler<T>) getSerializer(o.getClass());
        if (handler == null) {
            return null;
        } else {
            return handler.handle((T) o);
        }
    }
}
