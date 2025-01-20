package org.hofftech.edu.service.commandprocessor.impl;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

import java.util.List;

@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor {

    private final PackageRepository packageRepository;

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
