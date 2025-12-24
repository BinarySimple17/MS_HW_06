package ru.binarysimple.auth.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.binarysimple.auth.model.User;

public record UserFilter(String usernameStarts, String password) {
    public Specification<User> toSpecification() {
        return usernameStartsSpec()
                .and(passwordSpec());
    }

    private Specification<User> usernameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(usernameStarts)
                ? cb.like(cb.lower(root.get("username")), usernameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<User> passwordSpec() {
        return ((root, query, cb) -> StringUtils.hasText(password)
                ? cb.equal(root.get("password"), password)
                : null);
    }
}