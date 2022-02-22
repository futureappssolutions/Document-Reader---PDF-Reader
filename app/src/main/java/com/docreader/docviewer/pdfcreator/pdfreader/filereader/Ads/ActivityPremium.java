package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityPremium extends BaseActivity {
    public String strWeek, strMonth, strYear;
    public TextView txtWeekA, txtMonthA, txtYearA;
    public TextView txtWeekP, txtMonthP, txtYearP;
    public RadioButton rbWeek, rbMonth, rbYear;
    public LinearLayout llWeekly, llMonthly, llYearly;
    public SharedPrefs pref;
    public TextView toolBarTitle;
    public AppCompatButton btnBuyNow;
    public BillingClient billingClient;
    public int purchase_type = 0;
    RelativeLayout rvColorWeek, rvColorMonth, rvColorYear;
    CardView cardWeek, cardMonth, cardYear;

    PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    };


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        changeBackGroundColor(100);

        toolBarTitle = findViewById(R.id.toolBarTitle);
        txtWeekA = findViewById(R.id.txtWeekA);
        txtMonthA = findViewById(R.id.txtMonthA);
        txtYearA = findViewById(R.id.txtYearA);
        txtWeekP = findViewById(R.id.txtWeekP);
        txtMonthP = findViewById(R.id.txtMonthP);
        txtYearP = findViewById(R.id.txtYearP);
        rbWeek = findViewById(R.id.rbWeek);
        rbMonth = findViewById(R.id.rbMonth);
        rbYear = findViewById(R.id.rbYear);
        llWeekly = findViewById(R.id.llWeekly);
        llMonthly = findViewById(R.id.llMonthly);
        llYearly = findViewById(R.id.llYearly);
        rvColorWeek = findViewById(R.id.rvColorWeek);
        rvColorMonth = findViewById(R.id.rvColorMonth);
        rvColorYear = findViewById(R.id.rvColorYear);
        cardWeek = findViewById(R.id.cardWeek);
        cardMonth = findViewById(R.id.cardMonth);
        cardYear = findViewById(R.id.cardYear);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        toolBarTitle.setText(Html.fromHtml("Premium"));

        pref = new SharedPrefs(ActivityPremium.this);

        strWeek = pref.getRemove_ads_weekly();
        strMonth = pref.getRemove_ads_monthly();
        strYear = pref.getRemove_ads_yearly();

        llYearly.setSelected(true);
        rbYear.setChecked(true);
        rvColorMonth.setBackgroundColor(0x00000000);
        rvColorWeek.setBackgroundColor(0x00000000);
        rvColorYear.setBackgroundColor(ContextCompat.getColor(this, R.color.notepad_color_transparent5));

        ActiveWeekly();
        ActiveMonthly();
        ActiveYearly();

        rvColorWeek.setOnClickListener(v -> {
            purchase_type = 1;
            llWeekly.setSelected(true);
            llMonthly.setSelected(false);
            llYearly.setSelected(false);
            rbWeek.setChecked(true);
            rbMonth.setChecked(false);
            rbYear.setChecked(false);
            rvColorWeek.setBackgroundColor(ContextCompat.getColor(this, R.color.notepad_color_transparent5));
            rvColorMonth.setBackgroundColor(0x00000000);
            rvColorYear.setBackgroundColor(0x00000000);
        });

        rvColorMonth.setOnClickListener(v -> {
            purchase_type = 2;
            llWeekly.setSelected(false);
            llMonthly.setSelected(true);
            llYearly.setSelected(false);
            rbWeek.setChecked(false);
            rbMonth.setChecked(true);
            rbYear.setChecked(false);
            rvColorWeek.setBackgroundColor(0x00000000);
            rvColorMonth.setBackgroundColor(ContextCompat.getColor(this, R.color.notepad_color_transparent5));
            rvColorYear.setBackgroundColor(0x00000000);
        });

        rvColorYear.setOnClickListener(v -> {
            purchase_type = 3;
            llWeekly.setSelected(false);
            llMonthly.setSelected(false);
            llYearly.setSelected(true);
            rbWeek.setChecked(false);
            rbMonth.setChecked(false);
            rbYear.setChecked(true);
            rvColorMonth.setBackgroundColor(0x00000000);
            rvColorWeek.setBackgroundColor(0x00000000);
            rvColorYear.setBackgroundColor(ContextCompat.getColor(this, R.color.notepad_color_transparent5));
        });

        btnBuyNow.setOnClickListener(v -> {
            if (purchase_type == 1) {
                if (pref.getActive_Weekly().equals("true")) {
                    Toast.makeText(ActivityPremium.this, "Your Weekly Plan Already Activated", Toast.LENGTH_SHORT).show();
                } else {
                    purchase(strWeek);
                }
            } else if (purchase_type == 2) {
                if (pref.getActive_Monthly().equals("true")) {
                    Toast.makeText(ActivityPremium.this, "Your Monthly Plan Already Activated", Toast.LENGTH_SHORT).show();
                } else {
                    purchase(strMonth);
                }
            } else if (purchase_type == 3) {
                if (pref.getActive_Yearly().equals("true")) {
                    Toast.makeText(ActivityPremium.this, "Your Yearly Plan Already Activated", Toast.LENGTH_SHORT).show();
                } else {
                    purchase(strYear);
                }
            } else {
                purchase(strYear);
            }
        });

        billingClient = BillingClient.newBuilder(ActivityPremium.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getSkuDetails();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    private void ActiveWeekly() {
        if (pref.getActive_Weekly().equals("true")) {
            llWeekly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
            // txtWeekA.setVisibility(View.VISIBLE);
        }
    }

    private void ActiveMonthly() {
        if (pref.getActive_Monthly().equals("true")) {
            llMonthly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
            //  txtMonthA.setVisibility(View.VISIBLE);
        }
    }

    private void ActiveYearly() {
        if (pref.getActive_Yearly().equals("true")) {
            llYearly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
            //txtYearA.setVisibility(View.VISIBLE);
        }
    }

    private void getSkuDetails() {
        List<String> skuList_sub = new ArrayList<>();
        skuList_sub.add(strWeek);
        skuList_sub.add(strMonth);
        skuList_sub.add(strYear);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList_sub).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getSku().equals(strWeek)) {
                        txtWeekP.setText(Html.fromHtml(list.get(i).getPrice()));
                    } else if (list.get(i).getSku().equals(strMonth)) {
                        txtMonthP.setText(Html.fromHtml(list.get(i).getPrice()));
                    } else if (list.get(i).getSku().equals(strYear)) {
                        txtYearP.setText(Html.fromHtml(list.get(i).getPrice()));
                    } else {
                        Log.e("PriceList", String.valueOf(list));
                        Toast.makeText(ActivityPremium.this, String.valueOf(list), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void purchase(String stringSku) {
        List<String> skuList = new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        if (billingClient != null) {
            skuList.add(stringSku);
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        } else {
            Toast.makeText(ActivityPremium.this, "Billing not initialized", Toast.LENGTH_SHORT).show();
        }

        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(list.get(0)).build();
                int responseCode = billingClient.launchBillingFlow(ActivityPremium.this, billingFlowParams).getResponseCode();
            }
        });
    }

    private void handlePurchase(Purchase purchase) {
        List<String> strWeekNew = new ArrayList<>();
        strWeekNew.add(pref.getRemove_ads_weekly());

        List<String> strMonthNew = new ArrayList<>();
        strMonthNew.add(pref.getRemove_ads_monthly());

        List<String> strYearNew = new ArrayList<>();
        strYearNew.add(pref.getRemove_ads_yearly());

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        if (strWeekNew.equals(purchase.getSkus())) {
                            pref.setActive_Weekly("true");
                            //  txtWeekA.setVisibility(View.VISIBLE);
                            llWeekly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
                        } else if (strMonthNew.equals(purchase.getSkus())) {
                            pref.setActive_Monthly("true");
                            //  txtMonthA.setVisibility(View.VISIBLE);
                            llMonthly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
                        } else if (strYearNew.equals(purchase.getSkus())) {
                            pref.setActive_Yearly("true");
                            //txtYearA.setVisibility(View.VISIBLE);
                            llYearly.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_purchase));
                        } else {
                            pref.setActive_Weekly("");
                            pref.setActive_Monthly("");
                            pref.setActive_Yearly("");
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
