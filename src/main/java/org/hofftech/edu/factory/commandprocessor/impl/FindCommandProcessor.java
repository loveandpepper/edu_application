package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

public record FindCommandProcessor(PackageRepository packageRepository) implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new PackageNameException("Имя посылки не указано");
        }

        return packageRepository.findPackage(name)
                .map(foundPackage -> "Найдена посылка: " + foundPackage)
                .orElseThrow(() -> new PackageNotFoundException(
                        "Посылка с именем '" + name + "' не найдена."
                ));
    }
}
