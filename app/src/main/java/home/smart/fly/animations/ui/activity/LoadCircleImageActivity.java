package home.smart.fly.animations.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;

import de.hdodenhof.circleimageview.CircleImageView;
import home.smart.fly.animations.R;
import home.smart.fly.animations.internal.DarkWorld;

public class LoadCircleImageActivity extends AppCompatActivity {
    private static final String TAG = "LoadCircleImageActivity";
    private static final String PIC_URL = "http://f.hiphotos.baidu.com/zhidao/pic/item/8b82b9014a90f60326b707453b12b31bb051eda9.jpg";
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_load_circle_image);
        final CircleImageView userAvatar = (CircleImageView) findViewById(R.id.userAvatar);
        final ImageView userAvatar1 = (ImageView) findViewById(R.id.userAvatar1);
        final ImageView gif = findViewById(R.id.gif);
        final ImageView gif_gen = findViewById(R.id.gif_gen);


        findViewById(R.id.load).setOnClickListener(v -> {
            Glide.with(LoadCircleImageActivity.this).load(PIC_URL).into(userAvatar1);
            Glide.with(LoadCircleImageActivity.this).load(PIC_URL).into(userAvatar);


            simpleDarkWorld();
            hackDarkWorld();
        });


    }

    public Object getValue(Object object, String fieldName) {
        if (object == null) {
            return null;
        }
        if (TextUtils.isEmpty(fieldName)) {
            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }


    private void hackDarkWorld() {
        try {
            Class<?> classDarkWorld = Class.forName("home.smart.fly.animations.internal.DarkWorld");
            DarkWorld darkWorld = (DarkWorld) classDarkWorld.newInstance();

            Field msg = classDarkWorld.getDeclaredField("msg");
            Field count = classDarkWorld.getDeclaredField("count");
            msg.setAccessible(true);
            count.setAccessible(true);

            msg.set(darkWorld, "This is hackWorld");
            count.set(darkWorld, 101);
            darkWorld.log();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void simpleDarkWorld() {
        DarkWorld darkWorld = new DarkWorld();
        darkWorld.log();
    }
}
