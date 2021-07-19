package com.flockit.security;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.flockit.enums.User_Role;
import com.flockit.services.CustomUserDetailsService;
import com.flockit.utils.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key:mysecretDefault}")
  private String secretKey;

  //# 5 minutes duration by default: 5 minutes * 60 seconds * 1000 miliseconds
  @Value("${security.jwt.token.expire-length:300000}")
  private long validityInMilliseconds;

  @Autowired
  private CustomUserDetailsService myUserDetails;

  @PostConstruct
  protected void init() {
  	if (secretKey == null || secretKey.isEmpty()) secretKey = SecurityConstants.JWT_SECRET;
  }

  public String createToken(String username, com.flockit.enums.User_Role role) {

	  	Claims claims = Jwts.claims().setSubject(username);
    	  
	  	
	    List<String> roles = Arrays.asList(role.getValue());
	    /*
	    List<SimpleGrantedAuthority> authList = roles.stream()
	        	.map(s -> new SimpleGrantedAuthority(s))
	        	.collect(Collectors.toList());
	    
	    claims.put(SecurityConstants.TOKEN_CLAIM_AUTH, authList);
    	*/
    	claims.put(SecurityConstants.TOKEN_CLAIM_AUTH, role.getValue());

	    if (roles.contains(new SimpleGrantedAuthority(User_Role.ROLE_ADMIN.getValue()))) {
			claims.put("isAdmin", true);
		}
		if (roles.contains(new SimpleGrantedAuthority(User_Role.ROLE_USER.getValue()))) {
			claims.put("isUser", true);
		}
		
	    String secretKeyEnc = Base64.getEncoder().encodeToString(secretKey.getBytes());

    	String token = Jwts.builder()
			.signWith(SignatureAlgorithm.HS512,secretKeyEnc)
	         .setClaims(claims)
	         .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
             .setIssuer(SecurityConstants.TOKEN_ISSUER)
             .setAudience(SecurityConstants.TOKEN_AUDIENCE)
             .setSubject(username)
             .setIssuedAt(new Date(System.currentTimeMillis()))
             .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
             .compact();
    	
    	return token;
  }

  public String refreshToken(HttpServletRequest req) {
	  String token = resolveToken(req);
	  String secretKeyEnc = Base64.getEncoder().encodeToString(secretKey.getBytes());
	  Jws<Claims> _claims =	Jwts.parser().setSigningKey(secretKeyEnc).parseClaimsJws(token);	  
      return Jwts.builder().setClaims(_claims.getBody()).signWith(SignatureAlgorithm.HS512, secretKeyEnc).compact();
  }
  
  public Authentication getAuthentication(String token) {
	  UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
	  return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {

	  String secretKeyEnc = Base64.getEncoder().encodeToString(secretKey.getBytes());

	  return Jwts.parser().setSigningKey(secretKeyEnc).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest req) {
	  String bearerToken = req.getHeader(SecurityConstants.TOKEN_HEADER);
	  if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
		  return bearerToken.substring(7);
	  }
	  return null;
  }

  public boolean validateToken(String token) throws Exception {
	  try {
		  String secretKeyEnc = Base64.getEncoder().encodeToString(secretKey.getBytes());
		  Jws<Claims> _claims =	Jwts.parser().setSigningKey(secretKeyEnc).parseClaimsJws(token);
		  Date now = new Date();
		  Date exp = _claims.getBody().getExpiration();
	      return now.after(exp);
	  } catch (JwtException | IllegalArgumentException e) {
		  throw new BadCredentialsException("INVALID_CREDENTIALS", e);
	  }
  }
}
