package home.smart.fly.animations.activity;

import android.content.Context;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import home.smart.fly.animations.R;
import java8.util.Optional;
import java8.util.function.Consumer;

public class OptionalActivity extends AppCompatActivity implements View.OnClickListener {


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional);
        mContext = this;
        findViewById(R.id.nullValue).setOnClickListener(this);
        findViewById(R.id.notNullValue).setOnClickListener(this);
        findViewById(R.id.elseUse).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nullValue:
                // Optional.ofNullable 允许接收 null ，
                // ifPresentOrElse 当ofNullable 返回非空时，执行第一个 action ,否则执行第二个 action
                Student mStudent =null;
                Optional.ofNullable(mStudent).ifPresentOrElse(student -> Toast.makeText(mContext,
                        student.name + " " + student.age, Toast.LENGTH_SHORT).show(),
                        () -> Toast.makeText(mContext, "it is null", Toast.LENGTH_SHORT).show());

                break;
            case R.id.notNullValue:
                // Optional.of 不允许接收 null，遇到 null 会直接 throw NullPointerException
                // ifPresent of 返回为非空时，执行 ifPresent 中的 action ,否则什么都不做
                mStudent = new Student("mike", 18);
                Optional.of(mStudent).ifPresent(student -> Toast.makeText(mContext,
                        student.name + " " + student.age, Toast.LENGTH_SHORT).show());
                break;
            case R.id.elseUse:
                elseUse();
                break;
            default:
                break;
        }
    }

    private void elseUse() {
        Student student = new Student("lucy", 18);

        // 创建一个 为 Null 的 Optional
        Optional optional = Optional.empty();

        if (optional.isEmpty()) {
            System.out.println("optional is empty");
        }


        Optional<Student> studentOptional = Optional.ofNullable(student);
        // isPresent() 可以判断当前 Optional 是否为 Null
        if (studentOptional.isPresent()) {
            System.out.println("student is present");
        }
    }

    private class Student {
        public String name;
        public int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
