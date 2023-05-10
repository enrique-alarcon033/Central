package com.yonusa.central.zonas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.huella.FingerprintActivity;
import com.yonusa.central.zonas.model.zonas_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Update extends AppCompatActivity {

    EditText nombre,ubicacion_x,ubicacion_y,referencia;
    Button actualizar;
    LottieAnimationView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nombre = (EditText) findViewById(R.id.nombre_zona);
        ubicacion_x = (EditText) findViewById(R.id.ubicacion_x);
        ubicacion_y = (EditText) findViewById(R.id.ubicacion_y);
        referencia = (EditText) findViewById(R.id.referencia);

        actualizar = (Button) findViewById(R.id.btn_update_zona);

        loader = findViewById(R.id.loader_loguin);

        String id_net = getIntent().getStringExtra("id_net");
        String id_nodo = getIntent().getStringExtra("id_nodo");
        String id_zona = getIntent().getStringExtra("id_zona");

        try {
            obtener_datos_zona(id_net,id_nodo,id_zona);
            loader.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loader.setVisibility(View.VISIBLE);
                    actualizar_datos_zona(id_net,id_nodo,id_zona,nombre.getText().toString(),ubicacion_x.getText().toString(),ubicacion_y.getText().toString(),referencia.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean obtener_datos_zona(String net,String nodo, String zona) throws JSONException, UnsupportedEncodingException {

        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idNet",net);
        oJSONObject.put("idNodo", nodo);
        oJSONObject.put("idZona",zona);
        //   oJSONObject.put("coordenates",_contrasena);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  oEntity.setContentEncoding(new BasicHeader(HttpHeaders.AUTHORIZATION,  token));

        //  Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //    Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //      oEntity.setContentType("Authorization", "Bearer "+token);

        AsyncHttpClient oHttpClient = new AsyncHttpClient();
        //cambiar varible
        RequestHandle requestHandle = oHttpClient.get(getApplicationContext(),
                "https://central.payonusa.com/api/v1/zona/ObtenerZona",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started

                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                        System.out.println(statusCode);
                        System.out.println(responseBody);

                        try {
                            String content = new String(responseBody, "UTF-8");
                            JSONObject obj = new JSONObject(content);
                            String valor = String.valueOf(obj.get("code"));

                            if (valor.equals("0")){
                                String nombred = String.valueOf(obj.get("nombre"));
                                String cordxd = String.valueOf(obj.get("coordinateX"));
                                String cordyd = String.valueOf(obj.get("coordinateY"));
                                String referenciad = String.valueOf(obj.get("nombreReferencia"));
                                String stated = String.valueOf(obj.get("state"));

                                loader.setVisibility(View.INVISIBLE);

                                nombre.setText(nombred);
                                ubicacion_x.setText(cordxd);
                                ubicacion_y.setText(cordyd);
                                referencia.setText(referenciad);

                            }

                            if (valor.equals("-2")){
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                            }
                            // Toast.makeText(getApplicationContext(), String.valueOf(names), Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (statusCode == 404) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "404 !", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "500 !", Toast.LENGTH_LONG).show();
                            //sin_tarjetas();
                        } else if (statusCode == 403) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "403 !", Toast.LENGTH_LONG).show();
                        } else {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public boolean getUseSynchronousMode() {
                        return false;
                    }
                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        System.out.println(retryNo);
                    }
                });

        return false;
    }

    public boolean actualizar_datos_zona(String net,String nodo, String zona,String nombre,String cordX,String cordY,String ref) throws JSONException, UnsupportedEncodingException {

        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idNet",net);
        oJSONObject.put("idNodo",nodo);
        oJSONObject.put("idZona",zona);
        oJSONObject.put("nombre",nombre);
        oJSONObject.put("coordinateX",cordX);
        oJSONObject.put("coordinateY",cordY);
        oJSONObject.put("nombreReferencia",ref);

        //   oJSONObject.put("coordenates",_contrasena);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  oEntity.setContentEncoding(new BasicHeader(HttpHeaders.AUTHORIZATION,  token));

        //  Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //    Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //      oEntity.setContentType("Authorization", "Bearer "+token);

        AsyncHttpClient oHttpClient = new AsyncHttpClient();
        //cambiar varible
        RequestHandle requestHandle = oHttpClient.put(getApplicationContext(),
                "https://central.payonusa.com/api/v1/zona/ActualizarZona",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started

                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                        System.out.println(statusCode);
                        System.out.println(responseBody);

                        try {
                            String content = new String(responseBody, "UTF-8");
                            JSONObject obj = new JSONObject(content);
                            String valor = String.valueOf(obj.get("code"));

                            if (valor.equals("0")){
                                Toast.makeText(getApplicationContext(), "Datos Actualizados", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                loader.setVisibility(View.INVISIBLE);
                                startActivity(intent);
                                finish();
                            }else{
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Error en la conexión por favor intentalo mas tarde   "+obj.get("message"), Toast.LENGTH_LONG).show();
                            }
                            // Toast.makeText(getApplicationContext(), String.valueOf(names), Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        if (statusCode == 404) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "404 !", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "500 !", Toast.LENGTH_LONG).show();
                            //sin_tarjetas();
                        } else if (statusCode == 403) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "403 !", Toast.LENGTH_LONG).show();
                        } else {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public boolean getUseSynchronousMode() {
                        return false;
                    }
                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                        System.out.println(retryNo);
                    }
                });

        return false;
    }
}