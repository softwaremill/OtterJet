package org.jetstreamDrop;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/msgs")
public class MsgsController {

  private static final String TEMPLATE_NAME = "msgs-page";

  @Autowired ReaderService readerService;
  private String topic = "";
  private String type = "";

  @GetMapping()
  public String page(Model model) {
    System.out.println("amount of msgs: " + readerService.getMessages().size());
    model.addAttribute("messages", readerService.getMessages());
    model.addAttribute("topic", topic);
    model.addAttribute("type", type);
    return TEMPLATE_NAME;
  }

  @GetMapping("/messages")
  public String messages(Model model) {
    List<ReadMessage> filteredMessages = readerService.filter(topic, type);
    System.out.println("REFRESH -> amount of msgs: " + filteredMessages);
    model.addAttribute("messages", filteredMessages);
    model.addAttribute("topic", topic);
    model.addAttribute("type", type);
    return TEMPLATE_NAME;
  }

  @GetMapping("/filterMessages")
  public String filterMessages(
      @RequestParam("topic") String topic, @RequestParam("type") String type, Model model) {
    this.topic = topic;
    this.type = type;
    List<ReadMessage> filteredMessages = readerService.filter(topic, type);

    model.addAttribute("topic", topic);
    model.addAttribute("type", type);
    model.addAttribute("messages", filteredMessages);
    return TEMPLATE_NAME;
  }
}
