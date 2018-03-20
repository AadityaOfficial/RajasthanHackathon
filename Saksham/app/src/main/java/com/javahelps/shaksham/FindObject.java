package com.javahelps.shaksham;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.master.permissionhelper.PermissionHelper;
import com.javahelps.shaksham.Model;
import com.javahelps.shaksham.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
Activity Called for find Object
 */
public class FindObject extends AppCompatActivity {

    RelativeLayout speak;
    Button btSubmit;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    HashMap<Integer, Model> questions;
    Intent callingIntent;
    int quesNo = 0;
    int caller = 0;
    int learn = 0;

    TextView question ;
    String finalAns;
    TextToSpeech t1;
    ImageView cameraBt , half_circle , speaker;
    File mFile;
    Bitmap bitmap;
    PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_object);

        question = (TextView)findViewById(R.id.question_box);
        btSubmit = (Button)findViewById(R.id.submitButton);
        cameraBt = (ImageView)findViewById(R.id.btnCapture);

        half_circle = (ImageView)findViewById(R.id.iw_half_circle);
        speaker = (ImageView)findViewById(R.id.iv_speaker);
        callingIntent = getIntent();
        quesNo = callingIntent.getIntExtra("ques", 1);
        caller = callingIntent.getIntExtra("caller", 0);
        learn = callingIntent.getIntExtra("learn",0);
        speak = (RelativeLayout)findViewById(R.id.question_box_speach);

        initializeQuestions();
        caller = getIntent().getIntExtra("caller", 0);
        learn = getIntent().getIntExtra("learn",0);

        quesNo = getIntent().getIntExtra("ques", 1);

        if (caller == 4 && learn == 0){
            half_circle.setVisibility(View.GONE);
            speaker.setVisibility(View.GONE);
            question.setText(questions.get(quesNo).question.toString());
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("en","IN"));
                }
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toSpeak = questions.get(quesNo).question;
//                    Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPermissionHelper = new PermissionHelper(FindObject.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                mPermissionHelper.request(new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {

//                        sendPic(mFile);
                    }

                    @Override
                    public void onPermissionDenied() {

                    }

                    @Override
                    public void onPermissionDeniedBySystem() {

                    }
                });
            }
        });

        cameraBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromCamera();
            }});
        FirebaseApp.initializeApp(this);

    }

    /*
    initializing questions
     */
    private HashMap<Integer , Model> initializeQuestions() {
        questions = new HashMap<>();
        Model m = new Model();
        m.question = "Find bottle";
        m.answer = "bottle";
        questions.put(1, m);
        m = new Model();
        m.question = "Find chair";
        m.answer = "chair";
        questions.put(2, m);
        m = new Model();
        m.question = "Find Orange";
        m.answer = "orange";
        questions.put(3, m);
        m = new Model();
        m.question = "Find Table";
        m.answer = "table";
        questions.put(4, m);
        return questions;
    }

    private void selectFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 0);
    }
    //getting result from camera

    String mCurrentPhotoPath;

    /*
    Camera Result parsed and saved
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            if (data != null) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
                mFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try{
                    mFile.createNewFile();
                    fo = new FileOutputStream(mFile);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

                mPermissionHelper = new PermissionHelper(FindObject.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                mPermissionHelper.request(new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        sendPic(mFile);
                    }

                    @Override
                    public void onPermissionDenied() {

                    }

                    @Override
                    public void onPermissionDeniedBySystem() {

                    }
                });
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        System.out.println(encodedImage.length());
        return encodedImage;
    }

    String url1 = "http://192.168.43.254:8079/image";//ip

    private void sendPic(File f) {

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(f));
            final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
            Map<String, String> jsonParams = new HashMap<String, String>();
            String image = getStringImage(bitmap);

            jsonParams.put("imageFile", "2" + image);
            jsonParams.put("correctAns", questions.get(quesNo).answer.toString());
            JsonObjectRequest myRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url1,
                    new JSONObject(jsonParams),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                                verificationSuccess(response);
                            loading.dismiss();
//                                Toast.makeText(SpeachQuestionActivity.this, response + "", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
//                                verificationFailed(error);
                            volleyError.printStackTrace();
                            loading.dismiss();

//                                Toast.makeText(SpeachQuestionActivity.this, "" + volleyError, Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
//                MyApplication.getInstance().addToRequestQueue(myRequest, "tag");
            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(myRequest);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    receiving result from server
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String ans = intent.getExtras().getString("ans");

            Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_SHORT);
            if(ans.equals("true")){
                Intent mintent = new Intent(FindObject.this, AcceptAnswerActivity.class);
                mintent.putExtra("caller", caller);
                mintent.putExtra("ques", quesNo);
                startActivity(mintent);
                finish();
            }else{
                Intent mintent = new Intent(FindObject.this, AnswerRejectActivity.class);
                mintent.putExtra("caller", caller);
                mintent.putExtra("ques", quesNo);
                startActivity(mintent);
                finish();
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Answer")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


}
