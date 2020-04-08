package com.neu.test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class PDFActivity extends AppCompatActivity {
    private static final String TAG = "PDFActivity";
    private List<DetectionResult> resultData;
    private com.github.barteksc.pdfviewer.PDFView pdfView;
    private Button btn_pdf;
    private Task task = new Task();
    private Toolbar mToolbar;
    private TextView toolbar_title;
    private TextView toolbar_subtitleRight;
    private TextView toolabr_subtitleLeft;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        initview();
        initToolbar();

        btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PDFActivity.this,FragmentManagerActivity.class);
//                intent.putExtra("taskType",j);
//                intent.putExtra("hegeFlag",hegeFlag);
//                intent.putExtra("position",dataFlag);
//                finish();
//                startActivity(intent);
                getDataBypost();

            }
        });

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("1", "print", 1200, 1200))
                .setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0))
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR) // 可以不传
                //.setDuplexMode(PrintAttributes.DUPLEX_MODE_NONE)  // API小于23会报错，可以不传
                .build();
        PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PDFActivity.this, attributes);

        // 绘制PDF
        PdfDocument.Page page = pdfDocument.startPage(0);   // 开始页，页号从0开始
        PdfDocument.PageInfo pageInfo = page.getInfo(); // 获取页信息，可以根据长宽来排版
        Canvas canvas = page.getCanvas();
        int titleBaseLine = 10;
        int leftMargin = 25;
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(11);//36
        for ( int i = 0 ;i<resultData.size();i++){
            String text1 = null;
            text1 = resultData.get(i).getCHECKCONTENT();
            canvas.drawText(text1, leftMargin, titleBaseLine, paint);
            String text2 = null;

            //进行选项状态的判断 如果前一个界面没有点击是否合格的选项则设置为空
            if (resultData.get(i).getSTATUS()==null){
                text2 = " ";
            }else {
                text2 = resultData.get(i).getSTATUS();
            }

            titleBaseLine = titleBaseLine + 11;
            canvas.drawText(text2, leftMargin, titleBaseLine, paint);
            titleBaseLine = titleBaseLine + 11;
        }
        float top = titleBaseLine +11;
        float left = leftMargin;
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.mipmap.tab_5);
        canvas.drawBitmap(bitmap,left,top,paint);
        //paint.setTextSize(11);
        //canvas.drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint);
        //paint.setColor(Color.BLUE);
        //canvas.drawRect(100, 100, 172, 172, paint);
        pdfDocument.finishPage(page);  // 结束页

// 输出到文件
        String srtImgPath = Environment.getExternalStorageDirectory() +"/DCIM/"+LoginActivity.inputName+"/PDF";
        String pdfName = System.currentTimeMillis() + "PDF" + ".pdf";
        File appDir = new File(srtImgPath);
        if (!appDir.exists()) { //如果目录不存在就创建
            appDir.mkdir();
        }
        try {
            File file = new File(srtImgPath, pdfName);
            FileOutputStream outputStream = new FileOutputStream(file);
            pdfDocument.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
            pdfView.fromFile(new File(file.getAbsolutePath())).load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar_pdf);
        toolbar_title = findViewById(R.id.toolbar_pdf_title);
//        toolabr_subtitleLeft = findViewById(R.id.toolbar_pdf_subtitle_left);
//        toolbar_subtitleRight = findViewById(R.id.toolbar_pdf_subtitle_right);
        toolbar_title.setTextSize(18);
//        toolabr_subtitleLeft.setTextSize(13);
//        toolbar_subtitleRight.setTextSize(13);
        toolbar_title.setText(task.getDEVID());//加空格的原因是让其显示居中
//        toolabr_subtitleLeft.setText(title);
//        toolbar_subtitleRight.setText(taskType);
//        toolbar_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initview() {
        pdfView = findViewById(R.id.pdfView);
        btn_pdf = findViewById(R.id.btn_pdf);

        resultData = (List<DetectionResult>) getIntent().getSerializableExtra("listData");
        task  = (Task) getIntent().getSerializableExtra("task");
        Log.e(TAG, " id: "+task.getTASKID() );
        final int dataFlag = getIntent().getIntExtra("position",0);
        final int j = getIntent().getIntExtra("taskType",0);
        final int hegeFlag = getIntent().getIntExtra("hegeFlag",0);

    }


    public void textTransformPdf(String content, String pdf_save_address) {
        Document doc = new Document();// 创建一个document对象
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(pdf_save_address); // pdf_address为Pdf文件保存到sd卡的路径
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.setPageCount(1);
            doc.add(new Paragraph(content, setChineseFont())); // result为保存的字符串            // ,setChineseFont()为pdf字体
            // 一定要记得关闭document对象
            doc.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public Font setChineseFont() {
        BaseFont bf = null;
        Font fontChinese = null;
        try {            // STSong-Light : Adobe的字体            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            fontChinese = new Font(bf, 12, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fontChinese;
    }

    public void imgTransformPdf(String[] imgPaths, String pdf_save_address) {
        Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
        try {            //获取PDF书写器
            PdfWriter.getInstance(doc, new FileOutputStream(pdf_save_address));            //打开文档
            doc.open();            //图片对象
            Image img = null;            //遍历
            for (int i = 0; i < imgPaths.length; i++) {
                //获取图片
                img = Image.getInstance(new URL(imgPaths[i]));                //使图片与A4纸张大小自适应
                img.scaleToFit(new Rectangle(PageSize.A4));                //添加到PDF文档
                doc.add(img);                //下一页，每张图片一页
                doc.newPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {            //关闭文档
            doc.close();
        }
    }

    private void getDataBypost() {
        String url = BaseUrl.BaseUrl+"getTaskServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("username",LoginActivity.inputName);
//        map.put("password",inputPassword);
        Log.e(TAG,"map: "+ map.toString());


        String gson = new Gson().toJson(map);
        Log.e(TAG,"gson: "+ gson);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, gson, new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.toString());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                Toasty.warning(PDFActivity.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("获取任务成功")){

                    Toasty.success(PDFActivity.this,"登陆成功!",Toast.LENGTH_LONG,true).show();
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
//                        Toast.makeText(LoginActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
                    Toast.makeText(PDFActivity.this,"成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PDFActivity.this,FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    Log.e("TAG"," tasks: "+tasks.size());
                    intent.putExtra("userName",LoginActivity.inputName);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toasty.error(PDFActivity.this,"用户名或密码错误!",Toast.LENGTH_LONG,true).show();

                }
            }
        });
    }
}
