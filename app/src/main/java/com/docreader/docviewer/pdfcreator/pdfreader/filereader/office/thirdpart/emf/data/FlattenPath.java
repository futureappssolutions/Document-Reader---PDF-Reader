// Copyright 2002, FreeHEP.
package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.thirdpart.emf.data;

import java.io.IOException;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.thirdpart.emf.EMFInputStream;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.thirdpart.emf.EMFTag;

/**
 * FlattenPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: FlattenPath.java 10140 2006-12-07 07:50:41Z duns $
 */
public class FlattenPath extends EMFTag {

    public FlattenPath() {
        super(65, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

}
