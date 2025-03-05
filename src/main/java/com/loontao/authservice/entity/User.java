package com.loontao.authservice.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // @Column(nullable = false, name = "user_id")
    // private Long userId;

    @Column(nullable = true, name = "fullname")
    private String fullname;

    @Column(nullable = true, name = "first_name")
    private String firstName;

    @Column(nullable = true, name = "last_name")
    private String lastName;

    @Column(nullable = true, name = "password")
    private String password;

    @Column(nullable = true, name = "address")
    private String address;

    @Column(nullable = true, name = "city")
    private String city;

    @Column(nullable = true, name = "country")
    private String country;

    @Column(nullable = false, unique = true, name = "email_id")
    private String emailId;

    @Id
    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = true, name = "pincode")
    private String pincode;

    @Column(nullable = true, name = "date_of_birth")
    private String dateOfBirth;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;


   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());    
    return List.of(authority);
    }
    
    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return emailId;
    }

    
    public String getFullname() {
        return fullname;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getDob()
    {
        return dateOfBirth;
    }

    public String getPincode()
    {
        return pincode;
    }

    public String getCountry()
    {
        return country;
    }

    public String getCity()
    {
        return city;
    }

    public String getAddress()
    {
        return address;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getFirstName()
    {
        return firstName;
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

public User setFullname(String fullname) {
    this.fullname = fullname;
    return this;
}

public User setEmailId(String emailId) {
    this.emailId = emailId;
    return this;
}

public User setPassword(String password) {
    this.password = password;
    return this;
}

public User setAddress(String address) {
    this.address = address;
    return this;

}

public User setCity(String city) {
    this.city = city;
    return this;
}

public User setCountry(String country) {
    this.country = country;
    return this;
}

public User setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
}

public User setPincode(String pincode) {
    this.pincode = pincode;
    return this;
}

public User setDob(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
}

public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
}

public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
}

public Role getRole() {
    return role;
}

public User setRole(Role role) {
    this.role = role;
    
    return this;
}

}