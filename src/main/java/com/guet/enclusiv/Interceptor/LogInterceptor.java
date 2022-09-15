package com.guet.enclusiv.Interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws IOException {
        String url = String.valueOf(request.getRequestURL());

        if (!url.endsWith("error")) {

            System.out.println("------------------------------------------------------");
            System.out.println("Request URL: " + url);
            System.out.println(request.getMethod());

            if (request.getMethod().equals("GET"))
                System.out.println(request.getQueryString());

            System.out.println("Start Time: " + new Date());
        }else {
            System.out.println(url);
        }
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                           @NotNull Object handler, ModelAndView modelAndView){

        System.out.println("End Time: " + new Date());
        System.out.println("------------------------------------------------------");
    }

}
