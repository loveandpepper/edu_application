package org.hofftech.edu.repository;

import org.hofftech.edu.model.DefaultPackagesType;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.model.PackageStartPosition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Репозиторий для управления данными о посылках.
 * Предоставляет методы для добавления, редактирования, удаления и поиска посылок.
 */
public class PackageRepository {
    private final Map<String, Package> packages = new HashMap<>();
    /**
     * Добавляет новую посылку в репозиторий.
     *
     * @param pkg объект Package, который нужно добавить
     * @throws IllegalArgumentException если посылка с таким именем уже существует
     */
    public void addPackage(Package pkg) {
        if (packages.containsKey(pkg.getName())) {
            throw new IllegalArgumentException("Посылка с таким именем уже существует: " + pkg.getName());
        }
        packages.put(pkg.getName(), pkg);
    }
    /**
     * Ищет посылку по имени.
     *
     * @param name имя посылки
     * @return объект Package, если найден
     * @throws IllegalArgumentException если посылка с таким именем не найдена
     */
    public Package findPackage(String name) {
        for (String key : packages.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return packages.get(key);
            }
        }
        throw new IllegalArgumentException("Посылка не найдена: " + name);
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
            throw new IllegalArgumentException("Посылка не найдена: " + name);
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
            throw new IllegalArgumentException("Посылка не найдена: " + name);
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
                .collect(Collectors.toList());
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
                    type.getShape().getFirst().charAt(0),
                    new PackageStartPosition(0, 0)
            ));
        }
    }
}

