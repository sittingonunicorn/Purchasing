package com.test.purchasing.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "user_id"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> result = new HashSet<>();
        result.add(Role.USER);
        return result;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public BigDecimal getLocalizedBalance() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale.equals(Locale.US) ? balance : balance.multiply(BigDecimal.valueOf(26.7));
    }

    public void setLocalizedBalance(BigDecimal balance) {
        Locale locale = LocaleContextHolder.getLocale();
        balance = locale.equals(Locale.US) ? balance :
                balance.divide(BigDecimal.valueOf(26.7), 10, RoundingMode.HALF_EVEN );
        this.setBalance(balance);
    }
}
