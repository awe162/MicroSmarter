package com.impinj1.microsmarter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MyunmberinputSpinnerLength extends Spinner {
	private short sp_number = 2;

	public MyunmberinputSpinnerLength(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyunmberinputSpinnerLength(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}

		// 为MyunmberinputSpinner设置adapter，主要用于显示spinner的text值
		MyunmberinputSpinnerLength.this.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return sp_number;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				TextView text = new TextView(MyunmberinputSpinnerLength.this
						.getContext());
				text.setText("2");
				text.setTextColor(getResources().getColor(
						R.color.txt_show_color));
				return text;
			}
		});
	}

	@Override
	public boolean performClick() {
		NumberPickerDialogLength tpd = new NumberPickerDialogLength(getContext(),
				new NumberPickerDialogLength.OnMyNumberSetListener() {

					@Override
					public void onNumberSet(final short number, int mode) {
						sp_number = number;
						
						// 为MyDateSpinner动态设置adapter，主要用于修改spinner的text值
						MyunmberinputSpinnerLength.this.setAdapter(new BaseAdapter() {

							@Override
							public int getCount() {
								// TODO Auto-generated method stub
								return 1;
							}

							@Override
							public Object getItem(int arg0) {
								// TODO Auto-generated method stub
								return sp_number;
							}

							@Override
							public long getItemId(int arg0) {
								// TODO Auto-generated method stub
								return 0;
							}

							@Override
							public View getView(int arg0, View arg1,
									ViewGroup arg2) {
								// TODO Auto-generated method stub
								TextView text = new TextView(
										MyunmberinputSpinnerLength.this.getContext());
								text.setText(String.format("%d", number));
								text.setTextColor(getResources().getColor(
										R.color.txt_show_color));
								return text;
							}
						});
					}
				}, sp_number, 0);

		tpd.show();
		return true;
	}
}
