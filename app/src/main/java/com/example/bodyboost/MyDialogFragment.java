package com.example.bodyboost;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Создаем диалоговое окно без заголовка
        Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Устанавливаем макет диалогового окна
        dialog.setContentView(R.layout.activity_my_dialog_fragment);

        // Получаем ширину и высоту экрана
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);

        // Устанавливаем ширину и высоту диалогового окна
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(width, height);
        }

        // Разрешаем закрытие диалогового окна при касании вне его
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_dialog_fragment, container, false);

        TextView messageTextView = view.findViewById(R.id.messageTextView);

        // Устанавливаем текст сообщения
        messageTextView.setText("Статистика за выбранный период");

        return view;
    }
}