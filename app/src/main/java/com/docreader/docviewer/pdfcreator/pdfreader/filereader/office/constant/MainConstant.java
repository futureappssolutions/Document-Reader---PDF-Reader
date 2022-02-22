package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant;

import com.itextpdf.text.html.HtmlTags;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.openxml4j.opc.ContentTypes;

import java.util.regex.Pattern;

public final class MainConstant
{
    // word应用
    public static final byte APPLICATION_TYPE_WP = 0;
    // excel应用
    public static final byte APPLICATION_TYPE_SS = 1;
    // powerpoint应用
    public static final byte APPLICATION_TYPE_PPT = 2;
    // pdf应用
    public static final byte APPLICATION_TYPE_PDF = 3;
    // text 应用
    public static final byte APPLICATION_TYPE_TXT = 4;
    // doc文档格式
    public static final String FILE_TYPE_DOC = "doc";
    // docx文档格式
    public static final String FILE_TYPE_DOCX = "docx";
    // xls文档格式
    public static final String FILE_TYPE_XLS = "xls";
    // xlsx文档格式
    public static final String FILE_TYPE_XLSX = "xlsx";
    // ppt文档格式
    public static final String FILE_TYPE_PPT = "ppt";
    // pptx文档格式
    public static final String FILE_TYPE_PPTX = "pptx";
    // txt文档格式
    public static final String FILE_TYPE_TXT = "txt";
    //
    public static final String  FILE_TYPE_PDF = "pdf";

    public static final String FILE_TYPE_DOT = "dot";
    public static final String FILE_TYPE_DOTX = "dotx";
    public static final String FILE_TYPE_DOTM = "dotm";
    public static final String FILE_TYPE_XLT = "xlt";
    public static final String FILE_TYPE_XLTX = "xltx";
    public static final String FILE_TYPE_XLSM = "xlsm";
    public static final String FILE_TYPE_XLTM = "xltm";
    public static final String FILE_TYPE_POT = "pot";
    public static final String FILE_TYPE_PPTM = "pptm";
    public static final String FILE_TYPE_POTX = "potx";
    public static final String FILE_TYPE_POTM = "potm";


    /* ============ Activity之间Intent传值字段名称 */
    // 文件路径
    public static final String INTENT_FILED_FILE_PATH = "filePath";
    // 文件列表类型
    public static final String INTENT_FILED_FILE_LIST_TYPE = "fileListType";
    // 标星文档
    public static final String INTENT_FILED_MARK_FILES = "markFiles";
    // 最近打开的文档
    public static final String INTENT_FILED_RECENT_FILES = "recentFiles";
    // sdcard文档
    public static final String INTENT_FILED_SDCARD_FILES = "sdcard";
    // 文档标星状态
    public static final String INTENT_FILED_MARK_STATUS = "markFileStatus";



    /* ======= 以下定义组件的高度 ========== */
    // 组件之前的问隙
    public static final int GAP = 5;
    //
    public static final int ZOOM_ROUND = 10000000;


    /* ======= 以下定义一些视图公用常量 ========= */
    // Points DPI (72 pixels per inch)
    public static final float POINT_DPI = 72.0f;
    //厘米到磅
    public static final float MM_TO_POINT = 2.835f;
    // 缇到磅
    public static final float TWIPS_TO_POINT = 1 / 20.0f;
    // 磅到缇
    public static final float POINT_TO_TWIPS = 20.0f;
    // default tab width, 21磅
    public static final float DEFAULT_TAB_WIDTH_POINT = 21;
    //
    public static final int EMU_PER_INCH = 914400;
    // Pixels DPI (96 pixels per inch)
    public static final float PIXEL_DPI =  96.f;
    // 磅到像素
    public static final float POINT_TO_PIXEL = PIXEL_DPI / POINT_DPI;
    // 像素到磅
    public static final float PIXEL_TO_POINT = POINT_DPI / PIXEL_DPI;
    // 缇到像素
    public static final float TWIPS_TO_PIXEL = TWIPS_TO_POINT * POINT_TO_PIXEL;
    // 像素到缇
    public static final float PIXEL_TO_TWIPS = PIXEL_TO_POINT * POINT_TO_TWIPS;
    //// default tab width, 21磅
    public static final float DEFAULT_TAB_WIDTH_PIXEL = DEFAULT_TAB_WIDTH_POINT * POINT_TO_PIXEL;



    /* ============ 数据库中表名*/
    // recently opened files
    public static final String TABLE_RECENT = "openedfiles";
    // starred files
    public static final String TABLE_STAR = "starredfiles";
    // settings
    public static final String TABLE_SETTING ="settings";

    /* =========== 文件解析过程中message类型 =========*/
    // 文档解析成功+
    public static final int HANDLER_MESSAGE_SUCCESS = 0;
    // 文档解析失败
    public static final int HANDLER_MESSAGE_ERROR = HANDLER_MESSAGE_SUCCESS + 1;
    // 显示进度条对话框
    public static final int HANDLER_MESSAGE_SHOW_PROGRESS = HANDLER_MESSAGE_ERROR + 1;
    // 关闭进度条对话框
    public static final int HANDLER_MESSAGE_DISMISS_PROGRESS = HANDLER_MESSAGE_SHOW_PROGRESS + 1;
    // 释放内存
    public static final int HANDLER_MESSAGE_DISPOSE = HANDLER_MESSAGE_DISMISS_PROGRESS + 1;
    // 传递IReader实例
    public static final int HANDLER_MESSAGE_SEND_READER_INSTANCE = HANDLER_MESSAGE_DISPOSE;

    //zoom
    public static final int STANDARD_RATE = 10000;
    public static final int MAXZOOM = 30000;
    public static final int MAXZOOM_THUMBNAIL = 5000;

    // Drawing mode
    //not callout mode
    public static final int DRAWMODE_NORMAL = 0;
    //draw callout
    public static final int DRAWMODE_CALLOUTDRAW = 1;
    //erase callout
    public static final int DRAWMODE_CALLOUTERASE = 2;

    public static final byte APPLICATION_TYPE_ALL = 100;
    public static final byte APPLICATION_TYPE_CODE = 6;
    public static final byte APPLICATION_TYPE_CODE_FOLDER_VIEW = 7;
    public static final byte APPLICATION_TYPE_COMPRESS = 15;
    public static final byte APPLICATION_TYPE_CSV = 10;
    public static final byte APPLICATION_TYPE_DIR = 5;
    public static final byte APPLICATION_TYPE_EPUB = 14;
    public static final byte APPLICATION_TYPE_FAVORITE = 12;
    public static final byte APPLICATION_TYPE_FOLDER_VIEW = 8;
    public static final byte APPLICATION_TYPE_HTML = 11;



    public static final String FILE_TYPE_CSV = "csv";
    public static final String FILE_TYPE_DJVU = "djvu";

    public static final String FILE_TYPE_EPUB = "epub";
    public static final String FILE_TYPE_HTML = "html";
    public static final String FILE_TYPE_MOBI = "mobi";

    public static final String FILE_TYPE_RTF = "rtf";

    public static final String FILE_TYPE_ZIP = "zip";

    public static final int NOTEPAD_COLOR_THEM1 = 101;
    public static final int NOTEPAD_COLOR_THEM10 = 1010;
    public static final int NOTEPAD_COLOR_THEM2 = 102;
    public static final int NOTEPAD_COLOR_THEM3 = 103;
    public static final int NOTEPAD_COLOR_THEM4 = 104;
    public static final int NOTEPAD_COLOR_THEM5 = 105;
    public static final int NOTEPAD_COLOR_THEM6 = 106;
    public static final int NOTEPAD_COLOR_THEM7 = 107;
    public static final int NOTEPAD_COLOR_THEM8 = 108;
    public static final int NOTEPAD_COLOR_THEM9 = 109;

    public static final String STORAGE_LOCATION = "storage_location";


    public static Pattern getPattern() {
        return Pattern.compile(".*\\.(doc|doc|docx|xls|xlsx|ppt|pptx|txt|pdf|dotx|dotm|xlt|xltx|xlsm|xltm|pot|pptm|potx|potm|epub|mobi|csv|rtf)$");
    }

    public static int getFileType(String str) {
        String lowerCase = str.toLowerCase();
        if (lowerCase.endsWith(FILE_TYPE_DOC) || lowerCase.endsWith(FILE_TYPE_DOCX) || lowerCase.endsWith(FILE_TYPE_DOT) || lowerCase.endsWith(FILE_TYPE_DOTX) || lowerCase.endsWith(FILE_TYPE_DOTM)) {
            return 0;
        }
        if (lowerCase.endsWith(FILE_TYPE_XLS) || lowerCase.endsWith(FILE_TYPE_XLSX) || lowerCase.endsWith(FILE_TYPE_XLT) || lowerCase.endsWith(FILE_TYPE_XLTX) || lowerCase.endsWith(FILE_TYPE_XLTM) || lowerCase.endsWith(FILE_TYPE_XLSM)) {
            return 1;
        }
        if (lowerCase.endsWith(FILE_TYPE_PPT) || lowerCase.endsWith(FILE_TYPE_PPTX) || lowerCase.endsWith(FILE_TYPE_POT) || lowerCase.endsWith(FILE_TYPE_PPTM) || lowerCase.endsWith(FILE_TYPE_POTX) || lowerCase.endsWith(FILE_TYPE_POTM)) {
            return 2;
        }
        if (lowerCase.endsWith("pdf")) {
            return 3;
        }
        if (lowerCase.endsWith(FILE_TYPE_TXT)) {
            return 4;
        }
        if (lowerCase.endsWith(FILE_TYPE_RTF)) {
            return 13;
        }
        if (lowerCase.endsWith(FILE_TYPE_EPUB) || lowerCase.endsWith(FILE_TYPE_MOBI)) {
            return 14;
        }
        if (lowerCase.endsWith(FILE_TYPE_CSV)) {
            return 10;
        }
        return (lowerCase.endsWith(FILE_TYPE_HTML) || lowerCase.endsWith("ascii") || lowerCase.endsWith("asm") || lowerCase.endsWith("awk") || lowerCase.endsWith("bash") || lowerCase.endsWith("bat") || lowerCase.endsWith("bf") || lowerCase.endsWith("bsh") || lowerCase.endsWith("c") || lowerCase.endsWith("cert") || lowerCase.endsWith("cgi") || lowerCase.endsWith("clj") || lowerCase.endsWith("conf") || lowerCase.endsWith("cpp") || lowerCase.endsWith("cs") || lowerCase.endsWith("css") || lowerCase.endsWith(FILE_TYPE_CSV) || lowerCase.endsWith("elr") || lowerCase.endsWith("go") || lowerCase.endsWith("h") || lowerCase.endsWith("hs") || lowerCase.endsWith("htaccess") || lowerCase.endsWith("htm") || lowerCase.endsWith(FILE_TYPE_HTML) || lowerCase.endsWith("ini") || lowerCase.endsWith("java") || lowerCase.endsWith("js") || lowerCase.endsWith("json") || lowerCase.endsWith("key") || lowerCase.endsWith("lisp") || lowerCase.endsWith(".log") || lowerCase.endsWith(".lua") || lowerCase.endsWith("md") || lowerCase.endsWith("mkdn") || lowerCase.endsWith("pem") || lowerCase.endsWith("php") || lowerCase.endsWith("pl") || lowerCase.endsWith("py") || lowerCase.endsWith("rb") || lowerCase.endsWith("readme") || lowerCase.endsWith("scala") || lowerCase.endsWith("sh") || lowerCase.endsWith("sql") || lowerCase.endsWith("srt") || lowerCase.endsWith(HtmlTags.SUB) || lowerCase.endsWith("tex") || lowerCase.endsWith("vb") || lowerCase.endsWith("vbs") || lowerCase.endsWith("vhdl") || lowerCase.endsWith("wollok") || lowerCase.endsWith(ContentTypes.EXTENSION_XML) || lowerCase.endsWith("xsd") || lowerCase.endsWith("xsl") || lowerCase.endsWith("yaml") || lowerCase.endsWith("iml") || lowerCase.endsWith("gitignore") || lowerCase.endsWith("gradle")) ? 6 : 5;
    }

    public static Pattern getCodeFilePattern() {
        return Pattern.compile(".*\\.(html|ascii|asm|awk|bash|bat|bf|bsh|c|cert|cgi|clj|conf|cpp|cs|css|csv|elr|go|h|hs|htaccess|htm|html|ini|java|js|json|key|lisp|.log|.lua|md|mkdn|pem|php|pl|py|rb|readme|scala|sh|sql|srt|sub|tex|vb|vbs|vhdl|wollok|xml|xsd|xsl|yaml|iml|gitignore|gradle)$");
    }
}
