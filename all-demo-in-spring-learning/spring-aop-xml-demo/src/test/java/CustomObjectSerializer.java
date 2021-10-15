import com.alibaba.fastjson.serializer.JSONSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class CustomObjectSerializer implements com.alibaba.fastjson.serializer.AutowiredObjectSerializer {

    @Override
    public Set<Type> getAutowiredFor() {
        Set<Type> set = new HashSet<>();
        set.add(JsonTest.class);
        return set;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        System.out.println(object.toString());

    }
}
