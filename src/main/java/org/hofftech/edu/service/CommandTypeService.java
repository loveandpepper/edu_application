package org.hofftech.edu.service;

import org.hofftech.edu.model.CommandType;

/**
 * Сервис для определения типа команды на основе входного аргумента.
 */
public class CommandTypeService {

    /**
     * Определяет тип команды на основе переданного первого аргумента.
     * <p>
     * Метод проверяет аргумент на null или пустое значение и бросает исключение,
     * если аргумент невалиден. Если аргумент не совпадает с существующими типами команд,
     * выбрасывается {@link IllegalArgumentException}.
     * </p>
     *
     * @param firstArgument строка, содержащая первый аргумент команды.
     *                      Ожидается, что строка соответствует имени одного из значений {@link CommandType}.
     * @return тип команды {@link CommandType}, соответствующий переданному аргументу.
     * @throws RuntimeException         если аргумент пустой или равен null.
     * @throws IllegalArgumentException если переданный аргумент не соответствует ни одному типу команды.
     */
    public CommandType determineCommandType(String firstArgument) {
        if (firstArgument == null || firstArgument.isEmpty()) {
            throw new RuntimeException("Первый аргумент команды пуст");
        }
        try {
            return CommandType.valueOf(firstArgument);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестная команда: " + firstArgument, e);
        }
    }
}
