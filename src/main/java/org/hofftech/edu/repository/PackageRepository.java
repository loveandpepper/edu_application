package org.hofftech.edu.repository;

import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.DefaultPackagesType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий для управления данными о посылках.
 * Предоставляет методы для добавления, редактирования, удаления и поиска посылок.
 */
public class PackageRepository {
    private static final int FIRST_CHAR = 0;
    private static final int START_POSITION_X = 0;
    private static final int START_POSITION_Y = 0;

    private final Map<String, Package> packages = new HashMap<>();
    /**
     * Добавляет новую посылку в репозиторий.
     *
     * @param providedPackage объект Package, который нужно добавить
     * @throws IllegalArgumentException если посылка с таким именем уже существует
     */
    public void addPackage(Package providedPackage) {
        if (packages.containsKey(providedPackage.getName())) {
            throw new PackageNameException("Посылка с таким именем уже существует: " + providedPackage.getName());
        }
        packages.put(providedPackage.getName(), providedPackage);
    }
    /**
     * Ищет посылку по имени.
     *
     * @param name имя посылки
     * @return объект Package, если найден
     * @throws IllegalArgumentException если посылка с таким именем не найдена
     */
    public Optional<Package> findPackage(String name) {
        for (String key : packages.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return Optional.of(packages.get(key));
            }
        }
        return Optional.empty();
    }
    /**
     * Обновляет данные существующей посылки.
     *
     * @param name          имя существующей посылки
     * @param updatedPackage обновлённый объект Package
     * @throws IllegalArgumentException если посылка с таким именем не найдена
     */
    public void editPackage(String name, Package updatedPackage) {
        if (!packages.containsKey(name)) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
        packages.put(name, updatedPackage);
    }
    /**
     * Удаляет посылку по имени.
     *
     * @param name имя посылки
     * @throws IllegalArgumentException если посылка с таким именем не найдена
     */
    public void deletePackage(String name) {
        if (!packages.containsKey(name)) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
        packages.remove(name);
    }
    /**
     * Возвращает список всех посылок в репозитории.
     *
     * @return отсортированный список объектов Package
     */
    public List<Package> getAllPackages() {
        return packages.values().stream()
                .sorted(Comparator.comparing(Package::getName))
                .toList();
    }
    /**
     * Загружает предопределённые типы посылок в репозиторий.
     */
    public void loadDefaultPackages() {
        int counter = 1;
        for (DefaultPackagesType type : DefaultPackagesType.values()) {
            String name = "Посылка Тип " + counter++;
            packages.put(name, new Package(
                    name,
                    type.getShape(),
                    type.getShape().getFirst().charAt(FIRST_CHAR),
                    new PackageStartPosition(START_POSITION_X, START_POSITION_Y)
            ));
        }
    }
}

