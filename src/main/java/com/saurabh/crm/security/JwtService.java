package com.saurabh.crm.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.security.core.userdetails.UserDetails; import org.springframework.stereotype.Service; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.util.Date;
@Service public class JwtService { private final SecretKey key; private final long expiry;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expiry){if(secret.getBytes(StandardCharsets.UTF_8).length<32)throw new IllegalArgumentException("JWT_SECRET needs 32+ bytes");key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.expiry=expiry;}
 public String generate(UserDetails u){Date now=new Date();return Jwts.builder().subject(u.getUsername()).issuedAt(now).expiration(new Date(now.getTime()+expiry)).signWith(key).compact();}
 public String username(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
 public boolean valid(String token,UserDetails u){try{var c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();return c.getSubject().equalsIgnoreCase(u.getUsername())&&c.getExpiration().after(new Date());}catch(JwtException|IllegalArgumentException e){return false;}} }
