package com.sparkplatform.api;

/**
 * All the documented API response codes.
 */
public enum ApiParameter {
	// Search
	_filter,
	_expand,
	_select,
	// Pagination
	_pagination,
	_limit,
	_page,
	_orderby,
	// Market Stats
	LocationField,
	LocationValue,
	Options,
	PropertyTypeCode,
	// Hotsheet
	HotSheet,
	OpenHouses,
	// Nearby
	Lat,
	Lon,
	// Api client fundamentals
	ApiKey,
	ApiSig,
	AuthToken,
	ApiUser;

}
