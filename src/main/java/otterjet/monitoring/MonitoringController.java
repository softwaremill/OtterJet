package otterjet.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonitoringController {

  private static final String TEMPLATE_NAME = "monitoring";
  private final NatsMonitoringDataLoader natsMonitoringDataLoader;

  MonitoringController(NatsMonitoringDataLoader natsMonitoringDataLoader) {
    this.natsMonitoringDataLoader = natsMonitoringDataLoader;
  }

  @GetMapping("/monitoring")
  public String page(Model model) {
    if (!natsMonitoringDataLoader.isMonitoringEnabled()) {
      return "redirect:/";
    }
    MonitoringData metrics = natsMonitoringDataLoader.getMonitoringData();
    if (metrics instanceof DirectMonitoringResponse dmr) {
      model.addAttribute("metrics", dmr.response());
    }
    return TEMPLATE_NAME;
  }
}
