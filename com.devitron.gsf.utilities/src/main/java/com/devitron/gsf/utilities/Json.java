package com.devitron.gsf.utilities;

import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Json {

    public static Object jsonToObject(String json, Class objectClass) throws UtilitiesJsonParseException {

        Object object = null;

        try {
            Gson gson = new Gson();
            object = gson.fromJson(json, objectClass);

        } catch (JsonSyntaxException e) {
            throw new UtilitiesJsonParseException(e);
        }
        return object;

    }

    public static String objectToJson(Object o) {

        String json = null;

        Gson gson = new Gson();
        json = gson.toJson(o);

        return json;
    }


}
