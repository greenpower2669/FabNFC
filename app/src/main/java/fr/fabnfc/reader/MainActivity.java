package fr.fabnfc.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity implements NfcAdapter.ReaderCallback {
    private static final int IMPORT=81, EXPORT=82;
    private static final String STORE="badges.json";
    private static final int INK=Color.rgb(12,38,55), GREEN=Color.rgb(0,103,82);
    private NfcAdapter adapter;
    private TextToSpeech tts;
    private boolean speechReady=false;
    private LinearLayout layout, history;
    private TextView state, currentName, formatA,formatB, formatC,formatD;
    private EditText nameEdit;
    private final ArrayList<Entry> entries=new ArrayList<>();
    private Entry selected;
    private String recentUid="";
    private long recentTime=0;

    private static final class Entry {
        String hex, label;
        long date;
        Entry(String hex,String label,long date) {this.hex=hex;this.label=label;this.date=date;}
    }
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(INK);
        getWindow().setNavigationBarColor(INK);
        getWindow().getDecorView().setSystemUiVisibility(0);
        adapter=NfcAdapter.getDefaultAdapter(this);
        loadEntries();
        tts=new TextToSpeech(this, status -> {
            speechReady=(status==TextToSpeech.SUCCESS);
            if (speechReady) tts.setLanguage(Locale.FRENCH);
        });
        buildView();
        if (!entries.isEmpty()) showEntry(entries.get(0),false);
    }
    private int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    private TextView text(String value,int sp,boolean bold) {
        TextView view=new TextView(this);
        view.setText(value); view.setTextColor(INK);view.setTextSize(sp);
        if(bold)view.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        view.setPadding(dp(2),dp(8),dp(2),dp(8));
        return view;
    }
    private Button button(String title,Runnable action) {
        Button b=new Button(this);
        b.setText(title);b.setTextSize(18);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(GREEN));
        b.setMinHeight(dp(62));
        b.setOnClickListener(v->action.run());
        return b;
    }
    private void addButton(String title,Runnable action){layout.addView(button(title,action));}
    private void buildView(){
        ScrollView scroll=new ScrollView(this);
        scroll.setFillViewport(true); scroll.setBackgroundColor(Color.WHITE);
        layout=new LinearLayout(this);layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(18),dp(12),dp(18),dp(28));
        scroll.addView(layout);
        setContentView(scroll);
        if (Build.VERSION.SDK_INT>=30) {
            scroll.setOnApplyWindowInsetsListener((v,insets)->{
                android.graphics.Insets safe=insets.getInsets(WindowInsets.Type.systemBars());
                scroll.setPadding(safe.left,safe.top,safe.right,safe.bottom);
                return insets;
            });
            scroll.requestApplyInsets();
        }
        layout.addView(text("📶 Fab NFC",30,true));
        layout.addView(text("Lecture locale des identifiants de badge — aucune écriture NFC",16,false));
        state=text("Approche un badge de l'arrière du téléphone.",21,true);state.setTextColor(GREEN);
        layout.addView(state);
        addButton("Activer / vérifier le NFC",()->{
            if(adapter==null)state.setText("Ce téléphone ne possède pas de lecteur NFC.");
            else if(!adapter.isEnabled())startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            else state.setText("NFC activé. Approche un badge.");
        });
        layout.addView(text("IDENTIFIANT DU BADGE",22,true));
        currentName=text("Aucun badge sélectionné",20,true);layout.addView(currentName);
        nameEdit=new EditText(this);nameEdit.setSingleLine(true);nameEdit.setTextSize(20);
        nameEdit.setHint("Nom du badge (facultatif)");
        layout.addView(nameEdit);
        addButton("Enregistrer le nom",()->{
            if(selected==null){sayStatus("Scanne un badge d'abord.");return;}
            String label=nameEdit.getText().toString().trim();
            if(label.length()==0 || label.length()>80){sayStatus("Nom : 1 à 80 caractères.");return;}
            selected.label=label;saveEntries();showEntry(selected,false);
        });
        formatA=addFormat("1. UID HEXADÉCIMAL — ORDRE LU");
        addButton("Copier l'UID hexadécimal",()->copy(formatA));
        formatB=addFormat("2. UID DÉCIMAL — ORDRE LU");
        addButton("Copier l'UID décimal",()->copy(formatB));
        formatC=addFormat("3. HEXADÉCIMAL — OCTETS INVERSÉS");
        addButton("Copier l'hexadécimal inversé",()->copy(formatC));
        formatD=addFormat("4. DÉCIMAL — OCTETS INVERSÉS");
        addButton("Copier le décimal inversé",()->copy(formatD));
        addButton("Copier les quatre formats",()->{
            if(selected==null)return;
            byte[] id=UidFormats.fromHex(selected.hex),rev=UidFormats.reverse(id);
            copyText("UID HEX : "+UidFormats.hex(id)+"\nUID DÉCIMAL : "+UidFormats.decimal(id)+
                "\nHEX INVERSÉ : "+UidFormats.hex(rev)+"\nDÉCIMAL INVERSÉ : "+UidFormats.decimal(rev));
        });
        addButton("🔊 Écouter les identifiants",()->{
            if(selected==null){sayStatus("Aucun badge lu.");return;}
            byte[] id=UidFormats.fromHex(selected.hex);
            speak("Badge "+selected.label+". Hexadécimal : "+spell(UidFormats.hex(id))+
                ". Décimal : "+spell(UidFormats.decimal(id)));
        });
        layout.addView(text("Attention : ce sont des représentations calculées de l'UID NFC, pas nécessairement le numéro imprimé ni le code provisoire demandé par AGESCA. Demande leur confirmation.",16,true));
        layout.addView(text("SAUVEGARDES",22,true));
        addButton("Exporter la sauvegarde JSON",()->exportBackup());
        addButton("Importer une sauvegarde JSON",()->importBackup());
        layout.addView(text("Les sauvegardes contiennent des identifiants de badges : conserve-les en lieu sûr. Aucun transfert Internet par l'application.",15,false));
        layout.addView(text("HISTORIQUE LOCAL",22,true));
        history=new LinearLayout(this);history.setOrientation(LinearLayout.VERTICAL);
        layout.addView(history);
        refreshHistory();
    }
    private TextView addFormat(String title){
        layout.addView(text(title,17,true));
        TextView field=text("—",26,true);
        field.setTextIsSelectable(true);
        field.setContentDescription(title);
        field.setPadding(dp(10),dp(14),dp(10),dp(14));
        field.setBackgroundColor(Color.rgb(234,248,242));
        layout.addView(field);
        return field;
    }
    private void refreshHistory(){
        if(history==null)return;
        history.removeAllViews();
        if(entries.isEmpty()){history.addView(text("Aucun badge enregistré.",17,false));return;}
        for(int i=0;i<entries.size();i++){
            Entry item=entries.get(i);
            String when=DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,Locale.FRANCE)
                .format(new Date(item.date));
            Button b=button(item.label+" — "+when,()->showEntry(item,true));
            history.addView(b);
        }
        Button erase=button("Effacer tout l'historique local",()->new AlertDialog.Builder(this)
            .setTitle("Effacer tous les badges ?")
            .setMessage("Cette action efface la sauvegarde locale. Exporte d'abord un fichier si nécessaire.")
            .setNegativeButton("Annuler",null)
            .setPositiveButton("Effacer",(dialog,which)->{
                entries.clear();selected=null;saveEntries();refreshHistory();
                currentName.setText("Aucun badge sélectionné");
                nameEdit.setText("");
                formatA.setText("—");formatB.setText("—");
                formatC.setText("—");formatD.setText("—");
            }).show());
        erase.setBackgroundTintList(android.content.res.ColorStateList.valueOf(INK));
        history.addView(erase);
    }
    private void showEntry(Entry e,boolean announce){
        selected=e;currentName.setText(e.label);
        nameEdit.setText(e.label);
        byte[] id=UidFormats.fromHex(e.hex),rev=UidFormats.reverse(id);
        formatA.setText(UidFormats.hex(id));formatB.setText(UidFormats.decimal(id));
        formatC.setText(UidFormats.hex(rev));formatD.setText(UidFormats.decimal(rev));
        if(announce)sayStatus(e.label+" sélectionné. Formats disponibles.");
    }
    @Override protected void onResume(){
        super.onResume();
        if(adapter==null){if(state!=null)state.setText("NFC indisponible sur cet appareil.");return;}
        if(!adapter.isEnabled()){if(state!=null)state.setText("NFC désactivé : utilise le bouton pour l'activer.");return;}
        try {
            int flags=NfcAdapter.FLAG_READER_NFC_A|NfcAdapter.FLAG_READER_NFC_B|
                NfcAdapter.FLAG_READER_NFC_F|NfcAdapter.FLAG_READER_NFC_V|
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
            adapter.enableReaderMode(this,this,flags,null);
            if(state!=null)state.setText("NFC prêt : pose un badge contre l'arrière du téléphone.");
        }catch(Exception ex){if(state!=null)state.setText("Lecture NFC impossible : "+ex.getClass().getSimpleName());}
    }
    @Override protected void onPause(){
        if(adapter!=null)try{adapter.disableReaderMode(this);}catch(Exception ignored){}
        super.onPause();
    }
    @Override public void onTagDiscovered(Tag tag){
        byte[] raw=tag.getId();
        if(raw==null||raw.length==0){runOnUiThread(()->sayStatus("Badge détecté, mais aucun UID accessible."));return;}
        String hex=UidFormats.hex(raw);
        long now=SystemClock.elapsedRealtime();
        if(hex.equals(recentUid)&&now-recentTime<2200)return;
        recentUid=hex;recentTime=now;
        runOnUiThread(()->{
            Entry found=null;
            for(Entry e:entries)if(e.hex.equals(hex)){found=e;break;}
            if(found==null){
                found=new Entry(hex,"Badge "+(entries.size()+1),System.currentTimeMillis());
                entries.add(0,found);
            }else{found.date=System.currentTimeMillis();entries.remove(found);entries.add(0,found);}
            saveEntries();showEntry(found,false);refreshHistory();
            sayStatus("Badge lu et sauvegardé : "+found.label+". UID disponible à copier.");
        });
    }
    private void sayStatus(String msg){if(state!=null)state.setText(msg);speak(msg);}
    private void speak(String msg){
        if(speechReady&&tts!=null)tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null,"fabnfc-message");
    }
    private static String spell(String data){
        StringBuilder sb=new StringBuilder();
        for(char c:data.toCharArray())sb.append(c).append(' ');
        return sb.toString();
    }
    private void copy(TextView v){
        if(selected==null){sayStatus("Scanne un badge avant de copier.");return;}
        copyText(v.getText().toString());
    }
    private void copyText(String v){
        ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE))
            .setPrimaryClip(ClipData.newPlainText("FabNFC",v));
        sayStatus("Copié. Tu peux coller directement dans le mail.");
    }
    private JSONObject backupJson()throws Exception{
        JSONObject root=new JSONObject();root.put("schema",1);
        root.put("application","FabNFC");
        JSONArray a=new JSONArray();
        for(Entry e:entries){JSONObject j=new JSONObject();j.put("uidHex",e.hex);j.put("label",e.label);j.put("seenAt",e.date);a.put(j);}
        root.put("badges",a);return root;
    }
    private void saveEntries(){
        try(FileOutputStream stream=openFileOutput(STORE,MODE_PRIVATE)){
            stream.write(backupJson().toString(2).getBytes(StandardCharsets.UTF_8));
        }catch(Exception e){if(state!=null)state.setText("Erreur de sauvegarde locale. Exporte dès que possible.");}
    }
    private void loadEntries(){
        try(FileInputStream in=openFileInput(STORE)){
            entries.addAll(parseBackup(readAll(in)));
        }catch(java.io.FileNotFoundException ignored){}
        catch(Exception ignored){}
    }
    private static String readAll(InputStream input)throws Exception {
        ByteArrayOutputStream result=new ByteArrayOutputStream();
        byte[] buffer=new byte[4096];int n;int total=0;
        while((n=input.read(buffer))!=-1){
            total+=n;if(total>1048576)throw new IllegalArgumentException("Fichier supérieur à 1 Mo");
            result.write(buffer,0,n);
        }
        return result.toString("UTF-8");
    }
    private static ArrayList<Entry> parseBackup(String raw)throws Exception{
        JSONObject obj=new JSONObject(raw);
        if(obj.getInt("schema")!=1)throw new IllegalArgumentException("Version de sauvegarde inconnue");
        JSONArray arr=obj.getJSONArray("badges");
        if(arr.length()>500)throw new IllegalArgumentException("Trop de badges");
        ArrayList<Entry> list=new ArrayList<>();
        for(int i=0;i<arr.length();i++){
            JSONObject b=arr.getJSONObject(i);
            String hex=UidFormats.hex(UidFormats.fromHex(b.getString("uidHex")));
            String label=b.getString("label");
            if(label.length()<1||label.length()>80)throw new IllegalArgumentException("Nom incorrect");
            long date=b.getLong("seenAt");
            if(date<0)throw new IllegalArgumentException("Date incorrecte");
            for(Entry x:list)if(x.hex.equals(hex))throw new IllegalArgumentException("Doublon d'UID");
            list.add(new Entry(hex,label,date));
        }
        return list;
    }
    private void exportBackup(){
        Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");
        i.putExtra(Intent.EXTRA_TITLE,"FabNFC-sauvegarde.json");
        startActivityForResult(i,EXPORT);
    }
    private void importBackup(){
        Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");
        startActivityForResult(i,IMPORT);
    }
    @Override protected void onActivityResult(int request,int result,Intent data){
        super.onActivityResult(request,result,data);
        if(result!=RESULT_OK||data==null||data.getData()==null)return;
        try{
            if(request==EXPORT){
                try(OutputStream out=getContentResolver().openOutputStream(data.getData())){
                    if(out==null)throw new IllegalStateException("Destination inaccessible");
                    out.write(backupJson().toString(2).getBytes(StandardCharsets.UTF_8));
                }
                sayStatus("Sauvegarde exportée.");
            }else if(request==IMPORT){
                ArrayList<Entry> imported;
                try(InputStream in=getContentResolver().openInputStream(data.getData())){
                    if(in==null)throw new IllegalStateException("Fichier inaccessible");
                    imported=parseBackup(readAll(in));
                }
                int added=0;
                for(Entry e:imported){
                    boolean exists=false;for(Entry x:entries)if(x.hex.equals(e.hex)){exists=true;break;}
                    if(!exists){entries.add(e);added++;}
                }
                saveEntries();refreshHistory();
                if(selected==null&&!entries.isEmpty())showEntry(entries.get(0),false);
                sayStatus("Import terminé : "+added+" badge(s) ajoutés, anciens conservés.");
            }
        }catch(Exception error){
            new AlertDialog.Builder(this).setTitle("Opération non réalisée")
                .setMessage("Fichier illisible ou accès impossible : "+error.getMessage())
                .setPositiveButton("OK",null).show();
        }
    }
    @Override protected void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
}
