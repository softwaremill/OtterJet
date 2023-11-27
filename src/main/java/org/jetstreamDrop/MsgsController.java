package org.jetstreamDrop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/msgs")
public class MsgsController {

    private static final String TEMPLATE_NAME = "msgs-page";

    @Autowired
    ReaderService readerService;

    @GetMapping()
    public String page(Model model) {
        System.out.println("amount of msgs" + readerService.getMsgs().size());
        model.addAttribute("messages", readerService.getMsgs());
        return TEMPLATE_NAME;
    }
}
