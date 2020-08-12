package com.fujisann.ink;

import android.util.Log;

import com.fujisann.ink.weather.he.Weather;
import com.fujisann.ink.weather.region.Province;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    private static final String TAG = "ExampleUnitTest";

    @Test
    public void testGson(){
        String jsonObj = "{\"name\":\"江苏\", \"id\":\"1\"}";
        String jsonArr = "[{\"name\":\"江苏\", \"id\":\"1\"},{\"name\":\"安徽\", \"id\":\"2\"}]";
        Province province1 = new Gson().fromJson(jsonObj, Province.class);
        System.out.println("transient: " +province1);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Province province = gson.fromJson(jsonObj, Province.class);
        System.out.println("obj" + province);
        List<Province> list = buildData(Province.class, jsonArr);
        System.out.println("arr: " + Arrays.toString(list.toArray()));
    }

    private <T> List<T> buildData(Class<T> clazz, String string) {
        return new Gson().fromJson(string, new TypeToken<List<T>>(){}.getType());
    }

    @Test
    public void testWeather(){
        String s = "{\"HeWeather\":[{\"basic\":{\"cid\":\"CN101190101\",\"location\":\"南京\",\"parent_city\":\"南京\",\"admin_area\":\"江苏\",\"cnty\":\"中国\",\"lat\":\"36.62317657\",\"lon\":\"101.77891541\",\"tz\":\"+8.00\",\"city\":\"南京\",\"id\":\"CN101190101\",\"update\":{\"loc\":\"2020-08-11 00:56\",\"utc\":\"2020-08-10 16:56\"}},\"update\":{\"loc\":\"2020-08-11 00:56\",\"utc\":\"2020-08-10 16:56\"},\"status\":\"ok\",\"now\":{\"cloud\":\"91\",\"cond_code\":\"100\",\"cond_txt\":\"晴\",\"fl\":\"7\",\"hum\":\"6\",\"pcpn\":\"0.0\",\"pres\":\"1016\",\"tmp\":\"14\",\"vis\":\"16\",\"wind_deg\":\"277\",\"wind_dir\":\"西风\",\"wind_sc\":\"4\",\"wind_spd\":\"27\",\"cond\":{\"code\":\"100\",\"txt\":\"晴\"}},\"daily_forecast\":[{\"date\":\"2020-08-12\",\"cond\":{\"txt_d\":\"晴\"},\"tmp\":{\"max\":\"14\",\"min\":\"-2\"}},{\"date\":\"2020-08-13\",\"cond\":{\"txt_d\":\"多云\"},\"tmp\":{\"max\":\"11\",\"min\":\"-3\"}},{\"date\":\"2020-08-14\",\"cond\":{\"txt_d\":\"晴\"},\"tmp\":{\"max\":\"14\",\"min\":\"-2\"}},{\"date\":\"2020-08-15\",\"cond\":{\"txt_d\":\"多云\"},\"tmp\":{\"max\":\"13\",\"min\":\"-2\"}},{\"date\":\"2020-08-16\",\"cond\":{\"txt_d\":\"阴\"},\"tmp\":{\"max\":\"12\",\"min\":\"3\"}},{\"date\":\"2020-08-17\",\"cond\":{\"txt_d\":\"晴\"},\"tmp\":{\"max\":\"16\",\"min\":\"2\"}}],\"aqi\":{\"city\":{\"aqi\":\"89\",\"pm25\":\"47\",\"qlty\":\"良\"}},\"suggestion\":{\"comf\":{\"type\":\"comf\",\"brf\":\"较舒适\",\"txt\":\"白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。\"},\"sport\":{\"type\":\"sport\",\"brf\":\"较适宜\",\"txt\":\"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。\"},\"cw\":{\"type\":\"cw\",\"brf\":\"较适宜\",\"txt\":\"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。\"}},\"msg\":\"所有天气数据均为模拟数据，仅用作学习目的使用，请勿当作真实的天气预报软件来使用。\"}]}";
        Weather weather = null;
        try {
            weather = new Gson().fromJson(s, Weather.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "testWeather: " + weather.getHeWeather().get(0).getBasic().getCity());

    }
}