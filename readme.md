# JacksonSensible

基于Jackson的序列化脱敏方案。  
通过自定义序列化的方式，对数据进行脱敏处理。  
使用方法：

* 就是在需要脱敏的属性或是get方法上加上`@Sensitive`注解。
* 需要自定义的脱敏处理主需要实现`SerializeHandler`接口，并将实现类加入到`SerializerService`即可。

## 添加

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

> Gradle
> ```text
> allprojects {
>   repositories {
>       maven { url 'https://jitpack.io' }
>   }
> }
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>jackson-sensible</artifactId>
>            <version>alpha-0.1</version>
>        </dependency>
>    </dependencies>
> ```

> Gradle
> ```text
> dependencies {
>   implementation 'com.github.Verlif:jackson-sensible:alpha-0.1'
> }
> ```

## 使用

### 添加`@Sensitive`注解

在需要的属性上添加`@Sensitive`注解即可，可以选择脱敏策略。

```java
    public class Test implements Serializable {

    @Sensitive(strategy = Strategy.ALWAYS_NULL)
    private String alwaysNull;

    @Sensitive
    private String stringHandler;

    @Sensitive
    private int intValue;

    @Sensitive(strategy = Strategy.ALWAYS_VALUE, value = "123")
    private Integer integerValue;

    @Sensitive
    private Double longValue;

    // 省略get与set方法

}
```

### 自定义脱敏处理

1. 实现`SerializeHandler`接口。

例如实现一个将敏感数据转换成`*****`的处理器:

```java
public class StringHandler implements SerializeHandler<String> {

    /**
     * 处理器匹配的类。
     */
    @Override
    public Class<?>[] match() {
        return new Class[]{String.class};
    }

    @Override
    public Object handle(String val) {
        return "*****";
    }
}
```

2. 将自定义脱敏处理器添加到`SerializerService`。

由于`SerializerService`由`SensitiveSerialize`使用，所以需要从`SensitiveSerialize`中取出。

```java
    SerializerService ss=SensitiveSerialize.getSerializerService();
        ss.addOrReplace(new StringHandler());
```

## 示例

```java
    ObjectMapper objectMapper=new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    SerializerService ss=SensitiveSerialize.getSerializerService();

    // 添加自定义脱敏处理器
    ss.addOrReplace(new StringHandler());
    ss.addOrReplace(new IntegerHandler());

    // 生成测试对象
    Test test=new Test();
    test.alwaysNull="this is value";
    test.stringHandler="maybe";
    test.intValue=18;
    test.integerValue=25;
    test.longValue=77d;

    // 脱敏输出
    System.out.println(objectMapper.valueToTree(test));
```

这里只添加了String与int的脱敏处理器，对于double类型没有添加，脱敏选择器就会将double直接置空处理。  
所得的输出如下：

```json
{
  "alwaysNull": null,
  "stringHandler": "*****",
  "intValue": 520,
  "integerValue": "123",
  "longValue": null
}
```

虽然代码中设置了`JsonInclude.Include.NON_NULL`，当由于`alwaysNull`与`longValue`本身是有值的，所以会被输出。