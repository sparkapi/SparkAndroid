//
//  Copyright (c) 2013 Financial Business Systems, Inc. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

package com.sparkplatform.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.sparkplatform.api.models.Listing;

public class ListingFormatter {
	
	private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	private static final NumberFormat oneDecimalFormat = new DecimalFormat("0.0");
	
	static
	{
		currencyFormat.setMaximumFractionDigits(0);
	}

	public static String getListingTitle(Listing listing)
	{
		if(listing == null)
			return null;
		
		StringBuilder builder = new StringBuilder();
		Listing.StandardFields standardFields = listing.getStandardFields();
		String value = null;
		if((value = standardFields.getStreetNumber()) != null)
		{
			builder.append(value);
			builder.append(" ");
		}
		if((value = standardFields.getStreetDirPrefix()) != null)
		{
			builder.append(value);
			builder.append(" ");
		}
		if((value = standardFields.getStreetName()) != null)
		{
			builder.append(value);
			builder.append(" ");
		}
		if((value = standardFields.getStreetDirSuffix()) != null)
		{
			builder.append(value);
			builder.append(" ");
		}
		if((value = standardFields.getStreetSuffix()) != null)
			builder.append(value);
		
		return builder.toString();		
	}
	
	public static String getListingSubtitle(Listing listing)
	{
		if(listing == null)
			return null;
		
		StringBuilder builder = new StringBuilder();
		Listing.StandardFields standardFields = listing.getStandardFields();
		String value = null;
		if((value = standardFields.getCity()) != null)
			builder.append(value);
		if((value = standardFields.getStateOrProvince()) != null)
		{
	        if(builder.length() > 0)
	            builder.append(", ");
			builder.append(value);
		}
		if(builder.length() > 0)
			builder.append(" - ");
		if((value = standardFields.getBedsTotal()) != null)
		{
			builder.append(value);
			builder.append("br ");
		}
		Double dValue = null;
		if((dValue = standardFields.getBathsTotal()) != null)
		{
			builder.append(dValue.floatValue() - dValue.intValue() > 0 ? oneDecimalFormat.format(dValue) : dValue.intValue());
			builder.append("ba ");
		}
		if((dValue = standardFields.getListPrice()) != null)
			builder.append(formatPriceShort(dValue));
		
		return builder.toString();		
	}
	
	private static String formatPriceShort(Double price)
	{
		if(price == null || price.floatValue() <= 0)
			return null;
		
		StringBuilder builder = new StringBuilder();
		int trimmedPrice = (int)(price.floatValue() / 1000.0);
		
		if(trimmedPrice < 1000)
		{
			currencyFormat.setMaximumFractionDigits(0);
			builder.append(currencyFormat.format(trimmedPrice));
			builder.append("K");
		}
		else
		{
            float trimmedPriceFloat = (float)(trimmedPrice / 1000.0);
            if(trimmedPrice % 1000 == 0)
                currencyFormat.setMaximumFractionDigits(0);
            else
                currencyFormat.setMaximumFractionDigits(2);
			builder.append(currencyFormat.format(trimmedPriceFloat));
			builder.append("M");
		}
		
		return builder.toString();
	}
	
	public static String getListingPrimaryPhoto(Listing listing)
	{
		Listing.StandardFields standardFields = listing.getStandardFields();
		return standardFields != null && 
				standardFields.getPhotos() != null &&
				standardFields.getPhotos().size() > 0 ?
				standardFields.getPhotos().get(0).getUriThumb() : null;
	}
	
	public static List<String> getListingPhotos(JsonNode listing)
	{
		List<String> l = new ArrayList<String>();
		JsonNode standardFields = listing.get("StandardFields");
		JsonNode photos = standardFields.get("Photos");
		for(JsonNode photo : photos)
			l.add(photo.get("Uri1024").getTextValue());
		return l;
	}
}
