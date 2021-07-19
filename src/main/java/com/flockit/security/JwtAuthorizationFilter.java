package com.flockit.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.flockit.utils.SecurityConstants;

import antlr.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Value("${security.jwt.token.secret-key:mysecretDefault}")
	private String secretKey;
	
    private String[] defaultAuthorities = {com.flockit.enums.User_Role.ROLE_USER.getValue()};
    
    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
    	
    	if (secretKey == null || secretKey.isEmpty()) secretKey = SecurityConstants.JWT_SECRET;
    	
    	UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        
    	if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(token) 
        		&& token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
            	
            	String secretKeyEnc = Base64.getEncoder().encodeToString(secretKey.getBytes());

                Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(secretKeyEnc)
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
               
                String username = parsedToken.getBody().getSubject();

                Collection<? extends GrantedAuthority> authorities = this.getAuthorities(parsedToken.getBody());
                
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
                
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }
        }

        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
    	
        if (!map.containsKey(SecurityConstants.TOKEN_CLAIM_AUTH)) {
        	return AuthorityUtils.commaSeparatedStringToAuthorityList( org.springframework.util.StringUtils.arrayToCommaDelimitedString(this.defaultAuthorities));
        } 
        else {
        	
        	Object authorities = map.get(SecurityConstants.TOKEN_CLAIM_AUTH);
            
            if (authorities instanceof String) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList((String)authorities);
            } else if (authorities instanceof Collection) {
                return AuthorityUtils.commaSeparatedStringToAuthorityList(org.springframework.util.StringUtils.collectionToCommaDelimitedString((Collection)authorities));
            } else {
                throw new IllegalArgumentException("Authorities must be either a String or a Collection");
            }
        }
    }
}
