package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;

import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.PaintKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.ExtendPath;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.BackgroundAndFill;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.Gradient;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.LinearGradientShader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.RadialGradientShader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.TileShader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bookmark.Bookmark;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.borders.Border;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.borders.Borders;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.borders.Line;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bulletnumber.ListData;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bulletnumber.ListLevel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.hyperlink.Hyperlink;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.picture.Picture;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.pictureefftect.PictureEffectInfoFactory;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.AbstractShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.Arrow;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.GroupShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.IShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.LineShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.PictureShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.ShapeTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPAutoShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPGroupShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPPictureShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.wp.WPModelConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.FCKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ShapeKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ddf.EscherContainerRecord;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ddf.EscherOptRecord;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ddf.EscherSimpleProperty;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ddf.EscherTextboxRecord;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.HWPFDocument;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.FieldsDocumentPart;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.ListFormatOverride;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.ListTables;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.POIListData;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.POIListLevel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.model.PicturesTable;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Bookmarks;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.BorderCode;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.CharacterRun;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Field;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.HWPFAutoShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.HWPFShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.HWPFShapeGroup;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.HeaderStories;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.InlineWordArt;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.LineSpacingDescriptor;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.OfficeDrawing;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.POIBookmark;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Paragraph;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.PictureType;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Range;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Section;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Table;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.TableCell;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.TableRow;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.util.LittleEndian;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.awt.Color;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.awt.Rectangle;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.util.Arrays;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.font.FontTypefaceManage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.AttrManage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.IAttributeSet;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.IElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.LeafElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.ParagraphElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.SectionElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.ss.util.ModelUtil;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.AbstractReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IControl;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.CellElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.HFElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.RowElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.TableElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.WPDocument;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DOCReader extends AbstractReader {
    private List<Bookmark> bms = new ArrayList();
    private long docRealOffset;
    private String filePath;
    private String hyperlinkAddress;
    private Pattern hyperlinkPattern = Pattern.compile("[ \\t\\r\\n]*HYPERLINK \"(.*)\"[ \\t\\r\\n]*");
    private boolean isBreakChar;
    private long offset;
    private HWPFDocument poiDoc;
    private long textboxIndex;
    private WPDocument wpdoc;

    private int converterColorForIndex(short s) {
        switch (s) {
            case 1:
                return -16777216;
            case 2:
                return -16776961;
            case 3:
                return -16711681;
            case 4:
                return -16711936;
            case 5:
                return -65281;
            case 6:
                return SupportMenu.CATEGORY_MASK;
            case 7:
                return InputDeviceCompat.SOURCE_ANY;
            case 9:
                return -16776961;
            case 10:
                return -12303292;
            case 11:
                return -16711936;
            case 12:
                return -65281;
            case 13:
                return SupportMenu.CATEGORY_MASK;
            case 14:
                return InputDeviceCompat.SOURCE_ANY;
            case 15:
                return -7829368;
            case 16:
                return -3355444;
            default:
                return -1;
        }
    }

    private byte converterParaHorAlign(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 5) {
                    if (i != 8) {
                        return 0;
                    }
                }
            }
            return 2;
        }
        return 1;
    }

    public DOCReader(IControl iControl, String str) {
        this.control = iControl;
        this.filePath = str;
    }

    public Object getModel() throws Exception {
        WPDocument wPDocument = this.wpdoc;
        if (wPDocument != null) {
            return wPDocument;
        }
        this.wpdoc = new WPDocument();
        processDoc();
        return this.wpdoc;
    }

    private void processDoc() throws Exception {
        IElement leaf;
        String text;
        this.poiDoc = new HWPFDocument(new FileInputStream(this.filePath));
        processBulletNumber();
        processBookmark();
        this.offset = 0;
        this.docRealOffset = 0;
        Range range = this.poiDoc.getRange();
        int numSections = range.numSections();
        for (int i = 0; i < numSections && !this.abortReader; i++) {
            processSection(range.getSection(i));
            if (this.isBreakChar && (leaf = this.wpdoc.getLeaf(this.offset - 1)) != null && (leaf instanceof LeafElement) && (text = leaf.getText(this.wpdoc)) != null && text.length() == 1 && text.charAt(0) == 12) {
                ((LeafElement) leaf).setText(String.valueOf(10));
            }
        }
        processHeaderFooter();
    }

    private void processBookmark() {
        Bookmarks bookmarks = this.poiDoc.getBookmarks();
        if (bookmarks != null) {
            for (int i = 0; i < bookmarks.getBookmarksCount(); i++) {
                POIBookmark bookmark = bookmarks.getBookmark(i);
                Bookmark bookmark2 = new Bookmark(bookmark.getName(), (long) bookmark.getStart(), (long) bookmark.getEnd());
                this.control.getSysKit().getBookmarkManage().addBookmark(bookmark2);
                this.bms.add(bookmark2);
            }
        }
    }

    private void processBulletNumber() {
        ListTables listTables = this.poiDoc.getListTables();
        if (listTables != null) {
            int overrideCount = listTables.getOverrideCount();
            int i = 0;
            while (i < overrideCount) {
                ListData listData = new ListData();
                i++;
                POIListData listData2 = listTables.getListData(listTables.getOverride(i).getLsid());
                if (listData2 != null) {
                    listData.setListID(listData2.getLsid());
                    POIListLevel[] levels = listData2.getLevels();
                    int length = levels.length;
                    ListLevel[] listLevelArr = new ListLevel[length];
                    for (int i2 = 0; i2 < length; i2++) {
                        listLevelArr[i2] = new ListLevel();
                        processListLevel(levels[i2], listLevelArr[i2]);
                    }
                    listData.setLevels(listLevelArr);
                    listData.setSimpleList((byte) length);
                    this.control.getSysKit().getListManage().putListData(Integer.valueOf(listData.getListID()), listData);
                }
            }
        }
    }

    private void processListLevel(POIListLevel pOIListLevel, ListLevel listLevel) {
        listLevel.setStartAt(pOIListLevel.getStartAt());
        listLevel.setAlign((byte) pOIListLevel.getAlignment());
        listLevel.setFollowChar(pOIListLevel.getTypeOfCharFollowingTheNumber());
        listLevel.setNumberFormat(pOIListLevel.getNumberFormat());
        listLevel.setNumberText(converterNumberChar(pOIListLevel.getNumberChar()));
        listLevel.setSpecialIndent(pOIListLevel.getSpecialIndnet());
        listLevel.setTextIndent(pOIListLevel.getTextIndent());
    }

    private char[] converterNumberChar(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        for (int i = 0; i < cArr.length; i++) {
            if (cArr[i] == 61548) {
                cArr[i] = 9679;
            } else if (cArr[i] == 61550) {
                cArr[i] = 9632;
            } else if (cArr[i] == 61557) {
                cArr[i] = 9670;
            } else if (cArr[i] == 61692) {
                cArr[i] = 8730;
            } else if (cArr[i] == 61656) {
                cArr[i] = 9733;
            } else if (cArr[i] == 61618) {
                cArr[i] = 9734;
            } else if (cArr[i] >= 61536) {
                cArr[i] = 9679;
            }
        }
        return cArr;
    }

    private void processSection(Section section) {
        SectionElement sectionElement = new SectionElement();
        IAttributeSet attribute = sectionElement.getAttribute();
        AttrManage.instance().setPageWidth(attribute, section.getPageWidth());
        AttrManage.instance().setPageHeight(attribute, section.getPageHeight());
        AttrManage.instance().setPageMarginLeft(attribute, section.getMarginLeft());
        AttrManage.instance().setPageMarginRight(attribute, section.getMarginRight());
        AttrManage.instance().setPageMarginTop(attribute, section.getMarginTop());
        AttrManage.instance().setPageMarginBottom(attribute, section.getMarginBottom());
        AttrManage.instance().setPageHeaderMargin(attribute, section.getMarginHeader());
        AttrManage.instance().setPageFooterMargin(attribute, section.getMarginFooter());
        if (section.getGridType() != 0) {
            AttrManage.instance().setPageLinePitch(attribute, section.getLinePitch());
        }
        processSectionBorder(sectionElement, section);
        sectionElement.setStartOffset(this.offset);
        int numParagraphs = section.numParagraphs();
        int i = 0;
        while (i < numParagraphs && !this.abortReader) {
            Paragraph paragraph = section.getParagraph(i);
            if (paragraph.isInTable()) {
                Table table = section.getTable(paragraph);
                processTable(table);
                i += table.numParagraphs() - 1;
            } else {
                processParagraph(section.getParagraph(i));
            }
            i++;
        }
        sectionElement.setEndOffset(this.offset);
        this.wpdoc.appendSection(sectionElement);
    }

    private void processSectionBorder(SectionElement sectionElement, Section section) {
        BorderCode topBorder = section.getTopBorder();
        BorderCode bottomBorder = section.getBottomBorder();
        BorderCode leftBorder = section.getLeftBorder();
        BorderCode rightBorder = section.getRightBorder();
        if (topBorder != null || bottomBorder != null || leftBorder != null || rightBorder != null) {
            Borders borders = new Borders();
            borders.setOnType((byte) ((((byte) section.getPageBorderInfo()) >> 5) & 7));
            int i = -16777216;
            if (topBorder != null) {
                Border border = new Border();
                border.setColor(topBorder.getColor() == 0 ? -16777216 : converterColorForIndex(topBorder.getColor()));
                border.setSpace((short) ((int) (((float) topBorder.getSpace()) * 1.3333334f)));
                borders.setTopBorder(border);
            }
            if (bottomBorder != null) {
                Border border2 = new Border();
                border2.setColor(bottomBorder.getColor() == 0 ? -16777216 : converterColorForIndex(bottomBorder.getColor()));
                border2.setSpace((short) ((int) (((float) bottomBorder.getSpace()) * 1.3333334f)));
                borders.setBottomBorder(border2);
            }
            if (leftBorder != null) {
                Border border3 = new Border();
                border3.setColor(leftBorder.getColor() == 0 ? -16777216 : converterColorForIndex(leftBorder.getColor()));
                border3.setSpace((short) ((int) (((float) leftBorder.getSpace()) * 1.3333334f)));
                borders.setLeftBorder(border3);
            }
            if (rightBorder != null) {
                Border border4 = new Border();
                if (rightBorder.getColor() != 0) {
                    i = converterColorForIndex(rightBorder.getColor());
                }
                border4.setColor(i);
                border4.setSpace((short) ((int) (((float) rightBorder.getSpace()) * 1.3333334f)));
                borders.setRightBorder(border4);
            }
            AttrManage.instance().setPageBorder(sectionElement.getAttribute(), this.control.getSysKit().getBordersManage().addBorders(borders));
        }
    }

    private void processHeaderFooter() {
        HeaderStories headerStories = new HeaderStories(this.poiDoc);
        this.offset = 1152921504606846976L;
        this.docRealOffset = 1152921504606846976L;
        Range oddHeaderSubrange = headerStories.getOddHeaderSubrange();
        if (oddHeaderSubrange != null) {
            processHeaderFooterPara(oddHeaderSubrange, (short) 5, (byte) 1);
        }
        this.offset = 2305843009213693952L;
        this.docRealOffset = 2305843009213693952L;
        Range oddFooterSubrange = headerStories.getOddFooterSubrange();
        if (oddFooterSubrange != null) {
            processHeaderFooterPara(oddFooterSubrange, (short) 6, (byte) 1);
        }
    }

    private void processHeaderFooterPara(Range range, short s, byte b) {
        HFElement hFElement = new HFElement(s, b);
        hFElement.setStartOffset(this.offset);
        int numParagraphs = range.numParagraphs();
        int i = 0;
        while (i < numParagraphs && !this.abortReader) {
            Paragraph paragraph = range.getParagraph(i);
            if (paragraph.isInTable()) {
                Table table = range.getTable(paragraph);
                processTable(table);
                i += table.numParagraphs() - 1;
            } else {
                processParagraph(paragraph);
            }
            i++;
        }
        hFElement.setEndOffset(this.offset);
        this.wpdoc.appendElement(hFElement, this.offset);
    }

    private void processTable(Table table) {
        TableElement tableElement = new TableElement();
        tableElement.setStartOffset(this.offset);
        Vector vector = new Vector();
        int numRows = table.numRows();
        for (int i = 0; i < numRows; i++) {
            TableRow row = table.getRow(i);
            if (i == 0) {
                processTableAttribute(row, tableElement.getAttribute());
            }
            RowElement rowElement = new RowElement();
            rowElement.setStartOffset(this.offset);
            processRowAttribute(row, rowElement.getAttribute());
            int numCells = row.numCells();
            int i2 = 0;
            for (int i3 = 0; i3 < numCells; i3++) {
                TableCell cell = row.getCell(i3);
                cell.isBackward();
                CellElement cellElement = new CellElement();
                cellElement.setStartOffset(this.offset);
                processCellAttribute(cell, cellElement.getAttribute());
                int numParagraphs = cell.numParagraphs();
                for (int i4 = 0; i4 < numParagraphs; i4++) {
                    processParagraph(cell.getParagraph(i4));
                }
                cellElement.setEndOffset(this.offset);
                if (this.offset > cellElement.getStartOffset()) {
                    rowElement.appendCell(cellElement);
                }
                i2 += cell.getWidth();
                if (!vector.contains(Integer.valueOf(i2))) {
                    vector.add(Integer.valueOf(i2));
                }
            }
            rowElement.setEndOffset(this.offset);
            if (this.offset > rowElement.getStartOffset()) {
                tableElement.appendRow(rowElement);
            }
        }
        tableElement.setEndOffset(this.offset);
        if (this.offset > tableElement.getStartOffset()) {
            this.wpdoc.appendParagraph(tableElement, this.offset);
            int size = vector.size();
            int[] iArr = new int[size];
            for (int i5 = 0; i5 < size; i5++) {
                iArr[i5] = ((Integer) vector.get(i5)).intValue();
            }
            Arrays.sort(iArr);
            RowElement rowElement2 = (RowElement) tableElement.getElementForIndex(0);
            int i6 = 1;
            while (rowElement2 != null) {
                IElement elementForIndex = rowElement2.getElementForIndex(0);
                int i7 = 0;
                int i8 = 0;
                int i9 = 0;
                while (elementForIndex != null) {
                    i7 += AttrManage.instance().getTableCellWidth(elementForIndex.getAttribute());
                    while (true) {
                        if (i9 < size) {
                            if (i7 <= iArr[i9]) {
                                i9++;
                                break;
                            }
                            i8++;
                            rowElement2.insertElementForIndex(new CellElement(), i8);
                            i9++;
                        } else {
                            break;
                        }
                    }
                    i8++;
                    elementForIndex = rowElement2.getElementForIndex(i8);
                }
                RowElement rowElement3 = (RowElement) tableElement.getElementForIndex(i6);
                i6++;
                rowElement2 = rowElement3;
            }
        }
    }

    private void processTableAttribute(TableRow tableRow, IAttributeSet iAttributeSet) {
        if (tableRow.getRowJustification() != 0) {
            AttrManage.instance().setParaHorizontalAlign(iAttributeSet, tableRow.getRowJustification());
        }
        if (tableRow.getTableIndent() != 0) {
            AttrManage.instance().setParaIndentLeft(iAttributeSet, tableRow.getTableIndent());
        }
    }

    private void processRowAttribute(TableRow tableRow, IAttributeSet iAttributeSet) {
        if (tableRow.getRowHeight() != 0) {
            AttrManage.instance().setTableRowHeight(iAttributeSet, tableRow.getRowHeight());
        }
        if (tableRow.isTableHeader()) {
            AttrManage.instance().setTableHeaderRow(iAttributeSet, true);
        }
        if (tableRow.cantSplit()) {
            AttrManage.instance().setTableRowSplit(iAttributeSet, true);
        }
    }

    private void processCellAttribute(TableCell tableCell, IAttributeSet iAttributeSet) {
        if (tableCell.isFirstMerged()) {
            AttrManage.instance().setTableHorFirstMerged(iAttributeSet, true);
        }
        if (tableCell.isMerged()) {
            AttrManage.instance().setTableHorMerged(iAttributeSet, true);
        }
        if (tableCell.isFirstVerticallyMerged()) {
            AttrManage.instance().setTableVerFirstMerged(iAttributeSet, true);
        }
        if (tableCell.isVerticallyMerged()) {
            AttrManage.instance().setTableVerMerged(iAttributeSet, true);
        }
        AttrManage.instance().setTableCellVerAlign(iAttributeSet, tableCell.getVertAlign());
        AttrManage.instance().setTableCellWidth(iAttributeSet, tableCell.getWidth());
    }

    private void processParagraph(Paragraph para)
    {
        ParagraphElement paraElem = new ParagraphElement();
        // 属性
        IAttributeSet attr = paraElem.getAttribute();
        // 段前
        AttrManage.instance().setParaBefore(attr, para.getSpacingBefore());
        // 段后
        AttrManage.instance().setParaAfter(attr, para.getSpacingAfter());
        // 左缩进
        AttrManage.instance().setParaIndentLeft(attr, para.getIndentFromLeft());
        // 右缩进
        AttrManage.instance().setParaIndentRight(attr, para.getIndentFromRight());
        // 水平对齐
        AttrManage.instance().setParaHorizontalAlign(attr, converterParaHorAlign(para.getJustification()));
        // 垂直对齐
        AttrManage.instance().setParaVerticalAlign(attr, para.getFontAlignment());
        // 特殊缩进
        converterSpecialIndent(attr, para.getFirstLineIndent());
        // 行距
        converterLineSpace(para.getLineSpacing(), attr);
        // list level
        if (para.getIlfo() > 0)
        {
            ListTables listTables = poiDoc.getListTables();
            if (listTables != null)
            {
                // list ID
                ListFormatOverride listFormatOverride = listTables.getOverride(para.getIlfo());
                if(listFormatOverride != null)
                {
                    AttrManage.instance().setParaListID(attr, listFormatOverride.getLsid());
                }

                // list level
                AttrManage.instance().setParaListLevel(attr, para.getIlvl());
            }
        }
        if (para.isInTable())
        {
            AttrManage.instance().setParaLevel(attr, para.getTableLevel());
        }
        // 开始 offset
        paraElem.setStartOffset(offset);

        int runCount = para.numCharacterRuns();
        boolean isFieldCode = false;
        boolean isFieldText = false;
        Field field = null;
        CharacterRun preRun;
        CharacterRun run = null;
        String fieldCode ="";
        String fieldText ="";
        long before = docRealOffset;
        for (int i = 0; i < runCount && !abortReader; i++)
        {
            preRun = run;
            run = para.getCharacterRun(i);
            String text = run.text();
            if (text.length() == 0 || run.isMarkedDeleted())
            {
                continue;
            }
            docRealOffset += text.length();
            char ch = text.charAt(0);
            char lastch = text.charAt(text.length() - 1);
            if ((ch == '\t' && text.length() == 1)
                    || ch == 0x05 // 批注框
                /*|| ch == 0x01*/) // 嵌入对象占位符
            {
                continue;
            }
            else if (ch == 0x13 || lastch == 0x13) // 域开始符
            {
                if(ch != 0x15 || lastch != 0x13)
                {
                    long area = offset & WPModelConstant.AREA_MASK;
                    FieldsDocumentPart fieldsPart = area == WPModelConstant.HEADER || area == WPModelConstant.FOOTER
                            ? fieldsPart = FieldsDocumentPart.HEADER : FieldsDocumentPart.MAIN;
                    field = poiDoc.getFields().getFieldByStartOffset(fieldsPart, (int)run.getStartOffset());
                    isFieldCode = true;
                }
                continue;
            }
            else if (ch == 0x14 || lastch == 0x14) // 域分隔符
            {
                isFieldCode = false;
                isFieldText = true;
                continue;
            }
            else if (ch == 0x15 || lastch == 0x15) // 域结束符
            {
                if (preRun != null && fieldText != null
                        && field != null && field.getType() == Field.EMBED)
                {
                    //EMBED object
                    if (fieldText.indexOf("EQ") >= 0 &&
                            fieldText.indexOf("jc") >= 0)
                    {
                        processRun(preRun, para, field, paraElem, fieldCode, fieldText);
                    }
                    else
                    {
                        if(lastch == 0x15)
                        {
                            fieldText += text.substring(0, text.length() - 1);
                        }
                        processRun(run, para, field, paraElem, fieldCode, fieldText);
                    }
                }
                else if (isPageNumber(field, fieldCode))
                {
                    processRun(run, para, field, paraElem, fieldCode, fieldText);
                }

                isFieldText = false;
                isFieldCode = false;
                field = null;
                hyperlinkAddress = null;
                fieldCode = "";
                fieldText = "";
                continue;
            }
            if (isFieldCode)
            {
                fieldCode += run.text();
                continue;
            }

            if(isFieldText && isPageNumber(field, fieldCode))
            {
                fieldText += run.text();
                continue;
            }

            processRun(run, para, field, paraElem, null, null);
            //fieldText = "";
        }
        if (para.getTabClearPosition() > 0)
        {
            AttrManage.instance().setParaTabsClearPostion(attr, para.getTabClearPosition());
        }

        // 结束 offset
        if(offset == paraElem.getStartOffset())
        {
            //this paragraph is not Marked Deleted
            paraElem.dispose();
            paraElem = null;

            return;
        }

        paraElem.setEndOffset(offset);
        wpdoc.appendParagraph(paraElem, offset);
        //
        adjustBookmarkOffset(before, docRealOffset);
    }

    private boolean isPageNumber(Field field, String str) {
        if (field != null && (field.getType() == 33 || field.getType() == 26)) {
            return true;
        }
        if (str == null) {
            return false;
        }
        if (str.contains("NUMPAGES") || str.contains("PAGE")) {
            return true;
        }
        return false;
    }

    private void converterSpecialIndent(IAttributeSet iAttributeSet, int i) {
        AttrManage.instance().setParaSpecialIndent(iAttributeSet, i);
        if (i < 0) {
            AttrManage.instance().setParaIndentLeft(iAttributeSet, AttrManage.instance().getParaIndentLeft(iAttributeSet) + i);
        }
    }

    private void converterLineSpace(LineSpacingDescriptor lineSpacingDescriptor, IAttributeSet iAttributeSet) {
        float f;
        int i = 1;
        if (lineSpacingDescriptor.getMultiLinespace() == 1) {
            f = ((float) lineSpacingDescriptor.getDyaLine()) / 240.0f;
            if (f == 1.0f) {
                i = 0;
                f = 1.0f;
            } else if (((double) f) == 1.5d) {
                f = 1.5f;
            } else {
                if (f == 2.0f) {
                    f = 2.0f;
                }
                i = 2;
            }
        } else {
            f = (float) lineSpacingDescriptor.getDyaLine();
            if (f >= 0.0f) {
                i = 3;
            } else {
                i = 4;
                f = -f;
            }
        }
        AttrManage.instance().setParaLineSpace(iAttributeSet, f);
        AttrManage.instance().setParaLineSpaceType(iAttributeSet, i);
    }

    private void processRun(CharacterRun run, Range parentRange, Field field, ParagraphElement paraElem, String fieldCode, String fieldText) {
        String text = run.text();
        if (fieldText != null) {
            text = fieldText;
        }

        if (text != null && text.length() > 0) {
            // 嵌入对象占位符
            char ch = text.charAt(0);
            isBreakChar = ch == '\f';
            if (ch == '\b' || ch == 0x01) {
                for (int i = 0; i < text.length() && !run.isVanished(); i++) {
                    ch = text.charAt(i);
                    if (ch == '\b' || ch == 0x01) {
                        LeafElement leaf = new LeafElement(String.valueOf(ch));
                        // process shape error
                        if (!processShape(run, leaf, ch == '\b', i)) {
                            return;
                        }
                        // 开始 offset
                        leaf.setStartOffset(offset);
                        offset += 1;
                        // 结束 offset
                        leaf.setEndOffset(offset);
                        paraElem.appendLeaf(leaf);
                    }
                }
                return;
            }
        }

        LeafElement leaf = new LeafElement(text);
        // 属性
        IAttributeSet attr = leaf.getAttribute();
        // 字号
        AttrManage.instance().setFontSize(attr, (int) (run.getFontSize() / 2.f + 0.5));
        // 字体
        int index = FontTypefaceManage.instance().addFontName(run.getFontName());
        if (index >= 0) {
            AttrManage.instance().setFontName(attr, index);
        }
        // 字符颜色
        AttrManage.instance().setFontColor(attr, FCKit.BGRtoRGB(run.getIco24()));
        // 粗体
        AttrManage.instance().setFontBold(attr, run.isBold());
        // 斜体
        AttrManage.instance().setFontItalic(attr, run.isItalic());
        // 删除线
        AttrManage.instance().setFontStrike(attr, run.isStrikeThrough());
        // 双删除线
        AttrManage.instance().setFontDoubleStrike(attr, run.isDoubleStrikeThrough());
        // 下划线
        AttrManage.instance().setFontUnderline(attr, run.getUnderlineCode());
        // 下划线颜色
        AttrManage.instance().setFontUnderlineColr(attr, FCKit.BGRtoRGB(run.getUnderlineColor()));
        // 上下标
        AttrManage.instance().setFontScript(attr, run.getSubSuperScriptIndex());
        // 高亮
        AttrManage.instance().setFontHighLight(attr, converterColorForIndex(run.getHighlightedColor()));

        if (field != null && field.getType() == Field.HYPERLINK) {
            // hyperlink
            if (hyperlinkAddress == null) {
                Range firstSubrange = field.firstSubrange(parentRange);
                if (firstSubrange != null) {
                    String formula = firstSubrange.text();
                    Matcher matcher = hyperlinkPattern.matcher(formula);
                    if (matcher.find()) {
                        hyperlinkAddress = matcher.group(1);
                    }
                }
            }
            if (hyperlinkAddress != null) {
                index = control.getSysKit().getHyperlinkManage().addHyperlink(hyperlinkAddress, Hyperlink.LINK_URL);
                if (index >= 0) {
                    AttrManage.instance().setFontColor(attr, android.graphics.Color.BLUE);
                    AttrManage.instance().setFontUnderline(attr, 1);
                    AttrManage.instance().setFontUnderlineColr(attr, android.graphics.Color.BLUE);
                    AttrManage.instance().setHyperlinkID(attr, index);
                }
            }
        } else if (fieldCode != null) {
            if (fieldCode.indexOf("HYPERLINK") > 0) {
                index = fieldCode.indexOf("_Toc");
                if (index > 0) {
                    int endIndex = fieldCode.lastIndexOf('"');
                    if (endIndex > 0 && endIndex > index) {
                        String bmName = fieldCode.substring(index, endIndex);
                        index = control.getSysKit().getHyperlinkManage().addHyperlink(bmName, Hyperlink.LINK_BOOKMARK);
                        if (index >= 0) {
                            AttrManage.instance().setFontColor(attr, android.graphics.Color.BLUE);
                            AttrManage.instance().setFontUnderline(attr, 1);
                            AttrManage.instance().setFontUnderlineColr(attr, android.graphics.Color.BLUE);
                            AttrManage.instance().setHyperlinkID(attr, index);
                        }
                    }
                }
            } else {
                long area = offset & WPModelConstant.AREA_MASK;
                if (area == WPModelConstant.HEADER || area == WPModelConstant.FOOTER) {
                    byte pageNumberType = -1;
                    if (fieldCode != null) {
                        if (fieldCode.contains("NUMPAGES")) {
                            pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                        } else if (fieldCode.contains("PAGE")) {
                            pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                        }
                    }

                    if (pageNumberType > 0) {
                        AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pageNumberType);
                    }
                }
            }
        }

        // 开始 offset
        leaf.setStartOffset(offset);
        offset += text.length();
        // 结束 offset
        leaf.setEndOffset(offset);
        paraElem.appendLeaf(leaf);
    }

    private BackgroundAndFill converFill(HWPFAutoShape shape, OfficeDrawing drawing, int shapeType) {
        if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
                || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3
                || shapeType == ShapeTypes.CurvedConnector3) {
            return null;
        }

        BackgroundAndFill bgFill = null;
        if (shape != null) {
            int type = shape.getFillType();
            // 填充类型
            if (type == BackgroundAndFill.FILL_SOLID || type == BackgroundAndFill.FILL_BACKGROUND) {
                if (shape.getForegroundColor() != null) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    // 前景颜色
                    bgFill.setForegroundColor(shape.getForegroundColor().getRGB());
                }
            } else if (type == BackgroundAndFill.FILL_SHADE_LINEAR || type == BackgroundAndFill.FILL_SHADE_RADIAL
                    || type == BackgroundAndFill.FILL_SHADE_RECT || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                bgFill = new BackgroundAndFill();
                int angle = shape.getFillAngle();
                switch (angle) {
                    case -90:
                    case 0:
                        angle += 90;
                        break;
                    case -45:
                        angle = 135;
                        break;
                    case -135:
                        angle = 45;
                        break;
                }
                int focus = shape.getFillFocus();
                com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.awt.Color fillColor = shape.getForegroundColor();
                com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.awt.Color fillbackColor = shape.getFillbackColor();

                int[] colors = null;
                float[] positions = null;
                if (shape.isShaderPreset()) {
                    colors = shape.getShaderColors();
                    positions = shape.getShaderPositions();
                }

                if (colors == null) {
                    colors = new int[]{fillColor == null ? 0xFFFFFFFF : fillColor.getRGB(),
                            fillbackColor == null ? 0xFFFFFFFF : fillbackColor.getRGB()};
                }
                if (positions == null) {
                    positions = new float[]{0f, 1f};
                }

                Gradient gradient = null;
                if (type == BackgroundAndFill.FILL_SHADE_LINEAR) {
                    gradient = new LinearGradientShader(angle, colors, positions);
                } else if (type == BackgroundAndFill.FILL_SHADE_RADIAL
                        || type == BackgroundAndFill.FILL_SHADE_RECT
                        || type == BackgroundAndFill.FILL_SHADE_SHAPE) {
                    gradient =
                            new RadialGradientShader(shape.getRadialGradientPositionType(), colors, positions);
                }

                if (gradient != null) {
                    gradient.setFocus(focus);
                }

                bgFill.setFillType((byte) type);
                bgFill.setShader(gradient);
            } else if (type == BackgroundAndFill.FILL_SHADE_TILE) {
                // 背景为图片
                byte[] data = drawing.getPictureData(control, shape.getBackgroundPictureIdx());
                if (data != null && isSupportPicture(PictureType.findMatchingType(data))) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                    if (index < 0) {
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath(control));
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                        bgFill.setShader(
                                new TileShader(control.getSysKit().getPictureManage().getPicture(index),
                                        TileShader.Flip_None, 1f, 1.0f));
                    }
                }
            } else if (type == BackgroundAndFill.FILL_PICTURE) {
                // 背景为图片
                byte[] data = drawing.getPictureData(control, shape.getBackgroundPictureIdx());
                if (data != null && isSupportPicture(PictureType.findMatchingType(data))) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_PICTURE);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                    if (index < 0) {
                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath(control));
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                    }
                    bgFill.setPictureIndex(index);
                }
            } else if (type == BackgroundAndFill.FILL_PATTERN) {
   /*         	byte[] data = drawing.getBGPictureData(shape.getBackgroundPictureIdx());
                if (data != null  && isSupportPicture(PictureType.findMatchingType(data)))
                {
                	bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_PATTERN);
                    // 图片数据
                    int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath());
                    if (index < 0)
                    {
                    	control.getSysKit().getPictureManage().writeTempFile(data);

                        Picture picture = new Picture();
                        // 图片数据
                        picture.setTempFilePath(drawing.getTempFilePath());
                        // 图片类型
                        picture.setPictureType(PictureType.findMatchingType(data).getExtension());
                        index = control.getSysKit().getPictureManage().addPicture(picture);
                        bgFill.setShader(
                				new PatternShader(control.getSysKit().getPictureManage().getPicture(index),
                						shape.getBackgroundColor().getRGB(), shape.getForegroundColor().getRGB()));
                    }
                }
                else*/
                if (shape.getFillbackColor() != null) {
                    bgFill = new BackgroundAndFill();
                    bgFill.setFillType(BackgroundAndFill.FILL_SOLID);
                    // 前景颜色
                    bgFill.setForegroundColor(shape.getFillbackColor().getRGB());
                }
            }
        }
        return bgFill;
    }

    private void processRotation(HWPFAutoShape hWPFAutoShape, IShape iShape) {
        float rotation = (float) hWPFAutoShape.getRotation();
        if (hWPFAutoShape.getFlipHorizontal()) {
            iShape.setFlipHorizontal(true);
            rotation = -rotation;
        }
        if (hWPFAutoShape.getFlipVertical()) {
            iShape.setFlipVertical(true);
            rotation = -rotation;
        }
        if ((iShape instanceof LineShape) && ((rotation == 45.0f || rotation == 135.0f || rotation == 225.0f) && !iShape.getFlipHorizontal() && !iShape.getFlipVertical())) {
            rotation -= 90.0f;
        }
        iShape.setRotation(rotation);
    }

    private Rectangle processGrpSpRect(GroupShape groupShape, Rectangle rectangle) {
        if (groupShape != null) {
            rectangle.x += groupShape.getOffX();
            rectangle.y += groupShape.getOffY();
        }
        return rectangle;
    }

    private void processAutoshapePosition(HWPFAutoShape hWPFAutoShape, WPAutoShape wPAutoShape) {
        int position_H = hWPFAutoShape.getPosition_H();
        if (position_H == 0) {
            wPAutoShape.setHorPositionType((byte) 0);
        } else if (position_H == 1) {
            wPAutoShape.setHorizontalAlignment((byte) 1);
        } else if (position_H == 2) {
            wPAutoShape.setHorizontalAlignment((byte) 2);
        } else if (position_H == 3) {
            wPAutoShape.setHorizontalAlignment((byte) 3);
        } else if (position_H == 4) {
            wPAutoShape.setHorizontalAlignment((byte) 6);
        } else if (position_H == 5) {
            wPAutoShape.setHorizontalAlignment((byte) 7);
        }
        int positionRelTo_H = hWPFAutoShape.getPositionRelTo_H();
        if (positionRelTo_H == 0) {
            wPAutoShape.setHorizontalRelativeTo((byte) 1);
        } else if (positionRelTo_H == 1) {
            wPAutoShape.setHorizontalRelativeTo((byte) 2);
        } else if (positionRelTo_H == 2) {
            wPAutoShape.setHorizontalRelativeTo((byte) 0);
        } else if (positionRelTo_H == 3) {
            wPAutoShape.setHorizontalRelativeTo((byte) 3);
        }
        int position_V = hWPFAutoShape.getPosition_V();
        if (position_V == 0) {
            wPAutoShape.setVerPositionType((byte) 0);
        } else if (position_V == 1) {
            wPAutoShape.setVerticalAlignment((byte) 4);
        } else if (position_V == 2) {
            wPAutoShape.setVerticalAlignment((byte) 2);
        } else if (position_V == 3) {
            wPAutoShape.setVerticalAlignment((byte) 5);
        } else if (position_V == 4) {
            wPAutoShape.setVerticalAlignment((byte) 6);
        } else if (position_V == 5) {
            wPAutoShape.setVerticalAlignment((byte) 7);
        }
        int positionRelTo_V = hWPFAutoShape.getPositionRelTo_V();
        if (positionRelTo_V == 0) {
            wPAutoShape.setVerticalRelativeTo((byte) 1);
        } else if (positionRelTo_V == 1) {
            wPAutoShape.setVerticalRelativeTo((byte) 2);
        } else if (positionRelTo_V == 2) {
            wPAutoShape.setVerticalRelativeTo((byte) 10);
        } else if (positionRelTo_V == 3) {
            wPAutoShape.setVerticalRelativeTo((byte) 11);
        }
    }

    private byte[] getPictureframeData(OfficeDrawing officeDrawing, HWPFShape hWPFShape) {
        EscherSimpleProperty escherSimpleProperty;
        EscherOptRecord escherOptRecord = (EscherOptRecord) hWPFShape.getSpContainer().getChildById(EscherOptRecord.RECORD_ID);
        if (escherOptRecord == null || (escherSimpleProperty = (EscherSimpleProperty) escherOptRecord.lookup(MetaDo.META_SETROP2)) == null) {
            return null;
        }
        return officeDrawing.getPictureData(this.control, escherSimpleProperty.getPropertyValue());
    }

    private boolean convertShape(IElement leaf, OfficeDrawing drawing, GroupShape parent,
                                 HWPFShape poiShape, Rectangle rect, float zoomX, float zoomY)
    {
        if (rect == null)
        {
            return false;
        }


        if (poiShape instanceof HWPFAutoShape)
        {
            HWPFAutoShape shape = (HWPFAutoShape)poiShape;
            int shapeType = shape.getShapeType();
            BackgroundAndFill fill = converFill(shape, drawing, shapeType);
            Line line = poiShape.getLine(shapeType == ShapeTypes.Line);
            if (line != null || fill != null || shapeType == ShapeTypes.TextBox || shapeType == ShapeTypes.PictureFrame)
            {
                rect = processGrpSpRect(parent, rect);

                WPAutoShape autoShape = null;
                if(shapeType == ShapeTypes.PictureFrame)
                {
                    autoShape = new WPPictureShape();
                }
                else
                {
                    autoShape = new WPAutoShape();
                }

                autoShape.setShapeType(shapeType);
                autoShape.setAuotShape07(false);

                float angle = Math.abs(shape.getRotation());
                autoShape.setBounds(ModelUtil.processRect(rect, angle));

                autoShape.setBackgroundAndFill(fill);
                if (line != null)
                {
                    autoShape.setLine(line);
                }
                Float[] adj = shape.getAdjustmentValue();
                autoShape.setAdjustData(adj);
                processRotation(shape, autoShape);

                processAutoshapePosition(shape, autoShape);

                boolean isLineShape = false;
                if(shapeType == ShapeTypes.PictureFrame)
                {
                    byte[] b = getPictureframeData(drawing, shape);
                    if (b != null)
                    {
                        if (isSupportPicture(PictureType.findMatchingType(b)))
                        {
                            PictureShape picShape = new PictureShape();

                            int index = control.getSysKit().getPictureManage().getPictureIndex(drawing.getTempFilePath(control));
                            if (index < 0)
                            {
                                Picture picture = new Picture();
                                // 图片数据
                                picture.setTempFilePath(drawing.getTempFilePath(control));
                                // 图片类型
                                picture.setPictureType(PictureType.findMatchingType(b).getExtension());
                                index = control.getSysKit().getPictureManage().addPicture(picture);
                            }
                            picShape.setPictureIndex(index);
                            picShape.setBounds(rect);
                            picShape.setZoomX((short)1000);
                            picShape.setZoomY((short)1000);
                            picShape.setPictureEffectInfor(drawing.getPictureEffectInfor());
                            ((WPPictureShape)autoShape).setPictureShape(picShape);
                        }
                    }
                }
                else if (shapeType == ShapeTypes.Line || shapeType == ShapeTypes.StraightConnector1
                        || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3
                        || shapeType == ShapeTypes.CurvedConnector3)
                {
                    isLineShape = true;
                    if(autoShape.getShapeType() == ShapeTypes.BentConnector2 && adj == null)
                    {
                        autoShape.setAdjustData(new Float[]{1.0f});
                    }

                    int type = shape.getStartArrowType();
                    if (type > 0)
                    {
                        autoShape.createStartArrow((byte)type,
                                shape.getStartArrowWidth(),
                                shape.getStartArrowLength());
                    }

                    type = shape.getEndArrowType();
                    if (type > 0)
                    {
                        autoShape.createEndArrow((byte)type,
                                shape.getEndArrowWidth(),
                                shape.getEndArrowLength());
                    }
                }
                else if(shapeType == ShapeTypes.NotPrimitive || shapeType == ShapeTypes.NotchedCircularArrow)
                {
                    isLineShape = true;
                    autoShape.setShapeType(ShapeTypes.ArbitraryPolygon);

                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;

                    int startArrowType = shape.getStartArrowType();
                    if (startArrowType > 0)
                    {
                        ArrowPathAndTail arrowPathAndTail = ((HWPFAutoShape)shape).getStartArrowPath(rect);
                        if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
                        {
                            startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(arrowPathAndTail.getArrowPath());
                            pathExtend.setArrowFlag(true);
                            if(startArrowType != Arrow.Arrow_Arrow)
                            {
                                if((line == null || line.getBackgroundAndFill() == null)  && poiShape.getLineColor() != null)
                                {
                                    BackgroundAndFill arrowFill = new BackgroundAndFill();
                                    arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                    arrowFill.setForegroundColor(poiShape.getLineColor().getRGB());
                                    pathExtend.setBackgroundAndFill(arrowFill);
                                }
                                else
                                {
                                    pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                }
                            }
                            else
                            {
                                pathExtend.setLine(line);
                            }

                            autoShape.appendPath(pathExtend);
                        }

                    }

                    int endArrowType = shape.getEndArrowType();
                    if (endArrowType > 0)
                    {
                        ArrowPathAndTail arrowPathAndTail = ((HWPFAutoShape)shape).getEndArrowPath(rect);
                        if(arrowPathAndTail != null && arrowPathAndTail.getArrowPath() != null)
                        {
                            endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                            ExtendPath pathExtend = new ExtendPath();
                            pathExtend.setPath(arrowPathAndTail.getArrowPath());

                            pathExtend.setArrowFlag(true);
                            if(endArrowType != Arrow.Arrow_Arrow)
                            {
                                if((line == null || line.getBackgroundAndFill() == null)  && poiShape.getLineColor() != null)
                                {
                                    BackgroundAndFill arrowFill = new BackgroundAndFill();
                                    arrowFill.setFillType(BackgroundAndFill.FILL_SOLID);
                                    arrowFill.setForegroundColor(poiShape.getLineColor().getRGB());
                                    pathExtend.setBackgroundAndFill(arrowFill);
                                }
                                else
                                {
                                    pathExtend.setBackgroundAndFill(line.getBackgroundAndFill());
                                }
                            }
                            else
                            {
                                pathExtend.setLine(line);
                            }

                            autoShape.appendPath(pathExtend);
                        }

                    }

                    Path[] paths = shape.getFreeformPath(rect,
                            startArrowTailCenter, (byte)startArrowType, endArrowTailCenter, (byte)endArrowType);
                    for (int i = 0; i < paths.length; i++)
                    {
                        ExtendPath pathExtend = new ExtendPath();
                        pathExtend.setPath(paths[i]);
                        if (line != null)
                        {
                            pathExtend.setLine(line);
                        }
                        if (fill != null)
                        {
                            pathExtend.setBackgroundAndFill(fill);
                        }
                        autoShape.appendPath(pathExtend);
                    }
                }
                else
                {
                    processTextbox(shape.getSpContainer(),
                            autoShape,
                            poiDoc.getMainTextboxRange().getSection(0));
                }

                if (parent == null)
                {
                    // wrap
                    if (drawing.getWrap() == 3 && !drawing.isAnchorLock())
                    {
                        if(drawing.isBelowText())
                        {
                            autoShape.setWrap(WPAutoShape.WRAP_BOTTOM);
                        }
                        else
                        {
                            autoShape.setWrap(WPAutoShape.WRAP_TOP);
                            fill = autoShape.getBackgroundAndFill();
                        }
                    }
                    else
                    {
                        autoShape.setWrap(WPAutoShape.WRAP_OLE);
                    }
                    AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(autoShape));
                    return true;
                }
                else
                {
                    parent.appendShapes(autoShape);
                    return false;
                }
            }
        }
        else if (poiShape instanceof HWPFShapeGroup)
        {
            HWPFShapeGroup poiGroup = (HWPFShapeGroup)poiShape;

            AbstractShape shape = null;
            WPGroupShape groupShape = new WPGroupShape();
            if (parent == null)
            {
                shape = new WPAutoShape();
                ((WPAutoShape)shape).addGroupShape(groupShape);
            }
            else
            {
                shape = groupShape;
            }

            float zoom[] = poiGroup.getShapeAnchorFit(rect, zoomX, zoomY);
            rect = processGrpSpRect(parent, rect);
            Rectangle childRect = poiGroup.getCoordinates(zoom[0] * zoomX, zoom[1] * zoomY);
            groupShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);

            groupShape.setBounds(rect);
            groupShape.setParent(parent);
            groupShape.setRotation(poiGroup.getGroupRotation());
            groupShape.setFlipHorizontal(poiGroup.getFlipHorizontal());
            groupShape.setFlipVertical(poiGroup.getFlipVertical());

            HWPFShape[] shapes = poiGroup.getShapes();
            if (shapes != null)
            {
                for (int i = 0; i < shapes.length; i++)
                {
                    convertShape(leaf, drawing, groupShape, shapes[i], shapes[i].getAnchor(rect, zoom[0] * zoomX, zoom[1] * zoomY), zoom[0] * zoomX, zoom[1] * zoomY);
                }
            }

            if (parent == null)
            {
                // wrap
                if (drawing.getWrap() == 3 && !drawing.isAnchorLock())
                {
                    ((WPAutoShape)shape).setWrap(WPAutoShape.WRAP_TOP);
                }
                else
                {
                    ((WPAutoShape)shape).setWrap(WPAutoShape.WRAP_OLE);
                }
                AttrManage.instance().setShapeID(leaf.getAttribute(), control.getSysKit().getWPShapeManage().addShape(shape));
            }
            else
            {
                shape.setParent(parent);
                parent.appendShapes(shape);
            }
            return true;
        }
        return false;
    }
    private short getTextboxId(EscherContainerRecord escherContainerRecord) {
        byte[] data;
        EscherTextboxRecord escherTextboxRecord = (EscherTextboxRecord) escherContainerRecord.getChildById(EscherTextboxRecord.RECORD_ID);
        if (escherTextboxRecord == null || (data = escherTextboxRecord.getData()) == null || data.length != 4) {
            return -1;
        }
        return LittleEndian.getShort(data, 2);
    }

    private void processTextbox(EscherContainerRecord escherContainerRecord, WPAutoShape wPAutoShape, Section section) {
        if (section != null) {
            if (getTextboxId(escherContainerRecord) - 1 >= 0) {
                processSimpleTextBox(escherContainerRecord, wPAutoShape, section);
            } else {
                processWordArtTextbox(escherContainerRecord, wPAutoShape);
            }
        }
    }

    private void processSimpleTextBox(EscherContainerRecord escherContainerRecord, WPAutoShape wPAutoShape, Section section) {
        int textboxId = getTextboxId(escherContainerRecord) - 1;
        int textboxStart = this.poiDoc.getTextboxStart(textboxId);
        int textboxEnd = this.poiDoc.getTextboxEnd(textboxId);
        long j = this.offset;
        long j2 = this.textboxIndex;
        this.offset = (j2 << 32) + WPModelConstant.TEXTBOX;
        wPAutoShape.setElementIndex((int) j2);
        SectionElement sectionElement = new SectionElement();
        sectionElement.setStartOffset(this.offset);
        this.wpdoc.appendElement(sectionElement, this.offset);
        IAttributeSet attribute = sectionElement.getAttribute();
        AttrManage.instance().setPageWidth(attribute, (int) (((float) wPAutoShape.getBounds().width) * 15.0f));
        AttrManage.instance().setPageHeight(attribute, (int) (((float) wPAutoShape.getBounds().height) * 15.0f));
        if (section.getGridType() != 0) {
            AttrManage.instance().setPageLinePitch(attribute, section.getLinePitch());
        }
        AttrManage.instance().setPageMarginTop(attribute, (int) (ShapeKit.getTextboxMarginTop(escherContainerRecord) * 15.0f));
        AttrManage.instance().setPageMarginBottom(attribute, (int) (ShapeKit.getTextboxMarginBottom(escherContainerRecord) * 15.0f));
        AttrManage.instance().setPageMarginLeft(attribute, (int) (ShapeKit.getTextboxMarginLeft(escherContainerRecord) * 15.0f));
        AttrManage.instance().setPageMarginRight(attribute, (int) (ShapeKit.getTextboxMarginRight(escherContainerRecord) * 15.0f));
        int i = 0;
        AttrManage.instance().setPageVerticalAlign(attribute, (byte) 0);
        wPAutoShape.setTextWrapLine(ShapeKit.isTextboxWrapLine(escherContainerRecord));
        sectionElement.setStartOffset(this.offset);
        int numParagraphs = section.numParagraphs();
        int i2 = 0;
        while (i < numParagraphs && !this.abortReader) {
            Paragraph paragraph = section.getParagraph(i);
            i2 += paragraph.text().length();
            if (i2 > textboxStart && i2 <= textboxEnd) {
                if (paragraph.isInTable()) {
                    Table table = section.getTable(paragraph);
                    processTable(table);
                    i += table.numParagraphs() - 1;
                } else {
                    processParagraph(section.getParagraph(i));
                }
            }
            i++;
        }
        wPAutoShape.setElementIndex((int) this.textboxIndex);
        sectionElement.setEndOffset(this.offset);
        this.textboxIndex++;
        this.offset = j;
    }

    private void processWordArtTextbox(EscherContainerRecord escherContainerRecord, WPAutoShape wPAutoShape) {
        String unicodeGeoText = ShapeKit.getUnicodeGeoText(escherContainerRecord);
        if (unicodeGeoText != null && unicodeGeoText.length() > 0) {
            long j = this.offset;
            long j2 = this.textboxIndex;
            this.offset = (j2 << 32) + WPModelConstant.TEXTBOX;
            wPAutoShape.setElementIndex((int) j2);
            SectionElement sectionElement = new SectionElement();
            sectionElement.setStartOffset(this.offset);
            this.wpdoc.appendElement(sectionElement, this.offset);
            IAttributeSet attribute = sectionElement.getAttribute();
            AttrManage.instance().setPageWidth(attribute, (int) (((float) wPAutoShape.getBounds().width) * 15.0f));
            AttrManage.instance().setPageHeight(attribute, (int) (((float) wPAutoShape.getBounds().height) * 15.0f));
            AttrManage.instance().setPageMarginTop(attribute, (int) (ShapeKit.getTextboxMarginTop(escherContainerRecord) * 15.0f));
            AttrManage.instance().setPageMarginBottom(attribute, (int) (ShapeKit.getTextboxMarginBottom(escherContainerRecord) * 15.0f));
            AttrManage.instance().setPageMarginLeft(attribute, (int) (ShapeKit.getTextboxMarginLeft(escherContainerRecord) * 15.0f));
            AttrManage.instance().setPageMarginRight(attribute, (int) (ShapeKit.getTextboxMarginRight(escherContainerRecord) * 15.0f));
            AttrManage.instance().setPageVerticalAlign(attribute, (byte) 0);
            wPAutoShape.setTextWrapLine(ShapeKit.isTextboxWrapLine(escherContainerRecord));
            int textboxMarginLeft = (int) ((((float) wPAutoShape.getBounds().width) - ShapeKit.getTextboxMarginLeft(escherContainerRecord)) - ShapeKit.getTextboxMarginRight(escherContainerRecord));
            int textboxMarginTop = (int) ((((float) wPAutoShape.getBounds().height) - ShapeKit.getTextboxMarginTop(escherContainerRecord)) - ShapeKit.getTextboxMarginBottom(escherContainerRecord));
            int i = 12;
            Paint paint = PaintKit.instance().getPaint();
            paint.setTextSize((float) 12);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            while (((int) paint.measureText(unicodeGeoText)) < textboxMarginLeft && ((int) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent))) < textboxMarginTop) {
                i++;
                paint.setTextSize((float) i);
                fontMetrics = paint.getFontMetrics();
            }
            sectionElement.setStartOffset(this.offset);
            ParagraphElement paragraphElement = new ParagraphElement();
            paragraphElement.setStartOffset(this.offset);
            long j3 = this.docRealOffset;
            LeafElement leafElement = new LeafElement(unicodeGeoText);
            IAttributeSet attribute2 = leafElement.getAttribute();
            AttrManage.instance().setFontSize(attribute2, (int) (((float) (i - 1)) * 0.75f));
            Color foregroundColor = ShapeKit.getForegroundColor(escherContainerRecord, (Object) null, 2);
            if (foregroundColor != null) {
                AttrManage.instance().setFontColor(attribute2, foregroundColor.getRGB());
            }
            leafElement.setStartOffset(this.offset);
            long length = this.offset + ((long) unicodeGeoText.length());
            this.offset = length;
            leafElement.setEndOffset(length);
            paragraphElement.appendLeaf(leafElement);
            paragraphElement.setEndOffset(this.offset);
            this.wpdoc.appendParagraph(paragraphElement, this.offset);
            adjustBookmarkOffset(j3, this.docRealOffset);
            wPAutoShape.setElementIndex((int) this.textboxIndex);
            sectionElement.setEndOffset(this.offset);
            this.textboxIndex++;
            this.offset = j;
        }
    }

    private void processPicturePosition(OfficeDrawing officeDrawing, WPAutoShape wPAutoShape) {
        byte horizontalPositioning = officeDrawing.getHorizontalPositioning();
        if (horizontalPositioning == 0) {
            wPAutoShape.setHorPositionType((byte) 0);
        } else if (horizontalPositioning == 1) {
            wPAutoShape.setHorizontalAlignment((byte) 1);
        } else if (horizontalPositioning == 2) {
            wPAutoShape.setHorizontalAlignment((byte) 2);
        } else if (horizontalPositioning == 3) {
            wPAutoShape.setHorizontalAlignment((byte) 3);
        } else if (horizontalPositioning == 4) {
            wPAutoShape.setHorizontalAlignment((byte) 6);
        } else if (horizontalPositioning == 5) {
            wPAutoShape.setHorizontalAlignment((byte) 7);
        }
        byte horizontalRelative = officeDrawing.getHorizontalRelative();
        if (horizontalRelative == 0) {
            wPAutoShape.setHorizontalRelativeTo((byte) 1);
        } else if (horizontalRelative == 1) {
            wPAutoShape.setHorizontalRelativeTo((byte) 2);
        } else if (horizontalRelative == 2) {
            wPAutoShape.setHorizontalRelativeTo((byte) 0);
        } else if (horizontalRelative == 3) {
            wPAutoShape.setHorizontalRelativeTo((byte) 3);
        }
        byte verticalPositioning = officeDrawing.getVerticalPositioning();
        if (verticalPositioning == 0) {
            wPAutoShape.setVerPositionType((byte) 0);
        } else if (verticalPositioning == 1) {
            wPAutoShape.setVerticalAlignment((byte) 4);
        } else if (verticalPositioning == 2) {
            wPAutoShape.setVerticalAlignment((byte) 2);
        } else if (verticalPositioning == 3) {
            wPAutoShape.setVerticalAlignment((byte) 5);
        } else if (verticalPositioning == 4) {
            wPAutoShape.setVerticalAlignment((byte) 6);
        } else if (verticalPositioning == 5) {
            wPAutoShape.setVerticalAlignment((byte) 7);
        }
        byte verticalRelativeElement = officeDrawing.getVerticalRelativeElement();
        if (verticalRelativeElement == 0) {
            wPAutoShape.setVerticalRelativeTo((byte) 1);
        } else if (verticalRelativeElement == 1) {
            wPAutoShape.setVerticalRelativeTo((byte) 2);
        } else if (verticalRelativeElement == 2) {
            wPAutoShape.setVerticalRelativeTo((byte) 10);
        } else if (verticalRelativeElement == 3) {
            wPAutoShape.setVerticalRelativeTo((byte) 11);
        }
    }

    private boolean processShape(CharacterRun characterRun, IElement iElement, boolean z, int i) {
        if (z) {
            OfficeDrawing officeDrawingAt = this.poiDoc.getOfficeDrawingsMain().getOfficeDrawingAt(characterRun.getStartOffset() + i);
            if (officeDrawingAt == null) {
                return false;
            }
            Rectangle rectangle = new Rectangle();
            rectangle.x = (int) (((float) officeDrawingAt.getRectangleLeft()) * 0.06666667f);
            rectangle.y = (int) (((float) officeDrawingAt.getRectangleTop()) * 0.06666667f);
            rectangle.width = (int) (((float) (officeDrawingAt.getRectangleRight() - officeDrawingAt.getRectangleLeft())) * 0.06666667f);
            rectangle.height = (int) (((float) (officeDrawingAt.getRectangleBottom() - officeDrawingAt.getRectangleTop())) * 0.06666667f);
            byte[] pictureData = officeDrawingAt.getPictureData(this.control);
            if (pictureData == null) {
                HWPFShape autoShape = officeDrawingAt.getAutoShape();
                if (autoShape != null) {
                    return convertShape(iElement, officeDrawingAt, (GroupShape) null, autoShape, rectangle, 1.0f, 1.0f);
                }
            } else if (isSupportPicture(PictureType.findMatchingType(pictureData))) {
                PictureShape pictureShape = new PictureShape();
                int pictureIndex = this.control.getSysKit().getPictureManage().getPictureIndex(officeDrawingAt.getTempFilePath(this.control));
                if (pictureIndex < 0) {
                    Picture picture = new Picture();
                    picture.setTempFilePath(officeDrawingAt.getTempFilePath(this.control));
                    picture.setPictureType(PictureType.findMatchingType(pictureData).getExtension());
                    pictureIndex = this.control.getSysKit().getPictureManage().addPicture(picture);
                }
                pictureShape.setPictureIndex(pictureIndex);
                pictureShape.setBounds(rectangle);
                pictureShape.setZoomX((short) 1000);
                pictureShape.setZoomY((short) 1000);
                pictureShape.setPictureEffectInfor(officeDrawingAt.getPictureEffectInfor());
                WPPictureShape wPPictureShape = new WPPictureShape();
                wPPictureShape.setPictureShape(pictureShape);
                if (officeDrawingAt.getWrap() != 3 || officeDrawingAt.isAnchorLock()) {
                    wPPictureShape.setWrap((short) 2);
                } else {
                    if (officeDrawingAt.isBelowText()) {
                        wPPictureShape.setWrap((short) 6);
                    } else {
                        wPPictureShape.setWrap((short) 3);
                    }
                    processPicturePosition(officeDrawingAt, wPPictureShape);
                }
                AttrManage.instance().setShapeID(iElement.getAttribute(), this.control.getSysKit().getWPShapeManage().addShape(wPPictureShape));
                return true;
            }
        } else {
            PicturesTable picturesTable = this.poiDoc.getPicturesTable();
            com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hwpf.usermodel.Picture extractPicture = picturesTable.extractPicture(this.control.getSysKit().getPictureManage().getPicTempPath(), characterRun, false);
            if (extractPicture == null || !isSupportPicture(extractPicture.suggestPictureType())) {
                InlineWordArt extracInlineWordArt = picturesTable.extracInlineWordArt(characterRun);
                if (!(extracInlineWordArt == null || extracInlineWordArt.getInlineWordArt() == null)) {
                    WPAutoShape wPAutoShape = new WPAutoShape();
                    Rectangle rectangle2 = new Rectangle();
                    rectangle2.width = (int) (((((float) extracInlineWordArt.getDxaGoal()) * 0.06666667f) * ((float) extracInlineWordArt.getHorizontalScalingFactor())) / 1000.0f);
                    rectangle2.height = (int) (((((float) extracInlineWordArt.getDyaGoal()) * 0.06666667f) * ((float) extracInlineWordArt.getVerticalScalingFactor())) / 1000.0f);
                    wPAutoShape.setBounds(rectangle2);
                    wPAutoShape.setWrap((short) 2);
                    processWordArtTextbox(extracInlineWordArt.getInlineWordArt().getSpContainer(), wPAutoShape);
                    AttrManage.instance().setShapeID(iElement.getAttribute(), this.control.getSysKit().getWPShapeManage().addShape(wPAutoShape));
                    return true;
                }
            } else {
                PictureShape pictureShape2 = new PictureShape();
                int pictureIndex2 = this.control.getSysKit().getPictureManage().getPictureIndex(extractPicture.getTempFilePath());
                if (pictureIndex2 < 0) {
                    Picture picture2 = new Picture();
                    picture2.setTempFilePath(extractPicture.getTempFilePath());
                    picture2.setPictureType(extractPicture.suggestPictureType().getExtension());
                    pictureIndex2 = this.control.getSysKit().getPictureManage().addPicture(picture2);
                }
                pictureShape2.setPictureIndex(pictureIndex2);
                Rectangle rectangle3 = new Rectangle();
                rectangle3.width = (int) (((((float) extractPicture.getDxaGoal()) * 0.06666667f) * ((float) extractPicture.getHorizontalScalingFactor())) / 1000.0f);
                rectangle3.height = (int) (((((float) extractPicture.getDyaGoal()) * 0.06666667f) * ((float) extractPicture.getVerticalScalingFactor())) / 1000.0f);
                pictureShape2.setBounds(rectangle3);
                pictureShape2.setZoomX(extractPicture.getZoomX());
                pictureShape2.setZoomY(extractPicture.getZoomY());
                pictureShape2.setPictureEffectInfor(PictureEffectInfoFactory.getPictureEffectInfor(extractPicture));
                WPPictureShape wPPictureShape2 = new WPPictureShape();
                wPPictureShape2.setPictureShape(pictureShape2);
                wPPictureShape2.setWrap((short) 2);
                AttrManage.instance().setShapeID(iElement.getAttribute(), this.control.getSysKit().getWPShapeManage().addShape(wPPictureShape2));
                return true;
            }
        }
        return false;
    }

    private boolean isSupportPicture(PictureType pictureType) {
        String extension = pictureType.getExtension();
        return extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase(ContentTypes.EXTENSION_JPG_1) || extension.equalsIgnoreCase("bmp") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase(Picture.WMF_TYPE) || extension.equalsIgnoreCase(Picture.EMF_TYPE);
    }

    public boolean searchContent(File file, String str) throws Exception {
        Range range = new HWPFDocument(new FileInputStream(file)).getRange();
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (int i = 0; i < range.numSections(); i++) {
            Section section = range.getSection(i);
            int i2 = 0;
            while (true) {
                if (i2 >= section.numParagraphs()) {
                    break;
                }
                Paragraph paragraph = section.getParagraph(i2);
                for (int i3 = 0; i3 < paragraph.numCharacterRuns(); i3++) {
                    sb.append(paragraph.getCharacterRun(i3).text());
                }
                if (sb.indexOf(str) >= 0) {
                    z = true;
                    break;
                }
                sb.delete(0, sb.length());
                i2++;
            }
        }
        return z;
    }

    private void adjustBookmarkOffset(long j, long j2) {
        for (Bookmark next : this.bms) {
            if (next.getStart() >= j && next.getStart() <= j2) {
                next.setStart(this.offset);
            }
        }
    }

    public void dispose() {
        if (isReaderFinish()) {
            this.wpdoc = null;
            this.filePath = null;
            this.poiDoc = null;
            this.control = null;
            this.hyperlinkAddress = null;
            List<Bookmark> list = this.bms;
            if (list != null) {
                list.clear();
                this.bms = null;
            }
        }
    }
}
