package home.smart.fly.animations.ui.activity;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReadJarActivity extends AppCompatActivity {
    private static final String FILE_NAME = "hack.jar";
    private static final String TAG = "ReadJarActivity";
    private static final String target = "com/bumptech/glide/RequestManager.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_jar);
        findViewById(R.id.read_file).setOnClickListener(view -> readJarFromFile());
    }

    private void readJarFromFile() {

        String path = getFilesDir() + File.separator + FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            try {
                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    Log.e(TAG, "readJarFromFile: " + entry.getName());
                    if (target.equals(entry.getName())) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if (Tools.copyFileFromAssetsToBox(FILE_NAME, this)) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                readJarFromFile();
            }
        }
    }
}
