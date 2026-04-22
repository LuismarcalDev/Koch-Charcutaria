package br.com.koch.configuracao;

import br.com.koch.servico.cliente.ServicoDetalhesCliente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracaoSeguranca {

    private final ServicoDetalhesCliente servicoDetalhesCliente;

    public ConfiguracaoSeguranca(ServicoDetalhesCliente servicoDetalhesCliente) {
        this.servicoDetalhesCliente = servicoDetalhesCliente;
    }

    // Liga o seu ServicoDetalhesCliente ao Spring Security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(servicoDetalhesCliente);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/suinos", "/carnes", "/historia", "/sobre", "/delivery", "/contato", "/carrinho").permitAll()
                        .requestMatchers("/login", "/cadastro").permitAll()
                        .requestMatchers("/cliente/login", "/cliente/cadastro").permitAll()
                        .requestMatchers("/painel/**").hasRole("ADMINISTRADOR")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/cliente/login")
                        .loginProcessingUrl("/cliente/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/cliente/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/cliente/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}