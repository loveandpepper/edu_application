package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

public record DeleteCommandProcessor(PackageRepository packageRepository) implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new PackageNameException("Имя посылки не указано.");
        }

        packageRepository.deletePackage(name);
        return "Посылка '" + name + "' успешно удалена.";
    }
}
