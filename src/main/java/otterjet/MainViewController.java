package otterjet;

import otterjet.monitoring.NatsMonitoringDataLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

  private final NatsMonitoringDataLoader natsMonitoringDataLoader;

  public MainViewController(NatsMonitoringDataLoader natsMonitoringDataLoader) {
    this.natsMonitoringDataLoader = natsMonitoringDataLoader;
  }

  @GetMapping("/")
  public String mainView(Model model) {
    model.addAttribute("isMonitoringEnabled", natsMonitoringDataLoader.isMonitoringEnabled());
    return "main";
  }
}
