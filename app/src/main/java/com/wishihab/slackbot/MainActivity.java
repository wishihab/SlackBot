package com.wishihab.slackbot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "https://watson-coffee-api.herokuapp.com/api/v1/menu";
    private Button btnKirim;
    private TextView txtMessage;
    private EditText txtKirim;
    static int socketTimeout = 30000;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = (TextView)findViewById(R.id.textMessageBot);
        txtKirim = (EditText)findViewById(R.id.txt_kirim);
        btnKirim = (Button)findViewById(R.id.btn_kirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtkirim = txtKirim.getText().toString().trim();
                String formenu, quantity;
                if(txtkirim.length()>=1){
                    if(txtkirim.contains("show menu")){
                        txtkirim = "show_menu";
                        formenu = "";
                        quantity = "";
                        function_sent_message(txtkirim, formenu, quantity);
                    }else if(txtkirim.contains("how much")){
                        if(txtkirim.contains("1")){
                            quantity = "1";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }else if(txtkirim.contains("2")){
                            quantity = "2";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }else if(txtkirim.contains("3")){
                            quantity = "3";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }else if(txtkirim.contains("4")){
                            quantity = "4";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }else if(txtkirim.contains("0")){
                            quantity = "0";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }else{
                            quantity = "5";
                            txtkirim = "show_price";
                            formenu = "Vanilla latte";
                            function_sent_message(txtkirim, formenu, quantity);
                        }

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void function_sent_message(final String message, final String formenu, final String quantity){

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Tunggu sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest sr_cek_nik = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("menu")){
                                txtMessage.setText(jsonObject.getString("menu"));
                            }else if(jsonObject.has("price")){
                                txtMessage.setText("Name : " + formenu + "\nQuantity : " + jsonObject.getString("quantity") +"\n"
                                        + "Price : " + jsonObject.getString("price"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Terjadi kesalahan. Silakan coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }


        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("get", message);
                params.put("for", formenu);
                params.put("quantity", quantity);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        sr_cek_nik.setRetryPolicy(getRetryPolicy());
        requestQueue.add(sr_cek_nik);
    }

    public static RetryPolicy getRetryPolicy(){
        return new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
