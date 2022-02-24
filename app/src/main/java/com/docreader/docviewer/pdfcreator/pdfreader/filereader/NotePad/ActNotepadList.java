package com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter.NotepadFilesListAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.Advertisement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.AppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.DatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.NotepadItemModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActNotepadList extends BaseActivity implements AdapterView.OnItemClickListener {
    public DatabaseHelper dbhelper;
    public List<NotepadItemModel> list;
    public NotepadFilesListAdp mAdapter;
    public SharedPrefs prefs;
    public RecyclerView recyclerView;
    public MenuItem removePassword;
    public MenuItem setPassword;
    public MenuItem changePassword;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notepad_list);
        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.notepad));

        prefs = new SharedPrefs(this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
            switch (prefs.getAds_name()) {
                case "g":
                    Advertisement.GoogleBanner(ActNotepadList.this, ll_banner);
                    break;
                case "a":
                    AppLovinAds.AppLovinBanner(ActNotepadList.this, ll_banner);
                    break;
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dbhelper = new DatabaseHelper(this);

        setNotes();

        findViewById(R.id.createNewNoteBtn).setOnClickListener(view -> {
            if (!(prefs.getActive_Weekly().equals("true") || prefs.getActive_Monthly().equals("true") || prefs.getActive_Yearly().equals("true"))) {
                switch (prefs.getAds_name()) {
                    case "g":
                        if (Advertisement.adsdisplay) {
                            Advertisement.FullScreenLoad(ActNotepadList.this, () -> {
                                Advertisement.allcount60.start();
                                IntentNote();
                            });
                        } else {
                            IntentNote();
                        }
                        break;
                    case "a":
                        if (Advertisement.adsdisplay) {
                            AppLovinAds.AppLovinFullScreenShow(() -> {
                                Advertisement.allcount60.start();
                                IntentNote();
                            });
                        } else {
                            IntentNote();
                        }
                        break;

                }
            } else {
                IntentNote();
            }
        });
    }

    private void IntentNote() {
        Intent intent = new Intent(getApplicationContext(), ActCreateNote.class);
        intent.putExtra(ScreenCVEdit.FIELD_ID, "0");
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notepad_menu, menu);
        setPassword = menu.findItem(R.id.setPassword);
        removePassword = menu.findItem(R.id.removePassword);
        changePassword = menu.findItem(R.id.changePassword);
        if (prefs.isSetPasswordOnNotepad()) {
            setPassword.setVisible(false);
            removePassword.setVisible(true);
            changePassword.setVisible(true);
        } else {
            removePassword.setVisible(false);
            changePassword.setVisible(false);
            setPassword.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void setNotes() {
        list = new ArrayList<>();
        list = dbhelper.getAllNotes();
        if (list.size() == 0)
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
        mAdapter = new NotepadFilesListAdp(this, list, this);
        recyclerView.setAdapter(mAdapter);
    }

    public void onResume() {
        super.onResume();
        setNotes();
        if (setPassword == null) {
            return;
        }
        if (prefs.isSetPasswordOnNotepad()) {
            setPassword.setVisible(false);
            removePassword.setVisible(true);
            changePassword.setVisible(true);
            return;
        }
        removePassword.setVisible(false);
        changePassword.setVisible(false);
        setPassword.setVisible(true);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Intent intent = new Intent(this, ActCreateNote.class);
        intent.putExtra(ScreenCVEdit.FIELD_ID, list.get(i).getId() + "");
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent intent = new Intent(this, ActPassword.class);
        switch (menuItem.getItemId()) {
            case 16908332:
                finish();
                break;
            case R.id.changePassword:
                intent.putExtra("flag", ActPassword.CHANGE_PASSWORD);
                startActivity(intent);
                break;
            case R.id.removePassword:
                intent.putExtra("flag", ActPassword.REMOVE_PASSWORD);
                startActivity(intent);
                break;
            case R.id.setPassword:
                intent.putExtra("flag", ActPassword.SET_NEW_PASSWORD);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onItemClicked(NotepadItemModel notepadItemModel, int i) {
        Intent intent = new Intent(this, ActCreateNote.class);
        intent.putExtra(ScreenCVEdit.FIELD_ID, notepadItemModel.getId() + "");
        startActivity(intent);
    }

    public void onOptionClicked(View view, NotepadItemModel notepadItemModel, final int i0) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() != R.id.ic_remove) {
                return false;
            }
            new AlertDialog.Builder(ActNotepadList.this).setTitle(getResources().getString(R.string.remove)).setMessage(getResources().getString(R.string.areYouSureToRemove)).setPositiveButton(getResources().getString(R.string.remove), (dialogInterface, i) -> {
                dbhelper.removeNote(list.get(i0).getId() + "");
                dialogInterface.dismiss();
                setNotes();
            }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
            return false;
        });
        popupMenu.inflate(R.menu.menu_notepad_option);
        popupMenu.show();
    }
}
