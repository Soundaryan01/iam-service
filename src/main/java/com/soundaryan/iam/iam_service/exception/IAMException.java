package com.soundaryan.iam.iam_service.exception;

public class IAMException extends RuntimeException {

    private final String errorCode;

    public IAMException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // --- Predefined Error Codes ---
    public static final String DUPLICATE_USERNAME = "IAM_001";
    public static final String DUPLICATE_EMAIL = "IAM_002";

    // Factory methods for convenience
    public static IAMException duplicateUsername(String username) {
        return new IAMException(DUPLICATE_USERNAME, "Username '" + username + "' is already taken");
    }

    public static IAMException duplicateEmail(String email) {
        return new IAMException(DUPLICATE_EMAIL, "Email '" + email + "' is already registered");
    }
}
