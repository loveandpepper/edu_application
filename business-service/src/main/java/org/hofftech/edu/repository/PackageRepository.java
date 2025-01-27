package org.hofftech.edu.repository;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.exception.PackageNameException;
import org.hofftech.edu.exception.PackageNotFoundException;
import org.hofftech.edu.model.Package;
import org.hofftech.edu.util.SqlCommand;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PackageRepository {

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SYMBOL = "symbol";
    private static final String COLUMN_POS_X = "start_position_x";
    private static final String COLUMN_POS_Y = "start_position_y";
    private static final String TYPE_TEXT = "text";
    private static final String COLUMN_SHAPE = "shape";
    private static final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Package> packageRowMapper = (ResultSet rs, int rowNum) -> {
        Array shapeArray = rs.getArray(COLUMN_SHAPE);
        List<String> shape = List.of((String[]) shapeArray.getArray());
        return new Package(
                rs.getString(COLUMN_NAME),
                shape,
                rs.getString(COLUMN_SYMBOL).charAt(0),
                rs.getInt(COLUMN_POS_X),
                rs.getInt(COLUMN_POS_Y)
        );
    };

    public void addPackage(Package providedPackage) {
        try {
            jdbcTemplate.update(SqlCommand.ADD_PACKAGE.getQuery(),
                    providedPackage.getName(),
                    Objects.requireNonNull(jdbcTemplate.getDataSource()).
                            getConnection().createArrayOf(TYPE_TEXT, providedPackage.getShape().toArray()),
                    String.valueOf(providedPackage.getSymbol()),
                    providedPackage.getStartPositionX(),
                    providedPackage.getStartPositionY());
        } catch (SQLException e) {
            throw new PackageNameException("Ошибка при добавлении посылки: " + providedPackage.getName());
        }
    }

    public Optional<Package> findPackage(String name) {
        List<Package> packages = jdbcTemplate.query(SqlCommand.FIND_PACKAGE.getQuery(), packageRowMapper, name);
        return packages.isEmpty() ? Optional.empty() : Optional.of(packages.getFirst());
    }

    public void updatePackage(String name, Package updatedPackage) {
        int rowsAffected;
        try {
            rowsAffected = jdbcTemplate.update(SqlCommand.UPDATE_PACKAGE.getQuery(),
                    Objects.requireNonNull(jdbcTemplate.getDataSource()).
                            getConnection().createArrayOf(TYPE_TEXT, updatedPackage.getShape().toArray()),
                    String.valueOf(updatedPackage.getSymbol()),
                    updatedPackage.getStartPositionX(),
                    updatedPackage.getStartPositionY(),
                    name);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении посылки: " + name, e);
        }

        if (rowsAffected == 0) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
    }

    public void deletePackage(String name) {
        int rowsAffected = jdbcTemplate.update(SqlCommand.DELETE_PACKAGE.getQuery(), name);
        if (rowsAffected == 0) {
            throw new PackageNotFoundException("Посылка не найдена: " + name);
        }
    }

    public List<Package> getAllPackages(Pageable pageable) {
        return jdbcTemplate.query(
                SqlCommand.GET_ALL_PACKAGES.getQuery() + LIMIT_OFFSET,
                new Object[]{pageable.getPageSize(), pageable.getOffset()},
                packageRowMapper
        );
    }
}
