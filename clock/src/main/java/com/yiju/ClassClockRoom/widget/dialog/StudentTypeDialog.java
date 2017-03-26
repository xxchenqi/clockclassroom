package com.yiju.ClassClockRoom.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.StudentTypeSelectAdapter;
import com.yiju.ClassClockRoom.bean.StudentType;
import com.yiju.ClassClockRoom.util.CommonUtil;
import com.yiju.ClassClockRoom.util.UIUtils;

import java.util.List;

public class StudentTypeDialog {

    private Activity mActivity;
    private AlertDialog dialog;
    private IStudentTypeOnItemClick iStudentTypeOnItemClick;
    private List<StudentType> list;
    private String current_student_type;

    public interface IStudentTypeOnItemClick {
        void OnItemClick(StudentType studentType);
    }

    public StudentTypeDialog(
            Activity mActivity,
            List<StudentType> list,
            IStudentTypeOnItemClick iStudentType,
            String current_student_type) {
        this.mActivity = mActivity;
        this.list = list;
        this.iStudentTypeOnItemClick = iStudentType;
        this.current_student_type = current_student_type;
    }

    public void creatView() {
        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.popup_student_type_list, null);
        dialog = new AlertDialog.Builder(mActivity, R.style.dateDialogTheme).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
            window.setWindowAnimations(R.style.share_dialog_mystyle);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = CommonUtil.getScreenWidth(); // 设置宽度
            dialog.setContentView(view, lp);
        }
        ListView lv_student_type = (ListView) view.findViewById(R.id.lv_student_type);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        lv_student_type.setAdapter(new StudentTypeSelectAdapter(UIUtils.getContext(), list, current_student_type));

        lv_student_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list == null || list.size() == 0) {
                    return;
                }
                dialog.dismiss();
                iStudentTypeOnItemClick.OnItemClick(list.get(position));
            }
        });
        btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
