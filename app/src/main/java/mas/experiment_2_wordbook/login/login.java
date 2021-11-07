package mas.experiment_2_wordbook.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import mas.experiment_2_wordbook.MainActivity;
import mas.experiment_2_wordbook.R;

public class login extends AppCompatActivity {
    private EditText name;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        name=findViewById(R.id.name);
        login=findViewById(R.id.login);
        //register=findViewById(R.id.re+++gister);
    }
    public void onClick(View view){
        SharedPreferences mySharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("author", name.getText().toString());
        editor.putString("age", "20");
        editor.putString("like", "学习");

        //提交当前数据
        editor.commit();
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
    }


}
