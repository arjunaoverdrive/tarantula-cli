package org.arjunaoverdrive.app.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class JsonParser {

    private final static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");

    public static String traverse(JsonNode root, int level) {
        StringBuilder builder = new StringBuilder();
        if (root.isObject()) {
            Iterator<String> fieldNames = root.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = root.get(fieldName);
                if(fieldName.equals("detailed") ) {
                    level--;
                }
                if(fieldName.equals("statusTime")){
                    appendTimeValueInReadableFormat(builder, fieldValue, level);
                    continue;
                }
                builder.append("\t".repeat(level) + fieldName + ": ");
                level++;

                if (fieldValue.isObject() || fieldValue.isArray()) {
                    builder.append("\n");
                } else {
                    level--;
                }
                builder.append(traverse(fieldValue, level));
            }

        } else if (root.isArray()) {
            ArrayNode arrayNode = (ArrayNode) root;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                if (arrayElement.isObject() || arrayElement.isArray()) {
                    builder.append(traverse(arrayElement, level)).append("\n");
                }
            }
        } else {
            builder.append(root);
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String parseDate(String unformatted) throws ParseException {
        String trimQuotes = unformatted.substring(1, unformatted.length() - 1);
        LocalDateTime time = LocalDateTime.parse(trimQuotes);

        return time.format(sdf);
    }

    private static void appendTimeValueInReadableFormat(StringBuilder builder, JsonNode datetime, int level){
        try {
            builder.append("\t".repeat(level))
                    .append("status time: ")
                    .append(parseDate(String.valueOf(datetime)))
                    .append("\n");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
