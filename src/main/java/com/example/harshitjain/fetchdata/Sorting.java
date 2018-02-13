package com.example.harshitjain.fetchdata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Harshit Jain on 2/12/2018.
 */
public class Sorting implements Comparator<HashMap<String,String>> {
    public ArrayList<HashMap<String,String>> dt;
   /* public Sorting(ArrayList<HashMap<String, String>> details) {
        dt=details;

        Iterator it = details.iterator();

        while (it.hasNext()) {

            dt = (ArrayList<HashMap<String,String>>)it.next();

        }
       // Collection.sort((ArrayList<HashMap<String, String>>dt));
       // Collections.sort((ArrayList<HashMap<String, String>>)dt);
    }*/


    @Override
    public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
        int temp;
        temp=rhs.get("id").compareTo(lhs.get("id"));
        if(temp!=0) return temp;

        return temp;
    }
}
