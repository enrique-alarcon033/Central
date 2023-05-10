package com.yonusa.central.invitados.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.yonusa.central.invitados.Edit_invitado;
import com.yonusa.central.invitados.model.invitados_model;
import com.yonusa.central.zonas.Update;
import com.yonusa.central.zonas.model.zonas_model;

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
public class invitados_adapter extends RecyclerView.Adapter<invitados_adapter.ViewHolder>  {
    private Context mContextCol;
    private ArrayList<invitados_model> mDatasetcol;
    String codigo;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView iduser,nombre,password,email,telefono,idRol,idNet,fecha,hora;
        Context context;
        ImageView btn,config,maps;
        ConstraintLayout vista;

        CardView tarjeta;
        public Transition transition;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();
           // contenido = (TextView) v.findViewById(R.id.Nombre_Categoria);
           // iduser =(TextView) v.findViewById(R.id.Virtual_Categoria_id);
           iduser= (TextView) v.findViewById(R.id.id_user);
           nombre= (TextView) v.findViewById(R.id.nombre_invitado);
           password=(TextView) v.findViewById(R.id.password_invitado);
           email=(TextView) v.findViewById(R.id.email_invitado);
           telefono=(TextView) v.findViewById(R.id.telefono_invitado);
           idRol=(TextView) v.findViewById(R.id.id_rol);
           idNet=(TextView) v.findViewById(R.id.id_net_invitado);
           fecha = (TextView) v.findViewById(R.id.fecha_invitado);
           hora=(TextView) v.findViewById(R.id.hora_invitado);

            vista = (ConstraintLayout) v.findViewById(R.id.layout_tarjeta);
            tarjeta =(CardView) v.findViewById(R.id.tarjeta_vista);
         //   btn = (ImageView) v.findViewById(R.id.boton_zona);
            config=(ImageView) v.findViewById(R.id.config);





        }

        void setOnClickListener()    {
//            btn.setOnClickListener(this);
            config.setOnClickListener(this);
           // maps.setOnClickListener(this);
        }



      @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.config:
                    Intent i= new Intent(context, Edit_invitado.class);
                   i.putExtra("id_user",iduser.getText().toString());
                 //   i.putExtra("id_nodo",idNodo.getText().toString());
                 //   i.putExtra("id_zona",idZona.getText().toString());
                  //  i.putExtra("nombre",nombre.getText().toString());
                   // i.putExtra("estado",estado.getText().toString());
                    context.startActivity(i);
                    break;


            }
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public invitados_adapter(Context contextcol, ArrayList<invitados_model> myDatasetco) {
        mDatasetcol = myDatasetco;
        mContextCol= contextcol;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invitados, parent, false);
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
        holder.iduser.setText(mDatasetcol.get(position).getIdUser());
        holder.nombre.setText(mDatasetcol.get(position).getNombre());
        holder.password.setText(mDatasetcol.get(position).getPassword());
        holder.email.setText(mDatasetcol.get(position).getEmail());
        holder.telefono.setText(mDatasetcol.get(position).getTelefono());
        holder.idRol.setText(mDatasetcol.get(position).getIdRol());
        holder.idNet.setText(mDatasetcol.get(position).getIdNet());
        holder.fecha.setText(mDatasetcol.get(position).getFecha());
        holder.hora.setText(mDatasetcol.get(position).getHora());

        if (holder.hora.getText().toString().equals("00:00")){
            holder.fecha.setText("Este usuario no ha iniciado sesión");
            holder.fecha.setTextColor(R.color.alerta);
            holder.hora.setVisibility(View.INVISIBLE);
        }



        holder.setOnClickListener();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDatasetcol.size();
    }


}
