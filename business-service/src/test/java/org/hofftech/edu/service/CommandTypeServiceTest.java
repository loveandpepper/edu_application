//package org.hofftech.edu.service;
//
//import org.assertj.core.api.Assertions;
//import org.hofftech.edu.model.CommandType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class CommandTypeServiceTest {
//
//    private CommandTypeService commandTypeService;
//
//    @BeforeEach
//    void setUp() {
//        commandTypeService = new CommandTypeService();
//    }
//
//    @Test
//    void shouldDetermineValidCommandType() {
//        CommandType commandType = commandTypeService.determineCommandType("LIST");
//
//        Assertions.assertThat(commandType)
//                .isNotNull()
//                .isEqualTo(CommandType.LIST);
//    }
//
//    @Test
//    void shouldThrowExceptionForNullCommand() {
//        Assertions.assertThatThrownBy(() -> commandTypeService.determineCommandType(null))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Первый аргумент команды пуст");
//    }
//
//    @Test
//    void shouldThrowExceptionForUnknownCommand() {
//        Assertions.assertThatThrownBy(() -> commandTypeService.determineCommandType("UNKNOWN"))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Неизвестная команда: UNKNOWN");
//    }
//}

