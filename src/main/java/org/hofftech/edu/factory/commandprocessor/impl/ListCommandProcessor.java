package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        List<Package> packages = repository.getAllPackages();
        if (packages.isEmpty()) {
            result = "Нет доступных посылок.";
        } else {
            StringBuilder output = new StringBuilder("Список всех посылок:\n");
            packages.forEach(pkg -> output.append(pkg).append("\n"));
            result = output.toString();
        }
    }

}
