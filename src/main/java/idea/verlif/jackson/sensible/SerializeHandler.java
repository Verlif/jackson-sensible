package idea.verlif.jackson.sensible;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * json字段序列化处理器
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/1/26 11:28
 */
public interface SerializeHandler<T> {

    /**
     * 处理器匹配的参数类型
     *
     * @return 参数类型列表
     */
    default Class<?>[] match() {
        Type[] types = getClass().getGenericInterfaces();
        List<Class<?>> list = new ArrayList<>();
        for (Type type : types) {
            try {
                for (Type argument : ((ParameterizedType) type).getActualTypeArguments()) {
                    Class<?> cl = Class.forName(argument.getTypeName());
                    list.add(cl);
                }
            } catch (Exception ignored) {
            }
        }
        return list.toArray(new Class[]{});
    }

    /**
     * 处理原数据值
     *
     * @param val 原数据
     * @return 处理后的数据
     */
    Object handle(T val);

}
