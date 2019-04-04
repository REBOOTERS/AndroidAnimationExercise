package home.smart.fly.animations.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import home.smart.fly.animations.R;

public class ClipBoardActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ClipBoardActivity";
    private AppCompatEditText copyEdit, pasteEdit;
    private ClipboardManager mClipboardManager;
    private ClipData mClipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_board);

        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        findViewById(R.id.copy).setOnClickListener(this);
        findViewById(R.id.paste).setOnClickListener(this);
        copyEdit = findViewById(R.id.copyEdit);
        pasteEdit = findViewById(R.id.pasteEdit);

        mClipboardManager.addPrimaryClipChangedListener(() -> Log.e(TAG, "onPrimaryClipChanged: " ));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy:
                copy();
                break;
            case R.id.paste:
                paste();
                break;
            default:
                break;
        }
    }

    private void paste() {
            ClipData clipData = mClipboardManager.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);
            String content = item.getText().toString();
            pasteEdit.setText(content);

            Toast.makeText(this,"paste:"+content,Toast.LENGTH_SHORT).show();
    }

    private void copy() {
        String content = copyEdit.getText().toString().trim();
        mClipData  = ClipData.newPlainText("text",content);
        mClipboardManager.setPrimaryClip(mClipData);
        pasteEdit.setText("");
        Toast.makeText(this,"copy:"+content,Toast.LENGTH_SHORT).show();
    }
}
