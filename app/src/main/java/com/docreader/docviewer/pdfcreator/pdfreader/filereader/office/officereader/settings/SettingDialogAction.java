/*
 * 文件名称:           SettingDialogAction.java
 *  
 * 编译器:             android2.2
 * 时间:               下午4:42:51
 */
package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.officereader.settings;

import java.util.Vector;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.DialogConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.EventConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IControl;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IDialogAction;


/**
 * used in setting
 * <p>
 * <p>
 * Read版本:       Read V1.0
 * <p>
 * 作者:           jhy1790
 * <p>
 * 日期:           2012-1-11
 * <p>
 * 负责人:         jhy1790
 * <p>
 * 负责小组:         
 * <p>
 * <p>
 */
public class SettingDialogAction implements IDialogAction
{
    /**
     * 
     * @param control
     */
    public SettingDialogAction(IControl control)
    {
        this.control = control;
    }

    /**
     * 
     * @param id    对话框的ID
     * @param obj   回调action需要数据
     */
    public void doAction(int id, Vector<Object> model)
    {
        switch(id)
        {                
            case DialogConstant.SET_MAX_RECENT_NUMBER: // set maximum count of recently files
                if (model != null)
                {
                    control.actionEvent(EventConstant.SYS_SET_MAX_RECENT_NUMBER, model.get(0));
                }
                break;
                
            default:
                break;
        }
    }

    /**
     * 
     *
     */
    public IControl getControl()
    {
        return this.control;
    }

    /**
     * 
     *
     */
    public void dispose()
    {
        control = null;
    }

    //
    public IControl control;
}
