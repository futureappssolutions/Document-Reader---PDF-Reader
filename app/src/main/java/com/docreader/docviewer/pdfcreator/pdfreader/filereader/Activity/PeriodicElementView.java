package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAppLovinAds;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ElementModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.AtomSystemView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class PeriodicElementView extends BaseActivity {
    public ArrayList<ElementModel> elementModelsList = new ArrayList<>();
    public AtomSystemView atomSystemView;
    public CardView elementColor;
    public ImageView elementImage;
    public TextView atomicMassTv;
    public TextView atomicNumberTv;
    public TextView boilingPointTv;
    public TextView categoryTv;
    public TextView densityTv;
    public TextView discoveredByTv;
    public TextView electronAffinityTv;
    public TextView electronConfigurationSemanticTv;
    public TextView electronConfigurationTv;
    public TextView electronShellTv;
    public TextView electronTv;
    public TextView elementNameTv;
    public TextView elementSummaryTv;
    public TextView elementSymbolTv;
    public TextView gativityPaulingTv;
    public TextView meltingPointTv;
    public TextView molarHeatTv;
    public TextView namedByTv;
    public TextView neutronsTv;
    public TextView nextElementNameTv;
    public TextView periodTv;
    public TextView phaseTv;
    public TextView preElementNameTv;
    public TextView protonsTv;
    public TextView toolBarTitle;
    public int elementNumber = 0;
    public int elementPosition = 0;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_peroidic_element);
        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPrefs prefs = new SharedPrefs(this);
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAppLovinAds.showBannerAds(PeriodicElementView.this, ll_banner);


        toolBarTitle = findViewById(R.id.toolBarTitle);
        atomSystemView = findViewById(R.id.asElectronShell);

        atomSystemView.setOnClickListener(view -> {
            if (atomSystemView.isAnimating()) {
                atomSystemView.stopAnimation();
            } else {
                atomSystemView.startAnimation();
            }
        });

        if (getIntent() != null) {
            elementNumber = Integer.parseInt(getIntent().getStringExtra("elementNumber"));
        }

        elementColor = findViewById(R.id.elementColor);
        elementSymbolTv = findViewById(R.id.elementSymbolTv);
        elementNameTv = findViewById(R.id.elementNameTv);
        categoryTv = findViewById(R.id.categoryTv);
        elementSummaryTv = findViewById(R.id.elementSummaryTv);
        atomicMassTv = findViewById(R.id.atomicMassTv);
        boilingPointTv = findViewById(R.id.boilingPointTv);
        densityTv = findViewById(R.id.densityTv);
        discoveredByTv = findViewById(R.id.discoveredByTv);
        electronShellTv = findViewById(R.id.electronShellTv);
        meltingPointTv = findViewById(R.id.meltingPointTv);
        molarHeatTv = findViewById(R.id.molarHeatTv);
        namedByTv = findViewById(R.id.namedByTv);
        atomicNumberTv = findViewById(R.id.atomicNumberTv);
        periodTv = findViewById(R.id.periodTv);
        phaseTv = findViewById(R.id.phaseTv);
        preElementNameTv = findViewById(R.id.preElementNameTv);
        nextElementNameTv = findViewById(R.id.nextElementNameTv);
        electronTv = findViewById(R.id.electronTv);
        protonsTv = findViewById(R.id.protonsTv);
        neutronsTv = findViewById(R.id.neutronsTv);
        elementImage = findViewById(R.id.elementImage);
        electronConfigurationTv = findViewById(R.id.electronConfigurationTv);
        electronConfigurationSemanticTv = findViewById(R.id.electronConfigurationSemanticTv);
        electronAffinityTv = findViewById(R.id.electronAffinityTv);
        gativityPaulingTv = findViewById(R.id.gativityPaulingTv);

        findViewById(R.id.previousElementLayout).setOnClickListener(view -> {
            if (elementPosition != 0) {
                elementPosition--;
                setData(elementPosition);
            }
        });

        findViewById(R.id.nextElementLayout).setOnClickListener(view -> {
            if (elementPosition < 118) {
                elementPosition++;
                setData(elementPosition);
            }
        });

        loadData();
    }

    public void loadData() {
        int i;
        int i2;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11 = "electron_configuration";
        String str12 = "category";
        String str13 = "ypos";
        String str14 = "boil";
        String str15 = "xpos";
        String str16 = "atomic_mass";
        String str17 = "symbol";
        String str18 = "appearance";
        String str19 = "summary";
        String str20 = "spectral_img";
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset(this)).getJSONArray("elements");
            String str21 = "source";
            int i3 = 0;
            while (i3 < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i3);
                JSONArray jSONArray2 = jSONArray;
                ElementModel elementModel = new ElementModel();
                if (jSONObject.getString("name") != null) {
                    i = i3;
                    elementModel.setName(jSONObject.getString("name"));
                } else {
                    i = i3;
                }
                if (jSONObject.getString(str18) != null) {
                    elementModel.setAppearance(jSONObject.getString(str18));
                }
                if (jSONObject.getString(str16) != null) {
                    elementModel.setAtomic_mass(jSONObject.getString(str16));
                }
                if (jSONObject.getString(str14) != null) {
                    elementModel.setBoil(jSONObject.getString(str14));
                }
                if (jSONObject.getString(str12) != null) {
                    elementModel.setCategory(jSONObject.getString(str12));
                }
                if (jSONObject.getString("density") != null) {
                    elementModel.setDensity(jSONObject.getString("density"));
                }
                if (jSONObject.getString("discovered_by") != null) {
                    elementModel.setDiscovered_by(jSONObject.getString("discovered_by"));
                }
                if (jSONObject.getString("melt") != null) {
                    elementModel.setMelt(jSONObject.getString("melt"));
                }
                if (jSONObject.getString("molar_heat") != null) {
                    elementModel.setMolar_heat(jSONObject.getString("molar_heat"));
                }
                if (jSONObject.getString("named_by") != null) {
                    elementModel.setNamed_by(jSONObject.getString("named_by"));
                }
                if (jSONObject.getString("number") != null) {
                    elementModel.setNumber(jSONObject.getInt("number"));
                }
                if (jSONObject.getString("period") != null) {
                    elementModel.setPeriod(jSONObject.getInt("period"));
                }
                if (jSONObject.getString("phase") != null) {
                    elementModel.setPhase(jSONObject.getString("phase"));
                }
                String str22 = str21;
                if (jSONObject.getString(str22) != null) {
                    str = str12;
                    elementModel.setSource(jSONObject.getString(str22));
                } else {
                    str = str12;
                }
                String str23 = str20;
                if (jSONObject.getString(str23) != null) {
                    str2 = str14;
                    elementModel.setSpectral_img(jSONObject.getString(str23));
                } else {
                    str2 = str14;
                }
                String str24 = str19;
                if (jSONObject.getString(str24) != null) {
                    str3 = str23;
                    elementModel.setSummary(jSONObject.getString(str24));
                } else {
                    str3 = str23;
                }
                String str25 = str17;
                if (jSONObject.getString(str25) != null) {
                    str4 = str24;
                    elementModel.setSymbol(jSONObject.getString(str25));
                } else {
                    str4 = str24;
                }
                String str26 = str15;
                if (jSONObject.getString(str26) != null) {
                    str5 = str25;
                    elementModel.setXpos(jSONObject.getInt(str26));
                } else {
                    str5 = str25;
                }
                String str27 = str13;
                if (jSONObject.getString(str27) != null) {
                    str6 = str26;
                    elementModel.setYpos(jSONObject.getInt(str27));
                } else {
                    str6 = str26;
                }
                String str28 = str11;
                if (jSONObject.getString(str28) != null) {
                    str7 = str27;
                    elementModel.setElectron_configuration(jSONObject.getString(str28));
                } else {
                    str7 = str27;
                }
                if (jSONObject.getString("electron_configuration_semantic") != null) {
                    elementModel.setElectron_configuration_semantic(jSONObject.getString("electron_configuration_semantic"));
                }
                if (jSONObject.getString("electron_affinity") != null) {
                    elementModel.setElectron_affinity(jSONObject.getString("electron_affinity"));
                }
                if (jSONObject.getString("electronegativity_pauling") != null) {
                    elementModel.setElectronegativity_pauling(jSONObject.getString("electronegativity_pauling"));
                }
                if (jSONObject.getString("shells") != null) {
                    JSONArray jSONArray3 = jSONObject.getJSONArray("shells");
                    str10 = str28;
                    ArrayList arrayList = new ArrayList();
                    str9 = str16;
                    str8 = str18;
                    for (int i4 = 0; i4 < jSONArray3.length(); i4++) {
                        arrayList.add(Integer.valueOf(jSONArray3.getInt(i4)));
                    }
                    elementModel.setSells(arrayList);
                } else {
                    str10 = str28;
                    str9 = str16;
                    str8 = str18;
                }
                if (jSONObject.getString("electron_proton_neutron") != null) {
                    JSONArray jSONArray4 = jSONObject.getJSONArray("electron_proton_neutron");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i5 = 0; i5 < jSONArray4.length(); i5++) {
                        arrayList2.add(jSONArray4.getString(i5));
                    }
                    elementModel.setElectron_proton_neutron(arrayList2);
                }
                if (elementModel.getNumber() == elementNumber) {
                    i2 = i;
                    elementPosition = i2;
                } else {
                    i2 = i;
                }
                elementModelsList.add(elementModel);
                str14 = str2;
                jSONArray = jSONArray2;
                str16 = str9;
                str18 = str8;
                str20 = str3;
                str19 = str4;
                str17 = str5;
                str15 = str6;
                str13 = str7;
                str11 = str10;
                String str29 = str22;
                i3 = i2 + 1;
                str12 = str;
                str21 = str29;
            }
            setData(elementPosition);
        } catch (JSONException e2) {
            Utility.logCatMsg("JSONException " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public String loadJSONFromAsset(Context context) {
        try {
            InputStream open = context.getAssets().open("periodicTable/data.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        if (atomSystemView != null) {
            atomSystemView.stopAnimation();
        }
        super.onDestroy();
    }

    public void setData(int i) {
        ElementModel elementModel = elementModelsList.get(i);
        if (elementModel.getName() != null) {
            toolBarTitle.setText(elementModel.getName());
            elementNameTv.setText(elementModel.getName());
        }
        elementModel.getAppearance();
        if (elementModel.getAtomic_mass() != null) {
            atomicMassTv.setText(elementModel.getAtomic_mass());
        }
        if (elementModel.getBoil() != null) {
            boilingPointTv.setText(elementModel.getBoil() + " K");
        }
        if (elementModel.getCategory() != null) {
            categoryTv.setText(elementModel.getCategory());
        }
        if (elementModel.getDensity() != null) {
            densityTv.setText(elementModel.getDensity() + "");
        }
        if (elementModel.getDiscovered_by() != null) {
            discoveredByTv.setText(elementModel.getDiscovered_by());
        }
        if (elementModel.getMelt() != null) {
            meltingPointTv.setText(elementModel.getMelt() + " K");
        }
        if (elementModel.getMolar_heat() != null) {
            molarHeatTv.setText(elementModel.getMolar_heat() + " J/(mol·K)");
        }
        if (elementModel.getNamed_by() != null) {
            namedByTv.setText(elementModel.getNamed_by());
        }
        atomicNumberTv.setText(elementModel.getNumber() + "");
        elementColor.setBackgroundColor(getElementColor(elementModel.getNumber()));
        periodTv.setText(elementModel.getPeriod() + "");
        if (elementModel.getPhase() != null) {
            phaseTv.setText(elementModel.getPhase());
        }
        elementModel.getSpectral_img();
        if (elementModel.getSummary() != null) {
            elementSummaryTv.setText(elementModel.getSummary());
        }
        if (elementModel.getSymbol() != null) {
            elementSymbolTv.setText(elementModel.getSymbol());
        }
        if (elementModel.getElectron_configuration() != null) {
            electronConfigurationTv.setText(elementModel.getElectron_configuration());
        }
        if (elementModel.getElectron_configuration_semantic() != null) {
            electronConfigurationSemanticTv.setText(elementModel.getElectron_configuration_semantic());
        }
        if (elementModel.getElectron_affinity() != null) {
            electronConfigurationTv.setText(elementModel.getElectron_affinity());
        }
        if (elementModel.getElectronegativity_pauling() != null) {
            gativityPaulingTv.setText(elementModel.getElectronegativity_pauling());
        }
        if (elementModel.getSells() != null) {
            String str = "";
            for (int i2 = 0; i2 < elementModel.getSells().size(); i2++) {
                str = str + elementModel.getSells().get(i2) + " ";
            }
            electronShellTv.setText(str);
            atomSystemView.setData(str, elementModel.getSymbol(), getElementColor(elementModel.getNumber()));
            new Handler().postDelayed(() -> atomSystemView.startAnimation(), 1000);
        }
        if (elementModel.getElectron_proton_neutron() != null) {
            for (int i3 = 0; i3 < elementModel.getElectron_proton_neutron().size(); i3++) {
                if (i3 == 0) {
                    electronTv.setText(elementModel.getElectron_proton_neutron().get(i3));
                } else if (i3 == 1) {
                    protonsTv.setText(elementModel.getElectron_proton_neutron().get(i3));
                } else {
                    neutronsTv.setText(elementModel.getElectron_proton_neutron().get(i3));
                }
            }
        }
        if (i != 0) {
            TextView textView = preElementNameTv;
            StringBuilder sb = new StringBuilder();
            int i4 = i - 1;
            sb.append(elementModelsList.get(i4).getName());
            sb.append(" (");
            sb.append(elementModelsList.get(i4).getNumber());
            sb.append(")");
            textView.setText(sb.toString());
        } else {
            preElementNameTv.setText("");
        }
        if (i < 117) {
            TextView textView2 = nextElementNameTv;
            StringBuilder sb2 = new StringBuilder();
            int i5 = i + 1;
            sb2.append(elementModelsList.get(i5).getName());
            sb2.append(" (");
            sb2.append(elementModelsList.get(i5).getNumber());
            sb2.append(")");
            textView2.setText(sb2.toString());
            return;
        }
        nextElementNameTv.setText("");
    }

    public int getElementColor(int i) {
        getResources().getColor(R.color.periodic_color6);
        if (i == 1 || i == 6 || i == 7 || i == 8 || i == 15 || i == 16 || i == 34) {
            return getResources().getColor(R.color.periodic_color6);
        }
        if (i == 3 || i == 11 || i == 19 || i == 37 || i == 55 || i == 77) {
            return getResources().getColor(R.color.periodic_color3);
        }
        if (i == 4 || i == 12 || i == 20 || i == 38 || i == 56 || i == 88) {
            return getResources().getColor(R.color.periodic_color4);
        }
        if (i == 9 || i == 17 || i == 35 || i == 53 || i == 85 || i == 117) {
            return getResources().getColor(R.color.periodic_color1);
        }
        if (i == 2 || i == 10 || i == 18 || i == 36 || i == 54 || i == 86 || i == 188) {
            return getResources().getColor(R.color.periodic_color5);
        }
        if (i == 5 || i == 14 || i == 32 || i == 33 || i == 51 || i == 52 || i == 84) {
            return getResources().getColor(R.color.periodic_color9);
        }
        if (i == 13 || i == 31 || i == 49 || i == 50 || i == 81 || i == 82 || i == 83 || i == 113 || i == 114 || i == 115 || i == 116) {
            return getResources().getColor(R.color.periodic_color7);
        }
        if (i >= 57 && i <= 71) {
            return getResources().getColor(R.color.periodic_color10);
        }
        if (i < 89 || i > 103) {
            return getResources().getColor(R.color.periodic_color2);
        }
        return getResources().getColor(R.color.periodic_color11);
    }
}
