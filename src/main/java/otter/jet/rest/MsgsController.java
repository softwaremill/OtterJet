package otter.jet.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import otter.jet.reader.ReadMessage;
import otter.jet.reader.ReaderService;

@Controller
public class MsgsController {

    private static final String TEMPLATE_NAME = "msgs-page";
    private static final Logger LOG = LoggerFactory.getLogger(MsgsController.class);

    private final ReaderService readerService;

    public MsgsController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping("/msgs")
    public String page(
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "bodyContent", required = false) String bodyContent,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {
        String subjectFilter = Optional.ofNullable(subject).orElse("");
        String typeFilter = Optional.ofNullable(type).orElse("");
        String bodyContentFilter = Optional.ofNullable(bodyContent).orElse("");
        List<ReadMessage> filteredMessages = readerService.filter(subjectFilter, typeFilter, page, size, bodyContentFilter);
        LOG.info("amount of read messages: " + filteredMessages.size());
        model.addAttribute("messages", filteredMessages);
        model.addAttribute("subject", subjectFilter);
        model.addAttribute("type", typeFilter);
        model.addAttribute("bodyContent", bodyContentFilter);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return TEMPLATE_NAME;
    }
}
