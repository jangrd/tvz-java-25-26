package com.uni.app;

import com.uni.app.database.DatabaseManager;
import com.uni.app.exceptions.ValidationException;
import com.uni.app.io.ChangeLogStore;
import com.uni.app.io.CredentialStore;

public record AppContext(
        DatabaseManager dbManager,
        CredentialStore credentialStore,
        ChangeLogStore changeLogStore
) {
    public AppContext {
        if (dbManager == null) {
            throw new ValidationException("AppContext 'dbManager' must not be null");
        }
        if (credentialStore == null) {
            throw new ValidationException("AppContext 'credentialStore' must not be null");
        }
        if (changeLogStore == null) {
            throw new ValidationException("AppContext 'changeLogStore' must not be null");
        }
    }
}
