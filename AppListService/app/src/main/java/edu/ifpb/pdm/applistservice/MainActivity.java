package edu.ifpb.pdm.applistservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.ifpb.pdm.applistservice.receiver.MyReceiver;
import edu.ifpb.pdm.applistservice.receiver.Notice;
import edu.ifpb.pdm.applistservice.service.ServiceLoadList;

public class MainActivity extends AppCompatActivity implements Notice{
    private List<String> nomesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Criando e registrando o Broadcast Receiver
        MyReceiver receiver= new MyReceiver(this);
        IntentFilter filter = new IntentFilter("edu.ifpb.pdm.applistservice.receiver.MY_RECEIVER");
        registerReceiver(receiver, filter);

        this.nomesList = new ArrayList<>();
        nomesList.add("Teste");
        loadList(nomesList);
        //loadList(ServiceLoadList.nomes);
    }

    /**
     * Método que carrega uma lista de nomes na visão.
     * @param nomes
     */
    public void loadList(List<String> nomes){
        ListView list = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, nomes);
        list.setAdapter(adapter);
    }

    /**
     * Método que inicia o serviço.
     */
    public void startServiceLoadList(View view){
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ServiceLoadList.class);
        startService(intent);
    }

    /**
     * Método que para o serviço.
     */
    public void stopServiceLoadList(View view){
        Toast.makeText(this, "service stoping", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ServiceLoadList.class);
        stopService(intent);
        nomesList.clear();
        loadList(nomesList);
    }

    @Override
    public void process(List<String> list) {
        loadList(list);
    }


//    /**
//     * Método que verifica se houve ateração na lista e chama o método para
//     * carrega-la na visão.
//     */
//    public void getData(){
//        //TODO se houver alteração na variavel estatica chama o metodo loadList
//        MyReceiver receiver = new MyReceiver();
//        nomesList= receiver.getDataNames();
//        loadList(ServiceLoadList.nomes);
//    }


}
