package org.freeshr.identity.controller;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.util.UUID;

@Controller
public class IdentityController extends WebMvcConfigurerAdapter {

    public static final String SHR_IDENTITY_TOKEN = "SHR_IDENTITY_TOKEN";
    public static final String X_AUTH_TOKEN = "X-Auth-Token";
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

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String login(@RequestBody UserCredentials userCredentials
            , HttpServletResponse response, HttpServletRequest request) {
        return addTokenToResponse(userCredentials, response, request);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            headers = "content-type=application/x-www-form-urlencoded",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    String loginFromForm(@ModelAttribute UserCredentials userCredentials,
                         HttpServletRequest request, HttpServletResponse response) {
        return addTokenToResponse(userCredentials, response, request);
    }

    @RequestMapping(value = "/userInfo/{token}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    UserInfo userInfo(@PathVariable UUID token) {
        UserInfo userInfo = identityService.userInfo(token);
        if (null == userInfo) {
            throw new InvalidTokenException("Invalid token");
        }
        return userInfo;
    }

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

    private String addTokenToResponse(UserCredentials userCredentials, HttpServletResponse response, HttpServletRequest request) {
        try {
            UUID result = identityService.login(userCredentials);
            if (null == result) {
                throw new BadCredentialsException("Invalid Credentials");
            }
            response.addHeader(X_AUTH_TOKEN, result.toString());
            Cookie authCookie = new Cookie(SHR_IDENTITY_TOKEN, result.toString());
            response.addCookie(authCookie);

            String callBackUrl = request.getParameter("redirectTo");
            if (callBackUrl != null) {
                response.sendRedirect(callBackUrl);
            }
            return result.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private Map<String, String> addAccessTokenToResponse(UserCredentials userCredentials, HttpServletResponse response, HttpServletRequest request) {
        try {
            UUID result = identityService.signin(userCredentials);
            if (null == result) {
                throw new BadCredentialsException("Invalid Credentials");
            }
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(ACCESS_TOKEN, result.toString());
            Cookie authCookie = new Cookie(SHR_IDENTITY_TOKEN, result.toString());
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
