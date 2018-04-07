package mateuszhinc.springRestTemplate.helpers;

import java.util.List;

public class Predicates {

    public static boolean isNotEmpty(List list){
        return list!= null && !list.isEmpty();
    }

}
