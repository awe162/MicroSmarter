package com.impinj1.microsmarter;

import java.util.List;

import com.impinj1.rfidapi.*;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TagReadFragment extends Fragment {

	private Spinner spinnerArea;
	private TextView txtEpc;
	private TextView txtResult;
	private TextView txtWarnning;
	private MyunmberinputSpinnerAddress unmpOffset;
	private MyunmberinputSpinnerAddress unmpLength;
	private EditText editAccesspwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tag_read_layout, container,
				false);
		spinnerArea = (Spinner) rootView.findViewById(R.id.spinnerArea);
		String[] areas = new String[] { "EPC", "TID", "USER" };

		ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(
				rootView.getContext(),
				android.R.layout.browser_link_context_header, areas);
		areaAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
		spinnerArea.setAdapter(areaAdapter);

		final Button buttonRead = (Button) rootView
				.findViewById(R.id.buttonRead);
		buttonRead.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonRead.setClickable(false);
				ReadTag();
				buttonRead.setClickable(true);
			}
		});

		txtEpc = (TextView) rootView.findViewById(R.id.txtReadEpc);
		txtResult = (TextView) rootView.findViewById(R.id.txtReadResult);
		txtWarnning = (TextView) rootView.findViewById(R.id.txtWarnning);
		unmpOffset = (MyunmberinputSpinnerAddress) rootView
				.findViewById(R.id.myunmberinputSpinner_offset);
		unmpLength = (MyunmberinputSpinnerAddress) rootView
				.findViewById(R.id.myunmberinputSpinner_length);
		editAccesspwd = (EditText) rootView.findViewById(R.id.editAccesspwd);

		return rootView;
	}

	/**
	 * 读取标签操作
	 */
	public void ReadTag() {
		try {
			txtWarnning.setText("");
			txtResult.setText("");

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

			// 进行标作签读取操
			ReadParms parms = new ReadParms();
			switch (spinnerArea.getSelectedItemPosition()) {
			case 0:
				parms.memBank = MemoryBank.EPC;
				break;

			case 1:
				parms.memBank = MemoryBank.TID;
				break;

			case 2:
				parms.memBank = MemoryBank.USER;
				break;

			default:
				break;
			}

			parms.offset = Short.parseShort(unmpOffset.getSelectedItem()
					.toString());
			parms.length = Short.parseShort(unmpLength.getSelectedItem()
					.toString());
			parms.accesspassword = Integer.parseInt(editAccesspwd.getText()
					.toString(), 16);

			List<ReadResult> tagInfos = MainActivity.myRadio.TagInfoRead(parms);
			if (tagInfos.size() > 0) {
				// 取一个进行显示
				if (tagInfos.get(tagInfos.size() - 1).result == tagMemoryOpResult.Ok) {
					if (tagInfos.get(tagInfos.size() - 1).readData != null) {
						String readInfoString = "";
						for (int i = 0; i < tagInfos.get(tagInfos.size() - 1).readData.length; i++) {
							readInfoString += Integer.toHexString(((tagInfos.get(tagInfos.size() - 1).readData[i] >> 8) & 0x000000FF) | 0xFFFFFF00).substring(6) + Integer.toHexString((tagInfos.get(tagInfos.size() - 1).readData[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
						}

						txtResult.setText(readInfoString);
						Toast.makeText(getActivity(), readInfoString,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					txtWarnning.setText(getText(R.string.info_tag_read_error));
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

		// 无论是否发生异常，都进行一次解除选择操作
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
