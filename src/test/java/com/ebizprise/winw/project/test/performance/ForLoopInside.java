package com.ebizprise.winw.project.test.performance;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

public class ForLoopInside {

    public ForLoopInside(int howManySeconds) {
        Runtime runtime;
        NumberFormat format = NumberFormat.getInstance();

        long sumMemory = 0;
        LocalTime start = LocalTime.now().plusSeconds(howManySeconds);

        while (true) {
            List<Object> inside = new ArrayList<>();
            inside.add(new BaseFormVO());
            inside.add(new JSONObject());
            inside.add(new HashMap<>());
            inside.add(new BaseFormVO());
            inside.add(new JSONObject());
            inside.add(new HashMap<>());

            runtime = Runtime.getRuntime();
            long allocatedMemory = runtime.totalMemory();
            sumMemory += allocatedMemory;

            if (LocalTime.now().isAfter(start)) {
                break;
            }
        }

        System.out.println("SumMemory memory of ForLoopInside: " + format.format(sumMemory / 1024));
    }

    public static void main (String[] args) {
        new ForLoopInside(5);
    }

}
