package com.alibou.security.user;

import com.alibou.security.plan.Plan;
import com.alibou.security.token.Token;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Getter
  @Id
  @GeneratedValue
  private Integer id;
  @Getter
  private String firstname;
  @Getter
  private String lastname;
  @Getter
  private String contact;
  @Getter
  private Date dob;
  @Getter
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @Getter
  @ManyToOne
  @JoinColumn(name = "plan_id")
  private Plan plan;

  public User(String firstname, String lastname, String contact, Date dob, String email, String password){
    this.firstname = firstname;
    this.lastname = lastname;
    this.contact = contact;
    this.dob = dob;
    this.email = email;
    this.password = password;
  }

  public void  setPlan(Plan plan) {
    this.plan = plan;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public void setEmail(String email) {
    this.email = email;
  }



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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
}
