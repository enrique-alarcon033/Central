package com.yonusa.central.zonas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.invitados.Invitado;
import com.yonusa.central.invitados.Lista_invitados;
import com.yonusa.central.invitados.model.invitados_model;
import com.yonusa.central.loguin.login;
import com.yonusa.central.nodos.adapter.nodo_adapter;
import com.yonusa.central.nodos.model.nodo_model;
import com.yonusa.central.zonas.adapter.zonas_adapter;
import com.yonusa.central.zonas.model.zonas_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.function.ToLongBiFunction;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    private DrawerLayout drawerLayout;

    private RecyclerView mRecyclerViewAceptadas;
    private RecyclerView.Adapter mAdapterco;
    private RecyclerView.LayoutManager mLayoutManagerco;
TextView nombre,correo,hora;
    LottieAnimationView loader;
    private RecyclerView.Adapter mAdaptercoAcep;
    private RecyclerView.LayoutManager mLayoutManagercoAcep;
    //El dataset de tipo Photo
    private ArrayList<nodo_model> myDatasetCoAcep;
    Toolbar toolbar;
    Button actualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerViewAceptadas = (RecyclerView) findViewById(R.id.lista_zonas);
        mRecyclerViewAceptadas.setHasFixedSize(true);
        mLayoutManagercoAcep = new GridLayoutManager(getApplication(), 1);
        mRecyclerViewAceptadas.setLayoutManager(mLayoutManagercoAcep);
        actualizar = (Button) findViewById(R.id.actualizar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //loader.setVisibility(View.INVISIBLE);
                startActivity(intent);
                finish();
            }
        });

        loader = findViewById(R.id.loader_loguin);

       // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        getSupportActionBar().setTitle("Central de Monitoreo");

        try {
            loader.setVisibility(View.VISIBLE);
            obtener_nodos();
           // loader.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

      //  MenuItem menuItem = navigationView.getMenu().getItem(0);
        //onNavigationItemSelected(menuItem);
       // menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);

        View header = navigationView.getHeaderView(0);
        correo=(TextView) header.findViewById(R.id.correo_header);
        nombre= (TextView) header.findViewById(R.id.header_title_name);
        SharedPreferences misPreferencias = getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
        String named =  misPreferencias.getString("name","0");
        String correod = misPreferencias.getString("correo","0");

        correo.setText(correod);
        nombre.setText(named);

        navigationView.setItemIconTintList(null);

    }

    private void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });
    }

    private void refreshDataset_aceptadas () {
        if (mRecyclerViewAceptadas == null)
            return;

        if (mAdaptercoAcep == null) {
            mAdaptercoAcep = new nodo_adapter(getApplication(), myDatasetCoAcep);
            mRecyclerViewAceptadas.setAdapter(mAdaptercoAcep);
        } else {
            mAdaptercoAcep.notifyDataSetChanged();
        }
    }


    public boolean obtener_nodos() throws JSONException, UnsupportedEncodingException {
        myDatasetCoAcep = new ArrayList<nodo_model>();

        SharedPreferences misPreferencias = getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);

        String id_user =  misPreferencias.getString("usuarioId","0");
        String idNet = misPreferencias.getString("idNet","0");
        String token =  misPreferencias.getString("accessToken","0");
        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
       // oJSONObject.put("usuarioId",id_user);
        //   oJSONObject.put("coordenates",_contrasena);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  oEntity.setContentEncoding(new BasicHeader(HttpHeaders.AUTHORIZATION,  token));

        //  Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //    Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //      oEntity.setContentType("Authorization", "Bearer "+token);

        AsyncHttpClient oHttpClient = new AsyncHttpClient();
        oHttpClient.addHeader(
                "Authorization",
                token);
        //cambiar varible
        RequestHandle requestHandle = oHttpClient.get(getApplicationContext(),
                "https://central.payonusa.com/api/v1/zonas/obtenerNodos?idNet="+idNet,(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

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

                            JSONArray jsonArray = obj.getJSONArray("idNodos");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                try {
                                    JSONObject jsonObjectHijo = jsonArray.getJSONObject(i);
                                    if (jsonObjectHijo != null) {
                                        //Armamos un objeto Photo con el Title y la URL de cada JSONObject
                                        nodo_model photo = new nodo_model();

                                        if (jsonObjectHijo.has("idNodo"))
                                            photo.setIdNodo(jsonObjectHijo.getString("idNodo"));

                                        if (jsonObjectHijo.has("statusNodo"))
                                            photo.setStatusNodo(jsonObjectHijo.getString("statusNodo"));

                                        if (jsonObjectHijo.has("alertas"))
                                            photo.setAlertas(jsonObjectHijo.getString("alertas"));

                                        if (jsonObjectHijo.has("mensaje"))
                                            photo.setMensaje(jsonObjectHijo.getString("mensaje"));

                                        if (jsonObjectHijo.has("batery"))
                                            photo.setBateria(jsonObjectHijo.getString("batery"));



                                        //Agreagamos el objeto Photo al Dataset{

                                        myDatasetCoAcep.add(photo);


                                    }
                                    //   Toast.makeText(getApplicationContext(), String.valueOf(jsonObjectHijo), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    Log.e("Parser JSON", e.toString());
                                }finally {
                                    //Finalmente si hemos cargado datos en el Dataset
                                    // entonces refrescamos
                                    //progressplanes.setVisibility(View.GONE);
                                    if (myDatasetCoAcep.size() > 0){
                                        //  texto2.setVisibility(View.VISIBLE);
                                        //   aceptadas_s.setVisibility(View.GONE);
                                        refreshDataset_aceptadas();
                                        loader.setVisibility(View.INVISIBLE);

                                    }
                                    else{
                                        //  texto2.setVisibility(View.VISIBLE);
                                        mRecyclerViewAceptadas.setVisibility(View.GONE);
                                        //     aceptadas_s.setVisibility(View.GONE);
                                    }
                                }
                            }
                            //  Toast.makeText(getApplicationContext(), obj.getString("listConsents"), Toast.LENGTH_LONG).show();
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

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true; /** true -> el menú ya está visible */
    }



    public boolean Cerrar_sesion(String id_user) throws JSONException, UnsupportedEncodingException {

        SharedPreferences misPreferencias = getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);


        String id_token = misPreferencias.getString("token_noti", "0");
        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idUsuario", id_user);

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
                "https://central.payonusa.com/api/v1/cerrarSesion?idUsuario="+id_user,(HttpEntity) oEntity, "application/json" ,new AsyncHttpResponseHandler() {

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
                                loader.setVisibility(View.INVISIBLE);
                                SharedPreferences sharedPref2 =getSharedPreferences("Datos_acceso",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPref2.edit();
                                editor2.putString("correo", "");
                                editor2.putString("idUser","");
                                editor2.putString("idRol","");
                                editor2.putString("isAdmin","");
                                editor2.putString("name","");
                                editor2.putString("idNet","");
                                editor2.putString("horaSesion","");
                                editor2.commit();
                                // Toast.makeText(getApplicationContext(), "Sesión Cerrada", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), login.class);
                                startActivity(intent);
                                finish();
                            }

                            if (valor.equals("-1")){
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        SharedPreferences misPreferencias = getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
        String id_rol =  misPreferencias.getString("idRol","0");
        String id_user =  misPreferencias.getString("idUser","0");

        switch (menuItem.getItemId()) {
            case R.id.nav_agregar:
                if (id_rol.equals("1")){
                    Intent intent = new Intent(getApplicationContext(), Invitado.class);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permiso Denegado");
                    builder.setMessage("Este usuario no puede realizar esta acción por favor comunicate con un administrador de la Red");
                    builder.setPositiveButton("Aceptar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.nav_lista:
                if (id_rol.equals("1")){
                    Intent intent = new Intent(getApplicationContext(), Lista_invitados.class);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permiso Denegado");
                    builder.setMessage("Este usuario no puede realizar esta acción por favor comunicate con un administrador de la Red");
                    builder.setPositiveButton("Aceptar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;

            case R.id.nav_cerrar:
                try {
                    Cerrar_sesion(id_user);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.nav_notificaciones:
                Toast.makeText(getApplicationContext(), "Proximamente", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_permisos:
                Toast.makeText(getApplicationContext(), "Proximamente", Toast.LENGTH_LONG).show();
                break;

            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }



        //setTitle(getString(title));

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }


}