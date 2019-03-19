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
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    @Transactional
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return getUserWithRoles(users);
    }

    @Override
    @Transactional
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getUserWithRoles(users);
    }

    @Override
    @Transactional
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        List<Map<String, Object>> mapsUserRoles = jdbcTemplate.queryForList("SELECT * FROM user_roles");
        Map<Integer, Set<Role>> rolesMap = new HashMap<>();
        for (Map<String, Object> map : mapsUserRoles) {
            int userId = (int) map.get("user_id");
            Role role = Role.valueOf(map.get("role").toString());
            rolesMap.computeIfAbsent(userId, key -> EnumSet.of(role));
            Set<Role> roles = rolesMap.get(userId);
            roles.add(role);
            rolesMap.put(userId, roles);
        }
        users.forEach(user -> user.setRoles(rolesMap.get(user.getId())));
        return users;
    }

    private User getUserWithRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            Set<Role> roles = new HashSet<>();
            List<Map<String, Object>> mapsUserRoles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", user.getId());
            for (Map<String, Object> map : mapsUserRoles) {
                Role role = Role.valueOf(map.get("role").toString());
                roles.add(role);
            }
            user.setRoles(roles);
            return user;
        }
        return null;
    }
}
