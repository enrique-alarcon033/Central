package com.yonusa.central.invitados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.zonas.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Edit_invitado extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView foto_gallery,imagen_recuperada,eliminar;
    TextView image;
    EditText nombre,telefono,email;
    Button actualizar,convertir,recuperar_image;
    LottieAnimationView loader;
    Context context;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invitado);


        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();
        String id_user = extra.getString("id_user");

        nombre = (EditText) findViewById(R.id.editTextTextPersonName3);
        telefono = (EditText) findViewById(R.id.editTextTextPersonName4);
        email = (EditText) findViewById(R.id.editTextTextPersonName5);
        actualizar = (Button) findViewById(R.id.guardar_invidato);
        image = (TextView) findViewById(R.id.image_convertida);
        convertir= (Button) findViewById(R.id.convertirimage);
        recuperar_image = (Button) findViewById(R.id.recuperar_image);
        imagen_recuperada=(ImageView) findViewById(R.id.image_recuperada);
        eliminar = (ImageView)findViewById(R.id.image_eliminar);




        loader = findViewById(R.id.loader_loguin);
        foto_gallery = (ImageView)findViewById(R.id.image_user);
        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        convertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String base = "Base64 string values of some image";
             //

                Bitmap bitmap = ((BitmapDrawable) foto_gallery.getDrawable()).getBitmap();
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    image.setText(fotoEnBase64);

                    SharedPreferences sharedPref2 =getSharedPreferences("image", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPref2.edit();
                    editor2.putString("horaSesion",fotoEnBase64);
                    editor2.commit();

                    Toast.makeText(getApplicationContext(), "image: " +fotoEnBase64, Toast.LENGTH_LONG).show();
                }
            }
        });

        recuperar_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] decodedString = Base64.decode(image.getText().toString(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                byte[] bytes=Base64.decode(image.getText().toString(),Base64.DEFAULT);
                // Initialize bitmap
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                // set bitmap on imageView
                imagen_recuperada.setImageBitmap(bitmap);

             //   imagen_recuperada.setImageBitmap(decodedByte);
            }
        });

        try {
            loader.setVisibility(View.VISIBLE);
            obtener_usuario(id_user);
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
                    actualizar_datos_usuario(id_user,nombre.getText().toString(),telefono.getText().toString(),email.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Edit_invitado.this);
                    builder.setTitle("Eliminar Usuario "+ nombre.getText().toString());
                    builder.setMessage("¿Esta seguro de eliminar a "+ nombre.getText().toString()+"?");
                    //builder.setPositiveButton("Aceptar", null);
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            loader.setVisibility(View.VISIBLE);
                            try {
                                eliminar_usuario(id_user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        });

    }

    public boolean obtener_usuario(String iduser) throws JSONException, UnsupportedEncodingException {

        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idUsuario",iduser);

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
                "https://central.payonusa.com/api/v1/usuario/ObtenerUsuario?idUsuario="+iduser,(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

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
                                String nombred = String.valueOf(obj.get("name"));
                                String telefonod = String.valueOf(obj.get("telefono"));
                                String emaild = String.valueOf(obj.get("email"));


                                loader.setVisibility(View.INVISIBLE);

                                nombre.setText(nombred);
                               telefono.setText(telefonod);
                               email.setText(emaild);
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
    public boolean actualizar_datos_usuario(String id_user,String name, String telefono, String email) throws JSONException, UnsupportedEncodingException {

        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idUser", id_user);
        oJSONObject.put("email", email);
        oJSONObject.put("name",name);
        oJSONObject.put("telefono",telefono);


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
                "https://central.payonusa.com/api/v1/usuario/ActualizarPerfil",(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

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

    public boolean eliminar_usuario(String id_user) throws JSONException, UnsupportedEncodingException {

        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idUser", id_user);



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
                "https://central.payonusa.com/api/v1/usuario/EliminarUsuario?idUsuario="+id_user,(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

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
                                Toast.makeText(getApplicationContext(), "Usuario Eliminado", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Lista_invitados.class);
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

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            foto_gallery.setImageURI(imageUri);
        }
    }
}