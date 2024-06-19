package com.despani.core.controllers.mvc;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController {


}
