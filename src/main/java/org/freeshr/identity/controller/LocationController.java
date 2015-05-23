package org.freeshr.identity.controller;

import org.freeshr.identity.model.Resource;
import org.freeshr.identity.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/api/1.0/locations")
public class LocationController extends BaseController {

    @Autowired
    private ResourceRepository locations;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Resource getLocation(@PathVariable String id, HttpServletRequest request) {
        checkForValidUserRequest(request);

        Resource location = locations.findResource("location", id);
        if (location == null) {
            throw new ResourceNotFoundException();
        }
        return location;
    }

    @RequestMapping(value="/list/{type}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Resource> listDivisions(@RequestParam(value = "updatedSince", required = false) String updatedSince,
                                 @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                 @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                 @PathVariable String type,  HttpServletRequest request) {

        checkForValidUserRequest(request);

        try {
            return locations.findResources(type, updatedSince, offset, limit);
        } catch (ParseException e) {
            throw new BadRequest(e.getMessage());
        }

    }


}
