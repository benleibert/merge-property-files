package org.vr.utils;

import java.io.*;
import java.util.*;

public class CombinePropertyFiles
{

    /*
        When specifying two input files, the values in the later replace any existing ones in the former.
        (I had originally intended to rely on Transifex for this functionality. When I decided to keep
        our source files on github, though, I found myself in need of a tool for this.)

        If one file is specified, it is simply sorted and written to disk. This has the advantage over
        http://textmechanic.com/text-tools/basic-text-tools/sort-text-lines/ in that it ensures consist
        whitespace between the key, value, and equals sign - and thus provides a consistent alphabetical
        sort order.
    */
    public static void main(String[] args)
    {
        String file1 = args[0];
        String file2 = (args.length == 3) ? args[1] : null;
        String outputFile = args[args.length-1];

        HashMap<String, String> map1 = stripEscapeChars(getMapFromProperties(loadPropertyFile(file1)));
        HashMap<String, String> map2 = stripEscapeChars(getMapFromProperties(loadPropertyFile(file2)));

        map1.putAll(map2);

        saveMapToPropertyFile(map1, outputFile);
        sortFile(outputFile, outputFile + "_sorted");
    }



    public static void sortFile(String inputFile, String outputFile)
    {
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String inputLine;
            List<String> lineList = new ArrayList<String>();
            while ((inputLine = bufferedReader.readLine()) != null) {
                lineList.add(inputLine);
            }
            fileReader.close();

            Collections.sort(lineList);

            FileWriter fileWriter = new FileWriter(outputFile);
            PrintWriter out = new PrintWriter(fileWriter);
            for (String outputLine : lineList) {
                out.println(outputLine);
            }
            out.flush();
            out.close();
            fileWriter.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }


    public static HashMap<String, String> stripEscapeChars(HashMap<String, String> input)
    {
        if(input == null)
            return null;

        HashMap<String, String> output = new HashMap<String, String>(input.size());
        for (String key : input.keySet()) {
            String value = input.get(key);
            output.put(stripEscapeChars(key), stripEscapeChars(value));
        }

        return output;
    }

    public static String stripEscapeChars(String input)
    {
        return input.replaceAll("\\:", ":")
                    .replaceAll("\\!", "!")
                    .replaceAll("\\#", "#")
                    .replaceAll("\\=", "=");
    }

    public static void saveMapToPropertyFile(HashMap<String, String> map, String fileName)
    {
        Properties properties = new Properties();

        for (String key : map.keySet()) {
            String value = map.get(key);
            properties.put(key, value);
        }

        try {
            properties.store(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"), "");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void saveMapToNormalFile(HashMap<String, String> map, String fileName)
    {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF-8")))
        {
            for (String key : map.keySet())
            {
                String value = map.get(key);
                writer.write(key + "=" + value + System.lineSeparator());
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void printMap(HashMap<String, String> map)
    {
        for (String key : map.keySet()) {
            String value = map.get(key);
            System.out.println(key + " = " + value );
        }
    }

    public static HashMap<String, String> getMapFromProperties(Properties properties)
    {
        HashMap<String, String> map = new HashMap<>();
        if(properties != null)
        {
            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                map.put(key, value);
            }
        }

        return map;
    }

    public static Properties loadPropertyFile(String fileName)
    {
        Properties properties = new Properties();
        if(fileName == null)
            return properties;

        try {
            //InputStream input = new FileInputStream(fileName);
            Reader reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
            //properties.load(input);
            properties.load(reader);
            return properties;
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        return null;
    }
}
