package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.html.HtmlTags;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.PaintKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.AutoShapeDataKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.ExtendPath;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.pathbuilder.ArrowPathAndTail;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.autoshape.pathbuilder.LineArrowPathBuilder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.BackgroundAndFill;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.Gradient;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.LinearGradientShader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.bg.PatternShader;
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
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.pictureefftect.PictureEffectInfo;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.pictureefftect.PictureEffectInfoFactory;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.AbstractShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.AutoShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.GroupShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.IShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.PictureShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.ShapeTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPAbstractShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPAutoShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPChartShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPGroupShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WPPictureShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.common.shape.WatermarkShape;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.SchemeClrConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.wp.WPAttrConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.wp.WPModelConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.FCKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.LineKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.dom4j.Document;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.dom4j.Element;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.dom4j.ElementHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.dom4j.ElementPath;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.dom4j.io.SAXReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.exceptions.InvalidFormatException;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackagePart;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackageRelationship;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.PackageRelationshipTypes;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ZipPackage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ppt.reader.ReaderKit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ppt.reader.ThemeReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.xls.Reader.drawing.ChartReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.java.awt.Rectangle;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.pg.model.PGPlaceholderUtil;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.font.FontTypefaceManage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.AttrManage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.IAttributeSet;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.LeafElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.ParagraphElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.SectionElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.Style;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.simpletext.model.StyleManage;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.AbortReaderError;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.AbstractReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IControl;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.thirdpart.achartengine.chart.AbstractChart;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.CellElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.HFElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.RowElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.TableElement;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.wp.model.WPDocument;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kotlinx.coroutines.DebugKt;

public class DOCXReader extends AbstractReader {
    Hashtable<String, String> bulletNumbersID = new Hashtable<>();
    private String filePath;
    private PackagePart hfPart;
    private boolean isProcessHF;
    private boolean isProcessSectionAttribute;
    private boolean isProcessWatermark;
    /* access modifiers changed from: private */
    public long offset;
    private PackagePart packagePart;
    private List<IShape> relativeType = new ArrayList();
    private Map<IShape, int[]> relativeValue = new HashMap();
    private SectionElement secElem;
    private int styleID;
    private Map<String, Integer> styleStrID = new HashMap();
    private Map<Integer, Integer> tableGridCol = new HashMap();
    private Map<String, Integer> tableStyle = new HashMap();
    private long textboxIndex;
    private Map<String, Integer> themeColor;
    /* access modifiers changed from: private */
    public WPDocument wpdoc;
    private ZipPackage zipPackage;

    public DOCXReader(IControl iControl, String str) {
        this.control = iControl;
        this.filePath = str;
    }

    public Object getModel() throws Exception {
        WPDocument wPDocument = this.wpdoc;
        if (wPDocument != null) {
            return wPDocument;
        }
        this.wpdoc = new WPDocument();
        openFile();
        return this.wpdoc;
    }

    private void openFile() throws Exception {
        ZipPackage zipPackage2 = new ZipPackage(this.filePath);
        this.zipPackage = zipPackage2;
        if (zipPackage2.getParts().size() != 0) {
            PackageRelationship relationship = this.zipPackage.getRelationshipsByType(PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
            if (relationship.getTargetURI().toString().equals("/word/document.xml")) {
                this.packagePart = this.zipPackage.getPart(relationship);
                processThemeColor();
                processBulletNumber();
                processStyle();
                this.secElem = new SectionElement();
                this.offset = 0;
                SAXReader sAXReader = new SAXReader();
                DOCXSaxHandler dOCXSaxHandler = new DOCXSaxHandler();
                sAXReader.addHandler("/document/body/tbl", dOCXSaxHandler);
                sAXReader.addHandler("/document/body/p", dOCXSaxHandler);
                sAXReader.addHandler("/document/body/sdt", dOCXSaxHandler);
                Document read = sAXReader.read(this.packagePart.getInputStream());
                Element element = read.getRootElement().element("background");
                if (element != null) {
                    BackgroundAndFill backgroundAndFill = null;
                    if (element.element("background") != null) {
                        backgroundAndFill = processBackgroundAndFill(element.element("background"));
                    } else {
                        String attributeValue = element.attributeValue("color");
                        if (attributeValue != null) {
                            backgroundAndFill = new BackgroundAndFill();
                            backgroundAndFill.setForegroundColor(Color.parseColor("#" + attributeValue));
                        }
                    }
                    this.wpdoc.setPageBackground(backgroundAndFill);
                }
                processSection(read.getRootElement().element("body"));
                processRelativeShapeSize();
                return;
            }
            throw new Exception("Format error");
        }
        throw new Exception("Format error");
    }

    private void processStyle() throws Exception {
        PackagePart part;
        Element element;
        Element element2;
        Element element3;
        String attributeValue;
        Element element4;
        Element element5;
        PackageRelationship relationship = this.packagePart.getRelationshipsByType(PackageRelationshipTypes.STYLE_PART).getRelationship(0);
        if (relationship != null && (part = this.zipPackage.getPart(relationship.getTargetURI())) != null) {
            SAXReader sAXReader = new SAXReader();
            InputStream inputStream = part.getInputStream();
            Element rootElement = sAXReader.read(inputStream).getRootElement();
            List<Element> elements = rootElement.elements(HtmlTags.STYLE);
            Element element6 = rootElement.element("docDefaults");
            if (element6 != null) {
                Style style = new Style();
                this.styleStrID.put("docDefaults", Integer.valueOf(this.styleID));
                style.setId(this.styleID);
                this.styleID++;
                style.setType((byte) 0);
                style.setName("docDefaults");
                Element element7 = element6.element("pPrDefault");
                if (!(element7 == null || (element5 = element7.element("pPr")) == null)) {
                    processParaAttribute(element5, style.getAttrbuteSet(), 0);
                }
                Element element8 = element6.element("rPrDefault");
                if (!(element8 == null || (element4 = element8.element("rPr")) == null)) {
                    processRunAttribute(element4, style.getAttrbuteSet());
                }
                StyleManage.instance().addStyle(style);
            }
            for (Element element9 : elements) {
                if (this.abortReader) {
                    break;
                }
                if (!(!HtmlTags.TABLE.equals(element9.attributeValue("type")) || (element = element9.element("tblStylePr")) == null || !"firstRow".equals(element.attributeValue("type")) || (element2 = element.element("tcPr")) == null || (element3 = element2.element("shd")) == null || (attributeValue = element3.attributeValue("fill")) == null)) {
                    this.tableStyle.put(element9.attributeValue("styleId"), Integer.valueOf(Color.parseColor("#" + attributeValue)));
                }
                Style style2 = new Style();
                String attributeValue2 = element9.attributeValue("styleId");
                if (attributeValue2 != null) {
                    Integer num = this.styleStrID.get(attributeValue2);
                    if (num == null) {
                        this.styleStrID.put(attributeValue2, Integer.valueOf(this.styleID));
                        style2.setId(this.styleID);
                        this.styleID++;
                    } else {
                        style2.setId(num.intValue());
                    }
                }
                style2.setType(element9.attributeValue("type").equals("paragraph") ^ true ? (byte) 1 : 0);
                Element element10 = element9.element("name");
                if (element10 != null) {
                    style2.setName(element10.attributeValue("val"));
                }
                Element element11 = element9.element("basedOn");
                if (element11 != null) {
                    String attributeValue3 = element11.attributeValue("val");
                    if (attributeValue3 != null) {
                        Integer num2 = this.styleStrID.get(attributeValue3);
                        if (num2 == null) {
                            this.styleStrID.put(attributeValue3, Integer.valueOf(this.styleID));
                            style2.setBaseID(this.styleID);
                            this.styleID++;
                        } else {
                            style2.setBaseID(num2.intValue());
                        }
                    }
                } else if ("1".equals(element9.attributeValue("default"))) {
                    style2.setBaseID(0);
                }
                Element element12 = element9.element("pPr");
                if (element12 != null) {
                    processParaAttribute(element12, style2.getAttrbuteSet(), 0);
                }
                Element element13 = element9.element("rPr");
                if (element13 != null) {
                    processRunAttribute(element13, style2.getAttrbuteSet());
                }
                StyleManage.instance().addStyle(style2);
            }
            inputStream.close();
        }
    }

    private void processBulletNumber() throws Exception
    {
        PackageRelationship styleRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.BULLET_NUMBER_PART).getRelationship(0);
        //styleRel.get
        if (styleRel != null)
        {
            PackagePart part = zipPackage.getPart(styleRel.getTargetURI());
            if (part != null)
            {
                SAXReader saxreader = new SAXReader();
                InputStream in = part.getInputStream();
                Document doc = saxreader.read(in);
                Element root = doc.getRootElement();
                List<Element> nums = root.elements("num");
                Element temp;
                for (Element num : nums)
                {
                    temp = num.element("abstractNumId");
                    if (temp != null)
                    {
                        String val = temp.attributeValue("val");
                        String numID = num.attributeValue("numId");
                        bulletNumbersID.put(numID, val);
                    }
                }
                // bullet and number object
                nums = root.elements("abstractNum");
                for (Element num : nums)
                {
                    ListData listData = new ListData();
                    // ID
                    String abstractNumId = num.attributeValue("abstractNumId");
                    if (abstractNumId != null)
                    {
                        listData.setListID(Integer.parseInt(abstractNumId));
                    }
                    // list level
                    List<Element> levels = num.elements("lvl");
                    int len = levels.size();
                    ListLevel[] listLevels = new ListLevel[len];
                    listData.setSimpleList((byte)len);
                    for (int i = 0; i < len; i++)
                    {
                        listLevels[i] = new ListLevel();
                        processListLevel(listLevels[i], levels.get(i));
                    }
                    listData.setLevels(listLevels);
                    // simple list
                    listData.setSimpleList((byte)len);
                    if (len == 0)
                    {
                        Element styleLink = num.element("numStyleLink");
                        if (styleLink != null)
                        {
                            String styleID = styleLink.attributeValue("val");
                            if (styleID != null)
                            {
                                Integer a = styleStrID.get(styleID);
                                if (a != null)
                                {
                                    listData.setLinkStyleID(a.shortValue());
                                    // change style
                                    Style style = StyleManage.instance().getStyle(a.intValue());
                                    int styleListNumID = AttrManage.instance().getParaListID(style.getAttrbuteSet());
                                    if (styleListNumID >= 0)
                                    {
                                        styleListNumID = Integer.parseInt(bulletNumbersID.get(String.valueOf(styleListNumID)));
                                        AttrManage.instance().setParaListID(style.getAttrbuteSet(), styleListNumID);
                                    }
                                }
                            }
                        }
                    }
                    //
                    control.getSysKit().getListManage().putListData(listData.getListID(), listData);
                }
                in.close();
            }
        }
    }

    private void processListLevel(ListLevel level, Element elem)
    {
        // start at
        String val;
        Element temp = elem.element("start");
        if (temp != null)
        {
            level.setStartAt(Integer.parseInt(temp.attributeValue("val")));
        }
        // horizontal alignment;
        temp = elem.element("lvlJc");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("left".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_LEFT);
            }
            else if ("center".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_CENTER);
            }
            else if ("right".equals(val))
            {
                level.setAlign(WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
            }

        }
        // follow char
        temp = elem.element("suff");
        if (temp != null)
        {
            val = temp.attributeValue("val");
            if ("space".equals(val))
            {
                level.setFollowChar((byte)1);
            }
            else if("nothing".equals(val))
            {
                level.setFollowChar((byte)2);
            }
        }
        // number format
        temp = elem.element("numFmt");
        if (temp != null)
        {
            level.setNumberFormat(convertedNumberFormat(temp.attributeValue("val")));
        }
        // number text
        temp = elem.element("lvlText");
        if (temp != null)
        {
            StringBuilder sb = new StringBuilder();
            val = temp.attributeValue("val");
            for (int i = 0; i < val.length(); i++)
            {
                char c = val.charAt(i);
                if (c == '%')
                {
                    int a = Integer.parseInt(val.substring(i + 1, i + 2));
                    sb.append((char)(a - 1));
                    i++;
                }
                else
                {
                    if (c == 0xF06C)
                    {
                        c = 0x25CF;
                    }
                    else if (c == 0xF06E)
                    {
                        c = 0x25A0;
                    }
                    else if (c == 0xF075)
                    {
                        c = 0x25C6;
                    }
                    else if (c == 0xF0FC)
                    {
                        c = 0x221A;
                    }
                    else if (c == 0xF0D8)
                    {
                        c = 0x2605;
                    }
                    else if (c == 0xF0B2)
                    {
                        c = 0x2606;
                    }
                    else if (c >= 0xF000)
                    {
                        c = 0x25CF;
                    }
                    sb.append(c);
                }
            }
            char[] c = new char[sb.length()];
            sb.getChars(0, sb.length(), c, 0);
            level.setNumberText(c);
        }
        // indent
        temp = elem.element("pPr");
        if (temp != null)
        {
            temp = temp.element("ind");
            if (temp != null)
            {
                // special indent, default 21 POINT
                val = temp.attributeValue("hanging");
                if (val != null)
                {
                    level.setSpecialIndent(-Integer.parseInt(val));
                }
                // left text indent, default 21 point * level
                val = temp.attributeValue("left");
                if (val != null)
                {
                    level.setTextIndent(Integer.parseInt(val));
                }
            }
        }
    }

    private int convertedNumberFormat(String str) {
        if ("decimal".equalsIgnoreCase(str)) {
            return 0;
        }
        if ("upperRoman".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("lowerRoman".equalsIgnoreCase(str)) {
            return 2;
        }
        if ("upperLetter".equalsIgnoreCase(str)) {
            return 3;
        }
        if ("lowerLetter".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("chineseCountingThousand".equalsIgnoreCase(str)) {
            return 39;
        }
        if ("chineseLegalSimplified".equalsIgnoreCase(str)) {
            return 38;
        }
        if ("ideographTraditional".equalsIgnoreCase(str)) {
            return 30;
        }
        if ("ideographZodiac".equalsIgnoreCase(str)) {
            return 31;
        }
        if ("ordinal".equalsIgnoreCase(str)) {
            return 5;
        }
        if ("cardinalText".equalsIgnoreCase(str)) {
            return 6;
        }
        if ("ordinalText".equalsIgnoreCase(str)) {
            return 7;
        }
        if ("decimalZero".equalsIgnoreCase(str)) {
            return 22;
        }
        return 0;
    }

    private void processHeaderAndFooter(PackageRelationship packageRelationship, boolean z) throws Exception {
        if (packageRelationship != null) {
            PackagePart part = this.zipPackage.getPart(packageRelationship.getTargetURI());
            this.hfPart = part;
            if (part != null) {
                this.isProcessHF = true;
                this.offset = z ? 1152921504606846976L : 2305843009213693952L;
                SAXReader sAXReader = new SAXReader();
                InputStream inputStream = this.hfPart.getInputStream();
                List elements = sAXReader.read(inputStream).getRootElement().elements();
                HFElement hFElement = new HFElement(z ? (short) 5 : 6, (byte) 1);
                hFElement.setStartOffset(this.offset);
                processParagraphs(elements);
                hFElement.setEndOffset(this.offset);
                this.wpdoc.appendElement(hFElement, this.offset);
                inputStream.close();
                this.isProcessHF = false;
            }
        }
    }

    private void processSection(Element element) throws Exception {
        this.secElem.setStartOffset(0);
        this.secElem.setEndOffset(this.offset);
        this.wpdoc.appendSection(this.secElem);
        processSectionAttribute(element.element("sectPr"));
    }

    private void processSectionAttribute(Element element) {
        String str;
        String attributeValue;
        if (element != null && !this.isProcessSectionAttribute) {
            IAttributeSet attribute = this.secElem.getAttribute();
            Element element2 = element.element("pgSz");
            if (element2 != null) {
                AttrManage.instance().setPageWidth(attribute, Integer.parseInt(element2.attributeValue("w")));
                AttrManage.instance().setPageHeight(attribute, Integer.parseInt(element2.attributeValue("h")));
            }
            Element element3 = element.element("pgMar");
            if (element3 != null) {
                AttrManage.instance().setPageMarginLeft(attribute, Integer.parseInt(element3.attributeValue(HtmlTags.ALIGN_LEFT)));
                AttrManage.instance().setPageMarginRight(attribute, Integer.parseInt(element3.attributeValue(HtmlTags.ALIGN_RIGHT)));
                AttrManage.instance().setPageMarginTop(attribute, Integer.parseInt(element3.attributeValue(HtmlTags.ALIGN_TOP)));
                AttrManage.instance().setPageMarginBottom(attribute, Integer.parseInt(element3.attributeValue(HtmlTags.ALIGN_BOTTOM)));
                if (element3.attributeValue("header") != null) {
                    AttrManage.instance().setPageHeaderMargin(attribute, Integer.parseInt(element3.attributeValue("header")));
                }
                if (element3.attributeValue("footer") != null) {
                    AttrManage.instance().setPageFooterMargin(attribute, Integer.parseInt(element3.attributeValue("footer")));
                }
            }
            Element element4 = element.element("pgBorders");
            if (element4 != null) {
                Borders borders = new Borders();
                if (Annotation.PAGE.equals(element4.attributeValue("offsetFrom"))) {
                    borders.setOnType((byte) 1);
                }
                Element element5 = element4.element(HtmlTags.ALIGN_TOP);
                if (element5 != null) {
                    Border border = new Border();
                    processBorder(element5, border);
                    borders.setTopBorder(border);
                }
                Element element6 = element4.element(HtmlTags.ALIGN_LEFT);
                if (element6 != null) {
                    Border border2 = new Border();
                    processBorder(element6, border2);
                    borders.setLeftBorder(border2);
                }
                Element element7 = element4.element(HtmlTags.ALIGN_RIGHT);
                if (element7 != null) {
                    Border border3 = new Border();
                    processBorder(element7, border3);
                    borders.setRightBorder(border3);
                }
                Element element8 = element4.element(HtmlTags.ALIGN_BOTTOM);
                if (element8 != null) {
                    Border border4 = new Border();
                    processBorder(element8, border4);
                    borders.setBottomBorder(border4);
                }
                AttrManage.instance().setPageBorder(attribute, this.control.getSysKit().getBordersManage().addBorders(borders));
            }
            Element element9 = element.element("docGrid");
            if (element9 != null) {
                String attributeValue2 = element9.attributeValue("type");
                if (("lines".equals(attributeValue2) || "linesAndChars".equals(attributeValue2) || "snapToChars".equals(attributeValue2)) && (attributeValue = element9.attributeValue("linePitch")) != null && attributeValue.length() > 0) {
                    AttrManage.instance().setPageLinePitch(attribute, Integer.parseInt(attributeValue));
                    for (int i = 0; ((long) i) < this.textboxIndex; i++) {
                        AttrManage.instance().setPageLinePitch(this.wpdoc.getTextboxSectionElementForIndex(i).getAttribute(), AttrManage.instance().getPageLinePitch(this.secElem.getAttribute()));
                    }
                }
            }
            long j = this.offset;
            List elements = element.elements("headerReference");
            String str2 = "";
            if (elements != null && elements.size() > 0) {
                if (elements.size() != 1) {
                    Iterator it = elements.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            str = str2;
                            break;
                        }
                        Element element10 = (Element) it.next();
                        if ("default".equals(element10.attributeValue("type"))) {
                            str = element10.attributeValue(ScreenCVEdit.FIELD_ID);
                            break;
                        }
                    }
                } else {
                    str = ((Element) elements.get(0)).attributeValue(ScreenCVEdit.FIELD_ID);
                }
                if (str != null && str.length() > 0) {
                    try {
                        PackageRelationship relationshipByID = this.packagePart.getRelationshipsByType(PackageRelationshipTypes.HEADER_PART).getRelationshipByID(str);
                        if (relationshipByID != null) {
                            processHeaderAndFooter(relationshipByID, true);
                        }
                    } catch (Exception e) {
                        this.control.getSysKit().getErrorKit().writerLog(e, true);
                    }
                }
            }
            List elements2 = element.elements("footerReference");
            if (elements2 != null && elements2.size() > 0) {
                if (elements2.size() != 1) {
                    Iterator it2 = elements2.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        Element element11 = (Element) it2.next();
                        if ("default".equals(element11.attributeValue("type"))) {
                            str2 = element11.attributeValue(ScreenCVEdit.FIELD_ID);
                            break;
                        }
                    }
                } else {
                    str2 = ((Element) elements2.get(0)).attributeValue(ScreenCVEdit.FIELD_ID);
                }
                if (str2 != null && str2.length() > 0) {
                    try {
                        PackageRelationship relationshipByID2 = this.packagePart.getRelationshipsByType(PackageRelationshipTypes.FOOTER_PART).getRelationshipByID(str2);
                        if (relationshipByID2 != null) {
                            processHeaderAndFooter(relationshipByID2, false);
                        }
                    } catch (Exception e2) {
                        this.control.getSysKit().getErrorKit().writerLog(e2, true);
                    }
                }
            }
            this.offset = j;
            this.isProcessSectionAttribute = true;
        }
    }

    private void processBorder(Element element, Border border) {
        String attributeValue = element.attributeValue("color");
        if (attributeValue == null || DebugKt.DEBUG_PROPERTY_VALUE_AUTO.equals(attributeValue)) {
            border.setColor(-16777216);
        } else {
            border.setColor(Color.parseColor("#" + attributeValue));
        }
        String attributeValue2 = element.attributeValue("space");
        if (attributeValue2 == null) {
            border.setSpace((short) 32);
        } else {
            border.setSpace((short) ((int) (((float) Integer.parseInt(attributeValue2)) * 1.3333334f)));
        }
    }

    private void processTable(Element table)
    {
        TableElement tableElem = new TableElement();
        tableElem.setStartOffset(offset);

        Element trPr = table.element("tblPr");
        String tableStyleId = "";
        if (trPr != null)
        {
            processTableAttribute(trPr, tableElem.getAttribute());
            Element tStyleId = trPr.element("tblStyle");
            if (tStyleId != null)
            {
                tableStyleId = tStyleId.attributeValue("val");
                if (tableStyleId == null)
                {
                    tableStyleId = "";
                }
            }
        }
        // table grid column width
        Element tblGrid = table.element("tblGrid");
        if (tblGrid != null)
        {
            List<Element> grids = tblGrid.elements("gridCol");
            if (grids != null)
            {
                for (int i = 0; i < grids.size(); i++)
                {
                    tableGridCol.put(i, Integer.parseInt(grids.get(i).attributeValue("w")));
                }
            }
        }

        List<Element> rows = table.elements("tr");
        boolean firstRow = true;
        for (Element row : rows)
        {
            processRow(row, tableElem, firstRow, tableStyleId);
            firstRow = false;
        }

        tableElem.setEndOffset(offset);
        wpdoc.appendParagraph(tableElem, offset);
    }

    private void processTableAttribute(Element element, IAttributeSet iAttributeSet) {
        String attributeValue;
        Element element2 = element.element("jc");
        if (element2 != null) {
            String attributeValue2 = element2.attributeValue("val");
            if (HtmlTags.ALIGN_CENTER.equals(attributeValue2)) {
                AttrManage.instance().setParaHorizontalAlign(iAttributeSet, 1);
            } else if (HtmlTags.ALIGN_RIGHT.equals(attributeValue2)) {
                AttrManage.instance().setParaHorizontalAlign(iAttributeSet, 2);
            }
        }
        Element element3 = element.element("tblInd");
        if (element3 != null && (attributeValue = element3.attributeValue("w")) != null) {
            AttrManage.instance().setParaIndentLeft(iAttributeSet, Integer.parseInt(attributeValue));
        }
    }

    private void processRow(Element row, TableElement tableElem, boolean firstRow, String tableStyleId)
    {
        RowElement rowElem = new RowElement();
        rowElem.setStartOffset(offset);

        Element trPr = row.element("trPr");
        if (trPr != null)
        {
            processRowAttribute(trPr, rowElem.getAttribute());
        }

        List<Element> cells = row.elements("tc");
        int i = 0;

        for (Element cell : cells)
        {
            i += processCell(cell, rowElem, i, firstRow, tableStyleId);
        }

        rowElem.setEndOffset(offset);
        tableElem.appendRow(rowElem);
    }

    private void processRowAttribute(Element element, IAttributeSet iAttributeSet) {
        Element element2 = element.element("trHeight");
        if (element2 != null) {
            AttrManage.instance().setTableRowHeight(iAttributeSet, Integer.parseInt(element2.attributeValue("val")));
        }
    }

    private int processCell(Element element, RowElement rowElement, int i, boolean z, String str) {
        CellElement cellElement = new CellElement();
        cellElement.setStartOffset(this.offset);
        Element element2 = element.element("tcPr");
        int processCellAttribute = element2 != null ? processCellAttribute(element2, cellElement.getAttribute(), i) : 0;
        processParagraphs_Table(element.elements(), 1);
        cellElement.setEndOffset(this.offset);
        rowElement.appendCell(cellElement);
        if (z && this.tableStyle.containsKey(str)) {
            AttrManage.instance().setTableCellBackground(cellElement.getAttribute(), this.tableStyle.get(str).intValue());
        }
        if (processCellAttribute > 0) {
            for (int i2 = 1; i2 < processCellAttribute; i2++) {
                rowElement.appendCell(new CellElement());
            }
        }
        return processCellAttribute;
    }

    private int processCellAttribute(Element element, IAttributeSet iAttributeSet, int i) {
        Element element2 = element.element("gridSpan");
        int parseInt = element2 != null ? Integer.parseInt(element2.attributeValue("val")) : 1;
        Element element3 = element.element("tcW");
        int i2 = 0;
        if (element3 != null) {
            int parseInt2 = Integer.parseInt(element3.attributeValue("w"));
            String attributeValue = element3.attributeValue("type");
            if ("pct".equals(attributeValue) || DebugKt.DEBUG_PROPERTY_VALUE_AUTO.equals(attributeValue)) {
                for (int i3 = i; i3 < i + parseInt; i3++) {
                    i2 += this.tableGridCol.get(Integer.valueOf(i3)).intValue();
                }
                parseInt2 = Math.max(i2, parseInt2);
            }
            AttrManage.instance().setTableCellWidth(iAttributeSet, parseInt2);
        } else {
            for (int i4 = i; i4 < i + parseInt; i4++) {
                i2 += this.tableGridCol.get(Integer.valueOf(i4)).intValue();
            }
            AttrManage.instance().setTableCellWidth(iAttributeSet, i2);
        }
        Element element4 = element.element("vMerge");
        if (element4 != null) {
            AttrManage.instance().setTableVerMerged(iAttributeSet, true);
            if (element4.attributeValue("val") != null) {
                AttrManage.instance().setTableVerFirstMerged(iAttributeSet, true);
            }
        }
        Element element5 = element.element("vAlign");
        if (element5 != null) {
            String attributeValue2 = element5.attributeValue("val");
            if (HtmlTags.ALIGN_CENTER.equals(attributeValue2)) {
                AttrManage.instance().setTableCellVerAlign(iAttributeSet, 1);
            } else if (HtmlTags.ALIGN_BOTTOM.equals(attributeValue2)) {
                AttrManage.instance().setTableCellVerAlign(iAttributeSet, 2);
            }
        }
        return parseInt;
    }

    private void processParagraphs_Table(List<Element> list, int i) {
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element next = it.next();
            if ("sdt".equals(next.getName()) && (next = next.element("sdtContent")) != null) {
                processParagraphs_Table(next.elements(), i);
            }
            if (HtmlTags.P.equals(next.getName())) {
                processParagraph(next, i);
            } else if (PGPlaceholderUtil.TABLE.equals(next.getName())) {
                processEmbeddedTable(next, i);
            }
        }
    }

    private void processEmbeddedTable(Element table, int level)
    {
        List<Element> rows = table.elements("tr");
        for (Element row : rows)
        {
            List<Element> cells = row.elements("tc");
            for (Element cell : cells)
            {
                processParagraphs_Table(cell.elements(), level);
            }
        }
    }

    /* access modifiers changed from: private */
    public void processParagraph(Element element, int i) {
        ParagraphElement paragraphElement = new ParagraphElement();
        long j = this.offset;
        paragraphElement.setStartOffset(j);
        processParaAttribute(element.element("pPr"), paragraphElement.getAttribute(), i);
        processRun(element, paragraphElement, true);
        paragraphElement.setEndOffset(this.offset);
        long j2 = this.offset;
        if (j2 > j) {
            this.wpdoc.appendParagraph(paragraphElement, j2);
        }
    }

    private void processParaAttribute(Element pPr, IAttributeSet attr, int level)
    {
        if (level > 0)
        {
            AttrManage.instance().setParaLevel(attr, level);
        }
        if (pPr == null)
        {
            return;
        }

        // 样式
        Element temp = pPr.element("pStyle");
        if (temp != null)
        {
            String val = temp.attributeValue("val");
            if (val != null && val.length() > 0)
            {
                AttrManage.instance().setParaStyleID(attr, styleStrID.get(val));
            }
        }
        else
        {
            AttrManage.instance().setParaStyleID(attr, 0);
        }

        // 段前段后
        temp = pPr.element("spacing");
        if (temp != null)
        {
            // 行单位
            String val = temp.attributeValue("line");
            if (val != null)
            {
                int lineUnit = Integer.parseInt(val);
                // 默认行单位是12磅
                lineUnit = lineUnit == 0 ? 240 : lineUnit;
                // 段前
                val = temp.attributeValue("beforeLines");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaBefore(attr, (int)(Integer.parseInt(val) / 100.f * lineUnit));
                }
                if (val == null)
                {
                    // 段前
                    val = temp.attributeValue("before");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaBefore(attr, Integer.parseInt(val));
                    }
                }
                // 段后
                val = temp.attributeValue("afterLines");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaAfter(attr, (int)(Integer.parseInt(val) / 100.f * lineUnit));
                }
                if (val == null)
                {
                    // 段后
                    val = temp.attributeValue("after");
                    if (val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaAfter(attr, Integer.parseInt(val));
                    }
                }
            }
            // 非单位
            else
            {
                // 段前
                val = temp.attributeValue("before");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaBefore(attr, Integer.parseInt(val));
                }
                else
                {
                    val = temp.attributeValue("beforeLines");
                    if(val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaBefore(attr, (int)(Integer.parseInt(val) / 100.f * 240));
                    }
                }

                // 段后
                val = temp.attributeValue("after");
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaAfter(attr, Integer.parseInt(val));
                }
                else
                {
                    val = temp.attributeValue("afterLines");
                    if(val != null && val.length() > 0)
                    {
                        AttrManage.instance().setParaAfter(attr, (int)(Integer.parseInt(val) / 100.f * 240));
                    }
                }
            }
            // 行距
            val = temp.attributeValue("lineRule");
            float lineSpace = 0;
            if ((temp.attributeValue("line") != null))
            {
                lineSpace = Float.parseFloat(temp.attributeValue("line"));
            }
            // 多倍行距
            if ("auto".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr,lineSpace / 240.f);
                }
            }
            // 最小值
            else if("atLeast".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_LEAST);
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, lineSpace);
                }
            }
            // 固定值
            else if ("exact".equals(val))
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SPACE_EXACTLY);
                // 行距
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, -lineSpace);
                }
            }
            else
            {
                // 行距类型
                AttrManage.instance().setParaLineSpaceType(attr, WPAttrConstant.LINE_SAPCE_MULTIPLE);
                // 行距默认值
                if (lineSpace != 0)
                {
                    AttrManage.instance().setParaLineSpace(attr, -lineSpace / 240.f);
                }
            }

        }
        // 左右缩进
        temp = pPr.element("ind");
        if (temp != null)
        {
            int left = 0;
            int fontSize = 12;
            Style style = StyleManage.instance().getStyle(styleStrID.get("docDefaults"));
            if(style != null)
            {
                IAttributeSet  defaultAttr = style.getAttrbuteSet();
                if(defaultAttr != null)
                {
                    fontSize = AttrManage.instance().getFontSize(defaultAttr,defaultAttr);
                }
            }

            // 左缩进
            String val = temp.attributeValue("leftChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
                float leftChars = Integer.parseInt(val) / 100f;
                AttrManage.instance().setParaIndentLeft(attr, Math.round(fontSize * leftChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("left")) != null)
            {
                if (val != null && val.length() > 0)
                {
                    left = Integer.parseInt(val);
                    AttrManage.instance().setParaIndentLeft(attr, left);
                }
            }

            // 右缩进
            val = temp.attributeValue("rightChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
                float rightChars = Integer.parseInt(val) / 100f;
                AttrManage.instance().setParaIndentRight(attr, Math.round(fontSize * rightChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("right")) != null)
            {
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaIndentRight(attr, Integer.parseInt(val));
                }
            }

            // 首行缩进
            val = temp.attributeValue("firstLineChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
                float firstLineChars = Integer.parseInt(val) / 100f;
                AttrManage.instance().setParaSpecialIndent(attr, Math.round(fontSize * firstLineChars * MainConstant.POINT_TO_TWIPS));
            }
            else if((val = temp.attributeValue("firstLine")) != null)
            {
                if (val != null && val.length() > 0)
                {
                    AttrManage.instance().setParaSpecialIndent(attr, Integer.parseInt(val));
                }

            }
            // 悬挂缩进
            val = temp.attributeValue("hangingChars");
            if(val != null && val.length() > 0 && !val.equals("0"))
            {
                float hangingChars = Integer.parseInt(val) / 100f;
                int hanging = -Math.round(fontSize * hangingChars * MainConstant.POINT_TO_TWIPS);
                AttrManage.instance().setParaSpecialIndent(attr, hanging);
                // 悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
                if(left == 0)
                {
                    //left indent maybe not init, so get it repeatly
                    left = AttrManage.instance().getParaIndentLeft(attr);
                }
                if (hanging < 0)
                {
                    AttrManage.instance().setParaIndentLeft(attr, left + hanging);
                }
            }
            else if((val = temp.attributeValue("hanging")) != null)
            {
                if (val != null && val.length() > 0)
                {
                    int sp = -Integer.parseInt(val);
                    AttrManage.instance().setParaSpecialIndent(attr, sp);
                    // 悬挂缩进值也设置到左缩进，左缩进需要减去悬挂缩进
                    if (sp < 0)
                    {
                        AttrManage.instance().setParaIndentLeft(attr, left + sp);
                    }
                }
            }
        }
        // 对齐方式
        temp = pPr.element("jc");
        if(temp != null)
        {
            String value = temp.attributeValue("val");
            // left
            if ("left".equals(value)
                    || "both".equals(value)
                    || "distribute".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_LEFT);
            }
            else if ("center".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_CENTER);
            }
            else if ("right".equals(value))
            {
                AttrManage.instance().setParaHorizontalAlign(attr, WPAttrConstant.PARA_HOR_ALIGN_RIGHT);
            }
        }


        // bullet and number
        temp = pPr.element("numPr");
        if (temp != null)
        {
            Element t = temp.element("ilvl");
            // level
            if (t != null)
            {
                AttrManage.instance().setParaListLevel(attr, Integer.parseInt(t.attributeValue("val")));
            }
            // list ID
            t = temp.element("numId");
            if (t != null)
            {
                String val = t.attributeValue("val");
                if (val != null)
                {
                    String bnID = bulletNumbersID.get(val);
                    if (bnID != null)
                    {
                        AttrManage.instance().setParaListID(attr, Integer.parseInt(bnID));
                    }
                    else
                    {
                        //AttrManage.instance().setParaListID(attr, Integer.parseInt(val));
                    }
                }
            }
        }
        else
        {
            //check from paragraph style
            Style style = StyleManage.instance().getStyle(AttrManage.instance().getParaStyleID(attr));
            if(style != null)
            {
                int paraListLevel = AttrManage.instance().getParaListLevel(style.getAttrbuteSet());
                int paraListID = AttrManage.instance().getParaListID(style.getAttrbuteSet());
                if(paraListID > -1)
                {
                    if(paraListLevel < 0)
                    {
                        paraListLevel = 0;
                    }

                    AttrManage.instance().setParaListID(attr, paraListID);
                }

                if(paraListLevel > -1)
                {
                    AttrManage.instance().setParaListLevel(attr, paraListLevel);
                }
            }
        }

        temp =  pPr.element("tabs");
        if (temp != null)
        {
            List<Element> tabs = temp.elements("tab");
            if (tabs != null)
            {
                for (Element tab : tabs)
                {
                    if ("clear".equals(tab.attributeValue("val")))
                    {
                        String val = tab.attributeValue("pos");
                        if (val != null && val.length() > 0)
                        {
                            AttrManage.instance().setParaTabsClearPostion(attr, Integer.parseInt(val));
                        }
                    }
                }
            }
        }
        if (!isProcessSectionAttribute)
        {
            processSectionAttribute(pPr.element("sectPr"));
        }
    }

    private boolean processRun(Element element, ParagraphElement paragraphElement, boolean z) {
        return processRun(element, paragraphElement, (byte) -1, z);
    }

    private boolean processRun(Element para, ParagraphElement paraElem, byte pgNumberType, boolean addBreakPage)
    {
        List<Element> runs = para.elements();
        LeafElement leaf = null;
        boolean hasLeaf = false;
        String fieldCodeStr ="";
        String fieldTextStr = "";
        boolean hasField = false;
        boolean pageBreak = false;
        for (Element run : runs)
        {
            // hyper
            String name = run.getName();
            if("smartTag".equals(name))
            {
                hasLeaf = processRun(run, paraElem, false);
            }
            else if ("hyperlink".equals(name))
            {
                leaf = processHyperlinkRun(run, paraElem);
                hasLeaf = leaf != null;
                continue;
            }
            else if ("bookmarkStart".equals(name))
            {
                String val = run.attributeValue("name");
                if (val != null)
                {
                    control.getSysKit().getBookmarkManage().addBookmark(new Bookmark(val, offset, offset));
                }
                continue;
            }
            else if ("fldSimple".equals(name))
            {
                String instr = run.attributeValue("instr");
                byte pageNumberType = -1;
                if(instr != null)
                {
                    if(instr.contains("NUMPAGES"))
                    {
                        pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                    }
                    else if(instr.contains("PAGE"))
                    {
                        pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                    }
                }

                hasLeaf = processRun(run, paraElem, pageNumberType, false);

                leaf = null;
            }
            else if ("sdt".equals(name))
            {
                Element sdtContent = run.element("sdtContent");
                if (sdtContent != null)
                {
                    Element ele =  null;
                    if((ele = run.element("sdtPr")) != null
                            && (ele = ele.element("docPartObj")) != null
                            && (ele = ele.element("docPartGallery")) != null)
                    {
                        String docPartGallery = ele.attributeValue("val");
                        if(docPartGallery != null)
                        {
                            if(isProcessHF && docPartGallery.contains("Margins"))
                            {
                                return false;
                            }
                            else if(docPartGallery.contains("Watermarks"))
                            {
                                isProcessWatermark = true;
                            }
                        }
                    }

                    hasLeaf = processRun(sdtContent, paraElem, false);

                    leaf = null;
                }
            }
            else if("ins".equals(name))
            {
                hasLeaf = processRun(run, paraElem, false);
            }
            else if ("r".equals(name))
            {
                // field

                Element fld = run.element("fldChar");
                if (fld != null)
                {
                    String fldType = fld.attributeValue("fldCharType");
                    if ("begin".equals(fldType))
                    {
                        hasField = true;
                        continue;
                    }
                    else if ("separate".equals(fldType))
                    {
//                        hasField = false;
                        continue;
                    }
                    else if("end".equals(fldType))
                    {
                        hasField = false;
                        if (fieldCodeStr != null)
                        {
                            String str = "";
                            byte encloseType = -1;
                            byte pageNumberType = -1;
                            // 圆
                            if (fieldCodeStr.indexOf('\u25cb') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_ROUND;
                            }
                            // 正方形
                            else if (fieldCodeStr.indexOf('\u25a1') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_SQUARE;
                            }
                            // 三角形
                            else if (fieldCodeStr.indexOf('\u25b3') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_TRIANGLE;
                            }
                            // 菱形
                            else if (fieldCodeStr.indexOf('\u25c7') > 0)
                            {
                                str = fieldCodeStr.substring(fieldCodeStr.indexOf(",") + 1, fieldCodeStr.length() - 1);
                                encloseType = WPModelConstant.ENCLOSURE_TYPE_RHOMBUS;
                            }
                            //////////////////////////////////////////////////////page number property
                            else if(fieldCodeStr.contains("NUMPAGES"))
                            {
                                str = fieldTextStr;
                                pageNumberType = WPModelConstant.PN_TOTAL_PAGES;
                            }
                            else if(fieldCodeStr.contains("PAGE"))
                            {
                                str = fieldTextStr;
                                pageNumberType = WPModelConstant.PN_PAGE_NUMBER;
                            }
                            else
                            {
                                str = fieldTextStr;
                            }

                            if (str.length() > 0)
                            {
                                hasLeaf = true;
                                leaf = new LeafElement(str);
                                // 属性
                                Element rPr = run.element("rPr");
                                if (rPr != null)
                                {
                                    processRunAttribute(rPr, leaf.getAttribute());
                                }

                                leaf.setStartOffset(offset);
                                offset += str.length();
                                leaf.setEndOffset(offset);

                                if(encloseType >= 0)
                                {
                                    AttrManage.instance().setEncloseChanacterType(leaf.getAttribute(), encloseType);
                                }
                                else if(isProcessHF && hfPart !=  null && pageNumberType > 0)
                                {
                                    AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pageNumberType);
                                }

                                paraElem.appendLeaf(leaf);
                            }
                            fieldCodeStr = "";
                            fieldTextStr = "";
                        }
                        continue;
                    }
                }
                if(hasField)
                {
                    Element fldCode = run.element("instrText");
                    if (fldCode != null)
                    {
                        fieldCodeStr += fldCode.getText();
                    }
                    else
                    {
                        Element text = run.element("t");
                        if (text != null)
                        {
                            fieldTextStr += text.getText();
                        }
                    }
                    continue;
                }

                //Inline Embedded Object
                Element object = run.element("object");
                if(object != null)
                {
                    for (Iterator< ? > it = object.elementIterator(); it.hasNext();)
                    {
                        processAutoShapeForPict((Element)it.next(), paraElem, null, 1.0f, 1.0f);
                    }
                    leaf = null;
                    continue;
                }

                //picture and diagram
                Element drawing = run.element("drawing");
                if (drawing != null)
                {
                    processPictureAndDiagram(drawing, paraElem);
                    leaf = null;
                    continue;
                }

                //autoshape for 2007
                Element pict = run.element("pict");
                if (pict != null)
                {
                    for (Iterator< ? > it = pict.elementIterator(); it.hasNext();)
                    {
                        processAutoShapeForPict((Element)it.next(), paraElem, null, 1.0f, 1.0f);
                    }
                    leaf = null;
                    continue;
                }

                //autoshape for 2010
                Element alternateContent = run.element("AlternateContent");
                if (alternateContent != null)
                {
                    processAlternateContent(alternateContent, paraElem);
                    leaf = null;
                    continue;
                }
                Element ruby = run.element("ruby");
                if (ruby != null)
                {
                    ruby = ruby.element("rubyBase");
                    if (ruby != null)
                    {
                        run = ruby.element("r");
                        if (run == null)
                        {
                            continue;
                        }
                    }
                }
                String str = "";
                Element br = run.element("br");
                Element text = run.element("t");
                if (text != null)
                {
                    str = text.getText();
                    if (br != null)
                    {
                        str = String.valueOf('\u000b') + str;
                    }
                }
                else if (br != null)
                {
                    // 分页符
                    if ("page".equals(br.attributeValue("type")))
                    {
                        pageBreak = true;
                        str = String.valueOf('\f');
                    }
                    else
                    {
                        str = String.valueOf('\u000b');
                    }
                }
                int len = str.length();
                if (len > 0)
                {
                    hasLeaf = true;
                    leaf = new LeafElement(str);
                    // 属性
                    Element rPr = run.element("rPr");
                    if (rPr != null)
                    {
                        processRunAttribute(rPr, leaf.getAttribute());
                    }

                    if(isProcessHF && hfPart !=  null && pgNumberType > 0)
                    {
                        AttrManage.instance().setFontPageNumberType(leaf.getAttribute(), pgNumberType);
                    }

                    leaf.setStartOffset(offset);
                    offset += len;
                    leaf.setEndOffset(offset);
                    paraElem.appendLeaf(leaf);
                    //
                    if (fieldCodeStr != null)
                    {
                        String linkURL = null;
                        if (fieldCodeStr.indexOf("mailto") >= 0)
                        {
                            linkURL = fieldCodeStr.substring(fieldCodeStr.indexOf("mailto"));
                            int index = linkURL.indexOf("\"");
                            if (index > 0)
                            {
                                linkURL = linkURL.substring(0, index);
                            }
                        }
                        else if (fieldCodeStr.indexOf("HYPERLINK") >= 0)
                        {
                            linkURL = fieldCodeStr.substring(fieldCodeStr.indexOf("\"") + 1);
                            int index = linkURL.lastIndexOf("/");
                            if (index > 0)
                            {
                                linkURL = linkURL.substring(0, index);
                            }
                        }
                        if (linkURL != null)
                        {
                            int hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(linkURL, Hyperlink.LINK_URL);
                            if (hyIndex >= 0)
                            {
                                AttrManage.instance().setFontColor(leaf.getAttribute(), Color.BLUE);
                                AttrManage.instance().setFontUnderline(leaf.getAttribute(), 1);
                                AttrManage.instance().setFontUnderlineColr(leaf.getAttribute(), Color.BLUE);
                                AttrManage.instance().setHyperlinkID(leaf.getAttribute(), hyIndex);
                            }
                        }
                        fieldCodeStr = "";
                        fieldTextStr = "";
                    }
                }
            }
        }
        // 如果没有 r 元素，说明只有一个回车符的段落
        if (!hasLeaf)
        {
            leaf = new LeafElement("\n");
            Element ele = para.element("pPr");
            if (ele != null)
            {
                ele = ele.element("rPr");
            }
            if (ele != null)
            {
                processRunAttribute(ele, leaf.getAttribute());
            }
            leaf.setStartOffset(offset);
            offset += 1;
            leaf.setEndOffset(offset);
            paraElem.appendLeaf(leaf);
            return hasLeaf;
        }
        if (addBreakPage && leaf != null && !pageBreak)
        {
            leaf.setText(leaf.getText(wpdoc) + "\n");
            offset++;
        }
        return hasLeaf;
    }

    private LeafElement processHyperlinkRun(Element hyperlink, ParagraphElement paraElem)
    {
        PackageRelationship hyRel = null;
        try
        {
            String id = hyperlink.attributeValue("id");
            if (id != null)
            {
                hyRel = packagePart.getRelationshipsByType(PackageRelationshipTypes.HYPERLINK_PART).getRelationshipByID(id);
            }
        }
        catch (InvalidFormatException e)
        {
            control.getSysKit().getErrorKit().writerLog(e, true);
        }
        int hyIndex = -1;
        if (hyRel != null)
        {
            hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(hyRel.getTargetURI().toString(), Hyperlink.LINK_URL);
        }
        if (hyIndex == -1)
        {
            String anchor = hyperlink.attributeValue("anchor");
            if (anchor != null)
            {
                hyIndex = control.getSysKit().getHyperlinkManage().addHyperlink(anchor, Hyperlink.LINK_BOOKMARK);
            }
        }
        List<Element> runs = hyperlink.elements("r");
        LeafElement leaf = null;
        for (Element run : runs)
        {
            Element fldCode = run.element("instrText");
            if (fldCode != null)
            {
                String text = fldCode.getText();
                if (text != null && text.contains("PAGEREF"))
                {
                    hyIndex = -1;
                }
            }
            Element ruby = run.element("ruby");
            if (ruby != null)
            {
                ruby = ruby.element("rubyBase");
                if (ruby != null)
                {
                    run = ruby.element("r");
                    if (run == null)
                    {
                        continue;
                    }
                }
            }
            Element text = run.element("t");
            if (text == null)
            {
                Element drawing = run.element("drawing");
                if (drawing != null)
                {
                    processPictureAndDiagram(drawing, paraElem);
                    leaf = null;
                }
                continue;
            }
            String str = text.getText();
            int len = str.length()  ;
            if (len > 0)
            {
                leaf = new LeafElement(str);
                IAttributeSet attr = leaf.getAttribute();
                // 属性
                Element rPr = run.element("rPr");
                if (rPr != null)
                {
                    processRunAttribute(rPr, attr);
                }

                leaf.setStartOffset(offset);
                offset += len;
                leaf.setEndOffset(offset);
                paraElem.appendLeaf(leaf);

                if (hyIndex >= 0)
                {
                    AttrManage.instance().setFontColor(attr, Color.BLUE);
                    AttrManage.instance().setFontUnderline(attr, 1);
                    AttrManage.instance().setFontUnderlineColr(attr, Color.BLUE);
                    AttrManage.instance().setHyperlinkID(attr, hyIndex);
                }
            }
        }
        return leaf;
    }

    private void addShape(AbstractShape abstractShape, ParagraphElement paragraphElement) {
        if (abstractShape != null && paragraphElement != null) {
            LeafElement leafElement = new LeafElement(String.valueOf(1));
            leafElement.setStartOffset(this.offset);
            long j = this.offset + 1;
            this.offset = j;
            leafElement.setEndOffset(j);
            paragraphElement.appendLeaf(leafElement);
            AttrManage.instance().setShapeID(leafElement.getAttribute(), this.control.getSysKit().getWPShapeManage().addShape(abstractShape));
        }
    }

    private PictureShape addPicture(Element element, Rectangle rectangle) {
        Element element2;
        String attributeValue;
        PackagePart packagePart2;
        PackagePart packagePart3;
        if (!(element == null || rectangle == null || (element2 = element.element("blipFill")) == null)) {
            PictureEffectInfo pictureEffectInfor = PictureEffectInfoFactory.getPictureEffectInfor(element2);
            Element element3 = element2.element("blip");
            if (!(element3 == null || (attributeValue = element3.attributeValue("embed")) == null)) {
                if (!this.isProcessHF || (packagePart3 = this.hfPart) == null) {
                    packagePart2 = this.zipPackage.getPart(this.packagePart.getRelationship(attributeValue).getTargetURI());
                } else {
                    packagePart2 = this.zipPackage.getPart(packagePart3.getRelationship(attributeValue).getTargetURI());
                }
                if (packagePart2 != null) {
                    PictureShape pictureShape = new PictureShape();
                    try {
                        pictureShape.setPictureIndex(this.control.getSysKit().getPictureManage().addPicture(packagePart2));
                    } catch (Exception e) {
                        this.control.getSysKit().getErrorKit().writerLog(e);
                    }
                    pictureShape.setZoomX((short) 1000);
                    pictureShape.setZoomY((short) 1000);
                    pictureShape.setPictureEffectInfor(pictureEffectInfor);
                    pictureShape.setBounds(rectangle);
                    return pictureShape;
                }
            }
        }
        return null;
    }

    private byte getRelative(String str) {
        if ("margin".equalsIgnoreCase(str)) {
            return 1;
        }
        if (Annotation.PAGE.equalsIgnoreCase(str)) {
            return 2;
        }
        if ("column".equalsIgnoreCase(str)) {
            return 0;
        }
        if ("character".equalsIgnoreCase(str)) {
            return 3;
        }
        if ("leftMargin".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("rightMargin".equalsIgnoreCase(str)) {
            return 5;
        }
        if ("insideMargin".equalsIgnoreCase(str)) {
            return 8;
        }
        if ("outsideMargin".equalsIgnoreCase(str)) {
            return 9;
        }
        if ("paragraph".equalsIgnoreCase(str)) {
            return 10;
        }
        if ("line".equalsIgnoreCase(str)) {
            return 11;
        }
        if ("topMargin".equalsIgnoreCase(str)) {
            return 6;
        }
        if ("bottomMargin".equalsIgnoreCase(str)) {
            return 7;
        }
        return 0;
    }

    private byte getAlign(String str) {
        if (HtmlTags.ALIGN_LEFT.equalsIgnoreCase(str)) {
            return 1;
        }
        if (HtmlTags.ALIGN_RIGHT.equalsIgnoreCase(str)) {
            return 3;
        }
        if (HtmlTags.ALIGN_TOP.equalsIgnoreCase(str)) {
            return 4;
        }
        if (HtmlTags.ALIGN_BOTTOM.equalsIgnoreCase(str)) {
            return 5;
        }
        if (HtmlTags.ALIGN_CENTER.equalsIgnoreCase(str)) {
            return 2;
        }
        if ("inside".equalsIgnoreCase(str)) {
            return 6;
        }
        return "outside".equalsIgnoreCase(str) ? (byte) 7 : 0;
    }

    private void processWrapAndPosition_Drawing(WPAbstractShape shape, Element anchor, Rectangle rect)
    {
        //behindDoc or not
        String behindDoc = anchor.attributeValue("behindDoc");
        if("1".equalsIgnoreCase(behindDoc))
        {
            shape.setWrap(WPAbstractShape.WRAP_BOTTOM);
        }
        shape.setWrap(getDrawingWrapType(anchor));

        //horizontal position and horizontal relative position
        Element positionHElement = null;
        Element positionVElement = null;

        //relative position for word 2010
        List<Element> alternateContents = anchor.elements("AlternateContent");
        for(Element item : alternateContents)
        {
            Element choice = item.element("Choice");
            if(choice != null)
            {
                if(choice.element("positionH") != null)
                {
                    positionHElement = choice.element("positionH");
                }

                if(choice.element("positionV") != null)
                {
                    positionVElement = choice.element("positionV");
                }
            }
        }
        // 水平
        if(positionHElement == null)
        {
            positionHElement = anchor.element("positionH");
        }
        if(positionHElement !=  null)
        {
            shape.setHorizontalRelativeTo( getRelative(positionHElement.attributeValue("relativeFrom")));
            if(positionHElement.element("align") != null)
            {
                shape.setHorizontalAlignment(getAlign(positionHElement.element("align").getText()));
            }
            else if(positionHElement.element("posOffset") != null)
            {
                rect.translate(Math.round(Integer.parseInt(positionHElement.element("posOffset").getText())
                        * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH), 0);
            }
            else if(positionHElement.element("pctPosHOffset") != null)
            {
                //horizontal relative position
                shape.setHorRelativeValue(Integer.parseInt(positionHElement.element("pctPosHOffset").getText()) / 100);

                shape.setHorPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
            }
        }

        //vertical position and vertical relative position
        if(positionVElement == null)
        {
            positionVElement = anchor.element("positionV");
        }
        if(positionVElement !=  null)
        {
            shape.setVerticalRelativeTo(getRelative(positionVElement.attributeValue("relativeFrom")));
            if(positionVElement.element("align") != null)
            {
                shape.setVerticalAlignment(getAlign(positionVElement.element("align").getText()));
            }
            else if(positionVElement.element("posOffset") != null)
            {
                rect.translate(0, Math.round(Integer.parseInt(positionVElement.element("posOffset").getText())
                        * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH));
            }
            else if(positionVElement.element("pctPosVOffset") != null)
            {
                shape.setVerRelativeValue(Integer.parseInt(positionVElement.element("pctPosVOffset").getText()) / 100);

                shape.setVerPositionType(WPAbstractShape.POSITIONTYPE_RELATIVE);
            }
        }
    }

    private void processPictureAndDiagram(Element element, ParagraphElement paragraphElement) {
        boolean z;
        Element element2;
        Element element3;
        Element element4;
        Element element5;
        String attributeValue;
        String attributeValue2;
        String attributeValue3;
        PackageRelationship relationship;
        Element element6;
        Element element7 = element;
        ParagraphElement paragraphElement2 = paragraphElement;
        Element element8 = element7.element("inline");
        if (element8 == null) {
            element2 = element7.element("anchor");
            z = false;
        } else {
            element2 = element8;
            z = true;
        }
        if (element2 != null && (element3 = element2.element("graphic")) != null && (element4 = element3.element("graphicData")) != null) {
            Element element9 = element4.element(PGPlaceholderUtil.PICTURE);
            if (element9 != null) {
                Element element10 = element9.element("spPr");
                if (element10 != null) {
                    PackagePart packagePart2 = null;
                    if (!(element10.element("blipFill") == null || (element6 = element10.element("blipFill").element("blip")) == null || element6.attributeValue("embed") == null || (this.isProcessHF && (packagePart2 = this.hfPart) != null))) {
                        packagePart2 = this.packagePart;
                    }
                    PackagePart packagePart3 = packagePart2;
                    PictureShape addPicture = addPicture(element9, ReaderKit.instance().getShapeAnchor(element10.element("xfrm"), 1.0f, 1.0f));
                    if (addPicture != null) {
                        AutoShapeDataKit.processPictureShape(this.control, this.zipPackage, packagePart3, element10, this.themeColor, addPicture);
                        WPPictureShape wPPictureShape = new WPPictureShape();
                        wPPictureShape.setPictureShape(addPicture);
                        wPPictureShape.setBounds(addPicture.getBounds());
                        if (!z) {
                            processWrapAndPosition_Drawing(wPPictureShape, element2, addPicture.getBounds());
                        } else {
                            wPPictureShape.setWrap((short) 2);
                        }
                        addShape(wPPictureShape, paragraphElement2);
                    }
                }
            } else if (element4.element("chart") != null) {
                Element element11 = element4.element("chart");
                if (element11 != null && element11.attribute(ScreenCVEdit.FIELD_ID) != null && (relationship = this.packagePart.getRelationship(element11.attributeValue((String) ScreenCVEdit.FIELD_ID))) != null) {
                    try {
                        AbstractChart read = ChartReader.instance().read(this.control, this.zipPackage, this.zipPackage.getPart(relationship.getTargetURI()), this.themeColor, (byte) 0);
                        if (read != null) {
                            Rectangle rectangle = new Rectangle();
                            Element element12 = element2.element("simplePos");
                            if (element12 != null) {
                                String attributeValue4 = element12.attributeValue("x");
                                if (attributeValue4 != null) {
                                    rectangle.x = (int) ((((float) Integer.parseInt(attributeValue4)) * 96.0f) / 914400.0f);
                                }
                                String attributeValue5 = element12.attributeValue("y");
                                if (attributeValue5 != null) {
                                    rectangle.y = (int) ((((float) Integer.parseInt(attributeValue5)) * 96.0f) / 914400.0f);
                                }
                            }
                            Element element13 = element2.element("extent");
                            if (element13 != null) {
                                String attributeValue6 = element13.attributeValue("cx");
                                if (attributeValue6 != null) {
                                    rectangle.width = (int) ((((float) Integer.parseInt(attributeValue6)) * 96.0f) / 914400.0f);
                                }
                                String attributeValue7 = element13.attributeValue("cy");
                                if (attributeValue7 != null) {
                                    rectangle.height = (int) ((((float) Integer.parseInt(attributeValue7)) * 96.0f) / 914400.0f);
                                }
                            }
                            WPChartShape wPChartShape = new WPChartShape();
                            wPChartShape.setAChart(read);
                            wPChartShape.setBounds(rectangle);
                            if (!z) {
                                processWrapAndPosition_Drawing(wPChartShape, element2, rectangle);
                            } else {
                                wPChartShape.setWrap((short) 2);
                            }
                            addShape(wPChartShape, paragraphElement2);
                        }
                    } catch (Exception unused) {
                    }
                }
            } else if (element4.element("relIds") != null && (element5 = element4.element("relIds")) != null && (attributeValue = element5.attributeValue("dm")) != null) {
                try {
                    PackageRelationship relationshipByID = this.packagePart.getRelationshipsByType(PackageRelationshipTypes.DIAGRAM_DATA).getRelationshipByID(attributeValue);
                    if (relationshipByID != null) {
                        Rectangle rectangle2 = new Rectangle();
                        Element element14 = element2.element("extent");
                        if (element14 != null) {
                            if (!(element14.attribute("cx") == null || (attributeValue3 = element14.attributeValue("cx")) == null || attributeValue3.length() <= 0)) {
                                rectangle2.width = (int) ((((float) Integer.parseInt(attributeValue3)) * 96.0f) / 914400.0f);
                            }
                            if (!(element14.attribute("cy") == null || (attributeValue2 = element14.attributeValue("cy")) == null || attributeValue2.length() <= 0)) {
                                rectangle2.height = (int) ((((float) Integer.parseInt(attributeValue2)) * 96.0f) / 914400.0f);
                            }
                        }
                        PackagePart part = this.zipPackage.getPart(relationshipByID.getTargetURI());
                        processSmart(this.control, this.zipPackage, part, paragraphElement, element2, rectangle2, z);
                    }
                } catch (Exception unused2) {
                }
            }
        }
    }

    private void processSmart(IControl iControl, ZipPackage zipPackage2, PackagePart packagePart2, ParagraphElement paragraphElement, Element element, Rectangle rectangle, boolean z) throws Exception {
        InputStream inputStream;
        Element rootElement;
        Element element2;
        short s;
        Element element3;
        Element element4;
        String attributeValue;
        IControl iControl2 = iControl;
        ZipPackage zipPackage3 = zipPackage2;
        PackagePart packagePart3 = packagePart2;
        Element element5 = element;
        Rectangle rectangle2 = rectangle;
        if (packagePart3 != null && (inputStream = packagePart2.getInputStream()) != null) {
            SAXReader sAXReader = new SAXReader();
            Document read = sAXReader.read(inputStream);
            inputStream.close();
            Element rootElement2 = read.getRootElement();
            BackgroundAndFill processBackground = AutoShapeDataKit.processBackground(iControl2, zipPackage3, packagePart3, rootElement2.element("bg"), this.themeColor);
            Line createLine = LineKit.createLine(iControl2, zipPackage3, packagePart3, rootElement2.element("whole").element("ln"), this.themeColor);
            PackagePart packagePart4 = null;
            Element element6 = rootElement2.element("extLst");
            if (!(element6 == null || (element3 = element6.element("ext")) == null || (element4 = element3.element("dataModelExt")) == null || (attributeValue = element4.attributeValue("relId")) == null)) {
                packagePart4 = zipPackage3.getPart(this.packagePart.getRelationship(attributeValue).getTargetURI());
            }
            PackagePart packagePart5 = packagePart4;
            if (packagePart5 != null) {
                InputStream inputStream2 = packagePart5.getInputStream();
                Document read2 = sAXReader.read(inputStream2);
                inputStream2.close();
                if (read2 != null && (rootElement = read2.getRootElement()) != null && (element2 = rootElement.element("spTree")) != null) {
                    WPGroupShape wPGroupShape = new WPGroupShape();
                    WPAutoShape wPAutoShape = new WPAutoShape();
                    wPAutoShape.addGroupShape(wPGroupShape);
                    if (!z) {
                        processWrapAndPosition_Drawing(wPAutoShape, element5, rectangle2);
                        s = getDrawingWrapType(element5);
                    } else {
                        s = 2;
                    }
                    wPGroupShape.setBounds(rectangle2);
                    wPAutoShape.setBackgroundAndFill(processBackground);
                    wPAutoShape.setLine(createLine);
                    wPAutoShape.setShapeType(1);
                    if (s != 2) {
                        wPGroupShape.setWrapType(s);
                        wPAutoShape.setWrap(s);
                    } else {
                        wPGroupShape.setWrapType((short) 2);
                        wPAutoShape.setWrap((short) 2);
                    }
                    wPAutoShape.setBounds(rectangle2);
                    Iterator elementIterator = element2.elementIterator();
                    while (elementIterator.hasNext()) {
                        processAutoShape2010(packagePart5, paragraphElement, (Element) elementIterator.next(), wPGroupShape, 1.0f, 1.0f, 0, 0, false);
                    }
                    addShape(wPAutoShape, paragraphElement);
                }
            }
        }
    }

    private void processAutoShapeForPict(Element sp, ParagraphElement paraElem, WPGroupShape parent, float zoomX, float zoomY)
    {
        String name = sp.getName();
        if ("group".equalsIgnoreCase(name))
        {
            String val = sp.attributeValue("id");
            if (val != null && (val.startsWith("Genko")
                    || val.startsWith("Header")
                    || val.startsWith("Footer")))
            {
                return;
            }

            Rectangle rect = null;
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

            rect = processAutoShapeStyle(sp, shape, parent, zoomX, zoomY);
            if (rect != null)
            {
                float x = 0;
                float y = 0;
                float w = 0;
                float h = 0;
                String temp;
                String[] values;
                float[] zoom = {1.0f, 1.0f};
                Rectangle childRect = new Rectangle();
                if (sp.attribute("coordorigin") != null)
                {
                    temp = sp.attributeValue("coordorigin");
                    if (temp != null && temp.length() > 0)
                    {
                        values = temp.split(",");
                        if (values != null)
                        {
                            if(values.length == 2)
                            {
                                if(values[0].length() > 0)
                                {
                                    x = Float.parseFloat(values[0]);
                                }
                                y = Float.parseFloat(values[1]);
                            }
                            else if(values.length == 1)
                            {
                                x = Float.parseFloat(values[0]);
                            }
                        }
                    }
                }

                if (sp.attribute("coordsize") != null)
                {
                    temp = sp.attributeValue("coordsize");
                    if (temp != null && temp.length() > 0)
                    {
                        values = temp.split(",");
                        if (values != null)
                        {
                            if(values.length == 2)
                            {
                                if(values[0].length() > 0)
                                {
                                    w = Float.parseFloat(values[0]);
                                }
                                h = Float.parseFloat(values[1]);
                            }
                            else if(values.length == 1)
                            {
                                w = Float.parseFloat(values[0]);
                            }

                        }
                    }
                }
                if (w != 0 && h != 0)
                {
                    zoom[0] = rect.width * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomX / w;
                    zoom[1] = rect.height * MainConstant.EMU_PER_INCH / MainConstant.PIXEL_DPI / zoomY / h;
                }
                rect = processGrpSpRect(parent, rect);

                childRect.x = (int)(x * zoom[0] * zoomX * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.y = (int)(y * zoom[1] * zoomY * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.width = (int)(w * zoom[0] * zoomX * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                childRect.height = (int)(h * zoom[1] * zoomY * MainConstant.PIXEL_DPI / MainConstant.EMU_PER_INCH);
                if (parent == null)
                {
                    childRect.x += rect.x;
                    childRect.y += rect.y;
                }
                groupShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);

                groupShape.setBounds(rect);
                groupShape.setParent(parent);
                groupShape.setRotation(shape.getRotation());
                groupShape.setFlipHorizontal(shape.getFlipHorizontal());
                groupShape.setFlipVertical(shape.getFlipVertical());

                if(parent == null)
                {
                    short wrapType = getShapeWrapType(sp);
                    groupShape.setWrapType(wrapType);
                    ((WPAutoShape)shape).setWrap(wrapType);
                }
                else
                {
                    groupShape.setWrapType(parent.getWrapType());
                }

                for (Iterator< ? > it = sp.elementIterator(); it.hasNext();)
                {
                    processAutoShapeForPict((Element)it.next(), paraElem, groupShape, zoom[0] * zoomX, zoom[1] * zoomY);
                }

                IShape[] shapes = groupShape.getShapes();
                for(IShape sh : shapes)
                {
                    if(sh instanceof WPAbstractShape && shape instanceof WPAbstractShape)
                    {
                        ((WPAbstractShape)sh).setWrap((short)((WPAbstractShape)shape).getWrap());
                        ((WPAbstractShape)sh).setHorPositionType(((WPAbstractShape)shape).getHorPositionType());
                        ((WPAbstractShape)sh).setHorizontalRelativeTo(((WPAbstractShape)shape).getHorizontalRelativeTo());
                        ((WPAbstractShape)sh).setHorRelativeValue(((WPAbstractShape)shape).getHorRelativeValue());
                        ((WPAbstractShape)sh).setHorizontalAlignment(((WPAbstractShape)shape).getHorizontalAlignment());

                        ((WPAbstractShape)sh).setVerPositionType(((WPAbstractShape)shape).getVerPositionType());
                        ((WPAbstractShape)sh).setVerticalRelativeTo(((WPAbstractShape)shape).getVerticalRelativeTo());
                        ((WPAbstractShape)sh).setVerRelativeValue(((WPAbstractShape)shape).getVerRelativeValue());
                        ((WPAbstractShape)sh).setVerticalAlignment(((WPAbstractShape)shape).getVerticalAlignment());
                    }
                }
                if (parent == null)
                {
                    addShape(shape, paraElem);
                }
                else
                {
                    parent.appendShapes(shape);
                }
            }
            else
            {
                shape.dispose();
                shape = null;
            }
        }
        else
        {
            //ignore genko shapes
            String val = sp.attributeValue("id");
            if (val != null && (val.startsWith("Genko")
                    || val.startsWith("Header")
                    || val.startsWith("Footer")))
            {
                return;
            }

            if ("shape".equalsIgnoreCase(name))
            {
                if (sp.element("imagedata") != null)
                {
                    processPicture(sp, paraElem);
                }
                else
                {
                    WPAutoShape shape = processAutoShape(sp, paraElem, parent, zoomX, zoomY, hasTextbox2007(sp));
                    if(shape != null)
                    {
                        //
                        processTextbox2007(packagePart, shape, sp);
                    }
                }
            }
            else if ("line".equalsIgnoreCase(name) || "polyline".equalsIgnoreCase(name) || "curve".equalsIgnoreCase(name)
                    ||"rect".equalsIgnoreCase(name)||"roundrect".equalsIgnoreCase(name) || "oval".equalsIgnoreCase(name))
            {
                WPAutoShape shape = processAutoShape(sp, paraElem, parent, zoomX, zoomY, hasTextbox2007(sp));
                if(shape != null)
                {
                    //
                    processTextbox2007(packagePart, shape, sp);
                }
            }
        }
    }

    private short getShapeWrapType(Element element) {
        Element element2 = element.element("wrap");
        if (element2 != null) {
            String attributeValue = element2.attributeValue("type");
            if ("none".equalsIgnoreCase(attributeValue)) {
                return 2;
            }
            if ("square".equalsIgnoreCase(attributeValue)) {
                return 1;
            }
            if ("tight".equalsIgnoreCase(attributeValue)) {
                return 0;
            }
            if ("topAndBottom".equalsIgnoreCase(attributeValue)) {
                return 5;
            }
            if ("through".equalsIgnoreCase(attributeValue)) {
                return 4;
            }
        }
        String attributeValue2 = element.attributeValue(HtmlTags.STYLE);
        if (attributeValue2 == null) {
            return -1;
        }
        String[] split = attributeValue2.split(";");
        for (int length = split.length - 1; length >= 0; length--) {
            if (split[length].contains("z-index:")) {
                return Integer.parseInt(split[length].replace("z-index:", "")) > 0 ? (short) 3 : 6;
            }
        }
        return -1;
    }

    private short getDrawingWrapType(Element element) {
        if (element == null) {
            return -1;
        }
        if (element.element("wrapNone") != null) {
            return "1".equalsIgnoreCase(element.attributeValue("behindDoc")) ? (short) 6 : 3;
        }
        if (element.element("wrapSquare") != null) {
            return 1;
        }
        if (element.element("wrapTight") != null) {
            return 0;
        }
        if (element.element("wrapThrough") != null) {
            return 4;
        }
        return element.element("wrapTopAndBottom") != null ? (short) 5 : 2;
    }

    /* access modifiers changed from: private */
    public void processPicture(Element element, ParagraphElement paragraphElement) {
        String attributeValue;
        PackagePart packagePart2;
        AbstractShape abstractShape;
        PackagePart packagePart3;
        if (element != null) {
            Element element2 = element.element("imagedata");
            if (element2 == null && (element2 = element.element("rect")) != null) {
                Element element3 = element2;
                element2 = element2.element("fill");
                element = element3;
            }
            if (element2 != null && (attributeValue = element2.attributeValue((String) ScreenCVEdit.FIELD_ID)) != null) {
                if (!this.isProcessHF || (packagePart3 = this.hfPart) == null) {
                    packagePart2 = this.zipPackage.getPart(this.packagePart.getRelationship(attributeValue).getTargetURI());
                } else {
                    packagePart2 = this.zipPackage.getPart(packagePart3.getRelationship(attributeValue).getTargetURI());
                }
                String attributeValue2 = element.attributeValue(HtmlTags.STYLE);
                if (packagePart2 != null && attributeValue2 != null) {
                    String attributeValue3 = element.attributeValue(ScreenCVEdit.FIELD_ID);
                    if (attributeValue3 != null && attributeValue3.indexOf("PictureWatermark") > 0) {
                        this.isProcessWatermark = true;
                    }
                    try {
                        int addPicture = this.control.getSysKit().getPictureManage().addPicture(packagePart2);
                        short shapeWrapType = getShapeWrapType(element);
                        if (this.isProcessWatermark) {
                            abstractShape = new WatermarkShape();
                            String attributeValue4 = element2.attributeValue("blacklevel");
                            if (attributeValue4 != null) {
                                ((WatermarkShape) abstractShape).setBlacklevel(Float.parseFloat(attributeValue4) / 100000.0f);
                            }
                            String attributeValue5 = element2.attributeValue("gain");
                            if (attributeValue5 != null) {
                                ((WatermarkShape) abstractShape).setGain(Float.parseFloat(attributeValue5) / 100000.0f);
                            }
                            ((WatermarkShape) abstractShape).setWatermarkType((byte) 1);
                            ((WatermarkShape) abstractShape).setPictureIndex(addPicture);
                            ((WatermarkShape) abstractShape).setWrap(shapeWrapType);
                        } else {
                            PictureEffectInfo pictureEffectInfor_ImageData = PictureEffectInfoFactory.getPictureEffectInfor_ImageData(element2);
                            PictureShape pictureShape = new PictureShape();
                            pictureShape.setPictureIndex(addPicture);
                            pictureShape.setZoomX((short) 1000);
                            pictureShape.setZoomY((short) 1000);
                            pictureShape.setPictureEffectInfor(pictureEffectInfor_ImageData);
                            abstractShape = new WPPictureShape();
                            ((WPPictureShape) abstractShape).setPictureShape(pictureShape);
                            ((WPPictureShape) abstractShape).setWrap(shapeWrapType);
                        }
                        AbstractShape abstractShape2 = abstractShape;
                        Rectangle processAutoShapeStyle = processAutoShapeStyle(element, abstractShape2, (GroupShape) null, 1000.0f, 1000.0f);
                        if (!this.isProcessWatermark) {
                            PictureShape pictureShape2 = ((WPPictureShape) abstractShape2).getPictureShape();
                            pictureShape2.setBounds(processAutoShapeStyle);
                            pictureShape2.setBackgroundAndFill(processBackgroundAndFill(element));
                            pictureShape2.setLine(processLine(element));
                        }
                        addShape(abstractShape2, paragraphElement);
                        this.isProcessWatermark = false;
                    } catch (Exception e) {
                        this.control.getSysKit().getErrorKit().writerLog(e);
                    }
                }
            }
        }
    }

    private int getColor(String str, boolean z) {
        if (str == null) {
            return z ? -1 : -16777216;
        }
        int indexOf = str.indexOf(" ");
        if (indexOf > 0) {
            str = str.substring(0, indexOf);
        }
        if (str.charAt(0) == '#') {
            if (str.length() > 6) {
                return Color.parseColor(str);
            }
            if (str.length() == 4) {
                StringBuilder sb = new StringBuilder();
                sb.append('#');
                for (int i = 1; i < 4; i++) {
                    sb.append(str.charAt(i));
                    sb.append(str.charAt(i));
                }
                return Color.parseColor(sb.toString());
            }
        }
        if (!str.contains("black") && !str.contains("darken")) {
            if (str.contains("green")) {
                return -16744448;
            }
            if (str.contains("silver")) {
                return -4144960;
            }
            if (str.contains("lime")) {
                return -16711936;
            }
            if (str.contains("gray")) {
                return -8355712;
            }
            if (str.contains("olive")) {
                return -8355840;
            }
            if (str.contains("white")) {
                return -1;
            }
            if (str.contains("yellow")) {
                return InputDeviceCompat.SOURCE_ANY;
            }
            if (str.contains("maroon")) {
                return -8388608;
            }
            if (str.contains("navy")) {
                return -16777088;
            }
            if (str.contains("red")) {
                return SupportMenu.CATEGORY_MASK;
            }
            if (str.contains("blue")) {
                return -16776961;
            }
            if (str.contains("purple")) {
                return -8388480;
            }
            if (str.contains("teal")) {
                return -16744320;
            }
            if (str.contains("fuchsia")) {
                return -65281;
            }
            if (str.contains("aqua")) {
                return -16711681;
            }
            if (z) {
                return -1;
            }
        }
        return -16777216;
    }

    public void processRotation(AutoShape autoShape) {
        float rotation = autoShape.getRotation();
        if (autoShape.getFlipHorizontal()) {
            rotation = -rotation;
        }
        if (autoShape.getFlipVertical()) {
            rotation = -rotation;
        }
        int shapeType = autoShape.getShapeType();
        if ((shapeType == 32 || shapeType == 34 || shapeType == 38) && ((rotation == 45.0f || rotation == 135.0f || rotation == 225.0f) && !autoShape.getFlipHorizontal() && !autoShape.getFlipVertical())) {
            rotation -= 90.0f;
        }
        autoShape.setRotation(rotation);
    }

    private Rectangle processGrpSpRect(GroupShape groupShape, Rectangle rectangle) {
        if (groupShape != null) {
            rectangle.x += groupShape.getOffX();
            rectangle.y += groupShape.getOffY();
        }
        return rectangle;
    }

    private float processPolygonZoom(Element element, AbstractShape abstractShape, GroupShape groupShape, float f, float f2) {
        String attributeValue;
        String attributeValue2;
        float parseFloat;
        float parseFloat2;
        float parseFloat3 = 0;
        float parseFloat4 = 0;
        float parseFloat5;
        Element element2 = element;
        AbstractShape abstractShape2 = abstractShape;
        if (element2 == null || abstractShape2 == null || element2.attribute(HtmlTags.STYLE) == null || element2.attribute(HtmlTags.STYLE) == null || (attributeValue = element2.attributeValue((String) HtmlTags.STYLE)) == null) {
            return 1.0f;
        }
        String[] split = attributeValue.split(";");
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        for (String split2 : split) {
            String[] split3 = split2.split(":");
            if (!(split3 == null || split3[0] == null || split3[1] == null)) {
                if (HtmlTags.ALIGN_LEFT.equalsIgnoreCase(split3[0])) {
                    int indexOf = split3[1].indexOf("pt");
                    if (indexOf > 0) {
                        parseFloat4 = Float.parseFloat(split3[1].substring(0, indexOf));
                    } else {
                        int indexOf2 = split3[1].indexOf("in");
                        if (indexOf2 > 0) {
                            parseFloat5 = Float.parseFloat(split3[1].substring(0, indexOf2));
                            parseFloat4 = parseFloat5 * 72.0f;
                        } else {
                            parseFloat4 = ((Float.parseFloat(split3[1]) * f) * 72.0f) / 914400.0f;
                        }
                    }
                } else {
                    if (HtmlTags.ALIGN_TOP.equalsIgnoreCase(split3[0])) {
                        int indexOf3 = split3[1].indexOf("pt");
                        if (indexOf3 > 0) {
                            parseFloat3 = Float.parseFloat(split3[1].substring(0, indexOf3));
                        } else {
                            int indexOf4 = split3[1].indexOf("in");
                            if (indexOf4 > 0) {
                                parseFloat2 = Float.parseFloat(split3[1].substring(0, indexOf4));
                                parseFloat3 = parseFloat2 * 72.0f;
                            } else {
                                parseFloat = Float.parseFloat(split3[1]) * f2;
                                parseFloat3 = (parseFloat * 72.0f) / 914400.0f;
                            }
                        }
                    } else if ("margin-left".equalsIgnoreCase(split3[0])) {
                        int indexOf5 = split3[1].indexOf("pt");
                        if (indexOf5 > 0) {
                            parseFloat4 = Float.parseFloat(split3[1].substring(0, indexOf5));
                        } else {
                            int indexOf6 = split3[1].indexOf("in");
                            if (indexOf6 > 0) {
                                parseFloat5 = Float.parseFloat(split3[1].substring(0, indexOf6));
                                parseFloat4 = parseFloat5 * 72.0f;
                            } else {
                                parseFloat4 = (Float.parseFloat(split3[1]) * 72.0f) / 914400.0f;
                            }
                        }
                    } else if ("margin-top".equalsIgnoreCase(split3[0])) {
                        int indexOf7 = split3[1].indexOf("pt");
                        if (indexOf7 > 0) {
                            parseFloat3 = Float.parseFloat(split3[1].substring(0, indexOf7));
                        } else {
                            int indexOf8 = split3[1].indexOf("in");
                            if (indexOf8 > 0) {
                                parseFloat2 = Float.parseFloat(split3[1].substring(0, indexOf8));
                                parseFloat3 = parseFloat2 * 72.0f;
                            } else {
                                parseFloat = Float.parseFloat(split3[1]);
                                parseFloat3 = (parseFloat * 72.0f) / 914400.0f;
                            }
                        }
                    } else if (HtmlTags.WIDTH.equalsIgnoreCase(split3[0])) {
                        int indexOf9 = split3[1].indexOf("pt");
                        if (indexOf9 > 0) {
                            f5 = Float.parseFloat(split3[1].substring(0, indexOf9));
                        } else {
                            int indexOf10 = split3[1].indexOf("in");
                            if (indexOf10 > 0) {
                                f5 = Float.parseFloat(split3[1].substring(0, indexOf10)) * 72.0f;
                            } else {
                                f5 = ((Float.parseFloat(split3[1]) * f) * 72.0f) / 914400.0f;
                            }
                        }
                    } else if (HtmlTags.HEIGHT.equalsIgnoreCase(split3[0])) {
                        int indexOf11 = split3[1].indexOf("pt");
                        if (indexOf11 > 0) {
                            f6 = Float.parseFloat(split3[1].substring(0, indexOf11));
                        } else {
                            int indexOf12 = split3[1].indexOf("in");
                            if (indexOf12 > 0) {
                                f6 = Float.parseFloat(split3[1].substring(0, indexOf12)) * 72.0f;
                            } else {
                                f6 = ((Float.parseFloat(split3[1]) * f2) * 72.0f) / 914400.0f;
                            }
                        }
                    }
                    f4 += parseFloat3;
                }
                f3 += parseFloat4;
            }
        }
        Rectangle rectangle = new Rectangle();
        rectangle.x = (int) (f3 * 1.3333334f);
        rectangle.y = (int) (f4 * 1.3333334f);
        rectangle.width = (int) (f5 * 1.3333334f);
        rectangle.height = (int) (f6 * 1.3333334f);
        if (abstractShape.getType() != 7 && ((WPAutoShape) abstractShape2).getGroupShape() == null) {
            processGrpSpRect(groupShape, rectangle);
        }
        if (!(abstractShape2 instanceof WPAutoShape) || ((WPAutoShape) abstractShape2).getShapeType() != 233 || (attributeValue2 = element2.attributeValue("coordsize")) == null || attributeValue2.length() <= 0) {
            return 1.0f;
        }
        String[] split4 = attributeValue2.split(",");
        return Math.min(((float) Integer.parseInt(split4[0])) / ((float) rectangle.width), ((float) Integer.parseInt(split4[1])) / ((float) rectangle.height));
    }

    private float getValueInPt(String str, float f) {
        if (str.contains("pt")) {
            return Float.parseFloat(str.substring(0, str.indexOf("pt")));
        }
        if (str.contains("in")) {
            return Float.parseFloat(str.substring(0, str.indexOf("in"))) * 72.0f;
        }
        if (str.contains("mm")) {
            return Float.parseFloat(str.substring(0, str.indexOf("mm"))) * 2.835f;
        }
        return ((Float.parseFloat(str) * f) * 72.0f) / 914400.0f;
    }

    private Rectangle processAutoShapeStyle(Element element, AbstractShape abstractShape, GroupShape groupShape, float f, float f2) {
        String attributeValue;
        String attributeValue2;
        String[] strArr = new String[0];
        float valueInPt = 0;
        float valueInPt2 = 0;
        Element element2 = element;
        AbstractShape abstractShape2 = abstractShape;
        GroupShape groupShape2 = groupShape;
        float f3 = f;
        float f4 = f2;
        if (element2 == null || abstractShape2 == null || element2.attribute(HtmlTags.STYLE) == null || element2.attribute(HtmlTags.STYLE) == null || (attributeValue = element2.attributeValue((String) HtmlTags.STYLE)) == null) {
            return null;
        }
        String[] split = attributeValue.split(";");
        float f5 = 0.0f;
        char c = 0;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        int i = 0;
        while (i < split.length) {
            String[] split2 = split[i].split(":");
            if (!(split2 == null || split2[c] == null || split2[1] == null || "position".equalsIgnoreCase(split2[c]))) {
                if (HtmlTags.ALIGN_LEFT.equalsIgnoreCase(split2[c])) {
                    valueInPt2 = getValueInPt(split2[1], f3);
                } else {
                    if (HtmlTags.ALIGN_TOP.equalsIgnoreCase(split2[c])) {
                        valueInPt = getValueInPt(split2[1], f4);
                    } else if (!HtmlTags.TEXTALIGN.equalsIgnoreCase(split2[0])) {
                        if ("margin-left".equalsIgnoreCase(split2[0])) {
                            valueInPt2 = getValueInPt(split2[1], 1.0f);
                        } else if ("margin-top".equalsIgnoreCase(split2[0])) {
                            valueInPt = getValueInPt(split2[1], 1.0f);
                        } else {
                            if (HtmlTags.WIDTH.equalsIgnoreCase(split2[0])) {
                                strArr = split;
                                f7 = getValueInPt(split2[1], f3);
                            } else if (HtmlTags.HEIGHT.equalsIgnoreCase(split2[0])) {
                                strArr = split;
                                f8 = getValueInPt(split2[1], f4);
                            } else if ("mso-width-percent".equalsIgnoreCase(split2[0])) {
                                if (!this.relativeType.contains(abstractShape2)) {
                                    int[] iArr = new int[4];
                                    iArr[0] = (int) Float.parseFloat(split2[1]);
                                    this.relativeType.add(abstractShape2);
                                    this.relativeValue.put(abstractShape2, iArr);
                                } else {
                                    this.relativeValue.get(abstractShape2)[0] = (int) Float.parseFloat(split2[1]);
                                }
                            } else if ("mso-height-percent".equalsIgnoreCase(split2[0])) {
                                if (!this.relativeType.contains(abstractShape2)) {
                                    int[] iArr2 = new int[4];
                                    iArr2[2] = (int) Float.parseFloat(split2[1]);
                                    this.relativeType.add(abstractShape2);
                                    this.relativeValue.put(abstractShape2, iArr2);
                                } else {
                                    this.relativeValue.get(abstractShape2)[2] = (int) Float.parseFloat(split2[1]);
                                }
                            } else if ("flip".equalsIgnoreCase(split2[0])) {
                                if ("x".equalsIgnoreCase(split2[1])) {
                                    abstractShape2.setFlipHorizontal(true);
                                } else if ("y".equalsIgnoreCase(split2[1])) {
                                    abstractShape2.setFlipVertical(true);
                                }
                            } else if ("rotation".equalsIgnoreCase(split2[0])) {
                                if (split2[1].indexOf("fd") > 0) {
                                    split2[1] = split2[1].substring(0, split2[1].length() - 2);
                                    abstractShape2.setRotation((float) (Integer.parseInt(split2[1]) / 60000));
                                } else {
                                    abstractShape2.setRotation((float) Integer.parseInt(split2[1]));
                                }
                            } else if (!"mso-width-relative".equalsIgnoreCase(split2[0]) && !"mso-height-relative".equalsIgnoreCase(split2[0])) {
                                if (groupShape2 == null && abstractShape.getType() != 7 && "mso-position-horizontal".equalsIgnoreCase(split2[0])) {
                                    ((WPAutoShape) abstractShape2).setHorizontalAlignment(getAlign(split2[1]));
                                } else if (groupShape2 != null || abstractShape.getType() == 7 || !"mso-left-percent".equalsIgnoreCase(split2[0])) {
                                    strArr = split;
                                    if (groupShape2 != null || abstractShape.getType() == 7 || !"mso-position-horizontal-relative".equalsIgnoreCase(split2[0])) {
                                        if (groupShape2 == null && abstractShape.getType() != 7 && "mso-position-vertical".equalsIgnoreCase(split2[0])) {
                                            ((WPAutoShape) abstractShape2).setVerticalAlignment(getAlign(split2[1]));
                                        } else if (groupShape2 == null && abstractShape.getType() != 7 && "mso-top-percent".equalsIgnoreCase(split2[0])) {
                                            WPAutoShape wPAutoShape = (WPAutoShape) abstractShape2;
                                            wPAutoShape.setVerRelativeValue(Integer.parseInt(split2[1]));
                                            wPAutoShape.setVerPositionType((byte) 1);
                                        } else if (groupShape2 == null && abstractShape.getType() != 7 && "mso-position-vertical-relative".equalsIgnoreCase(split2[0])) {
                                            if ("line".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 11);
                                            } else if ("text".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 10);
                                            } else if ("margin".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 1);
                                            } else if (Annotation.PAGE.equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 2);
                                            } else if ("top-margin-area".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 6);
                                            } else if ("bottom-margin-area".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 7);
                                            } else if ("inner-margin-area".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 8);
                                            } else if ("outer-margin-area".equalsIgnoreCase(split2[1])) {
                                                ((WPAutoShape) abstractShape2).setVerticalRelativeTo((byte) 9);
                                            }
                                        }
                                    } else if ("margin".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 1);
                                    } else if (Annotation.PAGE.equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 2);
                                    } else if ("left-margin-area".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 4);
                                    } else if ("right-margin-area".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 5);
                                    } else if ("inner-margin-area".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 8);
                                    } else if ("outer-margin-area".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 9);
                                    } else if ("text".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 0);
                                    } else if ("char".equalsIgnoreCase(split2[1])) {
                                        ((WPAutoShape) abstractShape2).setHorizontalRelativeTo((byte) 3);
                                    }
                                } else {
                                    WPAutoShape wPAutoShape2 = (WPAutoShape) abstractShape2;
                                    wPAutoShape2.setHorRelativeValue(Integer.parseInt(split2[1]));
                                    wPAutoShape2.setHorPositionType((byte) 1);
                                }
                            }
                            i++;
                            Element element3 = element;
                            f3 = f;
                            f4 = f2;
                            split = strArr;
                            c = 0;
                        }
                    }
                    f6 += valueInPt;
                }
                f5 += valueInPt2;
            }
            strArr = split;
            i++;
            Element element32 = element;
            f3 = f;
            f4 = f2;
            split = strArr;
            c = 0;
        }
        Rectangle rectangle = new Rectangle();
        rectangle.x = (int) (f5 * 1.3333334f);
        rectangle.y = (int) (f6 * 1.3333334f);
        rectangle.width = (int) (f7 * 1.3333334f);
        rectangle.height = (int) (f8 * 1.3333334f);
        if (abstractShape.getType() != 7 && ((WPAutoShape) abstractShape2).getGroupShape() == null) {
            processGrpSpRect(groupShape2, rectangle);
        }
        if (abstractShape2 instanceof WPAutoShape) {
            WPAutoShape wPAutoShape3 = (WPAutoShape) abstractShape2;
            if (wPAutoShape3.getShapeType() == 233 && (attributeValue2 = element.attributeValue("coordsize")) != null && attributeValue2.length() > 0) {
                String[] split3 = attributeValue2.split(",");
                Matrix matrix = new Matrix();
                matrix.postScale(((float) rectangle.width) / ((float) Integer.parseInt(split3[0])), ((float) rectangle.height) / ((float) Integer.parseInt(split3[1])));
                for (ExtendPath path : wPAutoShape3.getPaths()) {
                    path.getPath().transform(matrix);
                }
            }
        }
        abstractShape2.setBounds(rectangle);
        return rectangle;
    }

    private byte getFillType(String str) {
        if ("gradient".equalsIgnoreCase(str)) {
            return 7;
        }
        if ("gradientRadial".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("pattern".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("tile".equalsIgnoreCase(str)) {
            return 2;
        }
        return "frame".equalsIgnoreCase(str) ? (byte) 3 : 0;
    }

    private int getRadialGradientPositionType(Element fill)
    {
        int positionType = RadialGradientShader.Center_TL;
        String focusposition = fill.attributeValue("focusposition");
        if(focusposition != null && focusposition.length() > 0)
        {
            String[] xy =  focusposition.split(",");
            if(xy != null)
            {
                if(xy.length == 2)
                {
                    if(".5".equalsIgnoreCase(xy[0]) && ".5".equalsIgnoreCase(xy[1]))
                    {
                        //radial from center
                        positionType = RadialGradientShader.Center_Center;
                    }
                    else if("1".equalsIgnoreCase(xy[0]) && "1".equalsIgnoreCase(xy[1]))
                    {
                        positionType = RadialGradientShader.Center_BR;
                    }
                    else if("".equalsIgnoreCase(xy[0]) && "1".equalsIgnoreCase(xy[1]))
                    {
                        positionType = RadialGradientShader.Center_BL;
                    }
                    else if("1".equalsIgnoreCase(xy[0]) && "".equalsIgnoreCase(xy[1]))
                    {
                        positionType = RadialGradientShader.Center_TR;
                    }
                }
                else if(xy.length == 1 && "1".equalsIgnoreCase(xy[0]))
                {
                    positionType = RadialGradientShader.Center_TR;
                }
            }
        }

        return positionType;
    }

    private int getAutoShapeType(Element element) {
        int i;
        String name = element.getName();
        if (name.equals("rect")) {
            i = 1;
        } else if (name.equals("roundrect")) {
            i = 2;
        } else if (name.equals("oval")) {
            i = 3;
        } else if (name.equals("curve")) {
            i = ShapeTypes.Curve;
        } else if (name.equals("polyline")) {
            i = ShapeTypes.DirectPolygon;
        } else {
            i = name.equals("line") ? 247 : 0;
        }
        if (element.attribute("type") != null) {
            String attributeValue = element.attributeValue("type");
            if (attributeValue == null || attributeValue.length() <= 9) {
                return i;
            }
            return Integer.parseInt(attributeValue.substring(9));
        } else if (element.attribute("path") != null) {
            return 233;
        } else {
            return i;
        }
    }

    private float getValue(String str) {
        float f;
        int indexOf = str.indexOf("pt");
        if (indexOf > 0) {
            f = Float.parseFloat(str.substring(0, indexOf));
        } else {
            int indexOf2 = str.indexOf("in");
            if (indexOf2 > 0) {
                f = Float.parseFloat(str.substring(0, indexOf2)) * 72.0f;
            } else {
                f = (Float.parseFloat(str) * 72.0f) / 914400.0f;
            }
        }
        return f * 1.3333334f;
    }

    private PointF[] processPoints(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null) {
            String[] split = str.split(",");
            int length = split.length;
            for (int i = 0; i < length - 1; i += 2) {
                arrayList.add(new PointF(getValue(split[i]), getValue(split[i + 1])));
            }
        }
        return (PointF[]) arrayList.toArray(new PointF[arrayList.size()]);
    }

    private void processArrow(WPAutoShape wPAutoShape, Element element) {
        Element element2 = element.element("stroke");
        if (element2 != null) {
            byte arrowType = getArrowType(element2.attributeValue("startarrow"));
            if (arrowType > 0) {
                wPAutoShape.createStartArrow(arrowType, getArrowWidth(element2.attributeValue("startarrowwidth")), getArrowLength(element2.attributeValue("startarrowlength")));
            }
            byte arrowType2 = getArrowType(element2.attributeValue("endarrow"));
            if (arrowType2 > 0) {
                wPAutoShape.createEndArrow(arrowType2, getArrowWidth(element2.attributeValue("endarrowwidth")), getArrowLength(element2.attributeValue("endarrowlength")));
            }
        }
    }

    private ExtendPath getArrowExtendPath(Path path, BackgroundAndFill backgroundAndFill, Line line, boolean z, byte b) {
        ExtendPath extendPath = new ExtendPath();
        extendPath.setArrowFlag(true);
        extendPath.setPath(path);
        if (backgroundAndFill != null || z) {
            if (z) {
                if (b == 5) {
                    extendPath.setLine(line);
                } else if (line != null) {
                    extendPath.setBackgroundAndFill(line.getBackgroundAndFill());
                } else if (backgroundAndFill != null) {
                    extendPath.setBackgroundAndFill(backgroundAndFill);
                }
            } else if (backgroundAndFill != null) {
                extendPath.setBackgroundAndFill(backgroundAndFill);
            }
        }
        return extendPath;
    }

    private WPAutoShape processAutoShape(Element shape, ParagraphElement paraElem, WPGroupShape parent, float zoomX, float zoomY, boolean hasTextbox)
    {
        if (shape != null)
        {
            String val;
            Float[] values = null;
            boolean border = true;
            int shapeType = getAutoShapeType(shape);
            WPAutoShape autoShape = null;

            // adjust values
            String[] adjustStr = null;
            if (shape.attribute("adj") != null)
            {
                val = shape.attributeValue("adj");
                if (val != null && val.length() > 0)
                {
                    adjustStr = val.split(",");
                }
            }
            if (adjustStr != null && adjustStr.length > 0)
            {
                values = new Float[adjustStr.length];
                for (int i = 0; i < adjustStr.length; i++)
                {
                    String temp = adjustStr[i];
                    if (temp != null && temp.length() > 0)
                    {
                        values[i] = Float.parseFloat(temp) / 21600;
                    }
                    else
                    {
                        values[i] = null;
                    }
                }
            }

            BackgroundAndFill fill = processBackgroundAndFill(shape);
            Line line = processLine(shape);

            if (shapeType == ShapeTypes.StraightConnector1
                    || shapeType == ShapeTypes.BentConnector2 || shapeType == ShapeTypes.BentConnector3
                    || shapeType == ShapeTypes.CurvedConnector3)
            {
                autoShape = new WPAutoShape();
                autoShape.setShapeType(shapeType);
                autoShape.setLine(line);
                processArrow(autoShape, shape);

                if(autoShape.getShapeType() == ShapeTypes.BentConnector2 && values == null)
                {
                    autoShape.setAdjustData(new Float[]{1.0f});
                }
                else
                {
                    autoShape.setAdjustData(values);
                }
            }
            else if(shapeType == ShapeTypes.ArbitraryPolygon)
            {
                autoShape = new WPAutoShape();
                autoShape.setShapeType(ShapeTypes.ArbitraryPolygon);
                processArrow(autoShape, shape);

                String pathContext = shape.attributeValue("path");

                float pathzoom = processPolygonZoom(shape, autoShape, parent, zoomX, zoomY);
                int lineWidth = Math.round((line != null ? line.getLineWidth() : 1) * pathzoom);
                PathWithArrow pathWithArrow = VMLPathParser.instance().createPath(autoShape, pathContext, lineWidth);

                if(pathWithArrow != null)
                {
                    Path[] paths = pathWithArrow.getPolygonPath();
                    if(paths != null)
                    {
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

                    if(pathWithArrow.getStartArrow() != null)
                    {
                        autoShape.appendPath(getArrowExtendPath(pathWithArrow.getStartArrow(), fill, line, border, autoShape.getStartArrow().getType()));
                    }

                    if(pathWithArrow.getEndArrow() != null)
                    {
                        autoShape.appendPath(getArrowExtendPath(pathWithArrow.getEndArrow(), fill, line, border, autoShape.getEndArrow().getType()));
                    }
                }
            }
            else if(shapeType == ShapeTypes.WP_Line
                    || shapeType == ShapeTypes.Curve
                    || shapeType == ShapeTypes.DirectPolygon)
            {
                autoShape = new WPAutoShape();
                autoShape.setShapeType(shapeType);
                processArrow(autoShape, shape);
                Path path = new Path();
                Path startArrowPath = null;
                Path endArrowPath = null;
                int lineWidth = (line != null ? line.getLineWidth() : 1);
                if(shapeType == ShapeTypes.Line)
                {
                    if(line != null)
                    {
                        fill = line.getBackgroundAndFill();
                    }

                    //one line, not StraightConnector1
                    PointF from = processPoints(shape.attributeValue("from"))[0];
                    PointF to = processPoints(shape.attributeValue("to"))[0];

                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;
                    if(autoShape.getStartArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getDirectLineArrowPath(to.x, to.y, from.x, from.y, autoShape.getStartArrow(), lineWidth);
                        startArrowPath = arrowPathAndTail.getArrowPath();
                        startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(autoShape.getEndArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getDirectLineArrowPath(from.x, from.y, to.x, to.y, autoShape.getEndArrow(), lineWidth);
                        endArrowPath = arrowPathAndTail.getArrowPath();
                        endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(startArrowTailCenter != null)
                    {
                        from =
                                LineArrowPathBuilder.getReferencedPosition(from.x, from.y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                    }

                    if(endArrowTailCenter != null)
                    {
                        to =
                                LineArrowPathBuilder.getReferencedPosition(to.x, to.y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                    }

                    path.moveTo(from.x, from.y);
                    path.lineTo(to.x, to.y);
                }
                else if(shapeType == ShapeTypes.Curve)
                {
                    //one beizer line
                    PointF from = processPoints(shape.attributeValue("from"))[0];
                    PointF ctr1 = processPoints(shape.attributeValue("control1"))[0];
                    PointF ctr2 = processPoints(shape.attributeValue("control2"))[0];
                    PointF to = processPoints(shape.attributeValue("to"))[0];

                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;
                    if(autoShape.getStartArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getCubicBezArrowPath(to.x, to.y, ctr2.x, ctr2.y, ctr1.x, ctr1.y, from.x, from.y, autoShape.getStartArrow(), lineWidth);
                        startArrowPath = arrowPathAndTail.getArrowPath();
                        startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(autoShape.getEndArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getCubicBezArrowPath(from.x, from.y, ctr1.x, ctr1.y, ctr2.x, ctr2.y,  to.x, to.y, autoShape.getEndArrow(), lineWidth);
                        endArrowPath = arrowPathAndTail.getArrowPath();
                        endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(startArrowTailCenter != null)
                    {
                        from =
                                LineArrowPathBuilder.getReferencedPosition(from.x, from.y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                    }

                    if(endArrowTailCenter != null)
                    {
                        to =
                                LineArrowPathBuilder.getReferencedPosition(to.x, to.y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                    }

                    path.moveTo(from.x, from.y);
                    path.cubicTo(ctr1.x, ctr1.y, ctr2.x, ctr2.y, to.x, to.y);
                }
                else if(shapeType == ShapeTypes.DirectPolygon)
                {
                    //direct polygon
                    PointF[] pts = processPoints(shape.attributeValue("points"));
                    int ptCnt = pts.length;

                    PointF startArrowTailCenter = null;
                    PointF endArrowTailCenter = null;
                    if(autoShape.getStartArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getDirectLineArrowPath(pts[1].x, pts[1].y, pts[0].x, pts[0].y, autoShape.getStartArrow(), lineWidth);
                        startArrowPath = arrowPathAndTail.getArrowPath();
                        startArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(autoShape.getEndArrowhead())
                    {
                        ArrowPathAndTail arrowPathAndTail =
                                LineArrowPathBuilder.getDirectLineArrowPath(pts[ptCnt - 2].x, pts[ptCnt - 2].y, pts[ptCnt - 1].x, pts[ptCnt - 1].y, autoShape.getEndArrow(), lineWidth);
                        endArrowPath = arrowPathAndTail.getArrowPath();
                        endArrowTailCenter = arrowPathAndTail.getArrowTailCenter();
                    }

                    if(startArrowTailCenter != null)
                    {
                        pts[0] =
                                LineArrowPathBuilder.getReferencedPosition(pts[0].x, pts[0].y, startArrowTailCenter.x, startArrowTailCenter.y, autoShape.getStartArrow().getType());
                    }

                    if(endArrowTailCenter != null)
                    {
                        pts[ptCnt - 1] =
                                LineArrowPathBuilder.getReferencedPosition(pts[ptCnt - 1].x, pts[ptCnt - 1].y, endArrowTailCenter.x, endArrowTailCenter.y, autoShape.getEndArrow().getType());
                    }

                    path.moveTo(pts[0].x, pts[0].y);
                    int index = 1;
                    while(index < pts.length)
                    {
                        path.lineTo(pts[index].x, pts[index].y);
                        index++;
                    }
                }

                ExtendPath pathExtend = new ExtendPath();
                pathExtend.setPath(path);
                if (line != null)
                {
                    pathExtend.setLine(line);
                }
                if (fill != null)
                {
                    pathExtend.setBackgroundAndFill(fill);
                }
                autoShape.appendPath(pathExtend);

                if(startArrowPath != null)
                {
                    autoShape.appendPath(getArrowExtendPath(startArrowPath, fill, line, border, autoShape.getStartArrow().getType()));
                }

                if(endArrowPath != null)
                {
                    autoShape.appendPath(getArrowExtendPath(endArrowPath, fill, line, border, autoShape.getEndArrow().getType()));
                }
            }
            else if (hasTextbox || fill != null || border)
            {
                String s = shape.attributeValue("id");
                if (s != null && s.indexOf("WaterMarkObject") > 0)
                {
                    isProcessWatermark = true;
                }
                if (isProcessWatermark)
                {
                    autoShape = new WatermarkShape();
                }
                else
                {
                    autoShape = new WPAutoShape();
                }
                autoShape.setShapeType(shapeType);
                processArrow(autoShape, shape);

                if (fill != null)
                {
                    autoShape.setBackgroundAndFill(fill);
                }

                if (line != null)
                {
                    autoShape.setLine(line);
                }
                autoShape.setAdjustData(values);
            }

            if (autoShape != null)
            {
                autoShape.setAuotShape07(true);
                if(parent == null)
                {
                    autoShape.setWrap(getShapeWrapType(shape));
                }
                else
                {
                    autoShape.setWrap(parent.getWrapType());
                }
                processAutoShapeStyle(shape, autoShape, parent, zoomX, zoomY);
                processRotation(autoShape);

                if (isProcessWatermark)
                {
                    processWatermark( ((WatermarkShape)autoShape), shape);
                    isProcessWatermark = false;
                }

                if (parent == null)
                {
                    addShape(autoShape, paraElem);
                }
                else
                {
                    parent.appendShapes(autoShape);
                }
            }

            return autoShape;
        }

        return null;
    }

    private void processWatermark(WatermarkShape watermarkShape, Element element) {
        Element element2 = element.element("textpath");
        if (element2 != null) {
            watermarkShape.setWatermarkType((byte) 0);
            String attributeValue = element.attributeValue("fillcolor");
            if (attributeValue != null && attributeValue.length() > 0) {
                watermarkShape.setFontColor(getColor(attributeValue, false));
            }
            Element element3 = element.element("fill");
            if (element3 != null) {
                watermarkShape.setOpacity(Float.parseFloat(element3.attributeValue("opacity")));
            }
            watermarkShape.setWatermartString(element2.attributeValue(TypedValues.Custom.S_STRING));
            for (String split : element2.attributeValue(HtmlTags.STYLE).split(";")) {
                String[] split2 = split.split(":");
                if (HtmlTags.FONTSIZE.equalsIgnoreCase(split2[0])) {
                    int parseInt = Integer.parseInt(split2[1].replace("pt", ""));
                    if (parseInt == 1) {
                        watermarkShape.setAutoFontSize(true);
                    } else {
                        watermarkShape.setFontSize(parseInt);
                    }
                }
            }
        }
    }

    private Line processLine(Element shape)
    {
        String val = shape.attributeValue("stroked");
        // border
        if ("f".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val))
        {
            return null;
        }

        String type = shape.attributeValue("type");
        List<Element> shapeTypes = shape.getParent().elements("shapetype");
        Element shapetype = null;
        if(type != null && shapeTypes != null)
        {
            for(Element shapeType : shapeTypes)
            {
                if(type.equals("#" + shapeType.attributeValue("id")))
                {
                    shapetype = shapeType;
                    break;
                }
            }
        }

        if(shapetype != null)
        {
            val = shapetype.attributeValue("stroked");
            if ("f".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val))
            {
                return null;
            }
        }

        Line line = null;
        int lineColor = 0xFF000000;
        val = shape.attributeValue("strokecolor");
        if (val != null && val.length() > 0)
        {
            lineColor = getColor(val, false);
        }

        BackgroundAndFill lineFill = new BackgroundAndFill();
        lineFill.setForegroundColor(lineColor);

        int lineWidth = 1;
        val = shape.attributeValue("strokeweight");
        if(shape.attributeValue("strokeweight") != null)
        {
            if (val.indexOf("pt") >= 0)
            {
                val = val.replace("pt", "");
            }
            else if (val.indexOf("mm") >= 0)
            {
                val = val.replace("mm", "");
            }
            else if (val.indexOf("cm") >= 0)
            {
                val = val.replace("cm", "");
            }
            lineWidth = Math.round(Float.parseFloat(val) * MainConstant.POINT_TO_PIXEL);
        }

        boolean dash = false;
        Element stroke = null;
        if(shape.element("stroke") != null)
        {
            stroke = shape.element("stroke");
            dash = stroke.attributeValue("dashstyle") != null;
        }

        line = new Line();
        line.setBackgroundAndFill(lineFill);
        line.setLineWidth(lineWidth);
        line.setDash(dash);

        return line;
    }

    private BackgroundAndFill processBackgroundAndFill(Element shape)
    {
        BackgroundAndFill fill = null;
        boolean filled = true;
        String val;
        // background
        if (shape.attribute("filled") != null)
        {
            val = shape.attributeValue("filled");
            if (val != null && val.length() > 0)
            {
                if (val.equals("f") || val.equals("false"))
                {
                    filled = false;
                }
            }
        }
        if (filled)
        {
            Element fillElem = shape.element("fill");
            if (fillElem != null && fillElem.attribute("id") != null)
            {
                //frame, pattern and tile
                val = fillElem.attributeValue("id");
                if (val != null && val.length() > 0)
                {
                    PackagePart picPart = null;
                    if (isProcessHF && hfPart != null)
                    {
                        picPart = zipPackage.getPart(hfPart.getRelationship(val).getTargetURI());
                    }
                    else
                    {
                        picPart = zipPackage.getPart(packagePart.getRelationship(val).getTargetURI());
                    }
                    if (picPart != null)
                    {
                        fill = new BackgroundAndFill();

                        byte type = getFillType(fillElem.attributeValue("type"));
                        try
                        {
                            if(type == BackgroundAndFill.FILL_SHADE_TILE)
                            {
                                fill.setFillType(BackgroundAndFill.FILL_SHADE_TILE);
                                int index = control.getSysKit().getPictureManage().addPicture(picPart);
                                fill.setShader(
                                        new TileShader(control.getSysKit().getPictureManage().getPicture(index),
                                                TileShader.Flip_None, 1f, 1.0f));

                            }
                            else if(type == BackgroundAndFill.FILL_PATTERN)
                            {
                                val = shape.attributeValue("fillcolor");
                                int foregroundColor = 0xFFFFFFFF;
                                if (val != null && val.length() > 0)
                                {
                                    foregroundColor = getColor(val, false);
                                }
                                int backgroundColor = 0xFFFFFFFF;
                                val = fillElem.attributeValue("color2");
                                if(val != null)
                                {
                                    backgroundColor = getColor(val, true);
                                }

                                fill.setFillType(BackgroundAndFill.FILL_PATTERN);
                                int index = control.getSysKit().getPictureManage().addPicture(picPart);
                                fill.setShader(
                                        new PatternShader(control.getSysKit().getPictureManage().getPicture(index),
                                                backgroundColor, foregroundColor));
                            }
                            else
                            {
                                fill.setFillType(BackgroundAndFill.FILL_PICTURE);
                                fill.setPictureIndex(control.getSysKit().getPictureManage().addPicture(picPart));
                            }
                        }
                        catch (Exception e)
                        {
                            control.getSysKit().getErrorKit().writerLog(e);
                        }
                    }
                }
            }
            if (fill == null)
            {
                fill = new BackgroundAndFill();

                byte type = BackgroundAndFill.FILL_SOLID;
                if(fillElem != null)
                {
                    type = getFillType(fillElem.attributeValue("type"));
                }

                if(fillElem == null || type == BackgroundAndFill.FILL_SOLID)
                {
                    int fillColor = Color.WHITE;
                    fill.setFillType(BackgroundAndFill.FILL_SOLID);

                    val = shape.attributeValue("fillcolor");
                    if (val != null && val.length() > 0)
                    {
                        fillColor = getColor(val, true);
                    }

                    if(fillElem !=  null)
                    {
                        String opacityStr = fillElem.attributeValue("opacity");
                        if(opacityStr != null)
                        {
                            float opacity = Float.parseFloat(opacityStr);
                            if(opacity > 1)
                            {
                                opacity /= 65536f;
                            }
                            opacity *= 255;
                            fillColor = ((byte)opacity << 24) | (fillColor & 0xFFFFFF);
                        }
                    }

                    fill.setForegroundColor(fillColor);
                }
                else
                {
                    Gradient gradient = readGradient(shape, fillElem, type);

                    fill.setFillType(type);
                    fill.setShader(gradient);
                }
            }
        }
        return fill;
    }

    private Gradient readGradient(Element shape, Element fillElem, byte type)
    {
        int focus = 0;
        String sFocus = fillElem.attributeValue("focus");
        if(sFocus != null)
        {
            focus = Integer.parseInt(sFocus.replace("%", ""));
        }

        int angle = 0;
        String sAngle = fillElem.attributeValue("angle");
        if(sAngle !=  null)
        {
            angle = Integer.parseInt(sAngle);
        }
        switch(angle)
        {
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

        int[] colors = null;
        float[] positions = null;
        String intermediateColors = fillElem.attributeValue("colors");
        if(intermediateColors != null)
        {
            String[] positionColor = intermediateColors.split(";");
            int length = positionColor.length;
            colors = new int[length];
            positions = new float[length];
            int index = 0;
            String pos = null;
            String color = null;
            for(int i = 0; i < length; i++)
            {
                index = positionColor[i].indexOf(" ");
                pos = positionColor[i].substring(0, index);
                if(pos.contains("f"))
                {
                    positions[i] = Float.parseFloat(pos) / 100000f;
                }
                else
                {
                    positions[i] = Float.parseFloat(pos);
                }
                colors[i] = getColor(positionColor[i].substring(index + 1, positionColor[i].length()), true);
            }
        }
        else
        {

            int color = getColor(shape.attributeValue("fillcolor"), true);
            int color2 = 0;
            String sColor2 = fillElem.attributeValue("color2");
            if(sColor2 != null)
            {
                sColor2 = sColor2.replace("fill ", "");
                int index1 = sColor2.indexOf("(");
                int index2 = sColor2.indexOf(")");
                if(index1 >= 0 && index2 >= 0)
                {
                    String sAlpha = sColor2.substring(index1 + 1, index2);
                }
                color2 = getColor(sColor2, true);
            }

            colors = new int[]{color, color2};

            positions = new float[]{0f, 1f};
        }

        Gradient gradient = null;
        if(type == BackgroundAndFill.FILL_SHADE_LINEAR)
        {
            gradient =
                    new LinearGradientShader(angle, colors, positions);
        }
        else if(type == BackgroundAndFill.FILL_SHADE_RADIAL)
        {
            int posType = getRadialGradientPositionType(fillElem);
            gradient =
                    new RadialGradientShader(posType, colors, positions);

        }

        if(gradient != null)
        {
            gradient.setFocus(focus);
        }
        return gradient;
    }

    private AbstractShape processAutoShape2010(ParagraphElement paragraphElement, Element element, WPGroupShape wPGroupShape, float f, float f2, int i, int i2, boolean z) {
        return processAutoShape2010(this.packagePart, paragraphElement, element, wPGroupShape, f, f2, i, i2, z);
    }

    private AbstractShape processAutoShape2010(PackagePart packagePart, ParagraphElement paraElem, Element sp, WPGroupShape parent,
                                               float zoomX, float zoomY, int x, int y, boolean bResetRect)
    {
        Rectangle rect = null;
        AbstractShape shape = null;
        if (sp != null)
        {
            Element temp = null;
            String name = sp.getName();

            // rect
            if (name.equals("wsp") || name.equals("sp") || name.equals("pic"))
            {
                temp = sp.element("spPr");
            }
            else if (name.equals("wgp") || name.equals("grpSp"))
            {
                temp = sp.element("grpSpPr");
            }
            if (temp != null)
            {
                rect = ReaderKit.instance().getShapeAnchor(temp.element("xfrm"), zoomX, zoomY);
                if (rect != null)
                {
                    if (bResetRect)
                    {
                        rect.x += x;
                        rect.y += y;
                    }

                    rect = processGrpSpRect(parent, rect);
                }
            }

            // shape
            if (rect != null && (name.equals("wgp") || name.equals("grpSp")))
            {
                float[] zoomXY = null;
                Rectangle childRect = null;
                Element grpSpPr = sp.element("grpSpPr");
                if (grpSpPr != null)
                {
                    zoomXY = ReaderKit.instance().getAnchorFitZoom(grpSpPr.element("xfrm"));
                    childRect = ReaderKit.instance().getChildShapeAnchor(grpSpPr.element("xfrm"), zoomXY[0]* zoomX, zoomXY[1]* zoomY);
                    if (bResetRect)
                    {
                        childRect.x += x;
                        childRect.y += y;
                    }

                    WPGroupShape grpShape = new WPGroupShape();
                    grpShape.setOffPostion(rect.x - childRect.x, rect.y - childRect.y);
                    grpShape.setBounds(rect);
                    ReaderKit.instance().processRotation(grpSpPr, grpShape);

                    for (Iterator< ? > it = sp.elementIterator(); it.hasNext();)
                    {
                        processAutoShape2010(packagePart, paraElem, (Element)it.next(), grpShape, zoomXY[0]* zoomX, zoomXY[1]* zoomY, 0, 0, false);
                    }

                    if (parent == null)
                    {
                        shape = new WPAutoShape();
                        ((WPAutoShape)shape).addGroupShape(grpShape);
                    }
                    else
                    {
                        shape = grpShape;
                    }
                }
            }
            else if (rect != null)
            {
                if (name.equals("wsp") || name.equals("sp"))
                {
                    try
                    {
                        if (isProcessHF && hfPart != null)
                        {
                            shape = AutoShapeDataKit.getAutoShape(control, zipPackage, hfPart, sp,
                                    rect, themeColor, MainConstant.APPLICATION_TYPE_WP, hasTextbox2010(sp));
                            if(shape != null)
                            {
                                processTextbox2010(packagePart, (WPAutoShape)shape, sp);
                            }
                        }
                        else
                        {
                            shape = AutoShapeDataKit.getAutoShape(control, zipPackage, packagePart, sp,
                                    rect, themeColor, MainConstant.APPLICATION_TYPE_WP, hasTextbox2010(sp));
                            if(shape != null)
                            {
                                processTextbox2010(packagePart, (WPAutoShape)shape, sp);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (name.equals("pic"))
                {
                    shape = addPicture(sp, rect);
                }
            }
            if (shape != null)
            {
                if (parent == null)
                {
                    addShape(shape, paraElem);
                }
                else
                {
                    shape.setParent(parent);
                    if(shape instanceof WPAutoShape)
                    {
                        ((WPAutoShape)shape).setWrap(parent.getWrapType());
                    }
                    parent.appendShapes(shape);
                }
            }
        }
        return shape;
    }

    private void processAlternateContent(Element element, ParagraphElement paragraphElement) {
        Element element2;
        Element element3;
        short s;
        Element element4;
        Element element5;
        String attributeValue;
        if (element != null && (element2 = element.element("Choice")) != null && (element3 = element2.element("drawing")) != null) {
            Element element6 = element3.element("anchor");
            if (element6 == null) {
                element6 = element3.element("inline");
                s = 2;
            } else {
                s = -1;
            }
            if (element6 != null) {
                Element element7 = element6.element("docPr");
                if ((element7 == null || (attributeValue = element7.attributeValue((String) "name")) == null || (!attributeValue.startsWith("Genko") && !attributeValue.startsWith("Header") && !attributeValue.startsWith("Footer"))) && (element4 = element6.element("graphic")) != null && (element5 = element4.element("graphicData")) != null) {
                    Iterator elementIterator = element5.elementIterator();
                    while (elementIterator.hasNext()) {
                        AbstractShape processAutoShape2010 = processAutoShape2010(paragraphElement, (Element) elementIterator.next(), (WPGroupShape) null, 1.0f, 1.0f, 0, 0, true);
                        if (processAutoShape2010 != null) {
                            if (processAutoShape2010 instanceof WPAutoShape) {
                                WPAutoShape wPAutoShape = (WPAutoShape) processAutoShape2010;
                                if (wPAutoShape.getGroupShape() != null) {
                                    WPGroupShape groupShape = wPAutoShape.getGroupShape();
                                    if (s == -1) {
                                        s = getDrawingWrapType(element6);
                                    }
                                    groupShape.setWrapType(s);
                                    setShapeWrapType(groupShape, s);
                                }
                            }
                            processWrapAndPosition_Drawing((WPAutoShape) processAutoShape2010, element6, processAutoShape2010.getBounds());
                        }
                    }
                }
            }
        }
    }

    private void setShapeWrapType(WPGroupShape wPGroupShape, short s) {
        for (IShape iShape : wPGroupShape.getShapes()) {
            if (iShape instanceof WPAbstractShape) {
                ((WPAbstractShape) iShape).setWrap(s);
            } else if (iShape instanceof WPGroupShape) {
                setShapeWrapType((WPGroupShape) iShape, s);
            }
        }
    }

    private void processRunAttribute(Element element, IAttributeSet iAttributeSet) {
        String attributeValue;
        int addFontName;
        Element element2 = element.element("szCs");
        Element element3 = element.element("sz");
        if (!(element2 == null && element3 == null)) {
            float f = 12.0f;
            if (element2 != null) {
                f = Math.max(12.0f, Float.parseFloat(element2.attributeValue("val")) / 2.0f);
            }
            if (element3 != null) {
                f = Math.max(f, Float.parseFloat(element3.attributeValue("val")) / 2.0f);
            }
            AttrManage.instance().setFontSize(iAttributeSet, (int) f);
        }
        Element element4 = element.element("rFonts");
        if (element4 != null) {
            String attributeValue2 = element4.attributeValue("hAnsi");
            if (attributeValue2 == null) {
                attributeValue2 = element4.attributeValue("eastAsia");
            }
            if (attributeValue2 != null && (addFontName = FontTypefaceManage.instance().addFontName(attributeValue2)) >= 0) {
                AttrManage.instance().setFontName(iAttributeSet, addFontName);
            }
        }
        Element element5 = element.element("color");
        if (element5 != null) {
            String attributeValue3 = element5.attributeValue("val");
            if (DebugKt.DEBUG_PROPERTY_VALUE_AUTO.equals(attributeValue3) || "FFFFFF".equals(attributeValue3)) {
                AttrManage.instance().setFontColor(iAttributeSet, -16777216);
            } else {
                AttrManage instance = AttrManage.instance();
                instance.setFontColor(iAttributeSet, Color.parseColor("#" + attributeValue3));
            }
        }
        int i = 1;
        if (element.element(HtmlTags.B) != null) {
            AttrManage.instance().setFontBold(iAttributeSet, true);
        }
        if (element.element(HtmlTags.I) != null) {
            AttrManage.instance().setFontItalic(iAttributeSet, true);
        }
        Element element6 = element.element(HtmlTags.U);
        if (!(element6 == null || element6.attributeValue("val") == null)) {
            AttrManage.instance().setFontUnderline(iAttributeSet, 1);
            String attributeValue4 = element6.attributeValue("color");
            if (attributeValue4 != null && attributeValue4.length() > 0) {
                AttrManage instance2 = AttrManage.instance();
                instance2.setFontUnderlineColr(iAttributeSet, Color.parseColor("#" + attributeValue4));
            }
        }
        Element element7 = element.element(HtmlTags.STRIKE);
        if (element7 != null) {
            AttrManage.instance().setFontStrike(iAttributeSet, !"0".equals(element7.attributeValue("val")));
        }
        Element element8 = element.element("dstrike");
        if (element8 != null) {
            AttrManage.instance().setFontDoubleStrike(iAttributeSet, !"0".equals(element8.attributeValue("val")));
        }
        Element element9 = element.element("vertAlign");
        if (element9 != null) {
            String attributeValue5 = element9.attributeValue("val");
            AttrManage instance3 = AttrManage.instance();
            if (!attributeValue5.equals("superscript")) {
                i = 2;
            }
            instance3.setFontScript(iAttributeSet, i);
        }
        Element element10 = element.element("rStyle");
        if (!(element10 == null || (attributeValue = element10.attributeValue("val")) == null || attributeValue.length() <= 0)) {
            AttrManage.instance().setParaStyleID(iAttributeSet, this.styleStrID.get(attributeValue).intValue());
        }
        Element element11 = element.element("highlight");
        if (element11 != null) {
            AttrManage.instance().setFontHighLight(iAttributeSet, FCKit.convertColor(element11.attributeValue("val")));
        }
    }

    private int processValue(String str, boolean z) {
        int i;
        int i2 = z ? 144 : 72;
        if (str == null) {
            return i2;
        }
        if (ReaderKit.instance().isDecimal(str)) {
            i = Integer.parseInt(str);
        } else {
            i = Integer.parseInt(str, 16);
        }
        return (int) (((((float) i) * 96.0f) / 914400.0f) * 15.0f);
    }

    /* access modifiers changed from: private */
    public void processParagraphs(List<Element> list) {
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element next = it.next();
            if ("sdt".equals(next.getName()) && (next = next.element("sdtContent")) != null) {
                processParagraphs(next.elements());
            }
            if (HtmlTags.P.equals(next.getName())) {
                processParagraph(next, 0);
            } else if (PGPlaceholderUtil.TABLE.equals(next.getName())) {
                processTable(next);
            }
        }
    }

    private boolean hasTextbox2007(Element element) {
        String attributeValue;
        Element element2 = element.element("textbox");
        if (element2 != null) {
            if (element2.element("txbxContent") != null) {
                return true;
            }
        } else if (element.element("textpath") == null || (attributeValue = element.element("textpath").attributeValue((String) TypedValues.Custom.S_STRING)) == null || attributeValue.length() <= 0) {
            return false;
        } else {
            return true;
        }
        return false;
    }

    private boolean processTextbox2007(PackagePart packagePart2, WPAutoShape wPAutoShape, Element element) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9 = null;
        String str10;
        String str11;
        String str12;
        SectionElement sectionElement;
        int i;
        int i2;
        int i3;
        int i4;
        String str13 = null;
        String str14;
        int i5;
        WPAutoShape wPAutoShape2 = wPAutoShape;
        Element element2 = element;
        Element element3 = element2.element("textbox");
        if (element3 != null) {
            Element element4 = element3.element("txbxContent");
            if (element4 == null) {
                return false;
            }
            long j = this.offset;
            long j2 = this.textboxIndex;
            String str15 = HtmlTags.ALIGN_BOTTOM;
            String str16 = "none";
            this.offset = (j2 << 32) + WPModelConstant.TEXTBOX;
            wPAutoShape2.setElementIndex((int) j2);
            SectionElement sectionElement2 = new SectionElement();
            sectionElement2.setStartOffset(this.offset);
            this.wpdoc.appendElement(sectionElement2, this.offset);
            processParagraphs(element4.elements());
            IAttributeSet attribute = sectionElement2.getAttribute();
            AttrManage.instance().setPageWidth(attribute, (int) (((float) wPAutoShape.getBounds().width) * 15.0f));
            AttrManage.instance().setPageHeight(attribute, (int) (((float) wPAutoShape.getBounds().height) * 15.0f));
            String attributeValue = element3.attributeValue("inset");
            if (attributeValue != null) {
                String[] split = attributeValue.split(",");
                if (split.length <= 0 || split[0].length() <= 0) {
                    sectionElement = sectionElement2;
                    i5 = 144;
                } else {
                    sectionElement = sectionElement2;
                    i5 = Math.round(getValueInPt(split[0], 1.0f) * 20.0f);
                }
                i2 = (split.length <= 1 || split[1].length() <= 0) ? 72 : Math.round(getValueInPt(split[1], 1.0f) * 20.0f);
                int round = (split.length <= 2 || split[2].length() <= 0) ? 144 : Math.round(getValueInPt(split[2], 1.0f) * 20.0f);
                int i6 = i5;
                if (split.length <= 3 || split[3].length() <= 0) {
                    i3 = round;
                    i = i6;
                    i4 = 72;
                } else {
                    i4 = Math.round(getValueInPt(split[3], 1.0f) * 20.0f);
                    i3 = round;
                    i = i6;
                }
            } else {
                sectionElement = sectionElement2;
                i4 = 72;
                i3 = 144;
                i2 = 72;
                i = 144;
            }
            AttrManage.instance().setPageMarginTop(attribute, i2);
            AttrManage.instance().setPageMarginBottom(attribute, i4);
            AttrManage.instance().setPageMarginLeft(attribute, i);
            AttrManage.instance().setPageMarginRight(attribute, i3);
            String attributeValue2 = element2.attributeValue(HtmlTags.STYLE);
            if (attributeValue2 != null) {
                String[] split2 = attributeValue2.split(";");
                int i7 = 0;
                while (i7 < split2.length) {
                    String[] split3 = split2[i7].split(":");
                    if (!(split3 == null || split3[0] == null || split3[1] == null || HtmlTags.TEXTALIGN.equalsIgnoreCase(split3[0]))) {
                        if (!"v-text-anchor".equalsIgnoreCase(split3[0])) {
                            str13 = str15;
                            if ("mso-wrap-style".equalsIgnoreCase(split3[0])) {
                                str14 = str16;
                                wPAutoShape2.setTextWrapLine(!str14.equalsIgnoreCase(split3[1]));
                                i7++;
                                str16 = str14;
                                str15 = str13;
                            }
                        } else if (HtmlTags.ALIGN_MIDDLE.equals(split3[1])) {
                            AttrManage.instance().setPageVerticalAlign(attribute, (byte) 1);
                        } else {
                            str13 = str15;
                            if (str13.equals(split3[1])) {
                                AttrManage.instance().setPageVerticalAlign(attribute, (byte) 2);
                            } else if (HtmlTags.ALIGN_TOP.equals(split3[1])) {
                                AttrManage.instance().setPageVerticalAlign(attribute, (byte) 0);
                            }
                        }
                        str14 = str16;
                        i7++;
                        str16 = str14;
                        str15 = str13;
                    }
                    str13 = str15;
                    str14 = str16;
                    i7++;
                    str16 = str14;
                    str15 = str13;
                }
            }
            wPAutoShape2.setElementIndex((int) this.textboxIndex);
            sectionElement.setEndOffset(this.offset);
            this.textboxIndex++;
            this.offset = j;
            return true;
        }
        String str17 = HtmlTags.ALIGN_BOTTOM;
        String str18 = "none";
        if (element2.element("textpath") == null) {
            return false;
        }
        String attributeValue3 = element2.element("textpath").attributeValue(TypedValues.Custom.S_STRING);
        wPAutoShape2.setBackgroundAndFill((BackgroundAndFill) null);
        String str19 = str17;
        long j3 = this.offset;
        long j4 = this.textboxIndex;
        String str20 = HtmlTags.ALIGN_MIDDLE;
        this.offset = (j4 << 32) + WPModelConstant.TEXTBOX;
        wPAutoShape2.setElementIndex((int) j4);
        SectionElement sectionElement3 = new SectionElement();
        sectionElement3.setStartOffset(this.offset);
        this.wpdoc.appendElement(sectionElement3, this.offset);
        ParagraphElement paragraphElement = new ParagraphElement();
        long j5 = this.offset;
        paragraphElement.setStartOffset(j5);
        String str21 = str20;
        int length = attributeValue3.length();
        if (length > 0) {
            LeafElement leafElement = new LeafElement(attributeValue3);
            str2 = str18;
            String attributeValue4 = element2.attributeValue("fillcolor");
            if (attributeValue4 == null || attributeValue4.length() <= 0) {
                str6 = "mso-wrap-style";
                str5 = "v-text-anchor";
                str4 = HtmlTags.TEXTALIGN;
            } else {
                str6 = "mso-wrap-style";
                AttrManage instance = AttrManage.instance();
                str5 = "v-text-anchor";
                IAttributeSet attribute2 = leafElement.getAttribute();
                str4 = HtmlTags.TEXTALIGN;
                instance.setFontColor(attribute2, getColor(attributeValue4, true));
            }
            float width = ((float) wPAutoShape.getBounds().getWidth()) - 19.2f;
            float height = ((float) wPAutoShape.getBounds().getHeight()) - 9.6f;
            int i8 = 12;
            Paint paint = PaintKit.instance().getPaint();
            str3 = ":";
            paint.setTextSize((float) 12);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            str = ";";
            while (((float) ((int) paint.measureText(attributeValue3))) < width && ((float) ((int) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent)))) < height) {
                i8++;
                paint.setTextSize((float) i8);
                fontMetrics = paint.getFontMetrics();
            }
            AttrManage.instance().setFontSize(leafElement.getAttribute(), (int) (((float) (i8 - 1)) * 0.75f));
            leafElement.setStartOffset(this.offset);
            long j6 = this.offset + ((long) length);
            this.offset = j6;
            leafElement.setEndOffset(j6);
            paragraphElement.appendLeaf(leafElement);
        } else {
            str2 = str18;
            str6 = "mso-wrap-style";
            str5 = "v-text-anchor";
            str4 = HtmlTags.TEXTALIGN;
            str3 = ":";
            str = ";";
        }
        paragraphElement.setEndOffset(this.offset);
        long j7 = this.offset;
        if (j7 > j5) {
            this.wpdoc.appendParagraph(paragraphElement, j7);
        }
        IAttributeSet attribute3 = sectionElement3.getAttribute();
        AttrManage.instance().setPageWidth(attribute3, (int) (((float) wPAutoShape.getBounds().width) * 15.0f));
        AttrManage.instance().setPageHeight(attribute3, (int) (((float) wPAutoShape.getBounds().height) * 15.0f));
        AttrManage.instance().setPageMarginTop(attribute3, 72);
        AttrManage.instance().setPageMarginBottom(attribute3, 72);
        AttrManage.instance().setPageMarginLeft(attribute3, 144);
        AttrManage.instance().setPageMarginRight(attribute3, 144);
        String attributeValue5 = element2.attributeValue(HtmlTags.STYLE);
        if (attributeValue5 != null) {
            String[] split4 = attributeValue5.split(str);
            int i9 = 0;
            while (i9 < split4.length) {
                String str22 = str3;
                String[] split5 = split4[i9].split(str22);
                if (split5 == null || split5[0] == null || split5[1] == null) {
                    WPAutoShape wPAutoShape3 = wPAutoShape;
                    str10 = str21;
                    str8 = str6;
                    str12 = str5;
                    str11 = str4;
                } else {
                    str11 = str4;
                    if (str11.equalsIgnoreCase(split5[0])) {
                        WPAutoShape wPAutoShape4 = wPAutoShape;
                        str10 = str21;
                        str8 = str6;
                        str12 = str5;
                    } else {
                        str12 = str5;
                        if (str12.equalsIgnoreCase(split5[0])) {
                            str10 = str21;
                            if (str10.equals(split5[1])) {
                                AttrManage.instance().setPageVerticalAlign(attribute3, (byte) 1);
                                WPAutoShape wPAutoShape5 = wPAutoShape;
                                str8 = str6;
                            } else {
                                str9 = str19;
                                if (str9.equals(split5[1])) {
                                    AttrManage.instance().setPageVerticalAlign(attribute3, (byte) 2);
                                } else if (HtmlTags.ALIGN_TOP.equals(split5[1])) {
                                    AttrManage.instance().setPageVerticalAlign(attribute3, (byte) 0);
                                }
                                WPAutoShape wPAutoShape6 = wPAutoShape;
                                str8 = str6;
                            }
                        } else {
                            str10 = str21;
                            str9 = str19;
                            str8 = str6;
                            if (str8.equalsIgnoreCase(split5[0])) {
                                str7 = str2;
                                wPAutoShape.setTextWrapLine(!str7.equalsIgnoreCase(split5[1]));
                                i9++;
                                str3 = str22;
                                str5 = str12;
                                str4 = str11;
                                str21 = str10;
                                str19 = str9;
                                str6 = str8;
                                str2 = str7;
                            } else {
                                WPAutoShape wPAutoShape7 = wPAutoShape;
                            }
                        }
                        str7 = str2;
                        i9++;
                        str3 = str22;
                        str5 = str12;
                        str4 = str11;
                        str21 = str10;
                        str19 = str9;
                        str6 = str8;
                        str2 = str7;
                    }
                }
                str9 = str19;
                str7 = str2;
                i9++;
                str3 = str22;
                str5 = str12;
                str4 = str11;
                str21 = str10;
                str19 = str9;
                str6 = str8;
                str2 = str7;
            }
        }
        wPAutoShape.setElementIndex((int) this.textboxIndex);
        sectionElement3.setEndOffset(this.offset);
        this.textboxIndex++;
        this.offset = j3;
        return true;
    }

    private boolean hasTextbox2010(Element element) {
        Element element2 = element.element("txbx");
        return (element2 == null || element2.element("txbxContent") == null) ? false : true;
    }

    private boolean processTextbox2010(PackagePart packagePart2, WPAutoShape wPAutoShape, Element element) {
        Element element2;
        Element element3 = element.element("txbx");
        boolean z = false;
        if (element3 == null || (element2 = element3.element("txbxContent")) == null) {
            return false;
        }
        long j = this.offset;
        long j2 = this.textboxIndex;
        this.offset = (j2 << 32) + WPModelConstant.TEXTBOX;
        wPAutoShape.setElementIndex((int) j2);
        SectionElement sectionElement = new SectionElement();
        sectionElement.setStartOffset(this.offset);
        this.wpdoc.appendElement(sectionElement, this.offset);
        processParagraphs(element2.elements());
        IAttributeSet attribute = sectionElement.getAttribute();
        AttrManage.instance().setPageWidth(attribute, (int) (((float) wPAutoShape.getBounds().width) * 15.0f));
        AttrManage.instance().setPageHeight(attribute, (int) (((float) wPAutoShape.getBounds().height) * 15.0f));
        Element element4 = element.element("bodyPr");
        if (element4 != null) {
            AttrManage.instance().setPageMarginTop(attribute, processValue(element4.attributeValue("tIns"), false));
            AttrManage.instance().setPageMarginBottom(attribute, processValue(element4.attributeValue("bIns"), false));
            AttrManage.instance().setPageMarginLeft(attribute, processValue(element4.attributeValue("lIns"), true));
            AttrManage.instance().setPageMarginRight(attribute, processValue(element4.attributeValue("rIns"), true));
            String attributeValue = element4.attributeValue("anchor");
            if ("ctr".equals(attributeValue)) {
                AttrManage.instance().setPageVerticalAlign(attribute, (byte) 1);
            } else if (HtmlTags.B.equals(attributeValue)) {
                AttrManage.instance().setPageVerticalAlign(attribute, (byte) 2);
            } else if ("t".equals(attributeValue)) {
                AttrManage.instance().setPageVerticalAlign(attribute, (byte) 0);
            }
            String attributeValue2 = element4.attributeValue("wrap");
            if (attributeValue2 == null || "square".equalsIgnoreCase(attributeValue2)) {
                z = true;
            }
            wPAutoShape.setTextWrapLine(z);
            wPAutoShape.setElementIndex((int) this.textboxIndex);
        }
        sectionElement.setEndOffset(this.offset);
        this.textboxIndex++;
        this.offset = j;
        return true;
    }

    public boolean searchContent(File file, String key) throws Exception
    {
        boolean isContain = false;
        zipPackage = new ZipPackage(filePath);
        PackageRelationship coreRel = zipPackage.getRelationshipsByType(
                PackageRelationshipTypes.CORE_DOCUMENT).getRelationship(0);
        packagePart = zipPackage.getPart(coreRel);

        SAXReader saxreader = new SAXReader();
        InputStream in = packagePart.getInputStream();
        Document poiDoc = saxreader.read(in);

        // 正文
        Element root = poiDoc.getRootElement();
        Element body = root.element("body");
        StringBuilder sb = new StringBuilder();
        if (body != null)
        {
            List<Element> paras = body.elements("p");
            for (Element para : paras)
            {
                List<Element> runs = para.elements("r");
                for (Element run : runs)
                {
                    Element text = run.element("t");
                    if (text == null)
                    {
                        continue;
                    }
                    sb.append(text.getText());
                }
                if (sb.indexOf(key) >= 0)
                {
                    isContain = true;
                    break;
                }
                sb.delete(0, sb.length());
            }
        }
        zipPackage= null;
        packagePart = null;

        in.close();

        return isContain;
    }

    /* renamed from: com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc.DOCXReader$DOCXSaxHandler */
    class DOCXSaxHandler implements ElementHandler {
        public void onStart(ElementPath elementPath) {
        }

        DOCXSaxHandler() {
        }

        public void onEnd(ElementPath elementPath) {
            if (!DOCXReader.this.abortReader) {
                Element current = elementPath.getCurrent();
                String name = current.getName();
                current.elements();
                if (HtmlTags.P.equals(name)) {
                    DOCXReader.this.processParagraph(current, 0);
                }
                if ("sdt".equals(current.getName())) {
                    current = current.element("sdtContent");
                    if (current != null) {
                        DOCXReader.this.processParagraphs(current.elements());
                    }
                } else if (PGPlaceholderUtil.TABLE.equals(name)) {
                    DOCXReader.this.processTable(current);
                } else if (Picture.PICT_TYPE.equals(name)) {
                    ParagraphElement paragraphElement = new ParagraphElement();
                    long access$400 = DOCXReader.this.offset;
                    paragraphElement.setStartOffset(DOCXReader.this.offset);
                    DOCXReader.this.processPicture(current, paragraphElement);
                    paragraphElement.setEndOffset(DOCXReader.this.offset);
                    if (DOCXReader.this.offset > access$400) {
                        DOCXReader.this.wpdoc.appendParagraph(paragraphElement, DOCXReader.this.offset);
                    }
                }
                current.detach();
                return;
            }
            throw new AbortReaderError("abort Reader");
        }
    }

    private void processThemeColor() throws Exception {
        PackageRelationship relationship;
        PackagePart part;
        PackagePart packagePart2 = this.packagePart;
        if (packagePart2 != null && (relationship = packagePart2.getRelationshipsByType(PackageRelationshipTypes.THEME_PART).getRelationship(0)) != null && (part = this.zipPackage.getPart(relationship.getTargetURI())) != null) {
            Map<String, Integer> themeColorMap = ThemeReader.instance().getThemeColorMap(part);
            this.themeColor = themeColorMap;
            if (themeColorMap != null) {
                themeColorMap.put(SchemeClrConstant.SCHEME_BG1, themeColorMap.get(SchemeClrConstant.SCHEME_LT1));
                Map<String, Integer> map = this.themeColor;
                map.put(SchemeClrConstant.SCHEME_TX1, map.get(SchemeClrConstant.SCHEME_DK1));
                Map<String, Integer> map2 = this.themeColor;
                map2.put(SchemeClrConstant.SCHEME_BG2, map2.get(SchemeClrConstant.SCHEME_LT2));
                Map<String, Integer> map3 = this.themeColor;
                map3.put(SchemeClrConstant.SCHEME_TX2, map3.get(SchemeClrConstant.SCHEME_DK2));
            }
        }
    }

    private void processRelativeShapeSize() {
        int pageWidth = AttrManage.instance().getPageWidth(this.secElem.getAttribute());
        int pageHeight = AttrManage.instance().getPageHeight(this.secElem.getAttribute());
        for (IShape next : this.relativeType) {
            int[] iArr = this.relativeValue.get(next);
            Rectangle bounds = next.getBounds();
            if (iArr[0] > 0) {
                bounds.width = (int) (((((float) pageWidth) * 0.06666667f) * ((float) iArr[0])) / 1000.0f);
            }
            if (iArr[2] > 0) {
                bounds.height = (int) (((((float) pageHeight) * 0.06666667f) * ((float) iArr[2])) / 1000.0f);
            }
        }
    }

    private byte getArrowType(String str) {
        if ("block".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("classic".equalsIgnoreCase(str)) {
            return 2;
        }
        if ("oval".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("diamond".equalsIgnoreCase(str)) {
            return 3;
        }
        return "open".equalsIgnoreCase(str) ? (byte) 5 : 0;
    }

    private int getArrowWidth(String str) {
        if ("narrow".equalsIgnoreCase(str)) {
            return 0;
        }
        return "wide".equalsIgnoreCase(str) ? 2 : 1;
    }

    private int getArrowLength(String str) {
        if ("short".equalsIgnoreCase(str)) {
            return 0;
        }
        return "long".equalsIgnoreCase(str) ? 2 : 1;
    }

    public void dispose() {
        if (isReaderFinish()) {
            this.filePath = null;
            this.zipPackage = null;
            this.wpdoc = null;
            this.packagePart = null;
            Map<String, Integer> map = this.styleStrID;
            if (map != null) {
                map.clear();
                this.styleStrID = null;
            }
            Map<Integer, Integer> map2 = this.tableGridCol;
            if (map2 != null) {
                map2.clear();
                this.tableGridCol = null;
            }
            this.tableGridCol = null;
            this.control = null;
            List<IShape> list = this.relativeType;
            if (list != null) {
                list.clear();
                this.relativeType = null;
            }
            Map<IShape, int[]> map3 = this.relativeValue;
            if (map3 != null) {
                map3.clear();
                this.relativeValue = null;
            }
            Hashtable<String, String> hashtable = this.bulletNumbersID;
            if (hashtable != null) {
                hashtable.clear();
                this.bulletNumbersID = null;
            }
        }
    }
}
