package net.yazsoft.frame.utils;

import org.springframework.stereotype.Component;

/**
 * Created by fec on 21/03/16.
 */
@Component
public class Tools {

    /**
     * Compares two objects that are Strings on their int value. Can be used to sort any column that contains Integer-based data.
     * @param obj1
     * @param obj2
     * @return
     */
    public int sortIdByString(Object obj1,Object obj2){
        int id1 = Integer.parseInt((String)obj1);
        int id2 = Integer.parseInt((String)obj2);
        if(id1 < id2){
            return -1;
        }else if(id1 == id2){
            return 0;
        }else{
            return 1;
        }
    }
}
