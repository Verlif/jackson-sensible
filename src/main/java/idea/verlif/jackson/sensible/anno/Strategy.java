package idea.verlif.jackson.sensible.anno;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/26 14:32
 */
public enum Strategy {

    /**
     * 总是null
     */
    ALWAYS_NULL,

    /**
     * 总是填充
     */
    ALWAYS_VALUE,

    /**
     * 总是处理。无论值状态，总是会调用{@linkplain idea.verlif.jackson.sensible.SerializeHandler SerializeHandler} 来处理值。
     */
    ALWAYS_HANDLE;
}
