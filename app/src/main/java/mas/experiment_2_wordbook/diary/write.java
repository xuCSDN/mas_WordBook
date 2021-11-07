package mas.experiment_2_wordbook.diary;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import mas.experiment_2_wordbook.DB.dbHelper;
import mas.experiment_2_wordbook.MainActivity;
import mas.experiment_2_wordbook.DB.initialization_dao;
import mas.experiment_2_wordbook.R;

public class write extends AppCompatActivity {
    private TextView author;
    private TextView date;
    private EditText title;
    private EditText content;
    private Button back;
    private Button image;
    private mas.experiment_2_wordbook.DB.dao dao;
    private Intent intent;
    private dbHelper db;
    private ImageView diary_image;

    private static final int TAKE_PICTURE_ACTION = 1;  //拍照
    private static final int CHOOSE_PICTURE_ACTION = 2;  //从相册中选择照片
    private static final int SCALE = 5;//照片缩小比例
    private String imagename2 = null;
    private  File image_file;
    private Uri imageuri;
    private String path;
    private  String imagename="";   //图片名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        author=findViewById(R.id.d_author);
        content=findViewById(R.id.d_content);
        date=findViewById(R.id.d_date);
        title=findViewById(R.id.d_title);
        back=findViewById(R.id.back);
        image=findViewById(R.id.image);
        DbUtil();
        intent=getIntent();
        author.setText(intent.getStringExtra("author"));
        date.setText((new Date()).toString());
        diary_image=findViewById(R.id.diary_picture);
        db = new dbHelper(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到两个输入框的值
                String str_title = title.getText().toString();
                String str_content = content.getText().toString();
                if(!title.equals("")){
                    diary diary=new diary();
                    diary.setTitle(str_title);
                    diary.setContent(str_content);
                    diary.setAuthor(author.getText().toString());
                    diary.setDate(date.getText().toString());
                    diary.setImage(imagename);
                    dao.insert(diary);
                }
                Intent intent = new Intent(write.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(intent);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmenu(view);
            }
        });


    }

    /**
     * 初始化dao
     */
    public void DbUtil(){
        dao = ((initialization_dao)this.getApplication()).getDao();
    }

    /**
     * 显示选项列表
     * @param view
     */
    public void showmenu(View view){
        PopupMenu menu = new PopupMenu(this,view);
        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            private int requst;
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.take_pic:
                        startCamera();
                        break;
                    case R.id.select_pic:
                        Intent select = new Intent(Intent.ACTION_GET_CONTENT);
                        requst = CHOOSE_PICTURE_ACTION;
                        select.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(select, requst);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.show();
    }


    /**
     * 启动摄像机
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startCamera() {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //创建文件
        createImageFile();
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //获取uri
        imageuri = FileProvider.getUriForFile(this, "mas.experiment_2_wordbook.fileProvider", image_file);
        System.out.println("mImageUri"+imageuri);
        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        //启动相机并要求返回结果
        startActivityForResult(intent, TAKE_PICTURE_ACTION);
    }

    /**
     * 创建图片文件
     */
    private void createImageFile(){
        //设置图片文件名（含后缀），以当前时间的毫秒值为名称
        imagename2 = Calendar.getInstance().getTimeInMillis() + ".jpg";
        //创建图片文件
        image_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "Pictures" +"/", imagename2);
        //将图片的绝对路径设置给mImagePath，后面会用到
        path = image_file.getAbsolutePath();
        //按设置好的目录层级创建
        image_file.getParentFile().mkdirs();
        //不加这句会报Read-only警告。且无法写入SD
        image_file.setWritable(true);
    }

    /**
     * 返回到上一个activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE_ACTION:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    Bitmap newBitmap = take_Image.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    diary_image.setImageBitmap(newBitmap);
                    String str=String.valueOf(System.currentTimeMillis());
                    take_Image.savePhotoToSDCard(newBitmap,getFilesDir().getAbsolutePath(), str);
                    imagename=str+".png";
                    break;

                case CHOOSE_PICTURE_ACTION:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = take_Image.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            //将处理过的图片显示在界面上，并保存到本地
                            diary_image.setImageBitmap(smallBitmap);
                            String s1=String.valueOf(System.currentTimeMillis());
                            take_Image.savePhotoToSDCard(smallBitmap,getFilesDir().getAbsolutePath(), s1);
                            imagename=s1+".png";
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
