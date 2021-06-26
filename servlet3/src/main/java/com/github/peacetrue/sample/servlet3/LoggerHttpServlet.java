package com.github.peacetrue.sample.servlet3;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author : xiayx
 * @since : 2021-06-26 16:28
 **/
@Slf4j
@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class LoggerHttpServlet extends HttpServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("初始: {}", config);
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("执行 Get 请求");

        HttpSession session = req.getSession();
        String countAttr = "count";
        int count = Optional.ofNullable(session.getAttribute(countAttr)).map(Integer.class::cast).orElse(0);
        session.setAttribute(countAttr, count + 1);

        ServletContext context = session.getServletContext();
        count = Optional.ofNullable(context.getAttribute(countAttr)).map(Integer.class::cast).orElse(0);
        context.setAttribute(countAttr, count + 1);

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(String.format("当前请求: GET %s", req.getRequestURL().toString()));
        writer.flush();
        writer.close();
    }


}
