package com.kirk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Nested;

public class AuxillaryMethodsTest {
    AuxiliaryMethods auxiliaryMethods;

    @BeforeEach
    void setUp(){
        auxiliaryMethods = new AuxiliaryMethods();
    }

    @Nested
    class inputChoice{
        @ParameterizedTest
        @CsvSource({
            "5, 5",
            "999, 999"
        })

        void shouldParseChoice(String a, Integer expected){
            Integer result = auxiliaryMethods.parseInputChoice(a);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource({
            "null",
            "abc",
            "2.8",
            "true",
            "false"
        })

        void shouldExceptionParseChoice(String a){
            Integer result = auxiliaryMethods.parseInputChoice(a);

            assertNull(result);
        }

        @ParameterizedTest
        @CsvSource({
            "5",
            "0",
            "7"
        })
        
        void shouldPositiveCheckChoice(Integer a){
            boolean result = auxiliaryMethods.checkChoice(a);

            assertTrue(result);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "null",
            "92",
            "-1",
            "8"
        }, nullValues = "null")

        void shouldNegativeCheckChoice(Integer a){
            boolean result = auxiliaryMethods.checkChoice(a);

            assertFalse(result);
        }
    }
    
}
