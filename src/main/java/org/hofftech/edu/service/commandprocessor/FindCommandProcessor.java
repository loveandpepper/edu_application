package org.hofftech.edu.service.commandprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.repository.PackageRepository;

@RequiredArgsConstructor
@Slf4j
public class FindCommandProcessor implements CommandProcessor {
    private final PackageRepository repository;

    @Override
    public void execute(ParsedCommand command) {
        String name = command.getName();
        if (name == null || name.isEmpty()) {
            log.error("Имя посылки не указано.");
            return;
        }

        Package pkg = repository.findPackage(name);
        if (pkg != null) {
            System.out.println("Найдена посылка: " + pkg);
        } else {
            log.error("Посылка с именем '{}' не найдена.", name);
        }
    }
}
