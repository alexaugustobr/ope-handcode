package br.com.ope.security

import br.com.ope.security.jwt.auth.SkipPathRequestMatcher
import br.com.ope.security.jwt.auth.extractor.TokenExtractor
import br.com.ope.security.jwt.entry.RestAuthenticationEntryPoint
import br.com.ope.security.jwt.filter.CustomCorsFilter
import br.com.ope.security.jwt.filter.JwtTokenAuthenticationFilter
import br.com.ope.security.jwt.filter.LoginRequestFilter
import br.com.ope.security.jwt.provider.JwtAuthenticationProvider
import br.com.ope.security.jwt.provider.LoginAuthenticationProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
class ApiSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val objectMapper: ObjectMapper? = null
    @Autowired
    private val authenticationEntryPoint: RestAuthenticationEntryPoint? = null
    @Autowired
    private val successHandler: AuthenticationSuccessHandler? = null
    @Autowired
    private val failureHandler: AuthenticationFailureHandler? = null
    @Autowired
    private val loginAuthenticationProvider: LoginAuthenticationProvider? = null
    @Autowired
    private val jwtAuthenticationProvider: JwtAuthenticationProvider? = null
    @Autowired
    private val tokenExtractor: TokenExtractor? = null
    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Throws(Exception::class)
    private fun buildLoginRequestFilterFilter(): LoginRequestFilter {
        val filter = LoginRequestFilter(LOGIN_ENTRY_POINT, successHandler!!, failureHandler!!, objectMapper!!)
        filter.setAuthenticationManager(this.authenticationManager)
        return filter
    }

    @Throws(Exception::class)
    private fun buildJwtTokenAuthenticationProcessingFilter(): JwtTokenAuthenticationFilter {
        val pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, LOGIN_ENTRY_POINT, HOME_API_URL)
        val matcher = SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT)
        val filter = JwtTokenAuthenticationFilter(failureHandler!!, tokenExtractor!!, matcher)
        filter.setAuthenticationManager(this.authenticationManager)
        return filter
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(loginAuthenticationProvider)
        auth.authenticationProvider(jwtAuthenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.antMatcher(TOKEN_BASED_AUTH_ENTRY_POINT).csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(this.authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENTRY_POINT, HOME_API_URL, TOKEN_REFRESH_ENTRY_POINT).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
                .and()
                .addFilterBefore(CustomCorsFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(buildLoginRequestFilterFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    companion object {

        private val LOGIN_ENTRY_POINT = "/api/auth/v1/login"
        private val HOME_API_URL = "/api/home/**"
        private val TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**"
        private val TOKEN_REFRESH_ENTRY_POINT = "/api/auth/v1/token"
    }

}
