package org.easy.ecm.service;

public class Constants {

	public static final String DEFAULT_WORKSPACE = "EasyECM";
	public static final String PATH_SEPARATOR = "/";
	
	
	public static final String ERROR_CODE_NOT_AUTHORIZED = "401";
	public static final String ERROR_MESSAGE_NOT_AUTHORIZED = "Not authorized";

	public static final String ERROR_CODE_NOT_ALLOWED = "405";
	public static final String ERROR_MESSAGE_NOT_ALLOWED = "Method not allowed";

	public static final String ERROR_CODE_MESSAGE_FORMAT_INVALID = "600";
	public static final String ERROR_CODE_INVALID_JSON = "601";
	public static final String ERROR_CODE_BAD_REQUEST = "602";

	public static final String ERROR_MESSAGE_NULL_POINTER_ENCOUNTERED = "We have encountered a problem during processing your request.";

	public static final String ERROR_MESSAGE_MESSAGE_FORMAT_INVALID = "One of the required IDs is not set";

	public static final String ERROR_MESSAGE_BAD_REQUEST = "Request could not be completed";

	public static final String ERROR_CODE_INTERNAL_ERROR = "703";

	public static final String ERROR_CODE_NUMBER_FORMAT_INVALID = "700";
	public static final String ERROR_CODE_NULL_POINTER_ENCOUNTERED = "701";

	public static final String ERROR_CODE_INVALID_MESSAGE_STRUCTURE = "702";

	public static final int ERROR_CODE_RESOURCE_DUPLICATE_INT = 798;
	public static final String ERROR_CODE_RESOURCE_DUPLICATE = "798";
	public static final String ERROR_MESSAGE_RESOURCE_DUPLICATE = "Resource already exists on the server";

	public static final int ERROR_CODE_RESOURCE_NOT_FOUND_INT = 799;
	public static final String ERROR_CODE_RESOURCE_NOT_FOUND = "799";
	public static final String ERROR_MESSAGE_RESOURCE_NOT_FOUND = "Resource was not found on the server";

	public static final int SUCCESS_CODE_CREATED_INT = 800;
	public static final String SUCCESS_CODE_CREATED = "800";
	public static final String SUCCESS_MESSAGE_CREATED = "Resource has been created on the server";

	public static final int SUCCESS_CODE_REMOVED_INT = 801;
	public static final String SUCCESS_CODE_REMOVED = "801";
	public static final String SUCCESS_MESSAGE_REMOVED = "Resource has been removed from the server";

	public static final int SUCCESS_CODE_UPDATED_INT = 802;
	public static final String SUCCESS_CODE_UPDATED = "802";
	public static final String SUCCESS_MESSAGE_UPDATED = "Resource has been updated on the server";

	public static final int SUCCESS_CODE_OK_INT = 803;
	public static final String SUCCESS_CODE_OK = "803";
	public static final String SUCCESS_MESSAGE_OK = "Request successful";
}
