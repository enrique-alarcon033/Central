package com.yonusa.central.zonas.adapter;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.yonusa.central.R;
import com.yonusa.central.invitados.Invitado;
import com.yonusa.central.zonas.Update;
import com.yonusa.central.zonas.model.zonas_model;
import com.yonusa.central.zonas.model.zonasm;

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
public class zonas_adapter extends RecyclerView.Adapter<zonas_adapter.ViewHolder>  {
    private Context mContextCol;
    private ArrayList<zonas_model> mDatasetcol;
    //private ArrayList<zonasm> misZonas;
    String codigo;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
            TextView idNet,idNodo,idZona,nombre,ubicacion,estado,state,bateria,alertas;
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
            idNet = (TextView) v.findViewById(R.id.id_net);
            idNodo = (TextView) v.findViewById(R.id.id_nodo);
            idZona=(TextView)v.findViewById(R.id.id_zona);
            nombre = (TextView) v.findViewById(R.id.nombre);
            ubicacion=(TextView) v.findViewById(R.id.ubicacion);
            estado=(TextView) v.findViewById(R.id.estado);
            state = (TextView) v.findViewById(R.id.estatus_nodo);
            vista = (ConstraintLayout) v.findViewById(R.id.layout_tarjeta);
            tarjeta =(CardView) v.findViewById(R.id.tarjeta_vista);
            bateria= (TextView) v.findViewById(R.id.bateria);
            alertas= (TextView) v.findViewById(R.id.alerta_zona);
            btn = (ImageView) v.findViewById(R.id.boton_zona);
            config=(ImageView) v.findViewById(R.id.config);
            maps=(ImageView) v.findViewById(R.id.maps);
            //lista = (RecyclerView) v.findViewById(R.id.reciclerzonas);





        }

        void setOnClickListener()    {
            btn.setOnClickListener(this);
            config.setOnClickListener(this);
            maps.setOnClickListener(this);
        }



      @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.boton_zona:
               /*     Intent i = new Intent(context, Informacion_tarjeta.class);
                    i.putExtra("cardId", cardId.getText());
                    i.putExtra("cardExpMont", cardExpMont.getText());
                    i.putExtra("cardExpYear", cardExpYear.getText());
                    i.putExtra("cardName", cardName.getText());
                    i.putExtra("cardLastFour", cardLastFour.getText());
                    i.putExtra("cardBrand", cardBrand.getText());
                    i.putExtra("predeterminada", defual.getText());*/
                 //  i.putExtra("cardLastFour", cardLastFour.getText());
                  //  context.startActivity(i);

                    if (estado.getText().equals("ON")){

                            SharedPreferences misPreferencias =mContextCol.getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
                            String id_rol =  misPreferencias.getString("idRol","0");
                            if (id_rol.equals("1")){

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Apagar: "+nombre.getText().toString());
                                builder.setMessage("Esta seguro de apagar: "+nombre.getText().toString());
                                builder.setNegativeButton("Cancelar", null);
                                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        startSound(mContextCol,R.raw.click,100);
                                        try {
                                            actualizar_datos_zona(idNet.getText().toString(),idNodo.getText().toString(),idZona.getText().toString(),"0");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        estado.setText("OFF");
                                        btn.setImageResource(R.drawable.ic_red);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();


                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Permiso Denegado");
                                builder.setMessage("Este usuario no puede realizar esta acción por favor comunicate con un administrador de la Red");
                                builder.setPositiveButton("Aceptar", null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }


                    }else if (estado.getText().equals("OFF")){
                            SharedPreferences misPreferencias =mContextCol.getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
                            String id_rol =  misPreferencias.getString("idRol","0");
                            if (id_rol.equals("1")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Encender: "+ nombre.getText().toString());
                                builder.setMessage("Esta seguro de encender:  "+ nombre.getText().toString());
                                builder.setNegativeButton("Cancelar", null);
                                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        startSound(mContextCol,R.raw.click,100);
                                        try {
                                            actualizar_datos_zona(idNet.getText().toString(),idNodo.getText().toString(),idZona.getText().toString(),"1");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        btn.setImageResource(R.drawable.ic_green);
                                        estado.setText("ON");
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Permiso Denegado");
                                builder.setMessage("Este usuario no puede realizar esta acción por favor comunicate con un administrador de la Red");
                                builder.setPositiveButton("Aceptar", null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                    }else {
                        Toast.makeText(mContextCol, "Esta Zona no contiene un interruptor", Toast.LENGTH_LONG).show();

                    }

                    break;

                case R.id.config:
                    SharedPreferences misPreferencias =mContextCol.getSharedPreferences("Datos_acceso", Context.MODE_PRIVATE);
                    String id_rol =  misPreferencias.getString("idRol","0");
                    if (id_rol.equals("1")){
                        Intent i= new Intent(context, Update.class);
                        i.putExtra("id_net",idNet.getText().toString());
                        i.putExtra("id_nodo",idNodo.getText().toString());
                        i.putExtra("id_zona",idZona.getText().toString());
                        i.putExtra("nombre",nombre.getText().toString());
                        i.putExtra("estado",estado.getText().toString());
                        context.startActivity(i);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Permiso Denegado");
                        builder.setMessage("Este usuario no puede realizar esta acción por favor comunicate con un administrador de la Red");
                        builder.setPositiveButton("Aceptar", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

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
    public zonas_adapter(Context contextcol, ArrayList<zonas_model> myDatasetco) {
       // misZonas = miszonas;
        mDatasetcol = myDatasetco;
        mContextCol= contextcol;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_zonas, parent, false);
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
        holder.idNet.setText(mDatasetcol.get(position).getIdNet());
        holder.idNodo.setText(mDatasetcol.get(position).getIdNodo());
        holder.idZona.setText(mDatasetcol.get(position).getIdZona());
        holder.nombre.setText(mDatasetcol.get(position).getNombre());
        holder.ubicacion.setText(mDatasetcol.get(position).getUbicacion());
        holder.estado.setText(mDatasetcol.get(position).getEstado());
        holder.bateria.setText(mDatasetcol.get(position).getBateria());
        holder.alertas.setText(mDatasetcol.get(position).getAlerta());
//        holder.state.setText(mDatasetcol.get(position).getEstatus());

        if (holder.alertas.getText().equals("1")){
            holder.alertas.setBackgroundResource(R.drawable.ic_campana2);
        }else {
            holder.alertas.setVisibility(View.GONE);
        }

        if (holder.bateria.getText().equals("0")){
            holder.bateria.setBackgroundResource(R.drawable.bateria_null);
            holder.bateria.setText("");
        }else{
            holder.bateria.setBackgroundResource(R.drawable.bateria_full);
            holder.bateria.setText("");
        }

        if(holder.estado.getText().equals("OFF")){
            //holder.vista.setBackgroundResource(R.color.alerta);
            holder.btn.setImageResource(R.drawable.ic_red);
            holder.idZona.setTextColor(R.color.white);
        }else if(holder.estado.getText().equals("ON")){
            holder.btn.setImageResource(R.drawable.ic_green);
        }else if(holder.estado.getText().equals("")){
            holder.btn.setImageResource(R.drawable.ic_lite);
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
