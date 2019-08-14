package home.smart.fly.animations.ui.activity;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.common.internal.ByteStreams;
import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class ReadJarActivity extends AppCompatActivity {
    private static final String FILE_NAME = "hack.jar";
    private static final String FILE_NAME_COPY = "hack_copy.jar";
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

        String copy = getFilesDir() + File.separator + FILE_NAME_COPY;
        JarOutputStream jarOutputStream;


        if (file.exists()) {
            try {
                File outJarFile = new File(copy);
                if (outJarFile.exists()) {
                    outJarFile.delete();
                }
                jarOutputStream = new JarOutputStream(new FileOutputStream(outJarFile));

                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    Log.e(TAG, "readJarFromFile: " + entry.getName());
                    String entryName = entry.getName();

                    ZipEntry zipEntry = new ZipEntry(entryName);
                    InputStream inputStream = jarFile.getInputStream(entry);
                    jarOutputStream.putNextEntry(zipEntry);

                    if (target.equals(entryName)) {
                        Log.e(TAG, "readJarFromFile: ignored " + entryName);
                    } else {
                        ByteStreams.copy(inputStream, jarOutputStream);
                    }
                    inputStream.close();
                    jarOutputStream.closeEntry();
                }
                jarOutputStream.close();
                jarFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "readJarFromFile fail :\n" + e.getMessage());
            }
        } else {
            if (Tools.copyFileFromAssetsToBox(FILE_NAME, this)) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                readJarFromFile();
            }
        }
    }
}
