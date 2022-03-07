package com.demo.util;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AppUtils {

    private static final Logger LOG = Logger.getLogger(AppUtils.class.getClass().getName());


   public static void merge(List<Integer> l1, List<Integer> l2) {
    for (int index1 = 0, index2 = 0; index2 < l2.size(); index1++) {
        if (index1 == l1.size() || l1.get(index1) > l2.get(index2)) {
            l1.add(index1, l2.get(index2++));
        }
    }
   }

    public static List<Integer> generateRandomNumber(Integer startRange, Integer endRange) {
        LOG.info("Generating random numbers for range : "+startRange+" to "+endRange);
        List<Integer> range = IntStream.range(startRange,endRange).boxed()
                .collect(Collectors.toList());
        Collections.shuffle(range);
        LOG.info("Random numbers successfully generated for range : "+startRange+" to "+endRange);
        return range;
    }

}
