package com.example.whereiss;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.TextView;

import com.example.whereiss.ApiRest.ISSLocService;
import com.example.whereiss.ApiRest.ISSLocationResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    ISSLocService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**< 1- Crear objeto retrofit */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.open-notify.org/")
                .addConverterFactory(GsonConverterFactory.create()) // Si no añadimos esto, devuelve un JSON de textos
                .build();
        service = retrofit.create(ISSLocService.class);
        realizarUpdate(service);

    }


    public void onClickedUpdate(View v){
        realizarUpdate(service);
    }

    /**
     * Recupera un recurso y lo muestra en el TextView tvContenido
     *
     */
    void realizarUpdate(ISSLocService service) {

        service.jsonISSLocation().enqueue(new Callback<ISSLocationResponse>() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onResponse(Call<ISSLocationResponse> call, Response<ISSLocationResponse> response) {

                // Copiar el cuerpo de la respuesta a un Tipo ISSLocationResponse
                ISSLocationResponse respuesta = response.body();

                // Poner en los TextView la respuesta recibida
                ((TextView)findViewById(R.id.longitude)).setText(respuesta.getIssPosition().getLongitude());
                ((TextView)findViewById(R.id.Latitude)).setText(respuesta.getIssPosition().getLatitude());
                ((TextView)findViewById(R.id.lastUpdate)).setText(respuesta.getTimestampFormatted());



            }

            @Override
            public void onFailure(Call<ISSLocationResponse> call, Throwable t) {
                Log.e("FALLO", "FALLO");
            }
        });
    }
}
