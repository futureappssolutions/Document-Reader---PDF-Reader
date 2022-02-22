package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.DocumentFiles;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.NotepadItemModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_DOCUMENT_FILE = "CREATE TABLE tblDocumentFiles(fileId INTEGER PRIMARY KEY,fileName TEXT,fileContent TEXT,fileCreatedAt TEXT,fileUpdatedAt TEXT,fileType INTEGER)";
    private static final String CREATE_TABLE_MSGs = "CREATE TABLE tblMsg(msg_id INTEGER PRIMARY KEY,msg_server_id TEXT,chat_id TEXT,senderId TEXT,receiverId TEXT,msg_type TEXT,is_seen TEXT,msg TEXT,msg_created_at TEXT)";
    private static final String CREATE_TABLE_NOTE = "CREATE TABLE notes (noteId INTEGER PRIMARY KEY AUTOINCREMENT, noteTitle TEXT NOT NULL, noteContent TEXT NOT NULL, noteThem INTEGER , notePin INTEGER , noteDate TEXT);";
    private static final String CREATE_TABLE_POST = "CREATE TABLE tblPost (postId INTEGER PRIMARY KEY AUTOINCREMENT, postSeverId TEXT , postTitle TEXT , postDescription TEXT , postImage TEXT , postCategoryId TEXT , postCategory TEXT , likes INTEGER , dislikes INTEGER , isLike INTEGER , isDislike INTEGER , postCreatedOn TEXT);";
    private static final String DATABASE_NAME = "officeMasterDB";
    private static final String KEY_FILE_CONTENT = "fileContent";
    private static final String KEY_FILE_CREATED_AT = "fileCreatedAt";
    private static final String KEY_FILE_ID = "fileId";
    private static final String KEY_FILE_NAME = "fileName";
    private static final String KEY_FILE_TYPE = "fileType";
    private static final String KEY_FILE_UPDATED_AT = "fileUpdatedAt";
    private static final String KEY_NOTE_CONTENT = "noteContent";
    private static final String KEY_NOTE_DATE = "noteDate";
    private static final String KEY_NOTE_ID = "noteId";
    private static final String KEY_NOTE_PIN = "notePin";
    private static final String KEY_NOTE_THEM = "noteThem";
    private static final String KEY_NOTE_TITLE = "noteTitle";
    private static final String TABLE_DOCUMENT_FILES = "tblDocumentFiles";
    private static final String TABLE_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_MSGs);
        sQLiteDatabase.execSQL(CREATE_TABLE_DOCUMENT_FILE);
        sQLiteDatabase.execSQL(CREATE_TABLE_NOTE);
        sQLiteDatabase.execSQL(CREATE_TABLE_POST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblMsg");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblDocumentFiles");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS tblPost");
        onCreate(sQLiteDatabase);
    }

    public void dropAllTable() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblMsg");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblDocumentFiles");
        writableDatabase.execSQL("DROP TABLE IF EXISTS notes");
        writableDatabase.execSQL("DROP TABLE IF EXISTS tblPost");
        onCreate(writableDatabase);
    }

    public int insertIntoTblDocumentFile(DocumentFiles documentFiles) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FILE_NAME, documentFiles.getFileName());
        contentValues.put(KEY_FILE_CONTENT, documentFiles.getFileContent());
        contentValues.put(KEY_FILE_CREATED_AT, documentFiles.getCreatedAt());
        contentValues.put(KEY_FILE_UPDATED_AT, documentFiles.getUpdatedAt());
        contentValues.put(KEY_FILE_TYPE, documentFiles.getFileType());
        long insert = writableDatabase.insert(TABLE_DOCUMENT_FILES, null, contentValues);
        Utility.logCatMsg("File data inserted into database " + insert);
        return (int) insert;
    }

    @SuppressLint("Range")
    public ArrayList<DocumentFiles> getAllDocumentFiles() {
        ArrayList<DocumentFiles> arrayList = new ArrayList<>();
        try {
            Utility.logCatMsg("DataBase " + "SELECT  * FROM tblDocumentFiles");
            @SuppressLint("Recycle") Cursor rawQuery = getReadableDatabase().rawQuery("SELECT  * FROM tblDocumentFiles", null);
            if (rawQuery.moveToFirst()) {
                do {
                    DocumentFiles documentFiles = new DocumentFiles();
                    documentFiles.setId(rawQuery.getInt(rawQuery.getColumnIndex(KEY_FILE_ID)));
                    documentFiles.setFileName(rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_NAME)));
                    documentFiles.setFileContent(rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_CONTENT)));
                    documentFiles.setCreatedAt(rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_CREATED_AT)));
                    documentFiles.setUpdatedAt(rawQuery.getString(rawQuery.getColumnIndex(KEY_FILE_UPDATED_AT)));
                    documentFiles.setFileType(rawQuery.getInt(rawQuery.getColumnIndex(KEY_FILE_TYPE)));
                    arrayList.add(documentFiles);
                } while (rawQuery.moveToNext());
            }
        } catch (Exception e) {
            Utility.logCatMsg("Error " + e.getMessage());
            e.printStackTrace();
        }
        return arrayList;
    }

    public void updateFileContent(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FILE_UPDATED_AT, Utility.getCurrentDateTime());
        contentValues.put(KEY_FILE_CONTENT, str2);
        writableDatabase.update(TABLE_DOCUMENT_FILES, contentValues, "fileId = ?", new String[]{str});
    }

    public void renameFileName(String str, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FILE_NAME, str2);
        writableDatabase.update(TABLE_DOCUMENT_FILES, contentValues, "fileId = ?", new String[]{str});
    }

    public void deleteFile(String str) {
        getWritableDatabase().delete(TABLE_DOCUMENT_FILES, "fileId = ?", new String[]{String.valueOf(str)});
    }

    public void addNote(String str, String str2, int i, int i2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE_TITLE, str);
        contentValues.put(KEY_NOTE_CONTENT, str2);
        contentValues.put(KEY_NOTE_DATE, System.currentTimeMillis() + "");
        contentValues.put(KEY_NOTE_THEM, i);
        contentValues.put(KEY_NOTE_PIN, i2);
        writableDatabase.insert(TABLE_NOTES, null, contentValues);
        writableDatabase.close();
    }

    public NotepadItemModel getNote(String str) {
        NotepadItemModel notepadItemModel;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        @SuppressLint("Recycle") Cursor query = writableDatabase.query(TABLE_NOTES, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_CONTENT, KEY_NOTE_DATE, KEY_NOTE_THEM, KEY_NOTE_PIN}, "noteId = ?", new String[]{str}, null, null, null);
        if (query.moveToFirst()) {
            do {
                notepadItemModel = new NotepadItemModel(query.getShort(0), query.getString(1), query.getString(2), query.getString(3), query.getInt(4), query.getInt(5));
            } while (query.moveToNext());
        } else {
            notepadItemModel = null;
        }
        writableDatabase.close();
        return notepadItemModel;
    }

    public ArrayList<NotepadItemModel> getAllNotes() {
        ArrayList<NotepadItemModel> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        @SuppressLint("Recycle") Cursor query = writableDatabase.query(TABLE_NOTES, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_CONTENT, KEY_NOTE_DATE, KEY_NOTE_THEM, KEY_NOTE_PIN}, null, null, null, null, "noteId DESC");
        if (query.moveToFirst()) {
            do {
                if (query.getInt(5) == 1) {
                    arrayList.add(0, new NotepadItemModel(query.getShort(0), query.getString(1), query.getString(2), query.getString(3), query.getInt(4), query.getInt(5)));
                } else {
                    arrayList.add(new NotepadItemModel(query.getShort(0), query.getString(1), query.getString(2), query.getString(3), query.getInt(4), query.getInt(5)));
                }
            } while (query.moveToNext());
        }
        writableDatabase.close();
        return arrayList;
    }

    public Cursor getNote(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor query = writableDatabase.query(TABLE_NOTES, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_CONTENT, KEY_NOTE_DATE}, "noteId = ?", new String[]{String.valueOf(i)}, null, null, null);
        query.moveToFirst();
        writableDatabase.close();
        return query;
    }

    public void removeNote(String str) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_NOTES, "noteId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }

    public void updateNote(String str, String str2, String str3, int i, int i2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE_TITLE, str2);
        contentValues.put(KEY_NOTE_CONTENT, str3);
        contentValues.put(KEY_NOTE_DATE, System.currentTimeMillis() + "");
        contentValues.put(KEY_NOTE_THEM, i);
        contentValues.put(KEY_NOTE_PIN, i2);
        writableDatabase.update(TABLE_NOTES, contentValues, "noteId = ?", new String[]{String.valueOf(str)});
        writableDatabase.close();
    }
}
