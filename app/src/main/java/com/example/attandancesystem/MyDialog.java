package com.example.attandancesystem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG="addClass";
    public static final String CLASS_UPDATE_DIALOG="updateClass";
    public static final String STUDENT_ADD_DIALOG="addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "UpdateStudent";

    private onClickListener listener;
    private int roll;
    private String name;

    public MyDialog(int roll, String name) {
        this.roll = roll;
        this.name = name;
    }

    public MyDialog() {

    }

    public interface onClickListener{
        void onClick(String text1,String text2);
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG))dialog=getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog=getAddStudentDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))dialog=getUpdateClassDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog=getUpdateStudentDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView title = view.findViewById(R.id.titledialog);
        title.setText("Update student");

        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt = view.findViewById(R.id.edt02);

        roll_edt.setHint("Roll Number");
        name_edt.setHint("Student Name");

        Button cancel = view.findViewById(R.id.btn_cancel);
        Button add = view.findViewById(R.id.btn_add);
        add.setText("Update");
        roll_edt.setText(roll+"");
        roll_edt.setEnabled(false);
        name_edt.setText(name);
        cancel.setOnClickListener(V-> dismiss());
        add.setOnClickListener(V-> {
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            listener.onClick(roll,name);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView title = view.findViewById(R.id.titledialog);
        title.setText("Update class");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText section_edt = view.findViewById(R.id.edt02);

        class_edt.setHint("Class Name");
        section_edt.setHint("Subject");

        Button cancel = view.findViewById(R.id.btn_cancel);
        Button add = view.findViewById(R.id.btn_add);
        add.setText("Update");

        cancel.setOnClickListener(V-> dismiss());
        add.setOnClickListener(V-> {
            String className = class_edt.getText().toString();
            String secName = section_edt.getText().toString();
            listener.onClick(className,secName);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView title = view.findViewById(R.id.titledialog);
        title.setText("Add new student");

        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt = view.findViewById(R.id.edt02);

        roll_edt.setHint("Roll Number");
        name_edt.setHint("Student Name");

        Button cancel = view.findViewById(R.id.btn_cancel);
        Button add = view.findViewById(R.id.btn_add);

        cancel.setOnClickListener(V-> dismiss());
        add.setOnClickListener(V-> {
            String roll = roll_edt.getText().toString();
            String name = name_edt.getText().toString();
            roll_edt.setText(String.valueOf(Integer.parseInt(roll)+1));
            name_edt.setText("");
            listener.onClick(roll,name);
//            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView title = view.findViewById(R.id.titledialog);
        title.setText("Add new class");

        EditText class_edt = view.findViewById(R.id.edt01);
        EditText section_edt = view.findViewById(R.id.edt02);

        class_edt.setHint("Class Name");
        section_edt.setHint("Subject");

        Button cancel = view.findViewById(R.id.btn_cancel);
        Button add = view.findViewById(R.id.btn_add);

        cancel.setOnClickListener(V-> dismiss());
        add.setOnClickListener(V-> {
            String className = class_edt.getText().toString();
            String secName = section_edt.getText().toString();
            listener.onClick(className,secName);
            dismiss();
        });
        return builder.create();
    }
}
