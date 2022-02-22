/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hssf.formula.function;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hssf.formula.eval.NumberEval;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.fc.hssf.formula.eval.ValueEval;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.ss.util.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;



/**
 * Implementation of Excel TODAY() Function<br/>
 *
 * @author Frank Taffelt
 */
public final class Today extends Fixed0ArgFunction {

	public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {

		Calendar now = new GregorianCalendar();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE),0,0,0);
		now.set(Calendar.MILLISECOND, 0);
		return new NumberEval(DateUtil.getExcelDate(now.getTime()));
	}
}
