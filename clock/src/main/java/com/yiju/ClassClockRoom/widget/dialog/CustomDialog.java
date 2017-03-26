package com.yiju.ClassClockRoom.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.common.callback.IOnClickListener;
import com.yiju.ClassClockRoom.util.UIUtils;

public class CustomDialog {
	private AlertDialog.Builder alertDialog;
	private IOnClickListener listener;

	/**
	 * @param context
	 * 上下文
	 * @param affirm
	 * 左边String
	 * @param cancel
	 * 右边
	 * @param msg
	 * 内容
	 */
	public CustomDialog(Context context, String affirm,String cancel,String msg) {
		alertDialog = new AlertDialog.Builder(context);
		alertDialog
				.setMessage(msg)
				.setPositiveButton(affirm, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(listener != null){
							listener.oncClick(true);
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if(listener != null){
									listener.oncClick(false);
								}
								dialog.dismiss();
							}
						}).create().show();
	}
	/**
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param msg
	 *            消息
	 */
	public CustomDialog(Context context, String title, String msg,boolean isCancel) {
		alertDialog = new AlertDialog.Builder(context);
		alertDialog
				.setMessage(msg)
				.setTitle(title)
				.setPositiveButton(UIUtils.getString(R.string.confirm), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(listener != null){
							listener.oncClick(true);
						}
						dialog.dismiss();
					}
				}).create().show();
	}
	
	/**
	 * 
	 * @param context
	 *            上下文
	 * @param msg
	 *            消息
	 */
	public CustomDialog(Context context, String msg) {
		alertDialog = new AlertDialog.Builder(context);
		alertDialog
				.setMessage(msg)
				.setPositiveButton(UIUtils.getString(R.string.confirm), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(listener != null){
							listener.oncClick(true);
						}
						dialog.dismiss();
					}
				}).create().show();
	}
	

	public void setOnClickListener(IOnClickListener listener) {
		this.listener = listener;
	}
}
