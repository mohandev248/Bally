package com.demo.util;

import com.demo.constant.AppConstants;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileUtils {

   private static final Logger LOG = Logger.getLogger(FileUtils.class.getClass().getName());

    public static boolean writeToFile(List<Integer> records,String filePath) throws Exception {

        LOG.info("writing records in file with record count : "+records.size());

       return writeBuffered(records, 4 * (int) AppConstants.MEG,filePath);

    }



    private static boolean writeBuffered(List<Integer> records, int bufSize,String filePath)  {
        File file = null;
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        boolean writeSuccess = false;
        try {
            file = new File(filePath);
            writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer, bufSize);
            LOG.info("Writing buffered (buffer size: " + bufSize + ")... ");
            write(records, bufferedWriter);
            writeSuccess = true;
        } catch (IOException e) {
            writeSuccess = false;
            LOG.severe("Error in writing file, Error Msg :"+e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                LOG.severe("Error in closing bufferedWriter, Error Msg :"+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        return writeSuccess;
    }

    private static void write(List<Integer> records, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        String num = null;
        for (int i=0;i<records.size();i++) {
            num = String.valueOf(records.get(i));
            if(i==records.size()-1){
                writer.write(num);
            }else {
                writer.write(num+System.lineSeparator());
            }
        }
        writer.close();
        long end = System.currentTimeMillis();
        LOG.info("Writing done in "+(end - start) / 1000f + " seconds");
    }

    public static List<Integer> readFileRecords(long start,long end,File file) {

        List<Integer> records = null;
        try {
            records = new ArrayList<>();
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(start);

            while (raf.getFilePointer() < end) {
                String line = raf.readLine();
                if (line == null) {
                    continue;
                }
                records.add(Integer.parseInt(line));
            }

            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  records;
    }



    public static long[] getFileOffsetsForChunks(File file, int chunks) {

        try {
            long[] offsets = new long[chunks];

            RandomAccessFile raf = new RandomAccessFile(file, "r");
            for (int i = 1; i < chunks; i++) {
                raf.seek(i * file.length() / chunks);

                while (true) {
                    int read = raf.read();
                    if (read == '\n' || read == -1) {
                        break;
                    }
                }

                offsets[i] = raf.getFilePointer();
            }
            raf.close();
            return  offsets;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;

    }
}
