package com.example.bodyboost;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class WaterActivity extends AppCompatActivity {

    private static final String WATER_AMOUNT_KEY = "water_amount";
    private static final int DAILY_GOAL = 2000; // Дневная цель потребления воды (в мл)
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    private TextView water;
    private TextView tvConsumedWater;
    private EditText editTextNumber;
    private TextView textView2;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);

        // Инициализация прогресс-бара с начальным значением
        int waterAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
        updateWaterAmountAndSave(waterAmount);

        // Инициализация элементов интерфейса
        water = findViewById(R.id.water);
        tvConsumedWater = findViewById(R.id.tvConsumedWater);
        editTextNumber = findViewById(R.id.editTextNumber);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        // Установка обработчиков событий на textView2 (добавление) и textView3 (вычитание)
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater();
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractWater();
            }
        });

        // Обновление tvConsumedWater при загрузке активности
        int consumedWaterAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
        tvConsumedWater.setText("Amount: " + consumedWaterAmount + "ml");
    }

    // Метод для добавления воды
    private void addWater() {
        String value = editTextNumber.getText().toString();
        if (!value.isEmpty()) {
            int amountToAdd = Integer.parseInt(value);
            int currentAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
            int newAmount = currentAmount + amountToAdd;
            water.setText(String.valueOf(newAmount));
            tvConsumedWater.setText("Amount: " + newAmount + "ml");

            // Обновление прогресса и сохранение данных
            updateWaterAmountAndSave(newAmount);
        }
    }

    // Метод для вычитания воды
    private void subtractWater() {
        String value = editTextNumber.getText().toString();
        if (!value.isEmpty()) {
            int amountToSubtract = Integer.parseInt(value);
            int currentAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
            int newAmount = currentAmount - amountToSubtract;
            if (newAmount < 0) {
                newAmount = 0; // чтобы избежать отрицательных значений
            }
            water.setText(String.valueOf(newAmount));
            tvConsumedWater.setText("Amount: " + newAmount + "ml");

            // Обновление прогресса и сохранение данных
            updateWaterAmountAndSave(newAmount);
        }
    }

    // Метод для получения текущего количества воды
    private int getCurrentWaterAmount() {
        return sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
    }

    private void showWaterAmountDialog() {
        final CharSequence[] items = {"+10 ml", "+20 ml", "+50 ml", "+100 ml"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Water Amount");
        builder.setItems(items, (dialog, which) -> {
            int amount = 0;
            switch (which) {
                case 0:
                    amount = 10;
                    break;
                case 1:
                    amount = 20;
                    break;
                case 2:
                    amount = 50;
                    break;
                case 3:
                    amount = 100;
                    break;
            }
            int updatedWaterAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0) + amount;

            updateWaterAmountAndSave(updatedWaterAmount);
        });
        builder.show();
    }

    private void updateWaterAmountAndSave(int waterAmount) {
        // Сохранение данных в SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER_AMOUNT_KEY, waterAmount);
        editor.apply();

        // Рассчитываем процент заполнения прогресс-бара
        float percentage = (waterAmount / (float) DAILY_GOAL) * 100;

        // Устанавливаем новое значение прогресса прогресс-бара
        progressBar.setProgress((int) percentage);

        // Обновляем UI
        updateUI(waterAmount);
    }

    private void updateUI(int waterAmount) {
        int percentage = (int) ((waterAmount / (float) DAILY_GOAL) * 100);

        TextView textViewPercentage = findViewById(R.id.textViewPercentage);

        textViewPercentage.setText(percentage + "%");

        // Обновление прогресса ProgressBar
        progressBar.setProgress(percentage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.water_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.water_del) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Обнуление данных
                        updateWaterAmountAndSave(0);
                        water.setText("0");
                        tvConsumedWater.setText("Amount: 0 ml");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Отмена действия удаления
                        dialog.dismiss();
                    }
                });
        // Создание диалогового окна и его отображение
        builder.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Сохранение данных перед выходом из активности
        int waterAmount = sharedPreferences.getInt(WATER_AMOUNT_KEY, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER_AMOUNT_KEY, waterAmount);
        editor.apply();
    }
}


