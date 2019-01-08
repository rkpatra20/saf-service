package com.monamitech.task.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static String toString(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("SAF:failed to convert the obejct into json", e);
		}
	}

	public static <T> T toObject(String json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("SAF:failed to convert to obejct from json", e);
		}
	}
}
