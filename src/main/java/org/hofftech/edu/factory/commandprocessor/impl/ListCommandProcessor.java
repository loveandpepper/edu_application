package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

import java.util.List;

public record ListCommandProcessor(PackageRepository packageRepository) implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        List<Package> packages = packageRepository.getAllPackages();
        if (packages.isEmpty()) {
            return "Нет доступных посылок.";
        } else {
            StringBuilder output = new StringBuilder("Список всех посылок:\n");
            packages.forEach(pkg -> output.append(pkg).append("\n"));
            return output.toString();
        }
    }

}
