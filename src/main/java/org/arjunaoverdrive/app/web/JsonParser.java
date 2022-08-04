package org.arjunaoverdrive.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class JsonParser {
    private final ObjectMapper mapper;
    private final StringBuilder builder;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");

    public static JsonParser getJsonParser() {
        return new JsonParser();
    }

    private JsonParser() {
        this.builder = new StringBuilder();
        this.mapper = new ObjectMapper();
    }

    public String convertJsonToString(String json) {
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return traverse(root, 0);
    }

    private String traverse(JsonNode root, int level) {
        if (root.isObject()) {
            parseObject(root, level);
        } else if (root.isArray()) {
            parseArray((ArrayNode) root, level);
        } else {
            parseString(root);
        }
        return builderToString();
    }

    private String builderToString() {
        String res = builder.toString();
        builder.delete(0, builder.length());
        return res;
    }

    private void parseObject(JsonNode root, int level) {
        Iterator<String> fieldNames = root.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldValue = root.get(fieldName);
            if (fieldName.equals("detailed")) {
                level--;
            }
            if (fieldName.equals("statusTime")) {
                appendTimeValueInReadableFormat(fieldValue, level);
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
    }

    private String parseDate(String unformatted) throws ParseException {
        String trimQuotes = unformatted.substring(1, unformatted.length() - 1);
        LocalDateTime time = LocalDateTime.parse(trimQuotes);

        return time.format(dtf);
    }

    private void parseArray(ArrayNode root, int level) {
        ArrayNode arrayNode = root;
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode arrayElement = arrayNode.get(i);
            if (arrayElement.isObject() || arrayElement.isArray()) {
                builder.append(traverse(arrayElement, level)).append("\n");
            }
        }
    }

    private void parseString(JsonNode root) {
        builder.append(root);
        builder.append("\n");
    }

    private void appendTimeValueInReadableFormat(JsonNode datetime, int level) {
        try {
            builder.append("\t".repeat(level))
                    .append("status time: ")
                    .append(parseDate(String.valueOf(datetime)))
                    .append("\n");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
