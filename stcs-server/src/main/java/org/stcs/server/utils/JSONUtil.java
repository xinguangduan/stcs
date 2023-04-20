package org.stcs.server.utils;


import lombok.experimental.UtilityClass;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
@UtilityClass
public class JSONUtil {
    public static final String VALIDATE_RESULT = "validateResult";
    public static final String VALIDATE_MESSAGE = "message";
    public static final String VALIDATE_SUCCESS = "SUCCESS";
    public static final String VALIDATE_FAILED = "FAILED";

    /**
     * @param jsonString - The string which to be validate
     * @return true - if the string is valid json
     * false - if the string is NOT valid json
     */
    public static boolean isStringValidJSON(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    /**
     * @param schema     - Json schema
     * @param jsonObject - The JSON object which will be validated
     * @return A JSON object which contains validate result and message
     */
    public static JSONObject validateJSONWithSchema(String schema, JSONObject jsonObject) {
        JSONObject resultObject = new JSONObject();

        if (null == schema || !isStringValidJSON(schema)) {
            resultObject.put(VALIDATE_RESULT, VALIDATE_FAILED);
            resultObject.put(VALIDATE_MESSAGE, "The inputted json schema is invalid, please check!");

            return resultObject;
        }

        if (null == jsonObject) {
            resultObject.put(VALIDATE_RESULT, VALIDATE_FAILED);
            resultObject.put(VALIDATE_MESSAGE, "The inputted json object is null, please check!");

            return resultObject;
        }

        JSONObject schemaObject = new JSONObject(new JSONTokener(schema));
        Schema jsonSchema = SchemaLoader.builder()
                .schemaJson(schemaObject).build().load().build();

        try {
            jsonSchema.validate(jsonObject);

            resultObject.put(VALIDATE_RESULT, VALIDATE_SUCCESS);
            resultObject.put(VALIDATE_MESSAGE, "Validate is success!");
        } catch (ValidationException e) {
            resultObject = e.toJSON();
            resultObject.put(VALIDATE_RESULT, VALIDATE_FAILED);
        } finally {
            return resultObject;
        }
    }

    public static void main(String[] args) {
        String schema = "{\n" +
                "    \"$schema\": \"http://json-schema.org/draft-07/schema\",\n" +
                "    \"$id\": \"http://example.com/root.json\",\n" +
                "    \"type\": \"object\",\n" +
                "    \"title\": \"The Root Schema\",\n" +
                "    \"description\": \"The root schema is the schema that comprises the entire JSON document.\",\n" +
                "    \"default\":\n" +
                "    {},\n" +
                "    \"required\":\n" +
                "    [\n" +
                "        \"checked\",\n" +
                "        \"dimensions\",\n" +
                "        \"id\",\n" +
                "        \"name\",\n" +
                "        \"price\",\n" +
                "        \"tags\"\n" +
                "    ],\n" +
                "    \"properties\":\n" +
                "    {\n" +
                "        \"checked\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/checked\",\n" +
                "            \"type\": \"boolean\",\n" +
                "            \"title\": \"The Checked Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\": false,\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                false\n" +
                "            ]\n" +
                "        },\n" +
                "        \"dimensions\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/dimensions\",\n" +
                "            \"type\": \"object\",\n" +
                "            \"title\": \"The Dimensions Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\":\n" +
                "            {},\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                {\n" +
                "                    \"height\": 10.0,\n" +
                "                    \"width\": 5.0\n" +
                "                }\n" +
                "            ],\n" +
                "            \"required\":\n" +
                "            [\n" +
                "                \"width\",\n" +
                "                \"height\"\n" +
                "            ],\n" +
                "            \"properties\":\n" +
                "            {\n" +
                "                \"width\":\n" +
                "                {\n" +
                "                    \"$id\": \"#/properties/dimensions/properties/width\",\n" +
                "                    \"type\": \"integer\",\n" +
                "                    \"title\": \"The Width Schema\",\n" +
                "                    \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "                    \"default\": 0,\n" +
                "                    \"examples\":\n" +
                "                    [\n" +
                "                        5\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"height\":\n" +
                "                {\n" +
                "                    \"$id\": \"#/properties/dimensions/properties/height\",\n" +
                "                    \"type\": \"integer\",\n" +
                "                    \"title\": \"The Height Schema\",\n" +
                "                    \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "                    \"default\": 0,\n" +
                "                    \"examples\":\n" +
                "                    [\n" +
                "                        10\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"id\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/id\",\n" +
                "            \"type\": \"integer\",\n" +
                "            \"title\": \"The Id Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\": 0,\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                1\n" +
                "            ]\n" +
                "        },\n" +
                "        \"name\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/name\",\n" +
                "            \"type\": \"string\",\n" +
                "            \"title\": \"The Name Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\": \"\",\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                \"A green door\"\n" +
                "            ]\n" +
                "        },\n" +
                "        \"price\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/price\",\n" +
                "            \"type\": \"number\",\n" +
                "            \"title\": \"The Price Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\": 0,\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                12.5\n" +
                "            ]\n" +
                "        },\n" +
                "        \"tags\":\n" +
                "        {\n" +
                "            \"$id\": \"#/properties/tags\",\n" +
                "            \"type\": \"array\",\n" +
                "            \"title\": \"The Tags Schema\",\n" +
                "            \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "            \"default\":\n" +
                "            [],\n" +
                "            \"examples\":\n" +
                "            [\n" +
                "                [\n" +
                "                    \"home\",\n" +
                "                    \"green\"\n" +
                "                ]\n" +
                "            ],\n" +
                "            \"items\":\n" +
                "            {\n" +
                "                \"$id\": \"#/properties/tags/items\",\n" +
                "                \"type\": \"string\",\n" +
                "                \"title\": \"The Items Schema\",\n" +
                "                \"description\": \"An explanation about the purpose of this instance.\",\n" +
                "                \"default\": \"\",\n" +
                "                \"examples\":\n" +
                "                [\n" +
                "                    \"home\",\n" +
                "                    \"green\"\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String json = "{\n" +
                "    \"json\":\n" +
                "    {\n" +
                "        \"checked\": false,\n" +
                "        \"dimensions\":\n" +
                "        {\n" +
                "            \"width\": 5,\n" +
                "            \"height\": 10\n" +
                "        },\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"A green door\",\n" +
                "        \"price\": 12.5,\n" +
                "        \"tags\":\n" +
                "        [\n" +
                "            \"home\",\n" +
                "            \"green\",\n" +
                "            \"yellow\"\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        long start = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject result = validateJSONWithSchema(schema, jsonObject);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(result);

    }
}