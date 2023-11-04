package me.merunko.holocraft.TopSubmittedItems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopSubmittedItems {
    public void updateTopItems(Logger logger) {
        Pattern logEntryPattern = Pattern.compile("\\[(\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})] (.+) submitted (\\d+) (.+), and gained (\\d+) point value.");
        Map<String, Integer> submittedItemsCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("plugins/Submitter/logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = logEntryPattern.matcher(line);
                if (matcher.find()) {
                    String itemName = matcher.group(4);
                    int submittedCount = Integer.parseInt(matcher.group(3));

                    submittedItemsCount.put(itemName, submittedItemsCount.getOrDefault(itemName, 0) + submittedCount);
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred while trying to read logs.txt file.");
        }

        TreeMap<Integer, String> sortedItemsCount = new TreeMap<>((a, b) -> b - a);

        submittedItemsCount.forEach((itemName, count) -> sortedItemsCount.put(count, itemName));

        try (PrintWriter writer = new PrintWriter("plugins/Submitter/topItem.txt")) {
            writer.println("Top Submitted Items:");
            int position = 1;
            for (Map.Entry<Integer, String> entry : sortedItemsCount.entrySet()) {
                String itemName = entry.getValue();
                int itemCount = entry.getKey();
                writer.println(position + ": " + itemName + " - " + itemCount + " times submitted");
                position++;
                if (position > 10) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred while trying to store top items to topItem.txt file.");
        }
    }
}
