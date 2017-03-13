package home.smart.fly.animationdemo.customview.wheel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.wheel.updateFile.Address;
import home.smart.fly.animationdemo.customview.wheel.widget.view.OptionsPickerView;
import home.smart.fly.animationdemo.customview.wheel.widget.view.WheelOptions;

public class WheelViewActivity extends AppCompatActivity {

    private Context mContext;

    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONArray mJsonObj;

    OptionsPickerView pvOptions;

    static ArrayList<String> item1;

    static ArrayList<ArrayList<String>> item2 = new ArrayList<>();

    static ArrayList<ArrayList<ArrayList<String>>> item3 = new ArrayList<>();

    ///
    private View view;
    private WheelOptions mWheelOptions;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 三级联动效果

            pvOptions.setPicker(item1, item2, item3, true);
            pvOptions.setCyclic(true, true, true);
            pvOptions.setSelectOptions(0, 0, 0);
            pvOptions.show();

            mWheelOptions = new WheelOptions(view);
            mWheelOptions.setPicker(item1, item2, item3, true);
            mWheelOptions.setCyclic(true, true, true);
            mWheelOptions.setCurrentItems(0, 0, 0);

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        mContext = this;
        // 选项选择器
        pvOptions = new OptionsPickerView(mContext);

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                ArrayList<Address> datas = new ArrayList<Address>();
                // 返回的分别是三个级别的选中位置
                String tx = item1.get(options1) + item2.get(options1).get(option2)
                        + item3.get(options1).get(option2).get(options3);
                Address address1 = new Address();
                address1.setNanme(item1.get(options1));

                Address address2 = new Address();
                address2.setNanme(item2.get(options1).get(option2));

                datas.add(address1);
                datas.add(address2);


                String jsonArray = "";

                Log.e(WheelViewActivity.class.getSimpleName(), jsonArray);


                Toast.makeText(mContext, tx, Toast.LENGTH_SHORT).show();
            }
        });
        initJsonData();

        findViewById(R.id.showDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Init();
            }
        });


        ////////
        view = findViewById(R.id.optionspicker);


    }

    public void Init() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println(System.currentTimeMillis());
                if (item1 != null && item2 != null && item3 != null) {
                    handler.sendEmptyMessage(0x123);
                    return;
                }
                initJsonData();

                item1 = new ArrayList<>();
                try {
                    if (mJsonObj != null && mJsonObj.length() > 0) {
                        for (int i = 0; i < mJsonObj.length(); i++) {
                            JSONObject provs = mJsonObj.getJSONObject(i);
                            item1.add(provs.getString("name"));

                            JSONArray citys = provs.getJSONArray("city");
                            if (citys != null && citys.length() > 0) {
                                ArrayList<String> datas = new ArrayList<>();
                                ArrayList<ArrayList<String>> datas2 = new ArrayList<>();
                                for (int m = 0; m < citys.length(); m++) {
                                    JSONObject city = citys.getJSONObject(m);
                                    datas.add(city.getString("name"));

                                    JSONArray areas = city.getJSONArray("area");
                                    if (areas != null && areas.length() > 0) {
                                        ArrayList<String> data3 = new ArrayList<>();
                                        for (int k = 0; k < areas.length(); k++) {
                                            data3.add(areas.get(k).toString());
                                        }

                                        datas2.add(data3);
                                    }
                                }
                                item2.add(datas);
                                item3.add(datas2);
                            }
                        }
                    }

                    handler.sendEmptyMessage(0x123);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getAssets().open("city.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            mJsonObj = new JSONArray(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
