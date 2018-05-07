package by.ras.entity;

import java.util.HashMap;
import java.util.Map;

public enum Occupation {
    STUDENT, UNEMPLOYED, EMPLOYED, HOUSEWIFE;


    private static final Map<Occupation, String> occupations = new HashMap<Occupation, String>(){
        {
          put(Occupation.STUDENT, "STUDENT");
          put(Occupation.UNEMPLOYED, "UNEMPLOYED");
          put(Occupation.EMPLOYED, "EMPLOYED");
          put(Occupation.HOUSEWIFE, "HOUSEWIFE");
        }
    };
}
