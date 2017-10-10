package io.github.bleoo.buttergun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.BindView;

import io.github.bleoo.butter.Butter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindView(R.id.tv_test1)
    TextView tv_test1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Butter.bind(this);

        tv_test.setText("ehhehh");
        tv_test1.setText("avafafafa");
    }
}
