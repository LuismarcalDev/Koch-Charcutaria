package br.com.koch.configuracao;

import br.com.koch.servico.ServicoDetalhesUsuario;
import br.com.koch.servico.cliente.ServicoDetalhesCliente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class ConfiguracaoSeguranca {

    private final ServicoDetalhesCliente servicoDetalhesCliente;
    private final ServicoDetalhesUsuario servicoDetalhesUsuario;

    public ConfiguracaoSeguranca(
            ServicoDetalhesCliente servicoDetalhesCliente,
            ServicoDetalhesUsuario servicoDetalhesUsuario
    ) {
        this.servicoDetalhesCliente = servicoDetalhesCliente;
        this.servicoDetalhesUsuario = servicoDetalhesUsuario;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain clienteSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/cliente/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/cliente/login", "/cliente/cadastro").permitAll()
                        .requestMatchers("/cliente/painel", "/cliente/painel/**").authenticated()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(clienteAuthenticationProvider())
                .formLogin(form -> form
                        .loginPage("/cliente/login")
                        .loginProcessingUrl("/cliente/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            String redirect = request.getParameter("redirect");
                            String destino = "/cliente/painel";
                            if (redirect != null && redirect.startsWith("/") && !redirect.startsWith("//")) {
                                destino = redirect;
                            }
                            response.sendRedirect(request.getContextPath() + destino);
                        })
                        .failureUrl("/cliente/login?error")
                        .permitAll()
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/css/**", "/js/**",
                                "/images/**", "/imagens/**", "/Imagens/**", "/img/**",
                                "/cliente/css/**", "/cliente/img/**", "/cliente/js/**",
                                "/webjars/**", "/favicon.ico",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(
                                "/",
                                "/catalogo", "/catalogo/**",
                                "/assinaturas", "/assinaturas/**",
                                "/nossa-historia", "/contato",
                                "/suinos", "/carnes", "/historia", "/sobre", "/delivery", "/carrinho"
                        ).permitAll()
                        .requestMatchers("/login", "/cadastro").permitAll()
                        .requestMatchers("/admin/**", "/painel", "/painel/**").hasRole("ADMINISTRADOR")
                        .anyRequest().permitAll()
                )
                .authenticationProvider(adminAuthenticationProvider())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider clienteAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(servicoDetalhesCliente);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(servicoDetalhesUsuario);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            String destino = isAdmin(authentication)
                    ? request.getContextPath() + "/login?logout"
                    : request.getContextPath() + "/cliente/login?logout";
            response.sendRedirect(destino);
        };
    }

    private static boolean isAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()));
    }
}
