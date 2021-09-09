package com.zhangpeng.avfun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zhangpeng.avfun.javabean.VideoCategoryEntity;
import com.zhangpeng.avfun.utils.ConstantUtils;
import com.zhangpeng.avfun.utils.DateToStringConverter;
import com.zhangpeng.avfun.utils.JsonUtils;
import com.zhangpeng.avfun.utils.TimeUtils;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String json="{\n" +
                "  \"code\": \"200\",\n" +
                "  \"message\": \"success\",\n" +
                "  \"data\": {\n" +
                "    \"count\": 6758,\n" +
                "    \"totalpage\": 141,\n" +
                "    \"curpage\": 1,\n" +
                "    \"mapList\": [\n" +
                "      {\n" +
                "        \"vid\": 4371,\n" +
                "        \"size\": \"[FHD MP4/4.4GB]\",\n" +
                "        \"imageurl\": \"http://192.168.0.109:9000/image/df86282.jpg\",\n" +
                "        \"docUrl\": \"https://aa1805.com/df/23781.html\",\n" +
                "        \"category\": \"censored\",\n" +
                "        \"videoTittle\": \"dasd655 物腰柔爆乳妻信頼上司寝取種付 永井瑪利亞[VIP1196]\",\n" +
                "        \"createDate\": 1604419200000,\n" +
                "        \"playUrl\": \"https://m3u8.cdnpan.com/sZnJ53Tf.m3u8\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date
                .class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        VideoCategoryEntity pageVideoResult = gson.fromJson(json, VideoCategoryEntity.class);

        assertEquals(4, 2 + 2);
    }
    @Test
    public void addition_isCorrect1() {
        System.out.println(ConstantUtils.selfphoto.name());
        System.out.println( ConstantUtils.selfphoto.getCategory());
        ConstantUtils selfphoto = ConstantUtils.valueOf("selfphoto");
        System.out.println(selfphoto);
        assertEquals(4, 2 + 2);
    }
    @Test
    public void test (){
        String json=" {\"code\":\"200\",\"message\":\"success\",\"data\":{\"count\":16650,\"totalpage\":8325,\"curpage\":1,\"mapList\":[{\"vid\":505,\"size\":\"[FHD/2.91G]\",\"imageurl\":\"http://192.168.0.109:9000/image/df93538.jpg\",\"docUrl\":\"https://aa1805.com/df/30787.html\",\"category\":\"uncensored\",\"videoTittle\":\"最新HEYZO 2384 想要肛门的女人~总2名~VIP1196\",\"createDate\":1604419200000,\"playUrl\":\"https://m3u8.cdnpan.com/gbQat0wr.m3u8\"},{\"vid\":519,\"size\":\"[HD/1.63G]\",\"imageurl\":\"http://192.168.0.109:9000/image/df93507.jpg\",\"docUrl\":\"https://aa1805.com/df/30757.html\",\"category\":\"uncensored\",\"videoTittle\":\"最新 C0930 ki201101 船越 政江 48歳VIP1196\",\"createDate\":1604419200000,\"playUrl\":\"https://m3u8.cdnpan.com/Ws8EMcWI.m3u8\"}]}}";
        VideoCategoryEntity entity = JsonUtils.parseJson(json, VideoCategoryEntity.class);
        System.out.println(entity.getData().getMapList());

    }
    @Test
    public void test1(){
        TimeUtils.formartTimes(8521256L);
        assertEquals(4, 2 + 2);

    }
    @Test
    public void test2(){
        DateToStringConverter dateLongConverter = new DateToStringConverter();
        String s = dateLongConverter.dateToString(new Date());
        Date date = dateLongConverter.stringToDate(s);
        System.out.println(date);
        System.out.println(s);
    }
}