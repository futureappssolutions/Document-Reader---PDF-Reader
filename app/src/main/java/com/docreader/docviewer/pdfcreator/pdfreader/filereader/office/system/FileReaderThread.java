package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system;

import android.os.Handler;
import android.os.Message;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc.DOCReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc.DOCXReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.doc.TXTReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.pdf.PDFReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ppt.PPTReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.ppt.PPTXReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.xls.XLSReader;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.xls.XLSXReader;

public class FileReaderThread extends Thread {
    private String encoding;
    private String filePath;
    private Handler handler;
    private IControl control;

    public FileReaderThread(IControl control, Handler handler, String filePath, String encoding) {
        this.control = control;
        this.handler = handler;
        this.filePath = filePath;
        this.encoding = encoding;
    }

    public void run() {
        Message msg = new Message();
        msg.what = MainConstant.HANDLER_MESSAGE_SHOW_PROGRESS;
        handler.handleMessage(msg);

        msg = new Message();
        msg.what = MainConstant.HANDLER_MESSAGE_DISMISS_PROGRESS;
        try {
            IReader reader;
            String fileName = filePath.toLowerCase();

            if (fileName.endsWith(MainConstant.FILE_TYPE_DOC)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOT)) {
                reader = new DOCReader(control, filePath);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_DOCX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_DOTM)) {
                reader = new DOCXReader(control, filePath);

            } else if (fileName.endsWith(MainConstant.FILE_TYPE_TXT)) {
                reader = new TXTReader(control, filePath, encoding);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_XLS)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLT)) {
                reader = new XLSReader(control, filePath);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_XLSX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_XLSM)) {
                reader = new XLSXReader(control, filePath);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_PPT)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POT)) {
                reader = new PPTReader(control, filePath);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_PPTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_PPTM)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTX)
                    || fileName.endsWith(MainConstant.FILE_TYPE_POTM)) {
                reader = new PPTXReader(control, filePath);
            } else if (fileName.endsWith(MainConstant.FILE_TYPE_PDF)) {
                reader = new PDFReader(control, filePath);
            } else {
                reader = new TXTReader(control, filePath, encoding);
            }

            Message mesReader = new Message();
            mesReader.obj = reader;
            mesReader.what = MainConstant.HANDLER_MESSAGE_SEND_READER_INSTANCE;
            handler.handleMessage(mesReader);


            msg.obj = reader.getModel();
            reader.dispose();
            msg.what = MainConstant.HANDLER_MESSAGE_SUCCESS;
        } catch (OutOfMemoryError | Exception | AbortReaderError eee) {
            msg.what = MainConstant.HANDLER_MESSAGE_ERROR;
            msg.obj = eee;
        } finally {
            handler.handleMessage(msg);
            control = null;
            handler = null;
            encoding = null;
            filePath = null;
        }
    }
}
