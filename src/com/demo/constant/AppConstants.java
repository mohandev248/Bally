package com.demo.constant;

public interface AppConstants {

    String START_RANGE_KEY = "SR";
    String END_RANGE_KEY = "ER";
    String RANDOM_FILEPATH_KEY = "FP";
    String SORTED_FILEPATH_KEY = "SFP";
    String FILE_CHUNK_KEY = "CK";
    double MEG = (Math.pow(1024, 2));

     interface PropertyFile {
        String PROPERTY_FILE_NAME = "src/resources/app.properties";
        String START_RANGE_KEY = "app.random.range.start";
        String END_RANGE_KEY = "app.random.range.end";
        String RANDOM_FILEPATH_KEY = "app.random.file.path";
        String FILE_CHUNK_KEY = "app.file.chunks";
        String SORTED_FILEPATH_KEY = "app.sorted.file.path";
    }


}
