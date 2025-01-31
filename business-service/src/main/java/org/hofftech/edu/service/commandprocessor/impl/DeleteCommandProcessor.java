package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

@RequiredArgsConstructor
public class DeleteCommandProcessor implements CommandProcessor {

    private final PackageRepository packageRepository;

    @Override
    public String execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new PackageNameException("Имя посылки не указано.");
        }
        if (!packageRepository.existsById(name)) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
        packageRepository.deleteById(name);

        return "Посылка '" + name + "' успешно удалена.";
    }
}
