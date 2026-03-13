package com.brainwave.core.config.properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConfigPropertiesValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void authProperties_withDefaultValue_shouldBeValid() {
        AuthProperties props = new AuthProperties();
        assertTrue(validator.validate(props).isEmpty());
    }

    @Test
    void authProperties_withBlankDemoUsername_shouldBeInvalid() {
        AuthProperties props = new AuthProperties();
        props.getDemo().setUsername(" ");
        assertFalse(validator.validate(props).isEmpty());
    }

    @Test
    void corsProperties_withDefaultValue_shouldBeValid() {
        CorsProperties props = new CorsProperties();
        assertTrue(validator.validate(props).isEmpty());
    }

    @Test
    void corsProperties_withEmptyAllowedOrigins_shouldBeInvalid() {
        CorsProperties props = new CorsProperties();
        props.getAllowedOrigins().clear();
        assertFalse(validator.validate(props).isEmpty());
    }

    @Test
    void auditProperties_withDefaultValue_shouldBeValid() {
        AuditProperties props = new AuditProperties();
        assertTrue(validator.validate(props).isEmpty());
    }

    @Test
    void auditProperties_withInvalidMaxBodyLength_shouldBeInvalid() {
        AuditProperties props = new AuditProperties();
        props.setMaxBodyLength(0);
        assertFalse(validator.validate(props).isEmpty());
    }

    @Test
    void storageProperties_withDefaultValue_shouldBeValid() {
        StorageProperties props = new StorageProperties();
        assertTrue(validator.validate(props).isEmpty());
    }

    @Test
    void storageProperties_withBlankBasePath_shouldBeInvalid() {
        StorageProperties props = new StorageProperties();
        props.getLocal().setBasePath(" ");
        assertFalse(validator.validate(props).isEmpty());
    }
}
