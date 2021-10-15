

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/12/20 0020
 * creat_time: 8:28
 **/
public class UnitTest {

    @Test
    public void test1() {
        String json = "{\n" +
                "        0: {\n" +
                "          \"converted\": false,\n" +
                "          \"value\": {\n" +
                "            \"beanName\": \"testService\",\n" +
                "            \"toParent\": false\n" +
                "          }\n" +
                "        },\n" +
                "        1: {\n" +
                "          \"converted\": false,\n" +
                "          \"value\": \"wire by constructor\"\n" +
                "        }\n" +
                "      }";
//        private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<Integer, ValueHolder>(0);
        Map<Integer, ConstructorArgumentValues.ValueHolder> object = JSON.parseObject(json,
                new TypeReference<Map<Integer, ConstructorArgumentValues.ValueHolder>>() {
                });
        System.out.println(object);

    }


    @Test
    public void test2() throws IOException {
        String json = StreamUtils.copyToString(new ClassPathResource("beanDefinition.json").getInputStream(), Charset.forName("UTF-8"));
        System.out.println(json);

        Map<Integer, ConstructorArgumentValues.ValueHolder> object = JSON.parseObject(json,
                new TypeReference<Map<Integer, ConstructorArgumentValues.ValueHolder>>() {
                });
        System.out.println(object);

    }

}
