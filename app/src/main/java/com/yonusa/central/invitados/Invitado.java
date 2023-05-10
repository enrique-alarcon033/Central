package com.yonusa.central.invitados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.zonas.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Invitado extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText nombre,telefono,email,password,tipo;
    TextView rol;
    Button guardar;
    LottieAnimationView loader;
    String[] bankNames={"INVITADO","MONITORISTA"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitado);



        nombre = (EditText) findViewById(R.id.editTextTextPersonName3);
        telefono = (EditText) findViewById(R.id.editTextTextPersonName4);
        email = (EditText) findViewById(R.id.editTextTextPersonName5);
        password = (EditText) findViewById(R.id.editTextTextPersonName6);
        rol = (TextView) findViewById(R.id.id_rol);
        tipo= (EditText) findViewById(R.id.tipo_usuario);
        guardar= (Button) findViewById(R.id.guardar_invidato);

        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);

//Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        loader = findViewById(R.id.loader_loguin);

        SharedPreferences misPreferencias = getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
        String id_user = misPreferencias.getString("idUser", "0");
        String id_net= misPreferencias.getString("idNet","0");

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (nombre.getText().length()>0&&telefono.getText().length()>0&&email.getText().length()>0&&password.getText().length()>0)
                    {
                        loader.setVisibility(View.VISIBLE);
                        guardar_invitado(nombre.getText().toString(),password.getText().toString(),email.getText().toString(),
                                telefono.getText().toString(),id_net,id_user,rol.getText().toString());
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Favor de llenar todos los campos", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public boolean guardar_invitado(String nombre,String password, String email,String telefono,String idNet, String idAdmin,String tipo_user) throws JSONException, UnsupportedEncodingException {

        SharedPreferences misPreferencias = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);


        String id_user = misPreferencias.getString("User_id", "0");
        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("name", nombre);
        oJSONObject.put("password", password);
        oJSONObject.put("email", email);
        oJSONObject.put("telefono",telefono);
        oJSONObject.put("idRol",tipo_user);
        oJSONObject.put("idNet",idNet);
        oJSONObject.put("idAdmin",idAdmin);
        //   oJSONObject.put("coordenates",_contrasena);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  oEntity.setContentEncoding(new BasicHeader(HttpHeaders.AUTHORIZATION,  token));

        //  Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //    Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //      oEntity.setContentType("Authorization", "Bearer "+token);

        AsyncHttpClient oHttpClient = new AsyncHttpClient();
        //cambiar varible
        RequestHandle requestHandle = oHttpClient.post(getApplicationContext(),
                "https://central.payonusa.com/api/v1/usuario/RegistrarUsuario",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started

                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody){
                        System.out.println(statusCode);
                        System.out.println(responseBody);
                        //     mMap = googleMap;d

                        try {
                            String content = new String(responseBody, "UTF-8");
                            JSONObject obj = new JSONObject(content);
                            String valor = String.valueOf(obj.get("code"));

                            if (valor.equals("0")){
                                Toast.makeText(getApplicationContext(), "Usuario Registrado.", Toast.LENGTH_LONG).show();
                                loader.setVisibility(View.INVISIBLE);
                                finish();
                            }

                            if (valor.equals("-4")){
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Revisa tu usuario o contraseña.", Toast.LENGTH_LONG).show();
                            }

                            if (valor.equals("-7")){
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Mac no valida, este usuario no tiene permisos de registrar nuevos usuarios.", Toast.LENGTH_LONG).show();
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
                        loader.setVisibility(View.INVISIBLE);
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "404 !", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "500 !", Toast.LENGTH_LONG).show();
                            //sin_tarjetas();
                        } else if (statusCode == 403) {
                            Toast.makeText(getApplicationContext(), "403 !", Toast.LENGTH_LONG).show();
                        } else {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), bankNames[position], Toast.LENGTH_LONG).show();
        tipo.setText(bankNames[position]);
        if (position==0){
            rol.setText("2");
        }else{
            rol.setText("3");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}