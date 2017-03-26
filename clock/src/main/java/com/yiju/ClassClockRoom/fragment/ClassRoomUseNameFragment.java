package com.yiju.ClassClockRoom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yiju.ClassClockRoom.R;
import com.yiju.ClassClockRoom.adapter.ClassRoomTypeUseNameAdapter;
import com.yiju.ClassClockRoom.bean.ClassroomArrangeData;

import java.util.List;

/**
 * 课室用途选择页_小类(ex:全科)
 * use_name fragment
 * Created by wh on 2016/5/16.
 */
public class ClassRoomUseNameFragment extends Fragment {

    public static int RESULT_CODE_FROM_CLASS_ROOM_USE_NAME_FRG = 2;
    private View rootView;
    private ListView lv_classroom_type;
    private List<ClassroomArrangeData.UseEntity> use;
    private String use_name;
    private int puse_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            use = (List<ClassroomArrangeData.UseEntity>) bundle.getSerializable("use");
            use_name = bundle.getString("use_name");
            puse_id = bundle.getInt("puse_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_classroom_type, container, false);
        initMyView();
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMyData();
    }

    private void initMyView() {
        if (rootView == null) {
            return;
        }
        lv_classroom_type = (ListView) rootView.findViewById(R.id.lv_classroom_type);
        lv_classroom_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (use == null) {
                    return;
                }
                Intent intent = getActivity().getIntent();
                intent.putExtra("use_entity", use.get(position));
                intent.putExtra("puse_id", puse_id);
                getActivity().setResult(RESULT_CODE_FROM_CLASS_ROOM_USE_NAME_FRG, intent);
                getActivity().finish();
            }
        });
    }

    private void initMyData() {
        if (use == null) {
            return;
        }
        for (int i = 0; i < use.size(); i++) {
            if (use_name.equals(use.get(i).getUse_name())) {
                use.get(i).setIsSelect(true);
            }
        }
        lv_classroom_type.setAdapter(new ClassRoomTypeUseNameAdapter(use));
    }
}
