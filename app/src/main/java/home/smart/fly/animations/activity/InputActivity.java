package home.smart.fly.animations.activity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.T;
import me.leolin.shortcutbadger.ShortcutBadger;

public class InputActivity extends AppCompatActivity {
    private static final String TAG = "InputActivity";

    @BindView(R.id.input)
    EditText mInput;
    @BindView(R.id.result)
    TextView mResult;
    @BindView(R.id.macAddress)
    TextView mMacAddress;
    @BindView(R.id.link)
    TextView mLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);

        mLink.getPaint().setFlags(TextPaint.UNDERLINE_TEXT_FLAG);
        mLink.getPaint().setAntiAlias(true);

    }

    @OnClick({R.id.set_badge_num, R.id.get, R.id.format, R.id.getMac1, R.id.getMac2, R.id.link})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.set_badge_num:
                int count = 0;
                if (!TextUtils.isEmpty(mInput.getText().toString())) {
                    count = Integer.parseInt(mInput.getText().toString());
                }

                if (ShortcutBadger.applyCount(this, count)) {
                    T.showSToast(this, "success");
                } else {
                    T.showSToast(this, "fail");
                }


                break;
            case R.id.get:
                mResult.setText(mInput.getText().toString());
                break;
            case R.id.format:
                String temp = mResult.getText().toString();

                temp = temp.replace("\n", "");

                mResult.setText(temp);
                break;
            case R.id.getMac1:
                getMacAddress1();
                break;
            case R.id.getMac2:
                getMacAddress2();
                break;
            case R.id.link:
                Toast.makeText(this, "link !", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void getMacAddress1() {
        WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        Log.e(TAG, "getMacAddress1: " + mWifiInfo.getMacAddress());
        mMacAddress.setText(mWifiInfo.getMacAddress());
    }

    private void getMacAddress2() {
        String mac = getAddressMAC(getApplicationContext());
        mMacAddress.setText(mac);
    }

    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAddressMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
            String result = null;
            try {
                result = getAdressMacByInterface();
                if (result != null) {
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }


    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


}
