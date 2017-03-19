package home.smart.fly.animationdemo.customview.wheel;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.wheel.updateFile.Address;
import home.smart.fly.animationdemo.customview.wheel.widget.adapter.ArrayWheelAdapter;
import home.smart.fly.animationdemo.customview.wheel.widget.lib.WheelView;
import home.smart.fly.animationdemo.customview.wheel.widget.listener.OnItemSelectedListener;
import home.smart.fly.animationdemo.customview.wheel.widget.view.WheelOptions;
import home.smart.fly.animationdemo.customview.wheel.widget.view.WheelViewDialog;

public class WheelViewActivity extends AppCompatActivity {
    private static final String TAG = "WheelViewActivity";

    private Context mContext;

    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONArray mJsonObj;
    WheelViewDialog mWheelViewDialog;
    static ArrayList<String> item1;
    static ArrayList<ArrayList<String>> item2 = new ArrayList<>();
    static ArrayList<ArrayList<ArrayList<String>>> item3 = new ArrayList<>();

    ///
    private View view;
    private WheelOptions mWheelOptions;
    private WheelView province, city, area;
    private TextView address;
    int[] pos = new int[]{0, 0, 0};

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 三级联动效果

            mWheelViewDialog.setPicker(item1, item2, item3, true);
            mWheelViewDialog.setCyclic(true, true, true);
            mWheelViewDialog.setSelectOptions(0, 0, 0);
            mWheelViewDialog.show();


        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        mContext = this;
        选项选择器弹框初始化();
        initJsonData();

        findViewById(R.id.showDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Init();
            }
        });


        ////////
        view = findViewById(R.id.optionspicker);
        mWheelOptions = new WheelOptions(view);
        setData();
        mWheelOptions.setPicker(item1, item2, item3, true);
        mWheelOptions.setCyclic(true, true, true);
        mWheelOptions.setCurrentItems(0, 0, 0);
        province = (WheelView) findViewById(R.id.province);
        city = (WheelView) findViewById(R.id.city);
        area = (WheelView) findViewById(R.id.area);
        address = (TextView) findViewById(R.id.address);


        province.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

                int opt2Select = 0;
                if (item2 != null) {
                    opt2Select = city.getCurrentItem();//上一个opt2的选中位置
                    //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt2Select = opt2Select >= item2.get(index).size() - 1 ? item2.get(index).size() - 1 : opt2Select;

                    city.setAdapter(new ArrayWheelAdapter(item2
                            .get(index)));
                    city.setCurrentItem(opt2Select);
                    area.setAdapter(new ArrayWheelAdapter(item3.get(index).get(opt2Select)));
                    area.setCurrentItem(0);
                }


                pos[0] = index;
                pos[1] = opt2Select;
                pos[2] = 0;
                updateAdress(pos);
            }
        });

        city.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {


                if (item3 != null) {
                    int opt1Select = province.getCurrentItem();
                    opt1Select = opt1Select >= item3.size() - 1 ? item3.size() - 1 : opt1Select;
                    index = index >= item2.get(opt1Select).size() - 1 ? item2.get(opt1Select).size() - 1 : index;
                    int opt3 = area.getCurrentItem();//上一个opt3的选中位置
                    //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt3 = opt3 >= item3.get(opt1Select).get(index).size() - 1 ? item3.get(opt1Select).get(index).size() - 1 : opt3;

                    area.setAdapter(new ArrayWheelAdapter(item3
                            .get(province.getCurrentItem()).get(
                                    index)));
                    area.setCurrentItem(0);


                    pos[0] = opt1Select;
                    pos[1] = index;
                    pos[2] = 0;
                    updateAdress(pos);

                }
            }
        });

        area.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                pos[2] = index;
                updateAdress(pos);
            }
        });

        updateAdress(pos);

    }

    private void updateAdress(int[] pos) {
        String temp = item1.get(pos[0]) + item2.get(pos[0]).get(pos[1]) + item3.get(pos[0]).get(pos[1]).get(pos[2]);
        address.setText(temp);
    }

    private void 选项选择器弹框初始化() {
        mWheelViewDialog = new WheelViewDialog(mContext);
        mWheelViewDialog.setOnoptionsSelectListener(new WheelViewDialog.OnOptionsSelectListener() {

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
    }

    private void setData() {
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
        } catch (Exception e) {

        }
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
                setData();
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
