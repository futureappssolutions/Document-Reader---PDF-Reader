package com.docreader.docviewer.pdfcreator.pdfreader.filereader.InvoiceMaker.IVGetSet;

import java.util.ArrayList;

public class Currency {
    String code;
    String currencyName;
    String symbol;

    public Currency() {
    }

    public Currency(String str, String str2, String str3) {
        this.code = str;
        this.symbol = str2;
        this.currencyName = str3;
    }

    public String getCode() {
        return this.code;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public ArrayList<Currency> getAllCurrency() {
        ArrayList<Currency> arrayList = new ArrayList<>();
        arrayList.add(new Currency("AED", "د.إ", "UAE dirham"));
        arrayList.add(new Currency("AFN", "؋", "Afghan afghani"));
        arrayList.add(new Currency("ALL", "L", "Albanian lek"));
        arrayList.add(new Currency("AMD", "դր", "Armenian dram"));
        arrayList.add(new Currency("ANG", "৳", "Netherlands Antillean gulden"));
        arrayList.add(new Currency("AOA", "Kz", "Angolan kwanza"));
        arrayList.add(new Currency("ARS", "$", "Argentine peso"));
        arrayList.add(new Currency("AUD", "$", "Australian dollar"));
        arrayList.add(new Currency("AWG", "Afl", "Aruban florin"));
        arrayList.add(new Currency("AZN", "AZN", "Azerbaijani manat"));
        arrayList.add(new Currency("BAM", "KM", "Bosnia and Herzegovina konvertibilna marka"));
        arrayList.add(new Currency("BBD", "$", "Barbadian dollar"));
        arrayList.add(new Currency("BDT", "৳", "Bangladeshi taka"));
        arrayList.add(new Currency("BGN", "лв", "Bulgarian lev"));
        arrayList.add(new Currency("BHD", ".د.ب", "Bahraini dinar"));
        arrayList.add(new Currency("BIF", "FBu", "Burundi franc"));
        arrayList.add(new Currency("BMD", "$", "Bermudian dollar"));
        arrayList.add(new Currency("BND", "$", "Brunei dollar"));
        arrayList.add(new Currency("BOB", "Bs", "Bolivian boliviano"));
        arrayList.add(new Currency("BRL", "R$", "Brazilian real"));
        arrayList.add(new Currency("BSD", "$", "Bahamian dollar"));
        arrayList.add(new Currency("BTN", "Nu", "Bhutanese ngultrum"));
        arrayList.add(new Currency("BWP", "P", "Botswana pula"));
        arrayList.add(new Currency("BYR", "Br", "Belarusian ruble"));
        arrayList.add(new Currency("BZD", "$", "Belize dollar"));
        arrayList.add(new Currency("CAD", "$", "Canadian dollar"));
        arrayList.add(new Currency("CDF", "FC", "Congolese franc"));
        arrayList.add(new Currency("CHF", "Fr", "Swiss franc"));
        arrayList.add(new Currency("CLP", "$", "Chilean peso"));
        arrayList.add(new Currency("CNY", "¥", "Chinese/Yuan renminbi"));
        arrayList.add(new Currency("COP", "$", "Colombian peso"));
        arrayList.add(new Currency("CRC", "₡", "Costa Rican colon"));
        arrayList.add(new Currency("CUC", "$", "Cuban peso"));
        arrayList.add(new Currency("CVE", "Esc", "Cape Verdean escudo"));
        arrayList.add(new Currency("CZK", "Kč", "Czech koruna"));
        arrayList.add(new Currency("DJF", "Fdj", "Djiboutian franc"));
        arrayList.add(new Currency("DKK", "Kr", "Danish krone"));
        arrayList.add(new Currency("DOP", "RD$", "Dominican peso"));
        arrayList.add(new Currency("DZD", "د.ج", "Algerian dinar"));
        arrayList.add(new Currency("EEK", "KR", "Estonian kroon"));
        arrayList.add(new Currency("EGP", "£", "Egyptian pound"));
        arrayList.add(new Currency("ERN", "Nfa", "Eritrean nakfa"));
        arrayList.add(new Currency("ETB", "Br", "Ethiopian birr"));
        arrayList.add(new Currency("EUR", "€", "European Euro"));
        arrayList.add(new Currency("FJD", "FJ$", "Fijian dollar"));
        arrayList.add(new Currency("FKP", "£", "Falkland Islands pound"));
        arrayList.add(new Currency("GBP", "£", "British pound"));
        arrayList.add(new Currency("GEL", "GEL", "Georgian lari"));
        arrayList.add(new Currency("GHS", "GH₵", "Ghanaian cedi"));
        arrayList.add(new Currency("GIP", "£", "Gibraltar pound"));
        arrayList.add(new Currency("GMD", "D", "Gambian dalasi"));
        arrayList.add(new Currency("GNF", "FG", "Guinean franc"));
        arrayList.add(new Currency("GQE", "CFA", "Central African CFA franc"));
        arrayList.add(new Currency("GTQ", "Q", "Guatemalan quetzal"));
        arrayList.add(new Currency("GYD", "GY$", "Guyanese dollar"));
        arrayList.add(new Currency("HKD", "HK$", "Hong Kong dollar"));
        arrayList.add(new Currency("HNL", "L", "Honduran lempira"));
        arrayList.add(new Currency("HRK", "kn", "Croatian kuna"));
        arrayList.add(new Currency("HTG", "G", "Haitian gourde"));
        arrayList.add(new Currency("HUF", "Ft", "Hungarian forint"));
        arrayList.add(new Currency("IDR", "Rp", "Indonesian rupiah"));
        arrayList.add(new Currency("ILS", "₪", "Israeli new sheqel"));
        arrayList.add(new Currency("INR", "₹", "Indian rupee"));
        arrayList.add(new Currency("IQD", "د.ع", "Iraqi dinar"));
        arrayList.add(new Currency("IRR", "IRR", "Iranian rial"));
        arrayList.add(new Currency("ISK", "kr", "Icelandic króna"));
        arrayList.add(new Currency("JMD", "J$", "Jamaican dollar"));
        arrayList.add(new Currency("JOD", "JOD", "Jordanian dinar"));
        arrayList.add(new Currency("JPY", "¥", "Japanese yen"));
        arrayList.add(new Currency("KES", "KSh", "Kenyan shilling"));
        arrayList.add(new Currency("KGS", "сом", "Kyrgyzstani som"));
        arrayList.add(new Currency("KHR", "៛", "Cambodian riel"));
        arrayList.add(new Currency("KMF", "KMF", "Comorian franc"));
        arrayList.add(new Currency("KPW", "W", "North Korean won"));
        arrayList.add(new Currency("KRW", "₩", "South Korean won"));
        arrayList.add(new Currency("KWD", "KWD", "Kuwaiti dinar"));
        arrayList.add(new Currency("KYD", "KY$", "Cayman Islands dollar"));
        arrayList.add(new Currency("KZT", "T", "Kazakhstani tenge"));
        arrayList.add(new Currency("LAK", "KN", "Lao kip"));
        arrayList.add(new Currency("LBP", "£", "Lebanese lira"));
        arrayList.add(new Currency("LKR", "Rs", "Sri Lankan rupee"));
        arrayList.add(new Currency("LRD", "L$", "Liberian dollar"));
        arrayList.add(new Currency("LSL", "M", "Lesotho loti"));
        arrayList.add(new Currency("LTL", "Lt", "Lithuanian litas"));
        arrayList.add(new Currency("LVL", "Ls", "Latvian lats"));
        arrayList.add(new Currency("LYD", "LD", "Libyan dinar"));
        arrayList.add(new Currency("MAD", "MAD", "Moroccan dirham"));
        arrayList.add(new Currency("MDL", "MDL", "Moldovan leu"));
        arrayList.add(new Currency("MGA", "FMG", "Malagasy ariary"));
        arrayList.add(new Currency("MKD", "MKD", "Macedonian denar"));
        arrayList.add(new Currency("MMK", "K", "Myanma kyat"));
        arrayList.add(new Currency("MNT", "₮", "Mongolian tugrik"));
        arrayList.add(new Currency("MOP", "P", "Macanese pataca"));
        arrayList.add(new Currency("MRO", "UM", "Mauritanian ouguiya"));
        arrayList.add(new Currency("MUR", "Rs", "Mauritian rupee"));
        arrayList.add(new Currency("MVR", "Rf", "Maldivian rufiyaa"));
        arrayList.add(new Currency("MWK", "MK", "Malawian kwacha"));
        arrayList.add(new Currency("MXN", "$", "Mexican peso"));
        arrayList.add(new Currency("MYR", "RM", "Malaysian ringgit"));
        arrayList.add(new Currency("MZM", "MTn", "Mozambican metical"));
        arrayList.add(new Currency("NAD", "N$", "Namibian dollar"));
        arrayList.add(new Currency("NGN", "₦", "Nigerian naira"));
        arrayList.add(new Currency("NIO", "C$", "Nicaraguan córdoba"));
        arrayList.add(new Currency("NOK", "kr", "Norwegian krone"));
        arrayList.add(new Currency("NPR", "NRs", "Nepalese rupee"));
        arrayList.add(new Currency("NZD", "NZ$", "New Zealand dollar"));
        arrayList.add(new Currency("OMR", "OMR", "Omani rial"));
        arrayList.add(new Currency("PAB", "B./", "Panamanian balboa"));
        arrayList.add(new Currency("PEN", "S/", "Peruvian nuevo sol"));
        arrayList.add(new Currency("PGK", "K", "Papua New Guinean kina"));
        arrayList.add(new Currency("PHP", "₱", "Philippine peso"));
        arrayList.add(new Currency("PKR", "Rs", "Pakistani rupee"));
        arrayList.add(new Currency("PLN", "zł", "Polish zloty"));
        arrayList.add(new Currency("PYG", "₲", "Paraguayan guarani"));
        arrayList.add(new Currency("QAR", "QR", "Qatari riyal"));
        arrayList.add(new Currency("RON", "L", "Romanian leu"));
        arrayList.add(new Currency("RSD", "din", "Serbian dinar"));
        arrayList.add(new Currency("RUB", "R", "Russian ruble"));
        arrayList.add(new Currency("SAR", "SR", "Saudi riyal"));
        arrayList.add(new Currency("SBD", "SI$", "Solomon Islands dollar"));
        arrayList.add(new Currency("SCR", "SR", "Seychellois rupee"));
        arrayList.add(new Currency("SDG", "SDG", "Sudanese pound"));
        arrayList.add(new Currency("SEK", "kr", "Swedish krona"));
        arrayList.add(new Currency("SGD", "S$", "Singapore dollar"));
        arrayList.add(new Currency("SHP", "£", "Saint Helena pound"));
        arrayList.add(new Currency("SLL", "Le", "Sierra Leonean leone"));
        arrayList.add(new Currency("SOS", "Sh", "Somali shilling"));
        arrayList.add(new Currency("SRD", "$", "Surinamese dollar"));
        arrayList.add(new Currency("SYP", "LS", "Syrian pound"));
        arrayList.add(new Currency("SZL", "E", "Swazi lilangeni"));
        arrayList.add(new Currency("THB", "฿", "Thai baht"));
        arrayList.add(new Currency("TJS", "TJS", "Tajikistani somoni"));
        arrayList.add(new Currency("TMT", "m", "Turkmen manat"));
        arrayList.add(new Currency("TND", "DT", "Tunisian dinar"));
        arrayList.add(new Currency("TRY", "TRY", "Turkish new lira"));
        arrayList.add(new Currency("TTD", "TT$", "Trinidad and Tobago dollar"));
        arrayList.add(new Currency("TWD", "NT$", "New Taiwan dollar"));
        arrayList.add(new Currency("TZS", "TZS", "Tanzanian shilling"));
        arrayList.add(new Currency("UAH", "UAH", "Ukrainian hryvnia"));
        arrayList.add(new Currency("UGX", "USh", "Ugandan shilling"));
        arrayList.add(new Currency("USD", "$", "United States dollar"));
        arrayList.add(new Currency("UYU", "$U", "Uruguayan peso"));
        arrayList.add(new Currency("UZS", "UZS", "Uzbekistani som"));
        arrayList.add(new Currency("VEB", "Bs", "Venezuelan bolivar"));
        arrayList.add(new Currency("VND", "₫", "Vietnamese dong"));
        arrayList.add(new Currency("VUV", "VT", "Vanuatu vatu"));
        arrayList.add(new Currency("WST", "WS$", "Samoan tala"));
        arrayList.add(new Currency("XAF", "CFA", "Central African CFA franc"));
        arrayList.add(new Currency("XCD", "EC$", "East Caribbean dollar"));
        arrayList.add(new Currency("XDR", "SDR", "Special Drawing Rights"));
        arrayList.add(new Currency("XOF", "CFA", "West African CFA franc"));
        arrayList.add(new Currency("XPF", "F", "CFP franc"));
        arrayList.add(new Currency("YER", "YER", "Yemeni rial"));
        arrayList.add(new Currency("ZAR", "R", "South African rand"));
        arrayList.add(new Currency("ZMK", "ZK", "Zambian kwacha"));
        arrayList.add(new Currency("ZWR", "Z$", "Zimbabwean dollar"));
        return arrayList;
    }
}
