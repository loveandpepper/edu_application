package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

@Getter
@RequiredArgsConstructor
public class DeleteCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Имя посылки не указано.");
        }

        repository.deletePackage(name);
        result = "Посылка '" + name + "' успешно удалена.";
    }
}
