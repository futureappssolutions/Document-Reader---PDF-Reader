package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import com.itextpdf.text.html.HtmlTags;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.picture.Picture;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RtfHtml {
    private List<String> colortbl;
    private List<String> fonttbl;
    private boolean newRootPar;
    private Map<String, Boolean> openedTags;
    private String output;
    private RtfState previousState;
    private RtfState state;
    private Stack<RtfState> states;

    public String format(RtfGroup rtfGroup) {
        return format(rtfGroup, false);
    }

    public String format(RtfGroup rtfGroup, boolean z) {
        previousState = null;
        openedTags = new LinkedHashMap<>();
        openedTags.put(HtmlTags.SPAN, false);
        openedTags.put(HtmlTags.P, true);
        states = new Stack<>();
        state = new RtfState();
        states.push(state);
        output = "<p>";
        newRootPar = true;
        formatGroup(rtfGroup);
        if (z) {
            wrapTags();
        }
        return output;
    }


    public void extractFontTable(List<RtfElement> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        int size = list.size();
        for (int i = 1; i < size; i++) {
            if (list.get(i) instanceof RtfGroup) {
                List<RtfElement> list2 = ((RtfGroup) list.get(i)).children;
                String str = "";
                for (int i2 = 1; i2 < list2.size(); i2++) {
                    RtfElement rtfElement = list2.get(i2);
                    if (rtfElement instanceof RtfControlWord) {
                        RtfControlWord rtfControlWord = (RtfControlWord) rtfElement;
                        if (!rtfControlWord.word.equals("fnil")) {
                            if (rtfControlWord.word.equals("froman")) {
                                str = "Times,serif";
                            } else if (rtfControlWord.word.equals("fswiss")) {
                                str = "Helvetica,Swiss,sans-serif";
                            } else if (rtfControlWord.word.equals("fmodern")) {
                                str = "Courier,monospace";
                            } else if (rtfControlWord.word.equals("fscript")) {
                                str = "Cursive";
                            } else if (rtfControlWord.word.equals("fdecor")) {
                                str = "'ITC Zapf Chancery'";
                            } else if (rtfControlWord.word.equals("ftech")) {
                                str = "Symbol,Wingdings";
                            } else if (rtfControlWord.word.equals("fbidi")) {
                                str = "Miriam";
                            } else if (rtfControlWord.word.equals("fcharset") && rtfControlWord.parameter == 2) {
                                str = "Symbol";
                            }
                        }
                    }
                    if (rtfElement instanceof RtfText) {
                        String str2 = ((RtfText) rtfElement).text;
                        if (!";".equals(str2)) {
                            if (str2.endsWith(";")) {
                                str2 = str2.substring(0, str2.length() - 1);
                            }
                            if (!str.contains(str2)) {
                                if (str.length() > 0) {
                                    str = "," + str;
                                }
                                str = "'" + str2 + "'" + str;
                            }
                        }
                    }
                }
                arrayList.add(str);
            }
        }
        fonttbl = arrayList;
    }


    public void extractColorTable(List<RtfElement> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(null);
        int size = list.size();
        String str = "";
        int i = 2;
        while (i < size) {
            if (list.get(i) instanceof RtfControlWord) {
                int i2 = ((RtfControlWord) list.get(i)).parameter;
                int i3 = ((RtfControlWord) list.get(i + 1)).parameter;
                i += 2;
                str = String.format("#%02x%02x%02x", i2, i3, ((RtfControlWord) list.get(i)).parameter);
            } else if (list.get(i) instanceof RtfText) {
                arrayList.add(str);
            }
            i++;
        }
        colortbl = arrayList;
    }


    public void formatGroup(RtfGroup rtfGroup) {
        if (rtfGroup.getType().equals("fonttbl")) {
            extractFontTable(rtfGroup.children);
        } else if (rtfGroup.getType().equals("colortbl")) {
            extractColorTable(rtfGroup.children);
        } else if (rtfGroup.getType().equals("stylesheet") || rtfGroup.getType().equals("info")) {
        } else {
            if ((rtfGroup.getType().length() < 4 || !rtfGroup.getType().startsWith(Picture.PICT_TYPE)) && !rtfGroup.isDestination()) {
                RtfState rtfState = (RtfState) state.clone();
                state = rtfState;
                states.push(rtfState);
                for (RtfElement next : rtfGroup.children) {
                    if (next instanceof RtfGroup) {
                        formatGroup((RtfGroup) next);
                    } else if (next instanceof RtfControlWord) {
                        formatControlWord((RtfControlWord) next);
                    } else if (next instanceof RtfControlSymbol) {
                        formatControlSymbol((RtfControlSymbol) next);
                    } else if (next instanceof RtfText) {
                        formatText((RtfText) next);
                    }
                }
                states.pop();
                state = states.peek();
            }
        }
    }


    public void formatControlWord(RtfControlWord rtfControlWord) {
        if (rtfControlWord.word.equals("plain") || rtfControlWord.word.equals("pard")) {
            state.reset();
        } else if (rtfControlWord.word.equals("f")) {
            state.font = rtfControlWord.parameter;
        } else {
            boolean z = true;
            if (rtfControlWord.word.equals(HtmlTags.B)) {
                RtfState rtfState = state;
                if (rtfControlWord.parameter <= 0) {
                    z = false;
                }
                rtfState.bold = z;
            } else if (rtfControlWord.word.equals(HtmlTags.I)) {
                RtfState rtfState2 = state;
                if (rtfControlWord.parameter <= 0) {
                    z = false;
                }
                rtfState2.italic = z;
            } else if (rtfControlWord.word.equals(HtmlTags.UL)) {
                RtfState rtfState3 = state;
                if (rtfControlWord.parameter <= 0) {
                    z = false;
                }
                rtfState3.underline = z;
            } else if (rtfControlWord.word.equals("ulnone")) {
                state.underline = false;
            } else if (rtfControlWord.word.equals(HtmlTags.STRIKE)) {
                RtfState rtfState4 = state;
                if (rtfControlWord.parameter <= 0) {
                    z = false;
                }
                rtfState4.strike = z;
            } else if (rtfControlWord.word.equals("v")) {
                RtfState rtfState5 = state;
                if (rtfControlWord.parameter <= 0) {
                    z = false;
                }
                rtfState5.hidden = z;
            } else if (rtfControlWord.word.equals("fs")) {
                RtfState rtfState6 = state;
                double d = rtfControlWord.parameter;
                Double.isNaN(d);
                rtfState6.fontSize = (int) Math.ceil((d / 24.0d) * 16.0d);
            } else if (rtfControlWord.word.equals("dn")) {
                RtfState rtfState7 = state;
                double d2 = rtfControlWord.parameter;
                Double.isNaN(d2);
                rtfState7.dnup = ((int) Math.ceil((d2 / 24.0d) * 16.0d)) * -1;
            } else if (rtfControlWord.word.equals("up")) {
                RtfState rtfState8 = state;
                double d3 = rtfControlWord.parameter;
                Double.isNaN(d3);
                rtfState8.dnup = (int) Math.ceil((d3 / 24.0d) * 16.0d);
            } else if (rtfControlWord.word.equals(HtmlTags.SUB)) {
                state.subscript = true;
                state.superscript = false;
            } else if (rtfControlWord.word.equals("super")) {
                state.subscript = false;
                state.superscript = true;
            } else if (rtfControlWord.word.equals("nosupersub")) {
                state.subscript = false;
                state.superscript = false;
            } else if (rtfControlWord.word.equals("cf")) {
                state.textColor = rtfControlWord.parameter;
            } else if (rtfControlWord.word.equals("cb") || rtfControlWord.word.equals("chcbpat") || rtfControlWord.word.equals("highlight")) {
                state.background = rtfControlWord.parameter;
            } else if (rtfControlWord.word.equals("lquote")) {
                applyStyle("&lsquo;");
            } else if (rtfControlWord.word.equals("rquote")) {
                applyStyle("&rsquo;");
            } else if (rtfControlWord.word.equals("ldblquote")) {
                applyStyle("&ldquo;");
            } else if (rtfControlWord.word.equals("rdblquote")) {
                applyStyle("&rdquo;");
            } else if (rtfControlWord.word.equals("emdash")) {
                applyStyle("&mdash;");
            } else if (rtfControlWord.word.equals("endash")) {
                applyStyle("&ndash;");
            } else if (rtfControlWord.word.equals("emspace")) {
                applyStyle("&emsp;");
            } else if (rtfControlWord.word.equals("enspace")) {
                applyStyle("&ensp;");
            } else if (rtfControlWord.word.equals("tab")) {
                applyStyle("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            } else if (rtfControlWord.word.equals("line")) {
                applyStyle("<br>");
            } else if (rtfControlWord.word.equals("bullet")) {
                applyStyle("&bull;");
            } else if (rtfControlWord.word.equals(HtmlTags.U)) {
                applyStyle("&#" + rtfControlWord.parameter + ";");
            } else if (rtfControlWord.word.equals("par") || rtfControlWord.word.equals("row")) {
                closeTags();
                output += "<p>";
                openedTags.put(HtmlTags.P, true);
                newRootPar = true;
            }
        }
    }


    public void applyStyle(String str) {
        if (!state.equals(previousState) || newRootPar) {
            String str2 = "";
            if (state.font >= 0) {
                str2 = str2 + "font-family:" + printFontFamily(state.font) + ";";
            }
            if (state.bold) {
                str2 = str2 + "font-weight:bold;";
            }
            if (state.italic) {
                str2 = str2 + "font-style:italic;";
            }
            if (state.underline) {
                str2 = str2 + "text-decoration:underline;";
            }
            if (state.strike) {
                str2 = str2 + "text-decoration:strikethrough;";
            }
            if (state.hidden) {
                str2 = str2 + "display:none;";
            }
            if (state.fontSize != 0) {
                str2 = str2 + "font-size:" + state.fontSize + "px;";
            }
            if (state.dnup != 0) {
                str2 = str2 + calculateReducedFontSize() + "vertical-align:" + state.dnup + "px;";
            }
            if (state.subscript) {
                str2 = str2 + calculateReducedFontSize() + "vertical-align:sub;";
            }
            if (state.superscript) {
                str2 = str2 + calculateReducedFontSize() + "vertical-align:super;";
            }
            if (state.textColor != 0) {
                str2 = str2 + "color:" + printColor(state.textColor) + ";";
            }
            if (state.background != 0) {
                str2 = str2 + "background-color:" + printColor(state.background) + ";";
            }
            previousState = (RtfState) state.clone();
            closeTag(HtmlTags.SPAN);
            output += "<span style=\"" + str2 + "\">" + str;
            openedTags.put(HtmlTags.SPAN, true);
        } else {
            output += str;
        }
        newRootPar = false;
    }


    public String calculateReducedFontSize() {
        if (state.fontSize == 0) {
            return "font-size:smaller;";
        }
        double d = state.fontSize;
        Double.isNaN(d);
        return "font-size:" + ((int) Math.ceil((d / 3.0d) * 2.0d)) + "px;";
    }


    public String printFontFamily(int i) {
        return (i < 0 || i >= fonttbl.size()) ? "" : fonttbl.get(i);
    }


    public String printColor(int i) {
        return (i < 1 || i >= colortbl.size()) ? "" : colortbl.get(i);
    }


    public void closeTag(String str) {
        if (openedTags.get(str)) {
            output += "</" + str + ">";
            openedTags.put(str, false);
        }
    }


    public void closeTags() {
        for (String closeTag : openedTags.keySet()) {
            closeTag(closeTag);
        }
    }


    public void wrapTags() {
        output = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta content=\"text/html;charset=UTF-8\" http-equiv=\"content-type\"/>\n" +
                "  </head>\n" +
                "  <body>\n" +
                output + "\n" +
                "  </body>\n" +
                "</html>\n";
    }


    public void formatControlSymbol(RtfControlSymbol rtfControlSymbol) {
        if (rtfControlSymbol.symbol == '\'') {
            applyStyle("&#" + rtfControlSymbol.parameter + ";");
        }
        if (rtfControlSymbol.symbol == '~') {
            output += "&nbsp;";
        }
    }


    public void formatText(RtfText rtfText) {
        applyStyle(rtfText.text);
    }
}
