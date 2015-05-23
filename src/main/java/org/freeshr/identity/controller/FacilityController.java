package org.freeshr.identity.controller;


import org.freeshr.identity.repository.ResourceRepository;
import org.freeshr.identity.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/api/1.0/facilities")
public class FacilityController extends BaseController {

    @Autowired
    private ResourceRepository facilities;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Resource getFacility(@PathVariable String id, HttpServletRequest request) {
        checkForValidUserRequest(request);
        Resource facility = facilities.findResource("facility", id);
        if (facility == null) {
            throw new ResourceNotFoundException();
        }
        return facility;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Resource> listFacilities(@RequestParam(value = "updatedSince", required = false) String updatedSince,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                  @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  HttpServletRequest request) {

        checkForValidUserRequest(request);

        try {
             return facilities.findResources("facility", updatedSince, offset, limit);
        } catch (ParseException e) {
            throw new BadRequest(e.getMessage());
        }

    }


}
