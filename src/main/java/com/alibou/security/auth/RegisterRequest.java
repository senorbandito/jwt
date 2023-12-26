package com.alibou.security.auth;

import com.alibou.security.plan.Plan;
import com.alibou.security.plan.PlanRepository;
import com.alibou.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
  private String contact;
  private Date dob;
  private Plan plan;



}
