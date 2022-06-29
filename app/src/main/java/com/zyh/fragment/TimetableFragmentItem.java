package com.zyh.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyh.R;
import com.zyh.beans.Course;
import com.zyh.beans.LoginBean;
import com.zyh.utills.Utills;

public class TimetableFragmentItem extends Fragment {
    private final String TAG = "TimetableFragmentItem";
    public LinearLayout nowShowAddNote;
    public LinearLayout nowAddNote;
    public TimetableFragmentItem timetableFragmentItem;
    int index;
    public static TimetableFragmentItem newInstance(int index) {
        TimetableFragmentItem newFragment = new TimetableFragmentItem();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            index = args.getInt("index");
        }
        String indexStr = index+"";
        View view = inflater.inflate(R.layout.mytable, container, false);
        TextView month = (TextView) view.findViewById(R.id.table_month);
        TextView monthWord = (TextView) view.findViewById(R.id.table_month_word);
        TextView[] weekDate = new TextView[7];
        CardView[][] courseItems = new CardView[5][7];
        Course[][] courseMsgs = new Course[5][7];
        CardView[][] course2Items = new CardView[2][7];     //占两大节的课
        Course[][] course2Msgs = new Course[2][7];
        LinearLayout[] weekLinearLayout = new LinearLayout[8];
        LinearLayout[][] showAddNotes = new LinearLayout[5][7];
        LinearLayout[][] addNotes = new LinearLayout[5][7];
        CardView[][] notes = new CardView[5][7];
        TextView[][] noteNames = new TextView[5][7];
        Utills.initCourseView(view, weekDate, courseItems, courseMsgs, course2Items, course2Msgs, weekLinearLayout, showAddNotes, addNotes, notes, noteNames);

        TimetableFragment timetableFragment = Utills.getTimetableFragmeent(this);
        timetableFragmentItem = this;
        //        if(!((TimetableFragment) timetableFragment).isFinished[index]){
//            Utills.postTimetable(loginBean,timetableFragment,((TimetableFragment) timetableFragment).semester,indexStr);
//        }
//        Toast.makeText(getActivity(), "  "+((TimetableFragment) timetableFragment).courseLists.get(1), Toast.LENGTH_SHORT).show();

        Utills.showTimetable(timetableFragment, timetableFragment.thisMainActivity,
                timetableFragment.courseLists.get(index),
                month, monthWord, weekDate, courseMsgs, course2Msgs, courseItems,
                course2Items, timetableFragment.nowWeek,
                TimetableFragment.semester,
                timetableFragment.originalSemester, weekLinearLayout, showAddNotes, addNotes,timetableFragmentItem, notes, noteNames,index);
        
        return view;
    }
}