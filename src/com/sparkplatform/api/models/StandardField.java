package com.sparkplatform.api.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;


@JsonDeserialize(using=StandardField.MapperDeserializer.class)
public class StandardField extends Base {
	@JsonProperty
	private Map<String, Field> fieldMap = new HashMap<String, StandardField.Field>();
	
	/**
	 * The design of the base class didn't anticipate results that are a loose map of results.  In
	 * this deserializer I work around jackson to explicitly create the mapping and bind it to a 
	 * class instance.
	 * 
	 * TODO I'm probably doing some of this wrong, but haven't looked much into deserializer 
	 * examples to clean up.
	 */
	public static class MapperDeserializer extends JsonDeserializer<StandardField> {
		@Override
		public StandardField deserialize(JsonParser jp,
				DeserializationContext ctxt) throws IOException,
				JsonProcessingException {
			JavaType jt = TypeFactory.mapType(Map.class, String.class, Field.class);
			DeserializerProvider deserializerProvider = ctxt.getDeserializerProvider();
			JsonDeserializer<Object> z = deserializerProvider.findTypedValueDeserializer(ctxt.getConfig(), jt);
			@SuppressWarnings("unchecked")
			Map<String, Field>o = (Map<String, Field>)z.deserialize(jp, ctxt);
			if(o == null) {
				throw new JsonMappingException("StandardField entity doesn't support the format presented", jp.getTokenLocation());
			}
			return new StandardField(o);
		}
	}
	
	public enum Type {
		Integer(Integer.class),
		Decimal(Double.class),
		Boolean(Boolean.class),
		Character(String.class);
		private Class<?> klass;
		private Type(Class<?> c){
			this.klass = c;
		}
		public Class<?> getKlass() {
			return klass;
		}
	}
	
	public static class Field extends Base {
		@JsonProperty("Searchable")
		private boolean searchable;
		@JsonProperty("Type")
		private Type type;
		@JsonProperty("ResourceUri")
		private String resourceUri;
		
		@JsonProperty("HasList")
		private boolean hasList;

		public boolean isSearchable() {
			return searchable;
		}

		public void setSearchable(boolean searchable) {
			this.searchable = searchable;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public String getResourceUri() {
			return resourceUri;
		}

		public void setResourceUri(String resourceUri) {
			this.resourceUri = resourceUri;
		}

		public boolean isHasList() {
			return hasList;
		}

		public void setHasList(boolean hasList) {
			this.hasList = hasList;
		}
		
	}
	
	public StandardField(Map<String, Field> fieldMap) {
		super();
		this.fieldMap = fieldMap;
	}

	public StandardField() {
		super();
		// Default
	}

	public void setField(String key, Field value){
		fieldMap.put(key, value);
	}
	
	public StandardField.Field getField(String key){
		return fieldMap.get(key);
	}

	@Override
	@JsonIgnore
	public void setAttribute(String key, Object value) {
		super.setAttribute(key, value);
	}
}
