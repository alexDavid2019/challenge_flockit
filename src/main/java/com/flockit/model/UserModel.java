package com.flockit.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@NotBlank
    @NotEmpty
	private String username;
	
	@JsonIgnore
	@NotBlank
    @NotEmpty
	private String password;
	
	@NotBlank
    private boolean loggedIn;

	@NotNull
    @Enumerated(EnumType.STRING)
    private com.flockit.enums.User_Role role;
 
    public UserModel() {
    }

    public UserModel(@NotBlank String username, @NotBlank String password, com.flockit.enums.User_Role role) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.role = role;
    }
    /*
    public Long getId() {
        return id;
    }
    public void setId(Long _id) {
        this.id= _id;
    }
	*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public com.flockit.enums.User_Role getRole() {
        return this.role;
    }

    public void setRole(com.flockit.enums.User_Role val) {
        this.role = val;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel user = (UserModel) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, loggedIn);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + role.getValue() + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
    }
}
