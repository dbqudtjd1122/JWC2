package com.example.bsyoo.jwc.camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bsyoo.jwc.R;
import com.example.bsyoo.jwc.mainimage.series.SeriesActivity;
import com.example.bsyoo.jwc.adapter.AdapterSeries;
import com.example.bsyoo.jwc.hppt.HttpCamera;
import com.example.bsyoo.jwc.model.ModelCamera;

import java.util.ArrayList;
import java.util.List;

public class CameraTabFragment2 extends CameraFragment {

    private View view = null;
    private AdapterSeries adapter;
    private List<ModelCamera> cameralist;
    private ModelCamera camera = new ModelCamera();
    private ListView listView;

    public CameraTabFragment2(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_camera_tab_fragment2, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ListView 생성
        listView = (ListView) view.findViewById(R.id.fraglist2);

        // 출력 데이터 생성
        cameralist = new ArrayList<>();

        // Adapter 생성
        adapter = new AdapterSeries(getContext(), R.layout.listitem_series, R.id.series_name, cameralist);

        // 리스트뷰에 어댑터 설정
        listView.setAdapter(adapter);
        new CameraTabFragment2.getCameraList().execute("녹화기");



        // 아이템 클릭 이벤트 (camera 모델값을 넘겨준다.)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SeriesActivity.class);
                camera = cameralist.get(position);
                intent.putExtra("series", camera.getOnlineseries().toString());
                startActivity(intent);
            }
        });
    }

    // Http List DB 가져오기
    public class getCameraList extends AsyncTask<String, Integer, List<ModelCamera>> {

        private ProgressDialog waitDlg = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitDlg = new ProgressDialog(getContext());
            waitDlg.setMessage("녹화기 리스트를 가져오는중 입니다.");
            waitDlg.show();
        }

        @Override
        protected List<ModelCamera> doInBackground(String... params) {

            try {
                cameralist = new HttpCamera().getCameraList(params[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return cameralist;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<ModelCamera> list) {
            super.onPostExecute(list);
            // 1.
            cameralist = list;
            adapter.clear();
            adapter.addAll(cameralist);
            adapter.notifyDataSetChanged();

            if (waitDlg != null) {
                waitDlg.dismiss();
                waitDlg = null;
            }
        }
    }
}
