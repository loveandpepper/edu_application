package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ListCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;

    @Override
    public void execute(ParsedCommand command) {
        List<Package> packages = repository.getAllPackages();
        if (packages.isEmpty()) {
            log.info("Нет доступных посылок.");
        } else {
            log.info("Список всех посылок:");
            packages.forEach(pkg -> System.out.println(pkg.toString()));
        }
    }
}
