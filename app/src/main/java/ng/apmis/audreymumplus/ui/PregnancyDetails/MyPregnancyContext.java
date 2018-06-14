package ng.apmis.audreymumplus.ui.PregnancyDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ng.apmis.audreymumplus.R;

public class MyPregnancyContext extends Fragment{
    MyPregnancyAdapter pregnancyAdapter;
    ArrayList<MyPregnancyModel> pregnancyModelArrayList = new ArrayList<>();
    private static String CLASSNAME = "MOM plus";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypregnancy_list,container,false);

        pregnancyAdapter = new MyPregnancyAdapter(getActivity());
        pregnancyModelArrayList.add(new MyPregnancyModel("0","YOUR BABY’S PROGRESS","Bigger baby, heavier baby !","Your growing baby now measures about 4 inches long, crown to rump, and weighs in at about 2 1/2 ounces (about the size of an apple). Her legs are growing longer than her arms now, and she can move all of her joints and limbs. Although her eyelids are still fused shut, she can sense light.",""));
        pregnancyModelArrayList.add(new MyPregnancyModel("1","WEEKLY TIPS   (WEEK 15)","Eating Healthy","Try fortified ready-to-eat or cooked breakfast cereals with fruit. Fortified cereals have added nutrients, like calcium.\n","Try fortified ready-to-eat or cooked breakfast cereals with fruit. Fortified cereals have added nutrients, like calcium.\n"));
        RecyclerView recyclerView = rootView.findViewById(R.id.chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(pregnancyAdapter);
        pregnancyAdapter.setAllChats(pregnancyModelArrayList);
        return rootView;
    }
}
