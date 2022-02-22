package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RTFView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.picture.Picture;

import java.util.ArrayList;
import java.util.List;

public class RtfGroup extends RtfElement {
    public List<RtfElement> children = new ArrayList<>();
    public RtfGroup parent = null;

    public String getType() {
        if (children.isEmpty()) {
            return "";
        }
        RtfElement rtfElement = children.get(0);
        if (!(rtfElement instanceof RtfControlWord)) {
            return "";
        }
        return ((RtfControlWord) rtfElement).word;
    }

    public boolean isDestination() {
        if (children.isEmpty()) {
            return false;
        }
        RtfElement rtfElement = children.get(0);
        if ((rtfElement instanceof RtfControlSymbol) && ((RtfControlSymbol) rtfElement).symbol == '*') {
            return true;
        }
        return false;
    }

    public void dump() {
        dump(0);
    }

    public void dump(int i) {
        System.out.println("<div>");
        indent(i);
        System.out.println("{");
        System.out.println("</div>");
        for (RtfElement next : children) {
            if (next instanceof RtfGroup) {
                RtfGroup rtfGroup = (RtfGroup) next;
                if (!rtfGroup.getType().equals("fonttbl") && !rtfGroup.getType().equals("colortbl") && !rtfGroup.getType().equals("stylesheet") && !rtfGroup.getType().equals("info")) {
                    if (rtfGroup.getType().length() >= 4) {
                        if (rtfGroup.getType().startsWith(Picture.PICT_TYPE)) {
                        }
                    }
                    if (rtfGroup.isDestination()) {
                    }
                }
            }
            next.dump(i + 2);
        }
        System.out.println("<div>");
        indent(i);
        System.out.println("}");
        System.out.println("</div>");
    }
}
