package org.hofftech.edu.util;

import lombok.Getter;

@Getter
public enum SqlCommand {
    ADD_PACKAGE("INSERT INTO edu.package (name, shape, symbol, start_position_x, start_position_y) " +
            "VALUES (?, ?, ?, ?, ?)"),
    FIND_PACKAGE("SELECT * FROM edu.package WHERE name = ?"),
    UPDATE_PACKAGE("UPDATE edu.package SET shape = ?, symbol = ?, start_position_x = ?, start_position_y = ? " +
            "WHERE name = ?"),
    DELETE_PACKAGE("DELETE FROM edu.package WHERE name = ?"),
    GET_ALL_PACKAGES("SELECT * FROM edu.package");

    private final String query;

    SqlCommand(String query) {
        this.query = query;
    }

}