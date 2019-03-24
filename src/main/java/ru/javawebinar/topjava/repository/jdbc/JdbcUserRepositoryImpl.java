package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        int userId = user.getId();
        List<Object[]> parameters = new ArrayList<>();
        user.getRoles().forEach(role -> parameters.add(new Object[]{userId, role.name()}));
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (?, ?)", parameters);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return getUserWithRoles(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getUserWithRoles(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        List<User> usersRoles = jdbcTemplate.query("SELECT user_id AS id, role AS roles FROM user_roles", ROW_MAPPER);
        Map<Integer, Set<Role>> rolesMap = new HashMap<>();
        usersRoles.forEach(user -> {
            int userId = user.getId();
            Set<Role> roleSet = user.getRoles();
            Set<Role> roles = rolesMap.computeIfAbsent(userId, key -> roleSet);
            roles.add(DataAccessUtils.singleResult(roleSet));
            rolesMap.put(userId, roles);
        });
        users.forEach(user -> user.setRoles(rolesMap.get(user.getId())));
        return users;
    }

    private User getUserWithRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            Set<Role> roles = new HashSet<>();
            List<User> userRoles = jdbcTemplate.query("SELECT role AS roles FROM user_roles WHERE user_id=?", ROW_MAPPER, user.getId());
            userRoles.forEach(userRole -> {
                Role role = DataAccessUtils.singleResult(userRole.getRoles());
                roles.add(role);
            });
            user.setRoles(roles);
            return user;
        }
        return null;
    }
}
