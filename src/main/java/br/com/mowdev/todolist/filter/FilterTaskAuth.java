package br.com.mowdev.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mowdev.todolist.user.UserModel;
import br.com.mowdev.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (!servletPath.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }

        var auth = request.getHeader("Authorization");
        var authEncoded = auth.substring("Basic".length()).trim();
        var decodedData = new String(java.util.Base64.getDecoder().decode(authEncoded));

        String[] credentials = decodedData.split(":");

        String username = credentials[0];
        String password = credentials[1];

        UserModel entity = userRepository.findByUsername(username);

        if (entity == null) {
            response.sendError(401, "User not found");
            return;
        }

        var passVerifyer = BCrypt.verifyer().verify(password.toCharArray(), entity.getPassword().toCharArray());

        if (!passVerifyer.verified) {
            response.sendError(401, "Invalid password");
            return;
        }


        request.setAttribute("userId", entity.getId());
        filterChain.doFilter(request, response);
    }
}
