package com.rgr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rgr.data_structure.BinaryTree;
import com.rgr.factory.Factory;
import com.rgr.type.UserType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private UserType userType;
    private Factory factory;
    private BinaryTree binaryTree;
    Button deleteByIdButton;
    Button findByIdButton;
    Button insertButton;

    private final String INTEGER_FILE = "Integer.dat";
    private final String INTEGER_ARRAY_FILE = "IntegerArray.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = new Factory();
        ArrayList<String> typeNameList = factory.getTypeNameList();
        String[] types = new String[typeNameList.size()];
        for (int i = 0; i < typeNameList.size(); i++) {
            types[i] = typeNameList.get(i);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = factory.getBuilderByName(parent.getSelectedItem().toString());
                assert userType != null;
                binaryTree = new BinaryTree(userType.getTypeComparator());
                setText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deleteByIdButton = (Button) findViewById(R.id.deleteButton);
        findByIdButton = (Button) findViewById(R.id.findButton);
        insertButton = (Button) findViewById(R.id.insertButton);

        //Найти элемент по id
        findByIdButton.setOnClickListener((view) -> {
            EditText findByIdField = (EditText) findViewById(R.id.find);
            if (findByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для поиска!", Toast.LENGTH_LONG).show();
            } else {
                if (binaryTree.findNodeByIndex(Integer.parseInt(String.valueOf(findByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для поиска!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Ваш элемент с индексом:\n"
                                    + Integer.parseInt(String.valueOf(findByIdField.getText()))
                                    + " )" +
                                    binaryTree.findNodeByIndex(Integer.parseInt(String.valueOf(findByIdField.getText()))).toString()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        //Удаление элемента по id
        deleteByIdButton.setOnClickListener((view) -> {
            EditText deleteByIdField = (EditText) findViewById(R.id.delete);
            if (deleteByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для удаления!", Toast.LENGTH_LONG).show();
            } else {
                if (binaryTree.findNodeByIndex(Integer.parseInt(String.valueOf(deleteByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для удаления!", Toast.LENGTH_LONG).show();
                } else {
                    binaryTree.removeByIndex(Integer.parseInt(String.valueOf(deleteByIdField.getText())));
                    setText();
                }
            }
        });

        insertButton.setOnClickListener((view)->{
            binaryTree.insert(userType.create());
            setText();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.balanceMenu:
                balanceTree();
                return true;
            case R.id.saveMenu:
                saveTree();
                return true;
            case R.id.loadMenu:
                loadTree();
                return true;
            case R.id.clearMenu:
                clearTree();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTree() {
        BufferedWriter bufferedWriter = null;
        try {
            if (userType.typeName().equals("Integer")) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput(INTEGER_FILE, MODE_PRIVATE))));
            } else {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput(INTEGER_ARRAY_FILE, MODE_PRIVATE))));
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
            bufferedWriter.write(userType.typeName() + "\n");
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        BufferedWriter finalBufferedWriter = bufferedWriter;
        binaryTree.forEachPreOrder(el -> {
            try {
                finalBufferedWriter.write(el.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Toast.makeText(getBaseContext(), "Дерево успешно сохранено в файл!", Toast.LENGTH_LONG).show();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTree() {
        BufferedReader bufferedReader;
        try {
            if (userType.typeName().equals("Integer")) {
                bufferedReader = new BufferedReader(new InputStreamReader((openFileInput(INTEGER_FILE))));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader((openFileInput(INTEGER_ARRAY_FILE))));
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
            return;
        }
        String line;
        try {
            line = bufferedReader.readLine();
            if (line == null) {
                Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!userType.typeName().equals(line)) {
                Toast.makeText(getBaseContext(), "Неправильный формат файла!", Toast.LENGTH_LONG).show();
                return;
            }
            binaryTree = new BinaryTree(userType.getTypeComparator());

            while ((line = bufferedReader.readLine()) != null) {
                try {
                    binaryTree.insert(userType.parseValue(line));
                } catch (Exception ex) {
                    Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setText();
    }

    private void clearTree(){
        binaryTree = new BinaryTree(userType.getTypeComparator());
        setText();
    }

    private void balanceTree(){
        binaryTree = binaryTree.balance();
        setText();
    }

    private void setText() {
        TextView outTextView = (TextView) findViewById(R.id.mainText);
        outTextView.setText(binaryTree.toString());
    }
}