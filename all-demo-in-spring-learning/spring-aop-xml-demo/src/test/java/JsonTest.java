import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.serializer.AutowiredObjectSerializer;
import com.alibaba.fastjson.util.ServiceLoader;

public class JsonTest  {

    public static void main(String[] args) {
        ServiceLoader.load(AutowiredObjectSerializer.class,
                Thread.currentThread().getContextClassLoader());
        JSONObject.toJSONString(new JsonTest());
    }
}
