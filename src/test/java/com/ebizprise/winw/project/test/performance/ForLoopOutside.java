package com.ebizprise.winw.project.test.performance;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.ebizprise.winw.project.base.vo.BaseFormVO;

public class ForLoopOutside {

	public ForLoopOutside (int howManySeconds) {
		Runtime runtime;
		NumberFormat format = NumberFormat.getInstance();
		
		long sumMemory = 0;
        List<Object> outside = null;
		LocalTime start = LocalTime.now().plusSeconds(howManySeconds);
    	
		while (true) {
    	    outside = new ArrayList<>();
    	    outside.add(new BaseFormVO());
    	    outside.add(new JSONObject());
    	    outside.add(new HashMap<>());
            outside.add(new BaseFormVO());
            outside.add(new JSONObject());
            outside.add(new HashMap<>());
            
    		runtime = Runtime.getRuntime();
    		long allocatedMemory = runtime.totalMemory();
    		sumMemory += allocatedMemory;
    		
    		if (LocalTime.now().isAfter(start)) {
    			break;
    		}
    	}

		System.out.println("SumMemory memory of ForLoopOutside: " + format.format(sumMemory / 1024));
	}

	public static void main (String[] args) {
		new ForLoopOutside(5);
	}
	
}
