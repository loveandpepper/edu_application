package org.hofftech.edu.service;

import org.assertj.core.api.Assertions;
import org.hofftech.edu.exception.ValidateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ValidatorServiceTest {

    private ValidatorService validatorService;

    @BeforeEach
    void setUp() {
        validatorService = new ValidatorService();
    }

    @Test
    void shouldValidateFormSuccessfully() {
        String form = "****,*  *,****";
        List<String> result = validatorService.validateForm(form);

        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(3)
                .containsExactly("****", "*  *", "****");
    }

    @Test
    void shouldThrowExceptionForEmptyForm() {
        Assertions.assertThatThrownBy(() -> validatorService.validateForm(""))
                .isInstanceOf(ValidateException.class)
                .hasMessageContaining("Форма посылки не указана.");
    }

    @Test
    void shouldThrowExceptionForDiagonalTouch() {
        List<String> invalidForm = List.of(
                " * ",
                "* *"
        );

        Assertions.assertThatThrownBy(() -> validatorService.validateDiagonalTouch(invalidForm))
                .isInstanceOf(ValidateException.class)
                .hasMessageContaining("Символ в позиции");
    }

    @Test
    void shouldValidateFileSuccessfully() {
        List<String> lines = List.of("Посылка Тип 1", "Посылка Тип 3");

        Assertions.assertThatCode(() -> validatorService.validateFile(lines))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionForEmptyFile() {
        Assertions.assertThatThrownBy(() -> validatorService.validateFile(List.of()))
                .isInstanceOf(ValidateException.class)
                .hasMessageContaining("Файл пустой или не содержит данных.");
    }
}

