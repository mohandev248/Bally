package com.demo.main;

import com.demo.constant.AppConstants;
import com.demo.exception.AppException;
import com.demo.util.AppUtils;
import com.demo.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class RandomNumberApplication {

    private static final Logger LOG = Logger.getLogger(RandomNumberApplication.class.getClass().getName());

    private static final Map<String,String> propertyMap;

    static{
        LOG.info("Loading Properties file");
        propertyMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(AppConstants.PropertyFile.PROPERTY_FILE_NAME);
            Properties properties = new Properties();
            properties.load(reader);
            propertyMap.put(AppConstants.START_RANGE_KEY,properties.getProperty(AppConstants.PropertyFile.START_RANGE_KEY));
            propertyMap.put(AppConstants.END_RANGE_KEY,properties.getProperty(AppConstants.PropertyFile.END_RANGE_KEY));
            propertyMap.put(AppConstants.RANDOM_FILEPATH_KEY,properties.getProperty(AppConstants.PropertyFile.RANDOM_FILEPATH_KEY));
            propertyMap.put(AppConstants.FILE_CHUNK_KEY,properties.getProperty(AppConstants.PropertyFile.FILE_CHUNK_KEY));
            propertyMap.put(AppConstants.SORTED_FILEPATH_KEY,properties.getProperty(AppConstants.PropertyFile.SORTED_FILEPATH_KEY));
            LOG.info("Properties File loaded successfully");
        } catch (FileNotFoundException e) {
            LOG.severe("Error in loading property file Error Msg :"+e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LOG.severe("Error in loading property file Error Msg :"+e.getLocalizedMessage());
            e.printStackTrace();
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        List<Integer> randomList = null;
        boolean fileWriteSuccess = false;
        String filePath = null;

        try {
            if (propertyMap.isEmpty()) {
                LOG.info("Exiting application Reason : Properties not loaded ");
                return;
            }

            filePath = propertyMap.get(AppConstants.RANDOM_FILEPATH_KEY);
            //Step 1 : Get Random Number List
            randomList = AppUtils.generateRandomNumber(Integer.parseInt(propertyMap.get(AppConstants.START_RANGE_KEY))
                    , Integer.parseInt(propertyMap.get(AppConstants.END_RANGE_KEY)));

            //Step 2 : Write to a File
            fileWriteSuccess = FileUtils.writeToFile(randomList, filePath);

            //Step 3 : Check if file is written successfully
            if (!fileWriteSuccess) {
                LOG.severe("Exiting application Reason : File Writing Failed ");
                return;
            }
            LOG.info("Random File writing success at location : "+filePath);

            try {

                //Step 4 : Read from file in Chunks and Merge each after sorting
                LOG.info("Reading file from location : "+filePath);
                int chunks = Integer.parseInt(propertyMap.get(AppConstants.FILE_CHUNK_KEY));
                LOG.info("Total chunks : "+chunks);
                File file = new File(filePath);
                List<Integer> chunkRecords = null;
                List<Integer> mergedSortedRecords = new ArrayList<>();
                long[] offsets = FileUtils.getFileOffsetsForChunks(file, chunks);
                long start = 0;
                long end = 0;
                for (int i = 0; i < chunks; i++) {
                    LOG.info("Reading chunk number : "+(i+1));
                    start = offsets[i];
                    end = i < chunks - 1 ? offsets[i + 1] : file.length();
                    chunkRecords = FileUtils.readFileRecords(start, end, file);
                    LOG.info("Successfully read chunk number : "+(i+1));
                    LOG.info("Sorting chunk number : "+(i+1));
                    Collections.sort(chunkRecords);
                    LOG.info("Merging chunk number : "+(i+1));
                    AppUtils.merge(mergedSortedRecords, chunkRecords);
                }


                LOG.info("Reading, Sorting and Merging done for total chunks : "+chunks);
                //Step 5 : Writing sorted records in to a different file
                fileWriteSuccess = FileUtils.writeToFile(mergedSortedRecords, propertyMap.get(AppConstants.SORTED_FILEPATH_KEY));

                if (!fileWriteSuccess) {
                    LOG.severe("Exiting application Reason : Sorted File Writing Failed ");
                    return;
                }
                LOG.info("File Writing done for sorted file at location : "+propertyMap.get(AppConstants.SORTED_FILEPATH_KEY));
                LOG.info("Terminating Application : Exit Code : Success");
            } catch (Exception ex) {
                LOG.severe("Exception occured while running application : Exception message :" + ex.getMessage());
                throw new AppException("Application failed due to " + ex.getLocalizedMessage());
            }

        }
        catch (AppException appException){
            LOG.severe("Terminating Application : Exit Code : Failure");
        }


    }



}
