package fnadaud.checkdebts;

/**
 * Created by florian on 01/03/17.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private ListView myListView;
    public static ArrayList<Debts> listItem;
    private SimpleAdapter lAdapter;
    private MyListAdapter myAdapter;
    private DebtsDAO datasource;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        datasource = new DebtsDAO(this);
        datasource.open();

        myListView = (ListView) findViewById(R.id.mylistView);
        listItem = datasource.getAllDebts();

        myAdapter = new MyListAdapter(this, R.layout.list_item, listItem);

        //Enfin on met un écouteur d'évènement sur notre listView
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                final Debts d = (Debts) myListView.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Suppression de \"" + d.getName() +  "\"");

                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        listItem.remove(d);
                        datasource.delete(d.getName());
                        myAdapter.notifyDataSetChanged();
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                adb.show();
                return true;
            }
        });
        Button add_button = (Button) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

                adb.setTitle("Ajout : Entrez un nom");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                adb.setView(input);

                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String s = input.getText().toString();
                        if(s.equals("")){
                            dialog.cancel();
                        }
                        else {
                            Debts debts;
                            debts = new Debts(s, "0.0");
                            datasource.insert(debts);
                            listItem.add(debts);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = adb.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });

        /*Button delete_button = (Button) findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Suppression : Entrez un nom");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                adb.setView(input);

                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        String s = input.getText().toString();
                        if(s.equals("")){
                            dialog.cancel();
                        }
                        else {
                            for(Debts d : listItem){
                                if(d.getName().equals(s)){
                                    listItem.remove(d);
                                    datasource.delete(s);
                                    myAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                });

                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = adb.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });*/

        myListView.setAdapter(myAdapter);
    }

    private class MyListAdapter extends ArrayAdapter<Debts>{
        private int layout;
        private ViewHolder viewHolder;
        private MyListAdapter(Context context, int resource, ArrayList<Debts> objects){
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolder mainViewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.debt = (TextView) convertView.findViewById(R.id.debt);
                viewHolder.plus = (Button) convertView.findViewById(R.id.plus_button);
                viewHolder.minus = (Button) convertView.findViewById(R.id.minus_button);

                convertView.setTag(viewHolder);
            }
            mainViewHolder = (ViewHolder) convertView.getTag();
            Debts debts = (Debts) getItem(position);
            mainViewHolder.name.setText(debts.getName());

            mainViewHolder.debt.setText(debts.getDebt());
            float val = Float.parseFloat(debts.getDebt());
            if(val == 0){
                mainViewHolder.debt.setTextColor(Color.WHITE);
            }
            else if(val < 0){
                mainViewHolder.debt.setTextColor(Color.RED);
            }
            else{
                mainViewHolder.debt.setTextColor(Color.GREEN);
            }

            mainViewHolder.plus = (Button) convertView.findViewById(R.id.plus_button);
            mainViewHolder.plus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setTitle("Entrez la somme à ajouter");
                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    adb.setView(input);

                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            String s = input.getText().toString();
                            if(s.equals("")){
                                dialog.cancel();
                            }
                            else {
                                float toAdd = Float.parseFloat(s);

                                Debts debts = getItem(position);
                                float val = Float.parseFloat(debts.getDebt());
                                val += toAdd;
                                System.out.println(val);
                                val = Math.round(val * 100) / 100f;
                                System.out.println(val);
                                debts.setDebt(String.valueOf(val));
                                datasource.update(debts);

                                viewHolder.debt.setText(String.valueOf(val));
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = adb.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
                }
            });
            mainViewHolder.minus = (Button) convertView.findViewById(R.id.minus_button);
            mainViewHolder.minus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                    adb.setTitle("Entrez la somme à enlever");
                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    adb.setView(input);

                    adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            String s = input.getText().toString();
                            if(s.equals("")){
                                dialog.cancel();
                            }
                            else {
                                float toSub = Float.parseFloat(s);

                                Debts debts = getItem(position);
                                float val = Float.parseFloat(debts.getDebt());
                                val -= toSub;
                                val = Math.round(val * 100) / 100f;
                                debts.setDebt(String.valueOf(val));
                                datasource.update(debts);

                                viewHolder.debt.setText(String.valueOf(val));
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = adb.create();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    dialog.show();
                }
            });
           return convertView;
        }
    }

    public class ViewHolder{
        TextView name;
        TextView debt;
        Button plus;
        Button minus;
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
