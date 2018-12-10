package com.impinj1.microsmarter;

import java.util.List;

import com.impinj1.rfidapi.*;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TagWriteFragment extends Fragment {
	private Spinner spinnerArea;
	private TextView txtEpc;
	private EditText editInput;
	private TextView txtWarnning;
	private MyunmberinputSpinnerAddress unmpOffset;
	private MyunmberinputSpinnerLength unmpLength;
	private EditText editAccesspwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tag_write_layout, container,
				false);
		spinnerArea = (Spinner) rootView.findViewById(R.id.spinnerArea);
		String[] areas = new String[] { "EPC", "USER" };

		ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
				rootView.getContext(),
				android.R.layout.simple_spinner_item, areas);
		areaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerArea.setAdapter(areaAdapter);

		final Button buttonWrite = (Button) rootView
				.findViewById(R.id.buttonWrite);
		buttonWrite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonWrite.setClickable(false);
				WriteTag();
				buttonWrite.setClickable(true);
			}
		});

		txtEpc = (TextView) rootView.findViewById(R.id.txtWriteEpc);
		editInput = (EditText) rootView.findViewById(R.id.editInputInfo);
		txtWarnning = (TextView) rootView.findViewById(R.id.txtWarnning);
		unmpOffset = (MyunmberinputSpinnerAddress) rootView
				.findViewById(R.id.myunmberinputSpinner_offset);
		unmpLength = (MyunmberinputSpinnerLength) rootView
				.findViewById(R.id.myunmberinputSpinner_length);
		editAccesspwd = (EditText) rootView.findViewById(R.id.editAccesspwd);
		return rootView;
	}

	public void WriteTag() {
		try {
			txtWarnning.setText("");

			// 先进行标签选择操作
			String maskValue = txtEpc.getText().toString();
			if (maskValue.equals(getText(R.string.txt_null).toString())) {
				// 未选择标签
				Toast.makeText(getActivity(),
						getText(R.string.info_no_sel_tag), Toast.LENGTH_SHORT)
						.show();
				return;
			}

			SingulationCriteria singulationCriteria = new SingulationCriteria();
			singulationCriteria.status = SingulationCriteriaStatus.Enabled;
			singulationCriteria.offset = 0;
			singulationCriteria.count = maskValue.length() * 4;
			singulationCriteria.match = matchType.Regular;

			for (int i = 0; i < (maskValue.length() / 2); i++) {
				singulationCriteria.mask[i] = (byte) (Short.parseShort(
						maskValue.substring(i * 2, i * 2 + 2), 16) & 0x00FF);
			}

			ctrlOperateResult result = MainActivity.myRadio
					.SetMatchCriteria(singulationCriteria);
			if (result != ctrlOperateResult.OK) {
				Toast.makeText(getActivity(), result.toString(),
						Toast.LENGTH_SHORT).show();
				return;
			}

			// 进行标签写入操作
			// 先判断输入的数据是否合法
			if (Short.parseShort(unmpLength.getSelectedItem().toString()) == 0) {
				Toast.makeText(getActivity(),
						getText(R.string.info_write_length_zero),
						Toast.LENGTH_SHORT).show();
				return;
			}

			String inputValue = editInput.getText().toString();
			if (Short.parseShort(unmpLength.getSelectedItem().toString()) != (inputValue
					.length() / 4)) {
				Toast.makeText(getActivity(),
						getText(R.string.info_write_length_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			WriteParms parms = new WriteParms();
			switch (spinnerArea.getSelectedItemPosition()) {
			case 0:
				parms.memBank = MemoryBank.EPC;
				break;

			case 1:
				parms.memBank = MemoryBank.USER;
				break;

			default:
				break;
			}

			parms.offset = Short.parseShort(unmpOffset.getSelectedItem().toString());
			parms.length = Short.parseShort(unmpLength.getSelectedItem()
					.toString());
			parms.accesspassword = Integer.parseInt(editAccesspwd.getText()
					.toString(), 16);
			short[] writeBuf ;
			// 重写pc
			if (parms.memBank == MemoryBank.EPC) {
				parms.offset = 1;
				
				writeBuf = new short[parms.length+1];
				inputValue="0000" +inputValue;
				for (int i = 0; i < writeBuf.length; i++) {
					writeBuf[i] = (short) (Integer.parseInt(
							inputValue.substring(i * 4, i * 4 + 4), 16) & 0x0000FFFF);
				}
				
				short len = (short) parms.length;
				len = (short) (len << 11);
				writeBuf[0] = len;
				parms.length++;
			}
			else {
				writeBuf = new short[parms.length];
		
			for (int i = 0; i < writeBuf.length; i++) {
				writeBuf[i] = (short) (Integer.parseInt(
						inputValue.substring(i * 4, i * 4 + 4), 16) & 0x0000FFFF);
			}
				}
			List<TagOperResult> tagInfos = MainActivity.myRadio.TagInfoWrite(
					parms, writeBuf);
			Log.i("123", "tagInfos.size() = "+tagInfos.size());
			if (tagInfos.size() > 0) {
				// 因为已经锁定过标签，因此取一个进行显示
				
				Log.i("123", "tagInfos.get(tagInfos.size() - 1).result = "+tagInfos.get(tagInfos.size() - 1).result);
				if (tagInfos.get(tagInfos.size() - 1).result == tagMemoryOpResult.Ok) {
					// 判断是否对EPC进行了改动，如果是则需要更新标签的EPC的显示，这样才能方便下次操作
					Log.i("123", "parms.memBank = "+parms.memBank);
					if (parms.memBank == MemoryBank.EPC) {
						// 判断修改的部分是否在显示区域
						Log.i("123", "parms.offset = "+(parms.offset > 1));
						Log.i("123", "parms.offset < maskValue.length() / 4 + 2 = "+(parms.offset < maskValue.length() / 4 + 2));
						if (parms.offset > 1
								&& parms.offset < maskValue.length() / 4 + 2) {
							Log.i("123", "come in ");
							int replaceLength = (parms.length * 4) > maskValue
									.length() ? maskValue.length()
									: (parms.length * 4);
							maskValue = maskValue.substring(0,
									(parms.offset - 2) * 4)
									+ inputValue.substring(0, replaceLength)
									+ maskValue.substring((parms.offset - 2)
											* 4 + replaceLength);
							// maskValue =
							// maskValue.replaceFirst(maskValue.substring((parms.offset
							// - 2) * 4, (parms.offset - 2) * 4 +
							// replaceLength), inputValue.substring(0,
							// replaceLength));

							// 更新本窗口的标签EPC
							Toast.makeText(getActivity(),
									maskValue,
									Toast.LENGTH_SHORT).show();
							txtEpc.setText(maskValue);

							// 更新读取窗口的标签EPC
							final FragmentManager fragmentManager = getActivity()
									.getFragmentManager();
							TagReadFragment objFragment = (TagReadFragment) fragmentManager
									.findFragmentById(R.id.fragment_tagRead);
							TextView txtReadEpc = (TextView) objFragment
									.getActivity()
									.findViewById(R.id.txtReadEpc);
							txtReadEpc.setText(maskValue);

							// 更新标签列表窗口中的标签EPC
							MainActivity.objFragment.updateSelItem(maskValue);
						}
					}

					Toast.makeText(getActivity(),
							getText(R.string.info_write_success),
							Toast.LENGTH_SHORT).show();
				} else {
					txtWarnning.setText(getText(R.string.info_tag_write_error));
				}
			} else {
				txtWarnning.setText(getText(R.string.info_no_tags));
			}
		} catch (radioBusyException e) {
			Toast.makeText(getActivity(), getText(R.string.info_readio_buzy),
					Toast.LENGTH_SHORT).show();
		} catch (radioFailException e) {
			Toast.makeText(getActivity(), getText(R.string.info_oper_fail),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getActivity(), getText(R.string.info_oper_fail),
					Toast.LENGTH_SHORT).show();
		}

		// 无论是否发生异常，都进行一次取消选择操作
		try {
			SingulationCriteria dissingulationCriteria = new SingulationCriteria();
			dissingulationCriteria.status = SingulationCriteriaStatus.Disabled;
			dissingulationCriteria.offset = 0;
			dissingulationCriteria.count = 0;
			dissingulationCriteria.match = matchType.Regular;

			ctrlOperateResult result = MainActivity.myRadio
					.SetMatchCriteria(dissingulationCriteria);
			if (result != ctrlOperateResult.OK) {
				Toast.makeText(getActivity(), result.toString(),
						Toast.LENGTH_SHORT).show();
			}
		} catch (radioBusyException e) {
			Toast.makeText(getActivity(), getText(R.string.info_readio_buzy),
					Toast.LENGTH_SHORT).show();
		}
	}
}
