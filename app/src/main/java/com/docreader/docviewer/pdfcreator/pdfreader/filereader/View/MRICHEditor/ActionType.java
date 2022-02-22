package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.MRICHEditor;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
    NONE(0),
    FAMILY(1),
    SIZE(2),
    LINE_HEIGHT(3),
    FORE_COLOR(4),
    BACK_COLOR(5),
    BOLD(6),
    ITALIC(7),
    UNDERLINE(8),
    SUBSCRIPT(9),
    SUPERSCRIPT(10),
    STRIKETHROUGH(11),
    NORMAL(12),
    H1(13),
    H2(14),
    H3(15),
    H4(16),
    H5(17),
    H6(18),
    JUSTIFY_LEFT(19),
    JUSTIFY_CENTER(20),
    JUSTIFY_RIGHT(21),
    JUSTIFY_FULL(22),
    ORDERED(23),
    UNORDERED(24),
    INDENT(25),
    OUTDENT(26),
    IMAGE(27),
    LINK(28),
    TABLE(29),
    LINE(30),
    BLOCK_QUOTE(31),
    BLOCK_CODE(32),
    CODE_VIEW(33);
    
    private static final Map<Integer, ActionType> actionTypeMap;
    private final int value;

    static {
        actionTypeMap = new HashMap<>();
        for (ActionType actionType : values()) {
            actionTypeMap.put(actionType.getValue(), actionType);
        }
    }

    ActionType(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static ActionType fromInteger(int i) {
        return actionTypeMap.get(i);
    }
}
