package com.docreader.docviewer.pdfcreator.pdfreader.filereader.EBookViewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.xmp.DublinCoreSchema;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class EpubBook extends Book {
    private static final String BOOK_CONTENT_DIR = "bookContentDir";
    private static final String ITEM = "item.";
    private static final String META_PREFIX = "meta.";
    private static final String ORDER = "order.";
    private static final String ORDERCOUNT = "ordercount";
    private static final String TOC = "toc";
    private static final String TOCCOUNT = "toccount";
    private static final String TOC_CONTENT = "toc.content.";
    private static final String TOC_LABEL = "toc.label.";
    private File bookContentDir;
    private final List<String> docFileOrder = new ArrayList<>();
    private final Map<String, String> docFiles = new LinkedHashMap<>();
    private final Map<String, String> tocPoints = new LinkedHashMap<>();

    private static final NamespaceContext packnsc = new NamespaceContext() {
        public String getPrefix(String str) {
            return null;
        }

        public Iterator getPrefixes(String str) {
            return null;
        }

        public String getNamespaceURI(String str) {
            return (str == null || !str.equals(DublinCoreSchema.DEFAULT_XPATH_ID)) ? "http://www.idpf.org/2007/opf" : "http://purl.org/dc/elements/1.1/";
        }
    };

    private static final NamespaceContext tocnsc = new NamespaceContext() {
        public String getNamespaceURI(String str) {
            return "http://www.daisy.org/z3986/2005/ncx/";
        }

        public String getPrefix(String str) {
            return null;
        }

        public Iterator getPrefixes(String str) {
            return null;
        }
    };

    public EpubBook(Context context) {
        super(context);
    }

    public void load() throws IOException {
        if (!getSharedPreferences().contains(ORDERCOUNT)) {
            for (File file : Zip.unzip(getFile(), getThisBookDir())) {
                Log.d("EPUB", "unzipped + " + file);
            }
            loadEpub();
        }
        SharedPreferences sharedPreferences = getSharedPreferences();
        this.bookContentDir = new File(sharedPreferences.getString(BOOK_CONTENT_DIR, ""));
        int i = sharedPreferences.getInt(ORDERCOUNT, 0);
        for (int i2 = 0; i2 < i; i2++) {
            String string = sharedPreferences.getString(ORDER + i2, "");
            String string2 = sharedPreferences.getString(ITEM + string, "");
            this.docFileOrder.add(string);
            this.docFiles.put(string, string2);
        }
        int i3 = sharedPreferences.getInt(TOCCOUNT, 0);
        for (int i4 = 0; i4 < i3; i4++) {
            String string3 = sharedPreferences.getString(TOC_LABEL + i4, "");
            this.tocPoints.put(sharedPreferences.getString(TOC_CONTENT + i4, ""), string3);
        }
    }

    public Map<String, String> getToc() {
        return Collections.unmodifiableMap(this.tocPoints);
    }


    public List<String> getSectionIds() {
        return Collections.unmodifiableList(this.docFileOrder);
    }


    public Uri getUriForSectionID(String str) {
        return Uri.fromFile(new File(getFullBookContentDir(), Objects.requireNonNull(this.docFiles.get(str))));
    }

    public Book.ReadPoint locateReadPoint(String str) {
        Uri uri;
        if (str == null) {
            return null;
        }
        try {
            uri = Uri.parse(Uri.decode(str));
        } catch (Exception e) {
            Log.e("Epub", e.getMessage(), e);
            uri = null;
        }
        if (uri == null) {
            return null;
        }
        if (uri.isRelative()) {
            uri = new Uri.Builder().scheme(Annotation.FILE).path(getFullBookContentDir().getPath()).appendPath(uri.getPath()).fragment(uri.getFragment()).build();
        }
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment == null) {
            return null;
        }
        String str2 = null;
        for (Map.Entry next : this.docFiles.entrySet()) {
            if (lastPathSegment.equals(next.getValue())) {
                str2 = (String) next.getKey();
            }
        }
        if (str2 == null) {
            return null;
        }
        Book.ReadPoint readPoint = new Book.ReadPoint();
        readPoint.setId(str2);
        readPoint.setPoint(uri);
        return readPoint;
    }

    private File getFullBookContentDir() {
        return new File(getThisBookDir(), this.bookContentDir.getPath());
    }

    private void loadEpub() throws FileNotFoundException {
        List<String> rootFilesFromContainer = getRootFilesFromContainer(new BufferedReader(new FileReader(new File(getThisBookDir(), "META-INF/container.xml"))));
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        String parent = new File(rootFilesFromContainer.get(0)).getParent();
        if (parent == null) {
            parent = "";
        }
        Map<String, ?> processBookDataFromRootFile = processBookDataFromRootFile(new BufferedReader(new FileReader(new File(getThisBookDir(), rootFilesFromContainer.get(0)))));
        edit.putString(BOOK_CONTENT_DIR, parent);
        for (Map.Entry next : processBookDataFromRootFile.entrySet()) {
            Object value = next.getValue();
            if (value instanceof String) {
                edit.putString((String) next.getKey(), (String) value);
            } else if (value instanceof Integer) {
                edit.putInt((String) next.getKey(), (Integer) value);
            }
        }
        if (processBookDataFromRootFile.get(TOC) != null) {
            String str = (String) processBookDataFromRootFile.get(ITEM + processBookDataFromRootFile.get(TOC));
            Log.d("EPUB", "tocfname = " + str + " bookContentDir =" + parent);
            for (Map.Entry next2 : processToc(new BufferedReader(new FileReader(new File(new File(getThisBookDir(), parent), Objects.requireNonNull(str))))).entrySet()) {
                Object value2 = next2.getValue();
                if (value2 instanceof String) {
                    edit.putString((String) next2.getKey(), (String) value2);
                } else if (value2 instanceof Integer) {
                    edit.putInt((String) next2.getKey(), (Integer) value2);
                }
            }
        }
        edit.apply();
    }

    public String getTitle() {
        return getSharedPreferences().getString("meta.dc:title", "No title");
    }

    public BookMetadata getMetaData() throws IOException {
        try (ZipFile zipFile = new ZipFile(getFile())) {
            ZipEntry entry = zipFile.getEntry("META-INF/container.xml");
            if (entry == null) {
                zipFile.close();
                return null;
            }
            List<String> rootFilesFromContainer = getRootFilesFromContainer(new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry))));
            if (rootFilesFromContainer.size() == 0) {
                zipFile.close();
                return null;
            }
            ZipEntry entry2 = zipFile.getEntry(rootFilesFromContainer.get(0));
            if (entry2 == null) {
                zipFile.close();
                return null;
            }
            Map<String, ?> processBookDataFromRootFile = processBookDataFromRootFile(new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry2))));
            if (processBookDataFromRootFile.size() == 0) {
                Log.d("Epub", "No data for " + getFile());
            }
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            for (String next : processBookDataFromRootFile.keySet()) {
                if (next.startsWith(META_PREFIX)) {
                    linkedHashMap.put(next.substring(META_PREFIX.length()), Objects.requireNonNull(processBookDataFromRootFile.get(next)).toString());
                }
            }
            BookMetadata bookMetadata = new BookMetadata();
            bookMetadata.setFilename(getFile().getPath());
            bookMetadata.setTitle((String) linkedHashMap.get(DublinCoreSchema.TITLE));
            bookMetadata.setAuthor((String) linkedHashMap.get(DublinCoreSchema.CREATOR));
            bookMetadata.setAlldata(linkedHashMap);
            zipFile.close();
            return bookMetadata;
        }
    }

    private static List<String> getRootFilesFromContainer(BufferedReader bufferedReader) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            bufferedReader.mark(4);
            if (65279 != bufferedReader.read()) {
                bufferedReader.reset();
            }
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(false);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(bufferedReader);
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2 && newPullParser.getName().equals("rootfile")) {
                    for (int i = 0; i < newPullParser.getAttributeCount(); i++) {
                        if (newPullParser.getAttributeName(i).equals("full-path")) {
                            arrayList.add(newPullParser.getAttributeValue(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("BMBF", "Error parsing xml " + e, e);
        }
        return arrayList;
    }

    private static Map<String, ?> processBookDataFromRootFile(BufferedReader bufferedReader) {
        String str;
        String str2;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        XPathFactory newInstance = XPathFactory.newInstance();
        DocumentBuilderFactory newInstance2 = DocumentBuilderFactory.newInstance();
        try {
            bufferedReader.mark(4);
            if (65279 != bufferedReader.read()) {
                bufferedReader.reset();
            }
            Document parse = newInstance2.newDocumentBuilder().parse(new InputSource(bufferedReader));
            XPath newXPath = newInstance.newXPath();
            NamespaceContext namespaceContext = packnsc;
            newXPath.setNamespaceContext(namespaceContext);
            Node node = (Node) newXPath.evaluate("/package", parse.getDocumentElement(), XPathConstants.NODE);
            XPath newXPath2 = newInstance.newXPath();
            newXPath2.setNamespaceContext(namespaceContext);
            NodeList nodeList = (NodeList) newXPath2.evaluate("metadata/*", node, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                if (item != null) {
                    NamedNodeMap attributes = item.getAttributes();
                    if (!item.getNodeName().equals("meta") || attributes == null) {
                        String nodeName = item.getNodeName();
                        str = item.getTextContent();
                        str2 = nodeName;
                    } else {
                        Node namedItem = attributes.getNamedItem("name");
                        str = null;
                        str2 = namedItem != null ? namedItem.getNodeValue() : null;
                        Node namedItem2 = attributes.getNamedItem("content");
                        if (namedItem2 != null) {
                            str = namedItem2.getNodeValue();
                        }
                    }
                    if (!(str2 == null || str == null)) {
                        linkedHashMap.put(META_PREFIX + str2, str);
                    }
                }
            }
            XPath newXPath3 = newInstance.newXPath();
            newXPath3.setNamespaceContext(packnsc);
            NodeList nodeList2 = (NodeList) newXPath3.evaluate("manifest/item", node, XPathConstants.NODESET);
            for (int i2 = 0; i2 < nodeList2.getLength(); i2++) {
                Node item2 = nodeList2.item(i2);
                if (item2.getNodeName().equals("item")) {
                    NamedNodeMap attributes2 = item2.getAttributes();
                    String nodeValue = attributes2.getNamedItem(ScreenCVEdit.FIELD_ID).getNodeValue();
                    String nodeValue2 = attributes2.getNamedItem(HtmlTags.HREF).getNodeValue();
                    linkedHashMap.put(ITEM + nodeValue, nodeValue2);
                }
            }
            XPath newXPath4 = newInstance.newXPath();
            newXPath4.setNamespaceContext(packnsc);
            Node node2 = (Node) newXPath4.evaluate("spine", node, XPathConstants.NODE);
            linkedHashMap.put(TOC, node2.getAttributes().getNamedItem(TOC).getNodeValue());
            NodeList nodeList3 = (NodeList) newXPath4.evaluate("itemref", node2, XPathConstants.NODESET);
            for (int i3 = 0; i3 < nodeList3.getLength(); i3++) {
                Node item3 = nodeList3.item(i3);
                if (item3.getNodeName().equals("itemref")) {
                    String nodeValue3 = item3.getAttributes().getNamedItem("idref").getNodeValue();
                    linkedHashMap.put(ORDER + i3, nodeValue3);
                }
            }
            linkedHashMap.put(ORDERCOUNT, Integer.valueOf(nodeList3.getLength()));
        } catch (Exception e) {
            Log.e("BMBF", "Error parsing xml " + e.getMessage(), e);
        }
        return linkedHashMap;
    }

    private static Map<String, ?> processToc(BufferedReader bufferedReader) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        XPathFactory newInstance2 = XPathFactory.newInstance();
        try {
            DocumentBuilder newDocumentBuilder = newInstance.newDocumentBuilder();
            bufferedReader.mark(4);
            if (65279 != bufferedReader.read()) {
                bufferedReader.reset();
            }
            Document parse = newDocumentBuilder.parse(new InputSource(bufferedReader));
            XPath newXPath = newInstance2.newXPath();
            newXPath.setNamespaceContext(tocnsc);
            linkedHashMap.put(TOCCOUNT, Integer.valueOf(readNavPoint((Node) newXPath.evaluate("/ncx/navMap", parse, XPathConstants.NODE), newXPath, linkedHashMap, 0)));
        } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
            Log.e("BMBF", "Error parsing xml " + e.getMessage(), e);
        }
        return linkedHashMap;
    }

    private static int readNavPoint(Node node, XPath xPath, Map<String, Object> map, int i) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.evaluate("navPoint", node, XPathConstants.NODESET);
        for (int i2 = 0; i2 < nodeList.getLength(); i2++) {
            Node item = nodeList.item(i2);
            String evaluate = xPath.evaluate("navLabel/text/text()", item);
            String evaluate2 = xPath.evaluate("content/@src", item);
            map.put(TOC_LABEL + i, evaluate);
            map.put(TOC_CONTENT + i, evaluate2);
            i = readNavPoint(item, xPath, map, i + 1);
        }
        return i;
    }
}
