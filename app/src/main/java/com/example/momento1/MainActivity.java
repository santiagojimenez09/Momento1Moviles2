package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView jtvrecargo, jtvtotal;
    EditText jetcodigo, jetenergia, jetagua, jettelefono;
    CheckBox jcbrecargo;
    Button jbtcalcular, jbtguardar, jbtconsultar, jbtlimpiar;
    int sw=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        jtvrecargo=findViewById(R.id.tvrecargo);
        jtvtotal=findViewById(R.id.tvtotal);
        jetcodigo=findViewById(R.id.etcodigo);
        jetenergia=findViewById(R.id.etenergia);
        jetagua=findViewById(R.id.etagua);
        jettelefono=findViewById(R.id.ettelefono);
        jcbrecargo=findViewById(R.id.cbrecargo);
        jbtcalcular=findViewById(R.id.btcalcular);
        jbtguardar=findViewById(R.id.btguardar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtlimpiar=findViewById(R.id.btlimpiar);

    }

    private void Calcular_total(){
        String energia, agua, telefono, recargo;
        energia=jetenergia.getText().toString();
        agua=jetagua.getText().toString();
        telefono=jettelefono.getText().toString();
        recargo=jtvrecargo.getText().toString();
        int vlrenergia, vlragua, vlrtelefono, vlrrecargo, vlrtotal;
        vlrenergia= Integer.parseInt(energia);
        vlragua=Integer.parseInt(agua);
        vlrtelefono=Integer.parseInt(telefono);
        vlrrecargo=Integer.parseInt(recargo);
        if(jcbrecargo.isChecked()){
            vlrrecargo = (vlrenergia + vlragua + vlrtelefono) * 10 / 100;
            jtvrecargo.setText(String.valueOf(vlrrecargo));
            vlrtotal = (vlrenergia + vlragua + vlrtelefono) + vlrrecargo;
            jtvtotal.setText(String.valueOf(vlrtotal));
        }else{
            vlrrecargo = 0;
            jtvrecargo.setText(String.valueOf(vlrrecargo));
            vlrtotal= vlrenergia + vlragua + vlrtelefono;

            jtvtotal.setText(String.valueOf(vlrtotal));


        }



    }

    public void Guardar(View view){
        Calcular_total();
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this,"servicios2.db",null,1);
        SQLiteDatabase db= admin.getWritableDatabase();
        String codigo,energia,agua,telefono,recargo;
        codigo=jetcodigo.getText().toString();
        energia=jetenergia.getText().toString();
        agua=jetagua.getText().toString();
        telefono=jettelefono.getText().toString();
        recargo=jtvrecargo.getText().toString();
        if(codigo.isEmpty() || energia.isEmpty() || agua.isEmpty() || telefono.isEmpty() || recargo.isEmpty()){
            Toast.makeText(this,"Todos los datos son requeridos",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }

        else{

            ContentValues dato= new ContentValues();
            dato.put("Codigo",codigo);
            dato.put("Energia",energia);
            dato.put("Agua",agua);
            dato.put("Telefono",telefono);
            /*dato.put("Recargo",recargo);*/
            long resp;
            resp = db.insert("Tblfactura",null,dato);
            if(resp > 0){
                Toast.makeText(this,"Datos guardados con exito",Toast.LENGTH_LONG).show();
                limpiar_campos();
            }
            else{
                Toast.makeText(this,"Error guardando los datos",Toast.LENGTH_LONG).show();
            }


        }db.close();

    }
    public void Consultar(View view){
        String codigo;
        codigo=jetcodigo.getText().toString();
        if (codigo.isEmpty()){
            Toast.makeText(this,"El codigo es requerido",Toast.LENGTH_LONG).show();
            jetcodigo.requestFocus();
        }
        else {
            AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this,"Servicios2.db",null,1);
            SQLiteDatabase db = admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from Tblfactura where Codigo='" + codigo + "'",null);
            if (fila.moveToFirst()) {
                sw=1;

                jetenergia.setText(fila.getString(1));
                jetagua.setText(fila.getString(2));
                jettelefono.setText(fila.getString(3));
                Calcular_total();
            }

            else{
                Toast.makeText(this,"Codigo no registrado", Toast.LENGTH_LONG).show();
                jetcodigo.requestFocus();
            }db.close();


        }

    }








    private void limpiar_campos(){
        jetcodigo.setText("");
        jetenergia.setText("");
        jetagua.setText("");
        jettelefono.setText("");
        jtvrecargo.setText("0");
        jtvtotal.setText("0");
        jcbrecargo.setChecked(false);
    }

    public void Limpiar(View view){limpiar_campos();}
    public void Calcular(View view){Calcular_total();}

}