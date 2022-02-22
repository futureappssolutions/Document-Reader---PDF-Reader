package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import java.util.Date;

public abstract class AbstractSortComparator {
    protected SortState mSortState;


    public int compareContent(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return 0;
        }
        if (obj == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        Class<?> cls = obj.getClass();
        if (Comparable.class.isAssignableFrom(cls)) {
            return ((Comparable) obj).compareTo(obj2);
        }
        if (cls.getSuperclass() == Number.class) {
            return compare((Number) obj, (Number) obj2);
        }
        if (cls == String.class) {
            return ((String) obj).compareTo((String) obj2);
        }
        if (cls == Date.class) {
            return compare((Date) obj, (Date) obj2);
        }
        if (cls == Boolean.class) {
            return compare((Boolean) obj, (Boolean) obj2);
        }
        return ((String) obj).compareTo((String) obj2);
    }

    public int compare(Number number, Number number2) {
        return Double.compare(number.doubleValue(), number2.doubleValue());
    }

    public int compare(Date date, Date date2) {
        return (date.getTime() > date2.getTime() ? 1 : (date.getTime() == date2.getTime() ? 0 : -1));
    }

    public int compare(Boolean bool, Boolean bool2) {
        return AbstractSortComparator$$ExternalSynthetic0.m485m0(bool.booleanValue(), bool2.booleanValue());
    }
}
