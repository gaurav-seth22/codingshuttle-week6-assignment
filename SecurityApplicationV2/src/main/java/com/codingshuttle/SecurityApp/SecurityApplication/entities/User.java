package com.codingshuttle.SecurityApp.SecurityApplication.entities;


import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Role;
import com.codingshuttle.SecurityApp.SecurityApplication.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String email;

    private String password;

    private String name;

    private final Integer SESSION_LIMIT=2;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Subscription subscription;
//One user can have many role and we have to annotate it as Enumerated
   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /*@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
   private Set<Permission> permissions;
*/



    //We are implementing here all the non-default methods
    //This method transforms the user's associated roles into a collection of
    // GrantedAuthority objects that can be used by the Spring Security framework
    // or other security mechanisms to manage user access control
    //Authority will help with permission mapping too
        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        /*   Set<SimpleGrantedAuthority> authorities= roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                    .collect(Collectors.toSet());*/

           /*permissions
                   .forEach(
                           permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))
                   );*/

            Set<SimpleGrantedAuthority> authorities=new HashSet<>();

            roles.forEach(
                    role -> {
                      Set<SimpleGrantedAuthority> permissions= PermissionMapping.getAuthoritiesForRole(role);
                      authorities.addAll(permissions);
                      authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
                    }
            );

           return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
