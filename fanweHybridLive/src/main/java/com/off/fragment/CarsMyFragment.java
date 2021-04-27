package com.off.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.off.appview.LiveMallCarsTabMyView;

/**
 * 我的座驾
 */
public class CarsMyFragment extends BaseFragment {


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new LiveMallCarsTabMyView(container.getContext());
    }

  /*  @ViewInject(R.id.rvCars)
    private RecyclerView rvCars;

    @ViewInject(R.id.svgaAnim)
    private SVGAImageView svgaAnim;


    private List<CarsBean> mListModel = new ArrayList<>();
    private MyCarAdapter mAdapter;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_cars_mall;
    }


    @Override
    protected void init() {
        super.init();

        mAdapter = new MyCarAdapter(mListModel, new MyCarAdapter.ClickInterface() {
            @Override
            public void play(int position) {
                svgaAnim.startAnimation();
            }

            @Override
            public void select(int position) {
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCars.setLayoutManager(layoutManager);
        rvCars.setAdapter(mAdapter);
        getMyCars();

    }

    private void getMyCars() {
        CommonInterface.requestMyCars(new AppRequestCallback<CarsModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                Gson gson = new Gson();
                CarsModel result = gson.fromJson(sdResponse.getResult(), CarsModel.class);
                mListModel.addAll(result.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }*/

}
