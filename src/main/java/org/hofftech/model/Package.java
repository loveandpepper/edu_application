package org.hofftech.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Package {
    private final PackageType type;
    private final int id;
}
