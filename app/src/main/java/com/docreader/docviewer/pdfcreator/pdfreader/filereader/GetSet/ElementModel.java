package com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet;

import java.util.ArrayList;

public class ElementModel {
    String appearance;
    String atomic_mass;
    String boil;
    String category;
    String density;
    String discovered_by;
    String electron_affinity;
    String electron_configuration;
    String electron_configuration_semantic;
    ArrayList<String> electron_proton_neutron;
    String electronegativity_pauling;
    String melt;
    String molar_heat;
    String name;
    String named_by;
    String phase;
    ArrayList<Integer> sells;
    String source;
    String spectral_img;
    String summary;
    String symbol;
    int xpos;
    int ypos;
    int number;
    int period;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getAppearance() {
        return this.appearance;
    }

    public void setAppearance(String str) {
        this.appearance = str;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public String getDiscovered_by() {
        return this.discovered_by;
    }

    public void setDiscovered_by(String str) {
        this.discovered_by = str;
    }

    public String getNamed_by() {
        return this.named_by;
    }

    public void setNamed_by(String str) {
        this.named_by = str;
    }

    public String getPhase() {
        return this.phase;
    }

    public void setPhase(String str) {
        this.phase = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getSpectral_img() {
        return this.spectral_img;
    }

    public void setSpectral_img(String str) {
        this.spectral_img = str;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String str) {
        this.symbol = str;
    }

    public String getElectron_configuration() {
        return this.electron_configuration;
    }

    public void setElectron_configuration(String str) {
        this.electron_configuration = str;
    }

    public String getElectron_configuration_semantic() {
        return this.electron_configuration_semantic;
    }

    public void setElectron_configuration_semantic(String str) {
        this.electron_configuration_semantic = str;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int i) {
        this.number = i;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int i) {
        this.period = i;
    }

    public int getXpos() {
        return this.xpos;
    }

    public void setXpos(int i) {
        this.xpos = i;
    }

    public int getYpos() {
        return this.ypos;
    }

    public void setYpos(int i) {
        this.ypos = i;
    }

    public String getAtomic_mass() {
        return this.atomic_mass;
    }

    public void setAtomic_mass(String str) {
        this.atomic_mass = str;
    }

    public String getBoil() {
        return this.boil;
    }

    public void setBoil(String str) {
        this.boil = str;
    }

    public String getDensity() {
        return this.density;
    }

    public void setDensity(String str) {
        this.density = str;
    }

    public String getMelt() {
        return this.melt;
    }

    public void setMelt(String str) {
        this.melt = str;
    }

    public String getMolar_heat() {
        return this.molar_heat;
    }

    public void setMolar_heat(String str) {
        this.molar_heat = str;
    }

    public String getElectron_affinity() {
        return this.electron_affinity;
    }

    public void setElectron_affinity(String str) {
        this.electron_affinity = str;
    }

    public String getElectronegativity_pauling() {
        return this.electronegativity_pauling;
    }

    public void setElectronegativity_pauling(String str) {
        this.electronegativity_pauling = str;
    }

    public ArrayList<Integer> getSells() {
        return this.sells;
    }

    public void setSells(ArrayList<Integer> arrayList) {
        this.sells = arrayList;
    }

    public ArrayList<String> getElectron_proton_neutron() {
        return this.electron_proton_neutron;
    }

    public void setElectron_proton_neutron(ArrayList<String> arrayList) {
        this.electron_proton_neutron = arrayList;
    }
}
