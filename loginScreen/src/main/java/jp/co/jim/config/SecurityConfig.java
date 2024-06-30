//package jp.co.jim.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/main.html", "/css/**", "/js/**", "/img/**").permitAll() // 允许访问 main.html 和静态资源
//                                .requestMatchers("/Adminlogin.html", "/Adminlogin").permitAll()
//                                .requestMatchers("/DGlogin.html", "/DGlogin").permitAll()
//                                .requestMatchers("/AdminaddNewUser.html").hasRole("ADMIN")
//                                .requestMatchers("/E.html").hasRole("DG")
//                                .anyRequest().authenticated()
//                )
//                .formLogin(formLogin -> {
//                    formLogin
//                            .loginPage("/Adminlogin.html")
//                            .loginProcessingUrl("/Adminlogin");
////                    formLogin
////                            .loginPage("/DGlogin.html")
////                            .loginProcessingUrl("/DGlogin")
////                            .defaultSuccessUrl("/E.html", true);
//                })
//                .logout(logout -> logout.permitAll());
//
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build());
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("dguser")
//                .password("password")
//                .roles("DG")
//                .build());
//        return manager;
//    }
//}
//
//
