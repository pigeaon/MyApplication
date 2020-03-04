package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.common.Common;
import com.example.myapplication.enums.Search;
import com.example.myapplication.enums.Tools;

public class MainActivity extends AppCompatActivity {
    public static String simpleSolve(String scrambledCube) {
        String result = new com.example.myapplication.csSolver.Search().solution(scrambledCube, 21, 100000000, 0, 0);
        System.out.println(result);
        return result;
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R'
    }

    public static void outputControl(String scrambledCube) {

        String result = new com.example.myapplication.csSolver.Search().solution(scrambledCube, 21, 100000000, 0, com.example.myapplication.csSolver.Search.APPEND_LENGTH);
        System.out.println(result);
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R' (21f)

        result = new com.example.myapplication.csSolver.Search().solution(scrambledCube, 21, 100000000, 0, com.example.myapplication.csSolver.Search.USE_SEPARATOR | com.example.myapplication.csSolver.Search.INVERSE_SOLUTION);
        System.out.println(result);
        // R  L2 D  R  F  U2 F' L  F' .  B2 D' R2 B2 R2 L2 U  F2 L2 B2 U2 R2
    }

    public static void findShorterSolutions(String scrambledCube) {
        //Find shorter solutions (try more probes even a solution has already been found)
        //In this example, we try AT LEAST 10000 phase2 probes to find shorter solutions.
        String result = new com.example.myapplication.csSolver.Search().solution(scrambledCube, 21, 100000000, 10000, 0);
        System.out.println(result);
        // L2 U  D2 R' B  U2 L  F  U  R2 D2 F2 U' L2 U  B  D  R'
    }

    public static String continueSearch(String scrambledCube) {
        //Continue to find shorter solutions
        com.example.myapplication.csSolver.Search searchObj = new com.example.myapplication.csSolver.Search();
        String result = searchObj.solution(scrambledCube, 21, 500, 0, 0);
        System.out.println(result);
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R'

        result = searchObj.next(500, 0, 0);
        System.out.println(result);
        // D2 L' D' L2 U  R2 F  B  L  B  D' B2 R2 U' R2 U' F2 R2 U' L2

        result = searchObj.next(500, 0, 0);
        System.out.println(result);
        // L' U  B  R2 F' L  F' U2 L  U' B' U2 B  L2 F  U2 R2 L2 B2

        result = searchObj.next(500, 0, 0);
        System.out.println(result);
        // Error 8, no solution is found after 500 phase2 probes. Let's try more probes.

        result = searchObj.next(500, 0, 0);

        System.out.println(result);
        return result;
        // L2 U  D2 R' B  U2 L  F  U  R2 D2 F2 U' L2 U  B  D  R'
    }



    private static final String TAG = "MainActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(MainActivity.this,"Added sucessfully",Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(MainActivity.this,"Remove Sucessfully",Toast.LENGTH_SHORT).show();
                break;
                default:
        }

        return true;
    }
    public static String testDemo( ){
        // Full initialization, which will take about 200ms.
        // The solver will be about 5x~10x slower without full initialization.
        long startTime = System.nanoTime();
        com.example.myapplication.csSolver.Search.init();
        System.out.println("Init time: " + (System.nanoTime() - startTime) / 1.0E6 + " ms");

        /** prepare scrambledCube as
         *
         *             |************|
         *             |*U1**U2**U3*|
         *             |************|
         *             |*U4**U5**U6*|
         *             |************|
         *             |*U7**U8**U9*|
         *             |************|
         * ************|************|************|************|
         * *L1**L2**L3*|*F1**F2**F3*|*R1**R2**R3*|*B1**B2**B3*|
         * ************|************|************|************|
         * *L4**L5**L6*|*F4**F5**F6*|*R4**R5**R6*|*B4**B5**B6*|
         * ************|************|************|************|
         * *L7**L8**L9*|*F7**F8**F9*|*R7**R8**R9*|*B7**B8**B9*|
         * ************|************|************|************|
         *             |************|
         *             |*D1**D2**D3*|
         *             |************|
         *             |*D4**D5**D6*|
         *             |************|
         *             |*D7**D8**D9*|
         *             |************|
         *
         * -> U1 U2 ... U9 R1 ... R9 F1 ... F9 D1 ... D9 L1 ... L9 B1 ... B9
         */
        String scrambledCube = "DUUBULDBFRBFRRULLLBRDFFFBLURDBFDFDRFRULBLUFDURRBLBDUDL";
        // scrambledCube can also be obtained by specific moves
        scrambledCube = com.example.myapplication.csSolver.Tools.fromScramble("R L2 D R F U2 F' L F' B2 D' R2 B2 R2 L2 U F2 L2 B2 U2 R2");
        System.out.println(scrambledCube);

        simpleSolve(scrambledCube);
        outputControl(scrambledCube);
        findShorterSolutions(scrambledCube);
        String an = continueSearch(scrambledCube);
        return an;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Common();
        String temp;
        long startTime = System.nanoTime();
        com.example.myapplication.csSolver.Search.init();
        System.out.println("Init time: " + (System.nanoTime() - startTime) / 1.0E6 + " ms");




        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        final TextView solveString = (TextView) findViewById(R.id.soveString);
       final TextView randomString = (TextView) findViewById(R.id.randomString);

        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scrambledCube =Tools.randomCube();
                // scrambledCube can also be obtained by specific moves
//                scrambledCube = com.example.myapplication.csSolver.Tools.fromScramble("R L2 D R F U2 F' L F' B2 D' R2 B2 R2 L2 U F2 L2 B2 U2 R2");
                System.out.println(scrambledCube);
                String ans = simpleSolve(scrambledCube);
                outputControl(scrambledCube);
                findShorterSolutions(scrambledCube);
                continueSearch(scrambledCube);

                randomString.setText(scrambledCube);
                solveString.setText(ans);
                
            }
        });

    }
}
