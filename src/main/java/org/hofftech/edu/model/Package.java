package org.hofftech.edu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Package {
    private final PackageType type;
    private final int id;
    private PackageStartPosition packageStartPosition;
}
