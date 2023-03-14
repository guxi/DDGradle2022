package edu.wust.gradleinitSpring;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class C_AI {

  @Value("${model.path}")
  private String modelPath;
  @Value("${model.testPic}")
  private String picPath;

  @GetMapping("/hello")
  public String hello() {
    return modelPath;
  }

  @GetMapping(value = "/IR")
  public List<Item> IR(@RequestParam String picUrl) {
    if (!picUrl.equals(""))
      picPath = picUrl;
    ImageRecognition IR = new ImageRecognition(modelPath, picPath);
    return IR.irTopK_item(3);
  }

  @GetMapping(value = "/IR1")
  public List<Item> IR1() {
    ImageRecognition IR = new ImageRecognition(modelPath, picPath);
    return IR.irTopK_item(3);
  }
}
