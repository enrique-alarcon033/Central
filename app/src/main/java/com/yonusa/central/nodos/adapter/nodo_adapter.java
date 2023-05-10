package com.yonusa.central.nodos.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.nodos.model.nodo_model;
import com.yonusa.central.zonas.Update;
import com.yonusa.central.zonas.model.zonas_model;
import com.yonusa.central.zonas.model.zonasm;
import com.yonusa.central.zonas.zonas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * Created by javiexpo on 26/7/16.
 */
public class nodo_adapter extends RecyclerView.Adapter<nodo_adapter.ViewHolder>  {
    private Context mContextCol;
    private ArrayList<nodo_model> mDatasetcol;
    String codigo;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
            TextView idNet,idNodo,estatus,alerta,mensaje,img_estatus,alerta_img,bateria;
        Context context;
        ImageView btn,config,maps;
        ConstraintLayout vista;
        RecyclerView lista;

        CardView tarjeta;
        public Transition transition;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();
           // contenido = (TextView) v.findViewById(R.id.Nombre_Categoria);
           // iduser =(TextView) v.findViewById(R.id.Virtual_Categoria_id);
           // idNet = (TextView) v.findViewById(R.id.id_net);
            idNodo = (TextView) v.findViewById(R.id.id_nodo);
            estatus=(TextView)v.findViewById(R.id.estatus_nodo);
            alerta = (TextView) v.findViewById(R.id.alertas_nodo);
            mensaje=(TextView) v.findViewById(R.id.mensaje_nodo);
            img_estatus=(TextView) v.findViewById(R.id.textView11_ic);
            alerta_img = (TextView) v.findViewById(R.id.alertas_nodo_ic);
            vista = (ConstraintLayout) v.findViewById(R.id.layout_tarjeta);
            tarjeta =(CardView) v.findViewById(R.id.tarjeta_vista);
            bateria = (TextView) v.findViewById(R.id.img_bateria);

        }

        void setOnClickListener()    {
            tarjeta.setOnClickListener(this);
         //   config.setOnClickListener(this);
        //    maps.setOnClickListener(this);
        }



      @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tarjeta_vista:

                    if (estatus.getText().equals("1")){
                        Intent i = new Intent(context, zonas.class);
                        i.putExtra("idNodo", idNodo.getText());
                        context.startActivity(i);

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Dispositivo Desconectado");
                        builder.setMessage("Este dispositivo esta fuera de linea. Para acceder a los controles necesita estar en linea");
                        builder.setPositiveButton("Aceptar", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }


                    break;

                case R.id.config:

                    break;

                case  R.id.maps:

                        String labelLocation = "Titulo: Si se quiere anexar algo de texto aquí";
                        String uri = "geo:<" + 1.21212+ ">,<" + 19.2324232+ ">?q=<" + 1.213122+ ">,<" + 1.23232+ ">(" + labelLocation + ")";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        context.startActivity(intent);

                    break;
            }
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public nodo_adapter(Context contextcol, ArrayList<nodo_model> myDatasetco) {
       // misZonas = miszonas;
        mDatasetcol = myDatasetco;
        mContextCol= contextcol;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nodos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//////////
        /////////////
        //////////////


     //   Glide.with(mContextCol).load(mDatasetcol.get(position).getImageUrlco()).into(holder.mImgtViewco);
     //   holder.idNet.setText(mDatasetcol.get(position).getIdNet());
        holder.idNodo.setText(mDatasetcol.get(position).getIdNodo());
        holder.estatus.setText(mDatasetcol.get(position).getStatusNodo());
        holder.alerta.setText(mDatasetcol.get(position).getAlertas());
        holder.mensaje.setText(mDatasetcol.get(position).getMensaje());

        holder.bateria.setText(mDatasetcol.get(position).getBateria());

        if (holder.bateria.getText().equals("1")){
            holder.bateria.setBackgroundResource(R.drawable.bateria_null);
            holder.bateria.setText("");
        }else{
            holder.bateria.setText("");
        }

        if(holder.mensaje.getText().equals("")){
            holder.mensaje.setText("Zonas Trabajando Correctamente");
            holder.vista.setBackgroundResource(R.color.purple_200);
            holder.alerta.setVisibility(View.GONE);
            holder.alerta_img.setVisibility(View.GONE);
        }

        if(holder.estatus.getText().equals("1")){
            int alert = Integer.parseInt(holder.alerta.getText().toString());
            if (alert>=1){
               holder.vista.setBackgroundResource(R.color.alerta);
            }
            //holder.vista.setBackgroundResource(R.color.alerta);
            //holder.btn.setImageResource(R.drawable.ic_red);
            //holder.idZona.setTextColor(R.color.white);
        }else if(holder.estatus.getText().equals("0")){
            holder.img_estatus.setBackgroundResource(R.drawable.ic_wifi_no);
            holder.vista.setBackgroundResource(R.color.gris);
        }else if(holder.estatus.getText().equals("")){
           // holder.btn.setImageResource(R.drawable.ic_lite);
        }




        holder.setOnClickListener();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetcol.size();
    }


    public boolean actualizar_datos_zona(String net,String nodo, String zona,String estado) throws JSONException, UnsupportedEncodingException {
        SharedPreferences misPreferencias =mContextCol.getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
        String id_user =  misPreferencias.getString("idUser","0");
        String aplicacion = "application/json";
        JSONObject oJSONObject = new JSONObject();
        oJSONObject.put("idNet",net);
        oJSONObject.put("idNodo",nodo);
        oJSONObject.put("idZona",zona);
        oJSONObject.put("state",estado);
        oJSONObject.put("idUsuario",id_user);

        //   oJSONObject.put("coordenates",_contrasena);
        ByteArrayEntity oEntity = new ByteArrayEntity(oJSONObject.toString().getBytes("UTF-8"));
        oEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  oEntity.setContentEncoding(new BasicHeader(HttpHeaders.AUTHORIZATION,  token));

        //  Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //    Toast.makeText(getApplicationContext(), oEntity.toString(), Toast.LENGTH_LONG).show();
        //      oEntity.setContentType("Authorization", "Bearer "+token);

        AsyncHttpClient oHttpClient = new AsyncHttpClient();
        //cambiar varible
        RequestHandle requestHandle = oHttpClient.put(mContextCol,
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
                              //  Toast.makeText(mContextCol, "Datos Actualizados", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(mContextCol, "Error en la conexión por favor intentalo mas tarde   "+obj.get("message"), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(mContextCol, "404 !", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(mContextCol, "500 !", Toast.LENGTH_LONG).show();
                            //sin_tarjetas();
                        } else if (statusCode == 403) {
                            Toast.makeText(mContextCol, "403 !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContextCol, error.toString(), Toast.LENGTH_LONG).show();
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

    public static void startSound(final Context context, final int sound, final float volume){

        new Thread(() -> {
            MediaPlayer mp = MediaPlayer.create(context, sound);
            mp.setVolume(volume,volume);
            mp.start();
        }).start();

    }
}
