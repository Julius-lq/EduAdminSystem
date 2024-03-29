package com.zyh.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyh.R;
import com.zyh.beans.ExamBean;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
    private final List<ExamBean.Exam> mExam;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView examYearText;
        TextView examMonthText;
        TextView examDayText;
        TextView examNameText;
        TextView examStartTimeText;
        TextView examEndTimeText;
        TextView examAddress;

        public ViewHolder(View view) {
            super(view);
            examYearText = (TextView) view.findViewById(R.id.exam_year);
            examMonthText = (TextView) view.findViewById(R.id.exam_month);
            examDayText = (TextView) view.findViewById(R.id.exam_day);
            examNameText = (TextView) view.findViewById(R.id.exam_name);
            examStartTimeText = (TextView) view.findViewById(R.id.exam_start_time);
            examEndTimeText = (TextView) view.findViewById(R.id.exam_end_time);
            examAddress = (TextView) view.findViewById(R.id.exam_address);
        }
    }

    public ExamAdapter(List<ExamBean.Exam> examList) {
        mExam = examList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.exam_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        6 25sp,12 22sp
        ExamBean.Exam exam = mExam.get(position);
        String[] startDateTime = exam.getStartTime().split(" ");
        String[] endDateTime = exam.getEndTime().split(" ");
        String[] startDate = startDateTime[0].split("-");
        String year = startDate[0];
        String month = startDate[1];
        String day = startDate[2];
        String startTime = startDateTime[1];
        String endTime = endDateTime[1];
        holder.examYearText.setText(year);
        holder.examMonthText.setText(month);
        holder.examDayText.setText(day);
//        holder.examNameText.setText(exam.getCourseName());
        holder.examStartTimeText.setText(startTime);
        holder.examEndTimeText.setText(endTime);
        holder.examAddress.setText(exam.getAddress());

        TextView examNameText = holder.examNameText;
        String courseName = exam.getCourseName();
        if (courseName.length() >= 12) {
            examNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        } else if (courseName.length() >= 6) {
            examNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
        examNameText.setText(courseName);
    }

    @Override
    public int getItemCount() {
        if (mExam == null) {
            return 0;
        }
        return mExam.size();
    }
}
