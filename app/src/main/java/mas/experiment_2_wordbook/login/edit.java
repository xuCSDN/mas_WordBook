package mas.experiment_2_wordbook.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mas.experiment_2_wordbook.MainActivity;
import mas.experiment_2_wordbook.R;

public class edit extends AppCompatActivity {

    private EditText author;
    private EditText age;
    private EditText like;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String aName;
    private String sexual;
    private String ages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        author=findViewById(R.id.edt_setusername);
        like = findViewById(R.id.like);
        age=findViewById(R.id.edt_setage);

        aName = sharedPreferences.getString("author", null);
        sexual = sharedPreferences.getString("like", null);
        ages=sharedPreferences.getString("age",null);

        if(aName.equals("")){
            author.setText("许庆胜");
        }
        else{
            author.setText(aName);
        }

        like.setText(sexual);
        age.setText(ages);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAuthorInfo();
    }

    /**
     * 保存到sharedPreferences中
     */
    private void saveAuthorInfo(){
        //save
        editor.putString("author", author.getText().toString());
        editor.putString("age", age.getText().toString());
        editor.putString("like", like.getText().toString());
        editor.commit();
    }
}
