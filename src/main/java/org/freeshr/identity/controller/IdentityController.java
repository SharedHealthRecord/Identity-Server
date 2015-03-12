package org.freeshr.identity.controller;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IdentityController extends WebMvcConfigurerAdapter {

    public static final String SHR_IDENTITY_TOKEN = "SHR_IDENTITY_TOKEN";
    public static final String ACCESS_TOKEN = "access_token";
    private IdentityService identityService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/loginForm").setViewName("form");
    }

    @Autowired
    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Deprecated
    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(UserCredentials userCredentials, HttpServletRequest request) throws Exception {
        ModelAndView view = new ModelAndView("form");
        String callBackUrl = request.getParameter("redirectTo");
        view.addObject("redirectTo", callBackUrl);
        return view;
    }

    //enhanced Identity Server

    @RequestMapping(value = "/signin", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Map<String, String> signin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpServletResponse response,
                               HttpServletRequest request) {

        UserCredentials userCredentials = new UserCredentials(request.getHeader("client_id"),
                request.getHeader("X-Auth-Token"), email, password);
        return addAccessTokenToResponse(userCredentials, response, request);
    }

    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    UserInfo userDetails(@PathVariable String token, HttpServletRequest request) {
        UserCredentials userCredentials = new UserCredentials(request.getHeader("client_id"),
                request.getHeader("X-Auth-Token"), null, null);
        UserInfo userInfo = identityService.userDetail(userCredentials, token);
        if (null == userInfo) {
            throw new InvalidTokenException("Invalid token");
        }
        return userInfo;
    }

    private Map<String, String> addAccessTokenToResponse(UserCredentials userCredentials, HttpServletResponse response, HttpServletRequest request) {
        try {
            String result = identityService.signin(userCredentials);
            if (null == result) {
                throw new BadCredentialsException("Invalid Credentials");
            }
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(ACCESS_TOKEN, result);
            Cookie authCookie = new Cookie(SHR_IDENTITY_TOKEN, result);
            response.addCookie(authCookie);

            String callBackUrl = request.getParameter("redirectTo");
            if (callBackUrl != null) {
                response.sendRedirect(callBackUrl);
            }
            return responseMap;
        } catch (IOException e) {
            return null;
        }
    }
}
