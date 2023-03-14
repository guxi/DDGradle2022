package edu.wust.gradleinitSpring;

// import java.io.FileNotFoundException;

// import java.net.URI;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ResourceUtils;

//import ai.djl.training.util.DownloadUtils;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;

import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Translator;

public class ImageRecognition {
  private String modelUrl = "";
  private String picUrl = "";

  // public ImageRecognition() {
  // }

  public ImageRecognition(String modelurl, String picurl) {
    modelUrl = modelurl;
    picUrl = picurl;
  }

  public List<Item> irTopK_item(int topNum) {
    Classifications cfs = ir();

    List<Item> items = new ArrayList<Item>();
    for (int i = 0; i < topNum; i++) {
      var cf = cfs.topK(topNum).get(i);
      Item item = new Item();
      item.setClassname(cf.getClassName());
      item.setValue(cf.getProbability());
      items.add(item);
    }
    return items;
  }

  public String irTopK_String() {
    return ir().toString();
  }

  public String irTopK_Json() {
    return ir().toJson();
  }

  @SuppressWarnings("rawtypes")
  private Classifications ir() {
    try {
      Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
          .addTransform(new Resize(256))
          .addTransform(new CenterCrop(224, 224))
          .addTransform(new ToTensor())
          .addTransform(new Normalize(
              new float[] { 0.485f, 0.456f, 0.406f },
              new float[] { 0.229f, 0.224f, 0.225f }))
          .optApplySoftmax(true)
          .build();
      /**
       * 判据
       */
      Criteria<Image, Classifications> criteria = Criteria.builder()
          .setTypes(Image.class, Classifications.class)
          .optModelUrls(modelUrl)
          .optModelPath(Paths.get(modelUrl))

          .optOption("mapLocation", "true") // this model requires mapLocation for GPU
          .optTranslator(translator)
          .optProgress(new ProgressBar()).build();

      ZooModel model = criteria.loadModel();

      var img = ImageFactory.getInstance().fromUrl(picUrl);
      img.getWrappedImage();
      @SuppressWarnings("unchecked")
      Predictor<Image, Classifications> predictor = model.newPredictor();

      Classifications classifications = predictor.predict(img);
      return classifications;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
