package org.freeshr.identity.controller;


import org.freeshr.identity.model.Resource;
import org.freeshr.identity.repository.ResourceRepository;
import org.freeshr.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/api/1.0/providers")
public class ProviderController extends BaseController {

    @Autowired
    private ResourceRepository repository;
    @Autowired
    private IdentityService identityService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Resource getProvider(@PathVariable String id, HttpServletRequest request) {
        checkForValidUserRequest(request);
        String addressable = getAddressableHostURL(request);

        Resource provider = repository.findResource("provider", id);
        if (provider == null) {
            throw new ResourceNotFoundException();
        }
        provider.replace("http://provider.example.org", addressable);
        provider.replace("http://facility.example.org", addressable);
        return provider;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Resource> listProviders(@RequestParam(value = "updatedSince", required = false) String updatedSince,
                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                 @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                 HttpServletRequest request) {
        String addressable = getAddressableHostURL(request);

        try {
            List<Resource> providers = repository.findResources("provider", updatedSince, offset, limit);
            for (Resource provider : providers) {
                provider.replace("http://provider.example.org", addressable);
                provider.replace("http://facility.example.org", addressable);
            }
            return providers;
        } catch (ParseException e) {
            throw new BadRequest(e.getMessage());
        }

    }

    private String getAddressableHostURL(HttpServletRequest request) {
        checkForValidUserRequest(request);
        String completeURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        return completeURL.substring(0, completeURL.indexOf(requestURI));
    }

}
