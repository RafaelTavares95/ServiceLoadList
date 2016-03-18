package edu.ifpb.pdm.applistservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.ifpb.pdm.applistservice.storage.PreferencesStorage;
import edu.ifpb.pdm.applistservice.storage.SqlStorage;

public class ServiceLoadList extends Service {
    private static final long TEMPO = (2000 * 60);
    public  List<String> nomes;
    private static String md5;
    private PreferencesStorage pref;
    private SqlStorage sqlite;
    public ServiceLoadList() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        nomes = new ArrayList<>();
        pref = new PreferencesStorage(getBaseContext());
        sqlite = new SqlStorage(getBaseContext());

        //Executando tarefa no tempo programado
        Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            public void run() {
                try {
                    md5 = initProcess();
       //             System.out.println(md5);
                    storageProcess(md5,nomes.size(), nomes);
                    process();
                    notifyReciver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(tarefa, 0, TEMPO);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Método para obter todos os nomes do webservice
     * @return
     */
    public List<String> getAllNames(){
        List<String> nomes;
        String url= "http://192.168.0.104:8080/nomes?a=todos";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        nomes = restTemplate.getForObject(url, List.class);
        return nomes;
    }

    /**
     * Método para obter um nome a partir de uma posição do webservice
     * @param position
     * @return
     */
    public String getName(Integer position){
        String url="http://192.168.0.104:8080/nomes?a=um&o="+position;
        List<String> nomes;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        nomes = restTemplate.getForObject(url, List.class);
        return nomes.get(0);
    }

    /**
     * Método para gerar um codigo hash
     * @param msg
     * @return
     */
    public String getMD5(String msg){
        String m = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, md.digest(msg.getBytes()));
        m = hash.toString(16);
        return m;
    }

    /**
     * Método que realiza as pre operações do serviço, que são carregar a lista e gerar o md5
     * @return
     */
    public String initProcess(){
        nomes = getAllNames();
        //System.out.println(nomes.toString());
        return getMD5(nomes.toString());
    }

    /**
     * Método que realiza as operações de verificar se a lista foi alterada e caso isso
     * ocorra, atualiza a lista e o md5 dela.
     */
    public void process(){
        List<String> tempList = getAllNames();
        List<String> listToStoraged= new ArrayList<>();
        int listSize = pref.getOffset();
        md5 = pref.getHash();
        nomes.clear();
        nomes = sqlite.findAll();
        if(!md5.equals(getMD5(tempList.toString()))) {
            for (int i = listSize; i < tempList.size(); i++) {
                nomes.add(getName(i));
                listToStoraged.add(getName(i));
            }
            md5 = getMD5(nomes.toString());
            storageProcess(md5, nomes.size(), listToStoraged);
        }
    }

    public void notifyReciver(){
        Intent intent = new Intent("edu.ifpb.pdm.applistservice.receiver.MY_RECEIVER");
        intent.putStringArrayListExtra("list", (ArrayList<String>) nomes);
        sendBroadcast(intent);
    }

    public void storageProcess(String hash, int offSet, List<String> list){
        sqlite.delete();
        pref.saveHash(hash);
        pref.saveOffSet(offSet);
        sqlite.saveList(list);
    }
}
