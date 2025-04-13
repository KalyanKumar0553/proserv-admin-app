package com.src.proserv.main.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name="user_info")
public class UserInfo extends Auditable implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

	@Column(unique=true)
	private String UUID;

	@Column(unique=true)
	private String email;

	@Column(unique=true)
	private String username;

	private String password;

	private String fullName;

	private String salt;

	@Column(unique=true)
	private String mobile;

	private String profilePic;

    private LocalDate dob;

	@Column
	private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
		return this.enabled;
	}

	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
}
