package org.jetstreamDrop;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MsgsController {

  private static final String TEMPLATE_NAME = "msgs-page";
  private static final Logger LOG = LoggerFactory.getLogger(MsgsController.class);

  private final ReaderService readerService;
  private String subjectFilter;
  private String typeFilter;

  public MsgsController(ReaderService readerService) {
    this.readerService = readerService;
  }

  @GetMapping("/msgs")
  public String page(
      @RequestParam(value = "subject", required = false) String subject,
      @RequestParam(value = "type", required = false) String type,
      Model model) {
    this.subjectFilter = Optional.ofNullable(subject).orElse("");
    this.typeFilter = Optional.ofNullable(type).orElse("");
    List<ReadMessage> filteredMessages = readerService.filter(subjectFilter, typeFilter);
    LOG.info("amount of read messages: " + filteredMessages.size());
    model.addAttribute("messages", filteredMessages);
    model.addAttribute("subject", subjectFilter);
    model.addAttribute("type", typeFilter);
    return TEMPLATE_NAME;
  }
}
