package mas.experiment_2_wordbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import mas.experiment_2_wordbook.DB.initialization_dao;
import mas.experiment_2_wordbook.diary.diary;
import mas.experiment_2_wordbook.diary.update;
import mas.experiment_2_wordbook.diary.adapter;
import mas.experiment_2_wordbook.login.edit;
import mas.experiment_2_wordbook.login.login;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private mas.experiment_2_wordbook.diary.adapter adapter;
    private Button write;
    private Button close;
    private Button edit;
    private mas.experiment_2_wordbook.DB.dao dao;
    private ArrayList<diary> list = new ArrayList<>();//用来存放数据的数组
    private ArrayList<diary> newList = new ArrayList<>();//用来存放数据的数组
    private int i=0;
    String author="xqs";
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        write=findViewById(R.id.write);
        close=findViewById(R.id.close);
        edit=findViewById(R.id.edit2);
        DbUtil();
        text=findViewById(R.id.text);


        SharedPreferences sharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE);
        if(!sharedPreferences.getString("author", "").equals("")){
            author =sharedPreferences.getString("author", "");
        }

        //显示lisview
        list=dao.find(author);
        adapter = new adapter(MainActivity.this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, update.class);
                i=position;
                intent.putExtra("d_author",list.get(i).getAuthor());
                intent.putExtra("d_date",list.get(i).getDate());
                intent.putExtra("d_title",list.get(i).getTitle());
                intent.putExtra("d_content",list.get(i).getContent());
                intent.putExtra("d_image",list.get(i).getImage());
                startActivityForResult(intent, i);
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, mas.experiment_2_wordbook.diary.write.class);
                intent.putExtra("author",author);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, edit.class);
                intent.putExtra("author",author);
                startActivity(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //删除对应的item索引
                dao.delete(list.get(position));
                list.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==i){
                Bundle bundle = data.getExtras();
                diary d=list.get(i);
                d.setTitle(bundle.getString("title"));
                d.setContent(bundle.getString("content"));
                d.setImage(bundle.getString("image"));
                dao.update(d);
                //使用新的容器获得最新查询出来的数据
                newList = dao.find(author);
                //清除原容器里的所有数据
                list.clear();
                //将新容器里的数据添加到原来容器里
                list.addAll(newList);
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 初始化dao
     */
    public void DbUtil(){
        dao = ((initialization_dao)this.getApplication()).getDao();
    }


    /**
     * 当页面回到此活动时使用+此方法刷新ListView
     */
    @Override
    protected void onResume() {
        super.onResume();
        //使用新的容器获得最新查询出来的数据
        newList = dao.find(author);
        //清除原容器里的所有数据
        list.clear();
        //将新容器里的数据添加到原来容器里
        list.addAll(newList);
        //刷新适配器
        adapter.notifyDataSetChanged();
    }


}