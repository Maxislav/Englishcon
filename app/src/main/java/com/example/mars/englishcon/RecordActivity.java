package com.example.mars.englishcon;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class RecordActivity extends ActionBarActivity {


    EditText editTextEn;
    EditText editTextRu;
    LinearLayout mainLayout;
    private final static String LOG = "RecortActivity";
    private static String editId;

    public final static String THIEF = "com.example.mars.englishcon.THIEF";
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        editTextEn = (EditText) findViewById(R.id.editTextEn);
        editTextRu = (EditText) findViewById(R.id.editTextRu);
        dataBaseHelper = new DataBaseHelper(this);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        setTitle("Edit");
        getData();
        editId = null;
    }

    private void updateView() {
        mainLayout.removeAllViews();
        getData();
    }

    private void getData() {
        ArrayList<Map> arrayList = dataBaseHelper.selectData(1);
        if (arrayList != null) {
            for (Map map : arrayList) {
                String valueEn = map.get("EN").toString();
                String valueRu = map.get("RU").toString();
                String _id = map.get("ID").toString();
                String show_ru = map.get("SHOW_RU").toString();
                String in_mind = map.get("IN_MIND").toString();
                String in_game = map.get("IN_GAME").toString();
                // Log.d("SHOW_RU", show_ru);
                createItems(valueEn, valueRu, _id, show_ru, in_mind, in_game);
            }
        } else {
            return;
        }
    }


    private void createItems(String en, String ru, String _id, String show_ru, String in_mind, String in_game) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_row_rec, mainLayout, false);
        LinearLayout rowLinearLayout = (LinearLayout) view;
        mainLayout.addView(rowLinearLayout);
        ViewGroup viewGroup = (ViewGroup) rowLinearLayout;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child, child2, child3, childInMind;
            ViewGroup viewGroup2;

            switch (i) {
                case 0:
                    //  child =;
                    viewGroup2 = (ViewGroup) viewGroup.getChildAt(i);
                    child2 = ((ViewGroup) viewGroup2.getChildAt(0)).getChildAt(0); //TextView
                    // child2 = ((ViewGroup)child2).getChildAt(0);
                    ((TextView) child2).setText(en);

                    clickListener(child2, _id, 0, true); //TextView
                    TextView inMintTv = ((TextView) ((ViewGroup) viewGroup2.getChildAt(1)).getChildAt(0));
                    inMintTv.setText(in_game);

                    childInMind = viewGroup2.getChildAt(2); //LinearLayout в котором img
                    if (in_mind.equals("0")) {
                        clickListenerInMind(childInMind, _id, inMintTv, false);
                        //childInMind.setVisibility(View.INVISIBLE);
                    } else {
                        clickListenerInMind(childInMind, _id, inMintTv, true);
                        //clickListener(childInMind, _id, 1);
                    }
                    break;
                case 1:
                    child = viewGroup.getChildAt(i);
                    viewGroup2 = (ViewGroup) child;
                    child2 = viewGroup2.getChildAt(0);
                    ((TextView) child2).setText(ru);
                    clickListenerDel(
                            ((ViewGroup) viewGroup2.getChildAt(1)).getChildAt(0),    //button minus

                            //((ViewGroup)((ViewGroup)((ViewGroup) rowLinearLayout).getChildAt(0)).getChildAt(2)).getChildAt(0), //dutton del
                            ((ViewGroup) ((ViewGroup) ((ViewGroup) ((ViewGroup) rowLinearLayout)
                                    .getChildAt(0))
                                    .getChildAt(0))
                                    .getChildAt(1))
                                    .getChildAt(0), //dutton del
                            rowLinearLayout,
                            _id);
                    break;
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.translate_show, R.anim.translate_left_hide);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_layout, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        //  actionBar.setIcon(R.drawable.en_con);
        actionBar.setCustomView(v);
        menu.findItem(R.id.action_record).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        Log.d("MyLog", id + "  : " + android.R.id.home);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_record) {
            return true;
        }
        if (id == R.id.action_learn) {
            Intent answerInent = new Intent();
            answerInent.putExtra(THIEF, "Сраный пёсик");
            setResult(RESULT_OK, answerInent);
            finish();
            return true;
        }

        if (id == R.id.action_game) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            Log.d("MyLog", "Home");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickBtnAdd(View view) {
        ContentValues cv = new ContentValues();
        String valueEn, valueRu;
        valueEn = editTextEn.getText().toString();
        valueRu = editTextRu.getText().toString();
        valueEn = valueEn.replaceAll("^\\s+", "");
        valueRu = valueRu.replaceAll("^\\s+", "");
        Toast toast;
        int k = dataBaseHelper.addRow(valueEn, valueRu);
        switch (k) {
            case 0:
                toast = Toast.makeText(getApplicationContext(),
                        "Add value ok!",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                editTextEn.setText("");
                editTextRu.setText("");
                updateView();
                break;
            case 1:
                toast = Toast.makeText(getApplicationContext(),
                        "Empty value",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case 2:
                toast = Toast.makeText(getApplicationContext(),
                        "Value already exist",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            default:
        }


    }

    public void clickListener(View view, String _id, int n, boolean visible) {
        final String ID = _id;
        final int N = n;

        final View img;
        switch (n) {
            case 1:
                img = ((ViewGroup) ((ViewGroup) view).getChildAt(0)).getChildAt(1);
                if (!visible) {
                    img.setVisibility(View.INVISIBLE);
                }
                break;
            default:
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (N) {
                    case 0:
                        Map<String, String> map = dataBaseHelper.selectById(ID);
                        Log.d(LOG, map.get("value_en") + ": " + map.get("value_ru"));
                        editTextEn.setText(map.get("value_en"));
                        editTextRu.setText(map.get("value_ru"));
                        editId = ID;
                        break;
                    case 1:
                        // View _img = img;
                        // img.setVisibility(View.INVISIBLE);
                        //  dataBaseHelper.setInMind(ID, false);
                        break;
                    default:
                }
            }
        });
    }


    public void clickListenerInMind(final View view, String _id, TextView _inMintTv, boolean visible) {
        final View img = ((ViewGroup) ((ViewGroup) view).getChildAt(0)).getChildAt(1);
        final TextView inMintTv = _inMintTv;
        final String ID = _id;
        if (!visible) {
            img.setVisibility(View.INVISIBLE);
        } else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img.setVisibility(View.INVISIBLE);
                    view.setClickable(false);
                    dataBaseHelper.setInMind(ID, false);
                    inMintTv.setText("0");
                }
            });
        }
    }

    public void clickListenerDel(View _minus, View _del, View _rowLinearLayout, String _id) {
        final View del = _del;
        final View minus = _minus;
        final String id = _id;
        final View rowLinearLayout = _rowLinearLayout;
        // final  Animation anim;
        _minus.setOnClickListener(new View.OnClickListener() {
            Animation anim;

            @Override
            public void onClick(View v) {
                if (del.isShown()) {
                    minus.setBackgroundResource(R.drawable.minus_0);
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_hide);
                    del.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            del.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else {
                    minus.setBackgroundResource(R.drawable.minus_90);
                    del.setVisibility(View.VISIBLE);
                    anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left_show);
                    del.startAnimation(anim);
                }
            }
        });

        _del.setOnClickListener(new View.OnClickListener() {
            Animation anim;

            @Override
            public void onClick(View v) {
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Handler().post(new Runnable() {
                            public void run() {

                                if (dataBaseHelper.delRowById(id)) {
                                    mainLayout.removeView(rowLinearLayout);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Ошибка удаления",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        });
                        // mainLayout.removeCh();
                        //  del.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rowLinearLayout.startAnimation(anim);
            }
        });
    }


    public void clickBtnEdit(View view) {
        boolean update = false;
        if (editId != null) {
            update = dataBaseHelper.updateValueRow(editTextEn.getText().toString(), editTextRu.getText().toString(), editId);
        }

        if (update) {
            updateView();
            editTextEn.setText("");
            editTextRu.setText("");
            updateView();
            editId = null;
        }
    }

    public void clickBtnDrop(View view) {
        //dataBaseHelper.dropTable();
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popuapmenu);
        popupMenu.show();
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuDel:
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Данные удалены",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }


    //создание колонки
    public void createColumn(View view) {
        //dataBaseHelper.createColumn();
    }

   /* public void setShow(View view) {
        dataBaseHelper.setShow();
    }*/
}
